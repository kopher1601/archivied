package config

import (
	"crypto/rand"
	"encoding/base64"
	"encoding/json"
	"errors"
	"github.com/gin-gonic/gin"
	"golang.org/x/oauth2"
	"golang.org/x/oauth2/google"
	"log"
	"os"
	"strings"
)

func NewOauthConfig() *oauth2.Config {
	oauthScopes := strings.Split(os.Getenv("OAUTH_SCOPES"), ",")
	return &oauth2.Config{
		RedirectURL:  os.Getenv("OAUTH_REDIRECT_URL"),
		ClientID:     os.Getenv("OAUTH_CLIENT_ID"),
		ClientSecret: os.Getenv("OAUTH_CLIENT_SECRET"),
		Scopes:       []string{oauthScopes[0], oauthScopes[1]},
		Endpoint:     google.Endpoint,
	}
}

type OAuth interface {
	CreateState(currentUri string) (string, error)
	AuthCodeURL(state string) string
	GetAccessToken(c *gin.Context, code string) (*oauth2.Token, error)
	GetUserID(c *gin.Context, token *oauth2.Token) (string, error)
}

func NewGoogleOAuth(config *oauth2.Config) OAuth {
	return &googleOAuth{config: config}
}

type googleOAuth struct {
	config *oauth2.Config
}

func (g *googleOAuth) CreateState(string) (string, error) {
	b := make([]byte, 16)
	if _, err := rand.Read(b); err != nil {
		log.Println(err)
		return "", err
	}
	return base64.URLEncoding.EncodeToString(b), nil
}

func (g *googleOAuth) AuthCodeURL(state string) string {
	return g.config.AuthCodeURL(state)
}

func (g *googleOAuth) GetAccessToken(c *gin.Context, code string) (*oauth2.Token, error) {
	token, err := g.config.Exchange(c, code)
	if err != nil {
		log.Println("Failed to receive access token from google", err)
		return nil, err
	}
	return token, nil
}

func (g *googleOAuth) GetUserID(c *gin.Context, token *oauth2.Token) (string, error) {
	client := g.config.Client(c, token)
	resp, err := client.Get(os.Getenv("OAUTH_USER_INFO"))
	if err != nil {
		log.Println("Failed to get user info from google", err)
		return "", err
	}
	defer resp.Body.Close()

	var userInfo map[string]any
	if err := json.NewDecoder(resp.Body).Decode(&userInfo); err != nil {
		return "", err
	}

	v, ok := userInfo["id"].(string)
	if !ok {
		return "", errors.New("invalid user id")
	}

	return v, nil
}
