package repository

import (
	"backend/ent"
	"backend/ent/skill"
	"context"
	"log"
)

type SkillRepository interface {
	FindByName(skill string) (*ent.Skill, error)
	Create(skill string) (*ent.Skill, error)
}

type skillRepository struct {
	orm *ent.Client
}

func NewSkillRepository(orm *ent.Client) SkillRepository {
	return &skillRepository{orm: orm}
}

func (s *skillRepository) FindByName(skillName string) (*ent.Skill, error) {
	foundSkill, err := s.orm.Skill.Query().Where(skill.Name(skillName)).First(context.Background())
	if err != nil {
		log.Println(err.Error())
		return nil, err
	}
	return foundSkill, nil
}

func (s *skillRepository) Create(skill string) (*ent.Skill, error) {
	savedSkill, err := s.orm.Skill.Create().SetName(skill).Save(context.Background())
	if err != nil {
		log.Println(err.Error())
		return nil, err
	}
	return savedSkill, nil
}
