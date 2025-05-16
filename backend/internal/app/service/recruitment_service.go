package service

import (
	"backend/ent"
	"backend/internal/app/domain"
	"backend/internal/app/repository"
	"errors"
	"time"
)

var ErrUnAuthorized = errors.New("unauthorized")

type RecruitmentService interface {
	Register(userID string, registerRecruitment *domain.RegisterRecruitment) error
	FindAll(keyword string, skills, positions []string, page, limit int) ([]*domain.RecruitmentOutput, error)
	FindByID(recruitmentID int) (*domain.RecruitmentOutput, error)
	DeleteByID(id string, recruitmentID int) error
	Update(sub string, recruitmentID int, input *domain.UpdateRecruitment) error
}

type recruitmentService struct {
	repo         repository.RecruitmentRepository
	skillRepo    repository.SkillRepository
	userRepo     repository.AuthRepository
	positionRepo repository.PositionRepository
}

func NewRecruitmentService(
	repo repository.RecruitmentRepository,
	skillRepo repository.SkillRepository,
	userRepo repository.AuthRepository,
	positionRepo repository.PositionRepository,
) RecruitmentService {
	return &recruitmentService{
		repo:         repo,
		skillRepo:    skillRepo,
		userRepo:     userRepo,
		positionRepo: positionRepo,
	}
}

func (r *recruitmentService) FindByID(recruitmentID int) (*domain.RecruitmentOutput, error) {
	recruitment, err := r.repo.FindByID(recruitmentID)
	if err != nil {
		return nil, err
	}

	var skills []string
	for _, s := range recruitment.Edges.Skills {
		skills = append(skills, s.Name)
	}

	var positions []string
	for _, p := range recruitment.Edges.Positions {
		positions = append(positions, p.Name)
	}

	output := &domain.RecruitmentOutput{
		ID:        recruitment.ID,
		UserID:    recruitment.UsersID,
		Positions: positions,
		Skills:    skills,
		Proceed:   recruitment.Proceed,
		Category:  recruitment.Category,
		Contact:   recruitment.Contact,
		Deadline:  recruitment.DeadLine,
		Period:    recruitment.Period,
		Members:   recruitment.Members,
		Title:     recruitment.Title,
		Content:   recruitment.Content,
	}
	return output, nil
}

func (r *recruitmentService) FindAll(keyword string, skills, positions []string, page, limit int) ([]*domain.RecruitmentOutput, error) {
	if limit == 0 {
		limit = 12
	}

	recruitments, err := r.repo.FindAll(keyword, skills, positions, page, limit)
	if err != nil {
		return nil, err
	}

	var outputs []*domain.RecruitmentOutput
	for _, re := range recruitments {
		var skills []string
		for _, s := range re.Edges.Skills {
			skills = append(skills, s.Name)
		}

		var positions []string
		for _, p := range re.Edges.Positions {
			positions = append(positions, p.Name)
		}

		output := &domain.RecruitmentOutput{
			ID:        re.ID,
			UserID:    re.UsersID,
			Positions: positions,
			Skills:    skills,
			Proceed:   re.Proceed,
			Category:  re.Category,
			Contact:   re.Contact,
			Deadline:  re.DeadLine,
			Period:    re.Period,
			Members:   re.Members,
			Title:     re.Title,
			Content:   re.Content,
		}
		outputs = append(outputs, output)
	}
	return outputs, nil
}

func (r *recruitmentService) Register(userID string, registerRecruitment *domain.RegisterRecruitment) error {

	foundUser, err := r.userRepo.FindUserBySub(userID)
	if err != nil {
		return err
	}

	var skills []*ent.Skill
	for _, s := range registerRecruitment.Skills {
		skill, err := r.skillRepo.FindByName(s)
		if err != nil && ent.IsNotFound(err) {
			skill, err = r.skillRepo.Create(s)
		}
		if err != nil {
			return err
		}
		skills = append(skills, skill)
	}

	var positions []*ent.Position
	for _, name := range registerRecruitment.Positions {
		position, err := r.positionRepo.FindByName(name)
		if err != nil && ent.IsNotFound(err) {
			position, err = r.positionRepo.Create(name)
		}
		if err != nil {
			return err
		}
		positions = append(positions, position)
	}
	parsedDeadline, err := time.Parse(time.DateOnly, registerRecruitment.Deadline)
	if err != nil {
		return err
	}
	recruitment := &domain.Recruitment{
		User:      foundUser,
		Positions: positions,
		Skills:    skills,
		Proceed:   registerRecruitment.Proceed,
		Category:  registerRecruitment.Category,
		Contact:   registerRecruitment.Contact,
		Deadline:  parsedDeadline,
		Period:    registerRecruitment.Period,
		Members:   registerRecruitment.Members,
		Title:     registerRecruitment.Title,
		Content:   registerRecruitment.Content,
	}

	return r.repo.Register(recruitment)
}

func (r *recruitmentService) DeleteByID(sub string, recruitmentID int) error {
	user, err := r.userRepo.FindUserBySub(sub)
	if err != nil {
		return err
	}

	recruitment, err := r.FindByID(recruitmentID)
	if err != nil {
		return err
	}

	if user.ID != recruitment.UserID {
		return ErrUnAuthorized
	}

	err = r.repo.DeleteByID(recruitmentID)
	if err != nil {
		return err
	}

	return nil
}

func (r *recruitmentService) Update(sub string, recruitmentID int, input *domain.UpdateRecruitment) error {
	user, err := r.userRepo.FindUserBySub(sub)
	if err != nil {
		return err
	}

	recruitment, err := r.repo.FindByID(recruitmentID)
	if err != nil {
		return err
	}

	if user.ID != recruitment.UsersID {
		return ErrUnAuthorized
	}

	var skills []*ent.Skill
	for _, s := range input.Skills {
		skill, err := r.skillRepo.FindByName(s)
		if err != nil && ent.IsNotFound(err) {
			skill, err = r.skillRepo.Create(s)
		}
		if err != nil {
			return err
		}
		skills = append(skills, skill)
	}

	var positions []*ent.Position
	for _, name := range input.Positions {
		position, err := r.positionRepo.FindByName(name)
		if err != nil && ent.IsNotFound(err) {
			position, err = r.positionRepo.Create(name)
		}
		if err != nil {
			return err
		}
		positions = append(positions, position)
	}
	parsedDeadline, err := time.Parse(time.DateOnly, input.Deadline)
	if err != nil {
		return err
	}

	updateRecruitment := &domain.Recruitment{
		User:      recruitment.Edges.Users,
		Positions: positions,
		Skills:    skills,
		Proceed:   input.Proceed,
		Category:  input.Category,
		Contact:   input.Contact,
		Deadline:  parsedDeadline,
		Period:    input.Period,
		Members:   input.Members,
		Title:     input.Title,
		Content:   input.Content,
	}

	err = r.repo.Update(recruitment, updateRecruitment)
	if err != nil {
		return err
	}

	return nil
}
