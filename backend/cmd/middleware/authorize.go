package middleware

import (
	"backend/internal/app/domain"
	"fmt"
	"github.com/gin-gonic/gin"
	"github.com/gofiber/fiber/v2/log"
	"github.com/golang-jwt/jwt/v5"
	"net/http"
	"os"
	"time"
)

func AuthorizeToken() gin.HandlerFunc {
	return func(c *gin.Context) {

		accessToken, err := c.Cookie("access_token")
		if err != nil {
			log.Info("could not get access_token")
			return
		}
		token, err := jwt.Parse(accessToken, func(token *jwt.Token) (interface{}, error) {
			if _, ok := token.Method.(*jwt.SigningMethodHMAC); !ok {
				return nil, fmt.Errorf("unexpected signing method: %v", token.Header["alg"])
			}
			return []byte(os.Getenv("JWT_SIGN_KEY")), nil
		})
		if err != nil {
			c.AbortWithStatusJSON(http.StatusUnauthorized, domain.NewError(http.StatusUnauthorized, "Unauthorized"))
			return
		}

		var userID string
		if claims, ok := token.Claims.(jwt.MapClaims); ok {
			// token 満了の場合
			if float64(time.Now().Unix()) > claims["exp"].(float64) {
				c.AbortWithStatusJSON(http.StatusUnauthorized, domain.NewError(http.StatusUnauthorized, "Unauthorized"))
				return
			}
			userID = claims["sub"].(string)
		}
		c.Set("userID", userID)
		c.Next()
	}
}
