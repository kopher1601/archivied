package service

import (
	"backend/config"
	"backend/ent"
	"backend/internal/app/domain"
	"backend/internal/app/repository"
	"context"
	"github.com/gin-gonic/gin"
	"github.com/golang-jwt/jwt/v5"
	"log"
	"os"
	"time"
)

const issuer = "topu"

type (
	AuthService interface {
		Login(currentUri string) (*domain.LoginOutput, error)
		GoogleCallback(c *gin.Context, code string) (string, error)
		GetUser(userID string) (*domain.UserOutput, error)
		UpdateProfile(ctx context.Context, userID string, input *domain.UpdateInput) error
	}

	authService struct {
		oAuth        config.OAuth
		skillRepo    repository.SkillRepository
		positionRepo repository.PositionRepository
		repo         repository.AuthRepository
	}
)

func NewAuthService(config config.OAuth,
	repo repository.AuthRepository,
	skillRepo repository.SkillRepository,
	positionRepo repository.PositionRepository,
) AuthService {
	return &authService{
		oAuth:        config,
		skillRepo:    skillRepo,
		positionRepo: positionRepo,
		repo:         repo,
	}
}

func (a *authService) Login(currentUri string) (*domain.LoginOutput, error) {

	url := a.oAuth.AuthCodeURL(currentUri)

	return &domain.LoginOutput{
		URL: url,
	}, nil
}

func (a *authService) GoogleCallback(c *gin.Context, code string) (string, error) {
	token, err := a.oAuth.GetAccessToken(c, code)
	if err != nil {
		return "", err
	}

	userID, err := a.oAuth.GetUserID(c, token)
	if err != nil {
		return "", err
	}

	if _, err := a.repo.FindTransientUserByID(userID); ent.IsNotFound(err) {
		if _, err = a.repo.Create(userID); err != nil {
			log.Println("failed to create user", err)
			return "", err
		}
	}

	accessToken := jwt.New(jwt.SigningMethodHS256)

	claims := accessToken.Claims.(jwt.MapClaims)
	claims["iss"] = issuer
	claims["sub"] = userID
	claims["exp"] = time.Now().Add(time.Minute * 30).Unix()

	signedAccessToken, err := accessToken.SignedString([]byte(os.Getenv("JWT_SIGN_KEY")))
	if err != nil {
		return "", err
	}

	// TODO refreshToken 実装、redis 使用

	return signedAccessToken, nil
}

func (a *authService) GetUser(userID string) (*domain.UserOutput, error) {
	user, err := a.repo.FindUserBySub(userID)

	var output *domain.UserOutput
	if err != nil {
		if ent.IsNotFound(err) {
			tu, err := a.repo.FindTransientUserByID(userID)
			if err != nil {
				return nil, err
			}
			output = &domain.UserOutput{
				Sub:       tu.Sub,
				Transient: true,
			}
			return output, nil
		}
		return nil, err
	}

	var skillNames []string
	if user.Edges.Skills != nil {
		skills := user.Edges.Skills
		for _, skill := range skills {
			skillNames = append(skillNames, skill.Name)
		}
	}
	position, err := user.QueryPosition().Only(context.Background())
	if err != nil {
		return nil, err
	}
	output = &domain.UserOutput{
		Sub:        user.Sub,
		Nickname:   user.Nickname,
		Position:   position.Name,
		CareerYear: user.CareerYear,
		Skills:     skillNames,
		Transient:  false,
		ProfileURL: user.ProfileUrl,
	}
	return output, nil
}

func (a *authService) UpdateProfile(ctx context.Context, userID string, input *domain.UpdateInput) error {
	// user が存在するかを確認
	_, err := a.GetUser(userID)
	if err != nil {
		return err
	}

	var skills []*ent.Skill
	for _, s := range input.Skills {
		skill, err := a.skillRepo.FindByName(s)
		if err != nil && ent.IsNotFound(err) {
			skill, err = a.skillRepo.Create(s)
		}
		if err != nil {
			return err
		}
		skills = append(skills, skill)
	}

	foundPosition, err := a.positionRepo.FindByName(input.Position)
	if err != nil && ent.IsNotFound(err) {
		foundPosition, err = a.positionRepo.Create(input.Position)
	}
	if err != nil {
		return err
	}

	// 更新
	updatedUser := &domain.UpdateUser{
		Sub:        userID,
		Nickname:   input.Nickname,
		Position:   foundPosition,
		CareerYear: input.CareerYear,
		Skills:     skills,
	}
	if err := a.repo.Update(ctx, userID, updatedUser); err != nil {
		return err
	}
	return nil
}
