package service

import (
	"backend/ent"
	"backend/internal/app/domain"
	"backend/internal/app/repository"
	"context"
	"log"
)

type SignupService interface {
	Signup(ctx context.Context, input *domain.SignupInput, sub string) error
}
type signupService struct {
	authRepository repository.AuthRepository
	positionRepo   repository.PositionRepository
	skillRepo      repository.SkillRepository
}

func NewSignupService(authRepository repository.AuthRepository,
	positionRepo repository.PositionRepository,
	skillRepo repository.SkillRepository) SignupService {
	return &signupService{
		authRepository: authRepository,
		positionRepo:   positionRepo,
		skillRepo:      skillRepo,
	}
}

func (s *signupService) Signup(ctx context.Context, input *domain.SignupInput, sub string) error {

	// 입력된 회원등록 정보로 회원정보 갱신
	var skills []*ent.Skill
	for _, skillName := range input.Skills {
		skill, err := s.skillRepo.FindByName(skillName)
		if err != nil && ent.IsNotFound(err) {
			skill, err = s.skillRepo.Create(skillName)
		}
		if err != nil {
			return err
		}
		skills = append(skills, skill)
	}

	foundPosition, err := s.positionRepo.FindByName(input.Position)
	if err != nil && ent.IsNotFound(err) {
		foundPosition, err = s.positionRepo.Create(input.Position)
	}
	if err != nil {
		return err
	}

	registerUser := &domain.RegisterUser{
		Sub:        sub,
		Nickname:   input.Nickname,
		Position:   foundPosition,
		CareerYear: input.CareerYear,
		Skills:     skills,
	}

	// TODO transaction 걸기
	_, err = s.authRepository.Signup(ctx, registerUser)
	if err != nil {
		log.Println("can't update user")
		return err
	}

	return nil
}
