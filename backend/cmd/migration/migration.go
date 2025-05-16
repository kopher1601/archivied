package main

import (
	"backend/config"
	"context"
	"entgo.io/ent/dialect/sql/schema"
	"github.com/joho/godotenv"
	"log"
)

func main() {
	err := godotenv.Load()
	if err != nil {
		log.Fatal("Error loading .env file", err)
	}
	orm := config.NewORM(config.MySQL())
	db := orm.GetDB()
	if err := db.Schema.Create(
		context.Background(),
		schema.WithDropIndex(true),
		schema.WithDropColumn(true),
	); err != nil {
		log.Fatalf("failed creating schema resources: %v", err)
	}
	log.Println("migration complete")
}
