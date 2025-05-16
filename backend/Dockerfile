FROM golang:1.23 AS builder
LABEL authors="kopher"

ENV CGO_ENABLED=0 \
    GOOS=linux \
    GOARCH=amd64

WORKDIR /app

COPY go.mod go.sum ./
RUN go mod download

COPY . .

RUN go generate ./ent
RUN go build -o topu ./cmd/topu/

# 🏃 Runtime Stage

FROM alpine:latest

WORKDIR /app

COPY --from=builder /app/.env .
COPY --from=builder /app/topu .

CMD ["./topu"]