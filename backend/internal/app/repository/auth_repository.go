package repository

import (
	"backend/ent"
	"backend/ent/transientuser"
	"backend/ent/user"
	"backend/internal/app/domain"
	"context"
	"log"
)

type AuthRepository interface {
	FindTransientUserByID(sub string) (*ent.TransientUser, error)
	FindUserBySub(userID string) (*ent.User, error)
	Create(userID string) (*ent.TransientUser, error)
	Signup(ctx context.Context, ru *domain.RegisterUser) (*ent.User, error)
	Update(ctx context.Context, sub string, updatedUser *domain.UpdateUser) error
}

type authRepository struct {
	orm *ent.Client
}

func NewAuthRepository(orm *ent.Client) AuthRepository {
	return &authRepository{orm: orm}
}

func (a *authRepository) Signup(ctx context.Context, ru *domain.RegisterUser) (*ent.User, error) {
	tx, err := a.orm.Tx(ctx)
	if err != nil {
		log.Println("Failed to start transaction", err)
		return nil, err
	}
	defer func() {
		if err := recover(); err != nil {
			tx.Rollback()
		}
	}()

	// ここで臨時ユーザーを探す
	transientUser, err := a.FindTransientUserByID(ru.Sub)
	if err != nil {
		return nil, err
	}

	createdUser, err := a.orm.User.Create().
		SetSub(transientUser.Sub).
		SetNickname(ru.Nickname).
		SetCareerYear(ru.CareerYear).
		AddSkills(ru.Skills...).
		AddPosition(ru.Position).
		SetProfileUrl("").
		Save(ctx)
	if err != nil {
		return nil, err
	}
	return createdUser, nil
}

func (a *authRepository) FindTransientUserByID(sub string) (*ent.TransientUser, error) {
	u, err := a.orm.TransientUser.Query().Where(transientuser.Sub(sub)).Only(context.Background())
	if err != nil {
		return nil, err
	}
	// TODO Domain 으로 맵핑해서 반환하기
	return u, nil
}

func (a *authRepository) FindUserBySub(userID string) (*ent.User, error) {
	u, err := a.orm.User.Query().Where(user.Sub(userID)).WithSkills(func(query *ent.SkillQuery) {
		query.All(context.Background())
	}).Only(context.Background())
	if err != nil {
		return nil, err
	}
	return u, nil
}

func (a *authRepository) Create(userID string) (*ent.TransientUser, error) {
	u, err := a.orm.TransientUser.Create().SetSub(userID).Save(context.Background())
	if err != nil {
		log.Println("failed to save user", err)
		return nil, err
	}
	// TODO Domain 으로 맵핑해서 반환하기
	return u, nil
}

func (a *authRepository) Update(ctx context.Context, sub string, updatedUser *domain.UpdateUser) error {
	_, err := a.orm.User.Update().
		SetNickname(updatedUser.Nickname).
		SetCareerYear(updatedUser.CareerYear).
		ClearPosition().
		ClearSkills().
		AddPosition(updatedUser.Position).
		AddSkills(updatedUser.Skills...).
		Where(user.Sub(sub)).Save(ctx)
	if err != nil {
		log.Println("failed to save user", err)
		return err
	}
	return nil
}
