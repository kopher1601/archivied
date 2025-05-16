# Backend for Topu service

## 環境構築
### ローカルで単純動作確認

下記のコマンドを実行
- **前提** : Dockerが Mac にインストールされている
#### mise 未使用の方
```shell
# 最初一回のみ実行
docker build -t topu_backend .
```
```shell
docker-compose up -d
```

#### mise 使用する方

```shell
mise run docker-up
```

### ローカルで開発するための設定
#### Java 21 設定 
```shell
mise i
```

#### アプリケーションスタート
```shell
mise run up
```

#### Google OAuth2 設定
- `client_id`, `client_secret` は Confluence を 参照
- `.env`ファイルを生成して Confluence に記載されている内容を貼り付ける



## Architecture
- 最初はレイヤードアーキテクチャー
- 後でヘキサゴナルアーキテクチャーに変更

## Tech
- Java 21
- Spring, Spring Boot
- JPA(Hibernate)
- QueryDSL
- REST Docs With asciidoc
- MySQL

## Backend Team Member
- kopher1601(falsystack)
- chans27

