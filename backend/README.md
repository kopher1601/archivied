# Topu project(Backend)

**topu** は、IT関連の勉強会を簡単に開設・募集・参加できるサービスです。知識を共有し、新しいつながりを作るためのプラットフォームを提供します。

## Architecture
- Layered Architecture

## Tech Stacks
- Go
- Gin
- ent
- MySQL
- Docker
- OpenAPI
- AWS
- Terraform

## Project Setup

> Required : go v1.23.0

データベース起動
```shell
docker compose up -d
```

Ent ファイル生成
```shell
go generate ./ent
```

マイグレーション実行
```shell
go run ./cmd/migration/
```

サーバー起動
```shell
go run ./cmd/topu/
```

Topu を Docker コンテナで起動しない場合
```shell
docker compose up mysql -d
go run ./cmd/topu/
```

### Google OAuth
Confluenceに記載した Client ID と Client Secret　を `.env` ファイルに入力必要