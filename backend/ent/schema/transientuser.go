package schema

import (
	"entgo.io/ent"
	"entgo.io/ent/schema/field"
)

// TransientUser holds the schema definition for the TransientUser entity.
type TransientUser struct {
	ent.Schema
}

// Fields of the TransientUser.
func (TransientUser) Fields() []ent.Field {
	return []ent.Field{
		field.String("sub").NotEmpty().Unique(),
	}
}

// Edges of the TransientUser.
func (TransientUser) Edges() []ent.Edge {
	return nil
}
