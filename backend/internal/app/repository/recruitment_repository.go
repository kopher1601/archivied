package repository

import (
	"backend/ent"
	"backend/ent/position"
	"backend/ent/predicate"
	"backend/ent/recruitment"
	"backend/ent/skill"
	"backend/internal/app/domain"
	"context"
	"log"
)

type RecruitmentRepository interface {
	Register(domain *domain.Recruitment) error
	FindAll(keyword string, skills, positions []string, page, limit int) ([]*ent.Recruitment, error)
	FindByID(recruitmentID int) (*ent.Recruitment, error)
	DeleteByID(recruitmentID int) error
	Update(r *ent.Recruitment, input *domain.Recruitment) error
}

type recruitmentRepository struct {
	orm *ent.Client
}

func NewRecruitmentRepository(orm *ent.Client) RecruitmentRepository {
	return &recruitmentRepository{orm: orm}
}

func (r *recruitmentRepository) FindByID(recruitmentID int) (*ent.Recruitment, error) {
	foundRecruitment, err := r.orm.Recruitment.Query().WithSkills().WithPositions().Where(recruitment.ID(recruitmentID)).Only(context.Background())
	if err != nil {
		return nil, err
	}
	return foundRecruitment, nil

}

func (r *recruitmentRepository) FindAll(keyword string, skills, positions []string, page, limit int) ([]*ent.Recruitment, error) {
	query := r.orm.Recruitment.Query().WithSkills().WithPositions()

	var conditions []predicate.Recruitment
	if len(skills) > 0 {
		conditions = append(conditions, recruitment.HasSkillsWith(skill.NameIn(skills...)))
	}

	if len(positions) > 0 {
		conditions = append(conditions, recruitment.HasPositionsWith(position.NameIn(positions...)))
	}

	if keyword != "" {
		conditions = append(conditions, recruitment.
			Or(
				recruitment.TitleContains(keyword),
				recruitment.ContentContains(keyword),
			),
		)
	}

	if len(conditions) > 0 {
		query.Where(recruitment.And(conditions...))
	}

	recruitments, err := query.
		Offset(page * limit).
		Limit(limit).
		Order(ent.Desc(recruitment.FieldID)).
		All(context.Background())
	if err != nil {
		log.Println(err)
		return nil, err
	}
	return recruitments, nil
}

func (r *recruitmentRepository) Register(domain *domain.Recruitment) error {
	_, err := r.orm.Recruitment.Create().
		SetUsers(domain.User).
		SetProceed(domain.Proceed).
		SetCategory(domain.Category).
		SetDeadLine(domain.Deadline).
		SetContact(domain.Contact).
		SetPeriod(domain.Period).
		SetMembers(domain.Members).
		SetTitle(domain.Title).
		SetContent(domain.Content).
		AddPositions(domain.Positions...).
		AddSkills(domain.Skills...).
		Save(context.Background())
	if err != nil {
		log.Println(err)
		return err
	}
	return nil
}

func (r *recruitmentRepository) DeleteByID(recruitmentID int) error {
	_, err := r.orm.Recruitment.Delete().Where(recruitment.ID(recruitmentID)).Exec(context.Background())
	if err != nil {
		log.Println(err)
		return err
	}
	return nil
}

func (r *recruitmentRepository) Update(recruitment *ent.Recruitment, input *domain.Recruitment) error {
	_, err := r.orm.Recruitment.UpdateOne(recruitment).
		SetProceed(input.Proceed).
		SetCategory(input.Category).
		SetDeadLine(input.Deadline).
		SetContact(input.Contact).
		SetPeriod(input.Period).
		SetMembers(input.Members).
		SetTitle(input.Title).
		SetContent(input.Content).
		ClearPositions().
		ClearSkills().
		AddPositions(input.Positions...).
		AddSkills(input.Skills...).
		Save(context.Background())
	if err != nil {
		log.Println(err)
		return err
	}
	return nil
}
