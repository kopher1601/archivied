package controller

import (
	"backend/internal/app/domain"
	"backend/internal/app/service"
	"fmt"
	"github.com/gin-gonic/gin"
	"net/http"
	"strings"
	"time"
)

type AuthController interface {
	Login(c *gin.Context)
	Logout(c *gin.Context)
	GoogleCallback(c *gin.Context)
	Me(c *gin.Context)
	UpdateProfile(c *gin.Context)
}

type authController struct {
	authService service.AuthService
}

func NewAuthController(authService service.AuthService) AuthController {
	return &authController{authService: authService}
}

func (a *authController) Logout(c *gin.Context) {

	// TODO redis に保存されているトークン満了させる

	http.SetCookie(c.Writer, &http.Cookie{
		Name:     "access_token",
		Value:    "",
		Expires:  time.Now().Add(-(time.Hour * 2)),
		Path:     "/",
		SameSite: http.SameSiteLaxMode,
		HttpOnly: true,
	})

	c.Status(http.StatusOK)
}

func (a *authController) Login(c *gin.Context) {
	currentUri := c.Query("google")

	output, err := a.authService.Login(currentUri)
	if err != nil {
		c.Status(http.StatusUnauthorized)
		return
	}

	c.JSON(http.StatusOK, output)
}

func (a *authController) GoogleCallback(c *gin.Context) {
	code := c.Query("code")
	currentUri := c.Query("state")

	accessToken, err := a.authService.GoogleCallback(c, code)
	if err != nil {
		c.JSON(http.StatusInternalServerError,
			domain.NewError(http.StatusInternalServerError, err.Error()))
		return
	}
	http.SetCookie(c.Writer, &http.Cookie{
		Name:     "access_token",
		Value:    accessToken,
		Expires:  time.Now().Add(time.Minute * 30),
		SameSite: http.SameSiteLaxMode,
		Path:     "/",
		// TODO 本番環境で ON にする
		// Secure:   true,
		HttpOnly: true,
	})

	strings.TrimPrefix(currentUri, "/")

	c.Redirect(http.StatusFound, fmt.Sprintf("http://localhost:3000/%s", currentUri))
}

func (a *authController) Me(c *gin.Context) {
	userID, exists := c.Get("userID")
	if !exists {
		c.Status(http.StatusUnauthorized)
		return
	}

	resp, err := a.authService.GetUser(userID.(string))
	if err != nil {
		c.JSON(http.StatusInternalServerError,
			domain.NewError(http.StatusInternalServerError, err.Error()))
		return
	}

	c.JSON(http.StatusOK, resp)
}

func (a *authController) UpdateProfile(c *gin.Context) {
	userID, exists := c.Get("userID")
	if !exists {
		c.Status(http.StatusUnauthorized)
		return
	}

	var input domain.UpdateInput
	if err := c.ShouldBindJSON(&input); err != nil {
		c.JSON(http.StatusBadRequest, domain.NewError(http.StatusBadRequest, err.Error()))
		return
	}

	if err := a.authService.UpdateProfile(c, userID.(string), &input); err != nil {
		c.JSON(http.StatusInternalServerError, domain.NewError(http.StatusInternalServerError, err.Error()))
		return
	}
	c.JSON(http.StatusOK, nil)
}
