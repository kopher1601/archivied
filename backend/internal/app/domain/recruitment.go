package domain

import (
	"backend/ent"
	"time"
)

type RegisterRecruitment struct {
	Positions []string `json:"positions"`
	Skills    []string `json:"skills"`
	Proceed   string   `json:"proceed"`
	Category  string   `json:"category"`
	Contact   string   `json:"contact"`
	Deadline  string   `json:"deadline"` // 応募の締め切り
	Period    int8     `json:"period"`
	Members   int8     `json:"members"`
	Title     string   `json:"title"`
	Content   string   `json:"content"`
}

type UpdateRecruitment struct {
	Positions []string `json:"positions"`
	Skills    []string `json:"skills"`
	Proceed   string   `json:"proceed"`
	Category  string   `json:"category"`
	Contact   string   `json:"contact"`
	Deadline  string   `json:"deadline"` // 応募の締め切り
	Period    int8     `json:"period"`
	Members   int8     `json:"members"`
	Title     string   `json:"title"`
	Content   string   `json:"content"`
}

type Recruitment struct {
	User      *ent.User
	Positions []*ent.Position
	Skills    []*ent.Skill
	Proceed   string // TODO enum 化
	Category  string // TODO enum 化
	Contact   string
	Deadline  time.Time
	Period    int8 // TODO enum 化
	Members   int8 // TODO enum 化
	Title     string
	Content   string
}

type RecruitmentOutput struct {
	ID        int       `json:"id"`
	UserID    int       `json:"user_id"`
	Positions []string  `json:"positions"`
	Skills    []string  `json:"skills"`
	Proceed   string    `json:"proceed"`
	Category  string    `json:"category"`
	Contact   string    `json:"contact"`
	Deadline  time.Time `json:"deadline"`
	Period    int8      `json:"period"`
	Members   int8      `json:"members"`
	Title     string    `json:"title"`
	Content   string    `json:"content"`
}
