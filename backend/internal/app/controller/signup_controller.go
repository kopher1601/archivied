package controller

import (
	"backend/internal/app/domain"
	"backend/internal/app/service"
	"github.com/gin-gonic/gin"
	"log"
	"net/http"
)

type SignupController interface {
	Signup(ctx *gin.Context)
}

type signupController struct {
	signupService service.SignupService
}

func NewSignupController(signupService service.SignupService) SignupController {
	return &signupController{signupService: signupService}
}

func (s *signupController) Signup(ctx *gin.Context) {
	userID := ctx.Value("userID").(string)

	input := &domain.SignupInput{}
	err := ctx.ShouldBindJSON(input)
	if err != nil {
		log.Println(err)
		ctx.JSON(http.StatusBadRequest, domain.NewError(http.StatusBadRequest, err.Error()))
		return
	}

	err = s.signupService.Signup(ctx, input, userID)
	if err != nil {
		ctx.JSON(http.StatusInternalServerError, domain.NewError(http.StatusInternalServerError, err.Error()))
		return
	}

	ctx.Status(http.StatusCreated)
}
