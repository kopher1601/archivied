package domain

import "backend/ent"

type User struct {
	Sub        string
	Nickname   string
	Position   string
	CareerYear int8
	Skills     []string
	Transient  bool
	ProfileURL string
}

type SignupInput struct {
	Nickname   string   `json:"nickname" validate:"required"`
	Position   string   `json:"position" validate:"required"`
	CareerYear int8     `json:"career_year" validate:"required"`
	Skills     []string `json:"skills" validate:"required"`
}

type UpdateInput struct {
	Nickname   string   `json:"nickname" validate:"required"`
	Position   string   `json:"position" validate:"required"`
	CareerYear int8     `json:"career_year" validate:"required"`
	Skills     []string `json:"skills" validate:"required"`
}

type RegisterUser struct {
	Sub        string
	Nickname   string
	Position   *ent.Position
	CareerYear int8
	Skills     []*ent.Skill
}

type UpdateUser struct {
	Sub        string
	Nickname   string
	Position   *ent.Position
	CareerYear int8
	Skills     []*ent.Skill
}

type UserOutput struct {
	Sub        string   `json:"sub"`
	Nickname   string   `json:"nickname"`
	Position   string   `json:"position"`
	CareerYear int8     `json:"careerYear"`
	Skills     []string `json:"skills"`
	Transient  bool     `json:"transient"`
	ProfileURL string   `json:"profileUrl"`
}
