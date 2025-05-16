package config

import (
	"backend/ent"
	"fmt"
	_ "github.com/go-sql-driver/mysql"
	_ "github.com/mattn/go-sqlite3"
	"log"
)

const (
	mysql   = "mysql"
	sqlite3 = "sqlite3"
)

type ORM interface {
	GetDB() *ent.Client
	getMySQL() *ent.Client
	getSqlite3() *ent.Client
}

type orm struct {
	db *DBConfig
}

func NewORM(db *DBConfig) ORM {
	return &orm{db: db}
}

func (o *orm) GetDB() *ent.Client {
	switch o.db.dbms {
	case mysql:
		return o.getMySQL()
	case sqlite3:
		return o.getSqlite3()
	default:
		return nil
	}
}

func (o *orm) getSqlite3() *ent.Client {
	client, err := ent.Open(sqlite3, "file:ent?mode=memory")
	if err != nil {
		log.Fatalln("failed to connect sqlite3", err)
		return nil
	}
	//defer client.Close()
	log.Println("successfully connected to sqlite3")
	return client
}

func (o *orm) getMySQL() *ent.Client {
	dsn := fmt.Sprintf("%s:%s@tcp(%s:%s)/%s?charset=utf8mb4&parseTime=True",
		o.db.user,
		o.db.password,
		o.db.host,
		o.db.port,
		o.db.db,
	)

	client, err := ent.Open(mysql, dsn)
	if err != nil {
		log.Fatalln("failed to connect mysql", err)
		return nil
	}
	log.Println("successfully connected to mysql")
	return client
}
