package schema

import (
	"entgo.io/ent"
	"entgo.io/ent/schema/edge"
	"entgo.io/ent/schema/field"
)

// User holds the schema definition for the User entity.
type User struct {
	ent.Schema
}

// Fields of the User.
func (User) Fields() []ent.Field {
	return []ent.Field{
		field.String("sub").Unique().NotEmpty(),
		field.String("nickname").Unique().NotEmpty(),
		field.Int8("careerYear"),
		field.String("profileUrl"),
	}
}

// Edges of the User.
func (User) Edges() []ent.Edge {
	return []ent.Edge{
		edge.To("position", Position.Type),
		edge.To("skills", Skill.Type),
		edge.To("recruitments", Recruitment.Type),
	}
}
