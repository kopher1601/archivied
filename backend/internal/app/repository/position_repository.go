package repository

import (
	"backend/ent"
	"backend/ent/position"
	"context"
)

type PositionRepository interface {
	FindByName(positionName string) (*ent.Position, error)
	Create(positionName string) (*ent.Position, error)
}

type positionRepository struct {
	orm *ent.Client
}

func NewPositionRepository(orm *ent.Client) PositionRepository {
	return &positionRepository{orm: orm}
}

func (p *positionRepository) FindByName(positionName string) (*ent.Position, error) {
	foundPosition, err := p.orm.Position.Query().Where(position.Name(positionName)).First(context.Background())
	if err != nil {
		return nil, err
	}
	return foundPosition, nil
}

func (p *positionRepository) Create(positionName string) (*ent.Position, error) {
	createdPosition, err := p.orm.Position.Create().SetName(positionName).Save(context.Background())
	if err != nil {
		return nil, err
	}
	return createdPosition, nil
}
