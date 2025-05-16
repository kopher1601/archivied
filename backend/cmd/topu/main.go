package main

import (
	"backend/cmd/middleware"
	"backend/config"
	"backend/ent"
	"backend/internal/app/controller"
	"backend/internal/app/repository"
	"backend/internal/app/service"
	"github.com/gin-contrib/cors"
	"github.com/gin-gonic/gin"
	"log"
)

func main() {
	Initialize()
	oAuth := config.NewGoogleOAuth(config.NewOauthConfig())
	orm := config.NewORM(config.MySQL())
	db := orm.GetDB()
	r := setupRouter(db, oAuth)
	log.Fatalln(r.Run(":8080"))
}

func setupRouter(db *ent.Client, oAuth config.OAuth) *gin.Engine {
	authRepository := repository.NewAuthRepository(db)
	skillRepository := repository.NewSkillRepository(db)
	positionRepository := repository.NewPositionRepository(db)

	authService := service.NewAuthService(oAuth, authRepository, skillRepository, positionRepository)
	authController := controller.NewAuthController(authService)

	signupService := service.NewSignupService(authRepository, positionRepository, skillRepository)
	signupController := controller.NewSignupController(signupService)

	recruitmentRepository := repository.NewRecruitmentRepository(db)
	recruitmentService := service.NewRecruitmentService(recruitmentRepository, skillRepository, authRepository, positionRepository)
	recruitmentController := controller.NewRecruitmentController(recruitmentService)

	app := gin.Default()
	app.Use(cors.New(cors.Config{
		AllowOrigins:     []string{"http://localhost:3000"},
		AllowHeaders:     []string{"Content-Type", "Authorization", "X-Requested-With", "DNT"},
		AllowMethods:     []string{"GET", "POST", "PUT", "DELETE", "OPTIONS"},
		AllowCredentials: true,
	}))

	app.GET("/v1/oauth2/google/callback", authController.GoogleCallback)
	app.GET("/v1/login", authController.Login)
	app.GET("/v1/logout", authController.Logout)
	app.GET("/v1/me", middleware.AuthorizeToken(), authController.Me)
	app.POST("/v1/signup", middleware.AuthorizeToken(), signupController.Signup)
	app.PUT("/v1/auth/update", middleware.AuthorizeToken(), authController.UpdateProfile)

	recruitments := app.Group("/v1/recruitments")
	recruitments.POST("", middleware.AuthorizeToken(), recruitmentController.Register)
	recruitments.GET("", recruitmentController.FindAll)
	recruitments.GET(":id", recruitmentController.FindByID)
	recruitments.DELETE(":id", middleware.AuthorizeToken(), recruitmentController.DeleteByID)
	recruitments.PUT(":id", middleware.AuthorizeToken(), recruitmentController.Update)
	return app
}
