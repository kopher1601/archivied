package config

import "os"

type DBConfig struct {
	user     string
	password string
	host     string
	port     string
	db       string
	driver   string
	dbms     string
}

func MySQL() *DBConfig {
	return &DBConfig{
		user:     os.Getenv("MYSQL_USER"),
		password: os.Getenv("MYSQL_PASSWORD"),
		host:     os.Getenv("MYSQL_HOST"),
		port:     os.Getenv("MYSQL_PORT"),
		db:       os.Getenv("MYSQL_DATABASE"),
		dbms:     "mysql",
	}
}

func Sqlite3() *DBConfig {
	return &DBConfig{
		dbms: "sqlite3",
	}
}
