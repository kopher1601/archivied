package schema

import (
	"entgo.io/ent"
	"entgo.io/ent/schema/edge"
	"entgo.io/ent/schema/field"
)

// Recruitment holds the schema definition for the Recruitment entity.
type Recruitment struct {
	ent.Schema
}

// Fields of the Recruitment.
func (Recruitment) Fields() []ent.Field {
	return []ent.Field{
		field.String("proceed").NotEmpty(),
		field.String("category").NotEmpty(),
		field.String("contact").NotEmpty(),
		field.Time("dead_line"),
		field.Int8("period").NonNegative(),
		field.Int8("members").NonNegative(),
		field.String("title").NotEmpty().MinLen(1),
		field.String("content").NotEmpty().MinLen(1),
		field.Int("users_id").Optional(),
	}
}

// Edges of the Recruitment.
func (Recruitment) Edges() []ent.Edge {
	return []ent.Edge{
		edge.To("skills", Skill.Type),
		edge.To("positions", Position.Type),
		edge.From("users", User.Type).
			Ref("recruitments").
			Unique().
			Field("users_id"),
	}
}
