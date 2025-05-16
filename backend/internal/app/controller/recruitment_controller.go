package controller

import (
	"backend/internal/app/domain"
	"backend/internal/app/service"
	"github.com/gin-gonic/gin"
	"log"
	"net/http"
	"strconv"
	"strings"
)

type RecruitmentController interface {
	Register(ctx *gin.Context)
	FindAll(ctx *gin.Context)
	FindByID(ctx *gin.Context)
	DeleteByID(ctx *gin.Context)
	Update(ctx *gin.Context)
}

type recruitmentController struct {
	service service.RecruitmentService
}

func NewRecruitmentController(service service.RecruitmentService) RecruitmentController {
	return &recruitmentController{service: service}
}

func (r *recruitmentController) FindByID(ctx *gin.Context) {
	recruitmentID, err := strconv.Atoi(ctx.Param("recruitment_id"))
	if err != nil {
		ctx.JSON(http.StatusBadRequest,
			domain.NewError(http.StatusBadRequest, err.Error()))
		return
	}

	recruitment, err := r.service.FindByID(recruitmentID)
	if err != nil {
		ctx.JSON(http.StatusInternalServerError,
			domain.NewError(http.StatusInternalServerError, err.Error()))
		return
	}
	ctx.JSON(http.StatusOK, recruitment)
	return
}

func (r *recruitmentController) FindAll(ctx *gin.Context) {
	keyword := ctx.Query("keyword")
	querySkill := ctx.Query("skills")
	queryPosition := ctx.Query("positions")
	queryPage := ctx.Query("page")
	queryLimit := ctx.Query("limit")

	var skills []string
	if querySkill != "" {
		skills = strings.Split(querySkill, ",")
	}

	var positions []string
	if queryPosition != "" {
		positions = strings.Split(queryPosition, ",")
	}

	page, err := strconv.Atoi(queryPage)
	limit, err := strconv.Atoi(queryLimit)
	if err != nil {
		ctx.JSON(http.StatusBadRequest, domain.NewError(http.StatusBadRequest, err.Error()))
		return
	}

	recruitments, err := r.service.FindAll(keyword, skills, positions, page, limit)
	if err != nil {
		ctx.JSON(http.StatusInternalServerError, domain.NewError(http.StatusInternalServerError, err.Error()))
		return
	}

	ctx.JSON(http.StatusOK, recruitments)
	return
}

func (r *recruitmentController) Register(ctx *gin.Context) {
	userID, exists := ctx.Get("userID")
	if !exists {
		ctx.JSON(http.StatusBadRequest, domain.NewError(http.StatusBadRequest, "user id is required"))
		return
	}

	input := &domain.RegisterRecruitment{}
	err := ctx.ShouldBindJSON(input)
	if err != nil {
		ctx.JSON(http.StatusBadRequest, domain.NewError(http.StatusBadRequest, err.Error()))
		return
	}

	err = r.service.Register(userID.(string), input)
	if err != nil {
		log.Println(err.Error())
		ctx.JSON(http.StatusInternalServerError, domain.NewError(http.StatusInternalServerError, err.Error()))
		return
	}
	ctx.Status(http.StatusCreated)
	return
}

func (r *recruitmentController) DeleteByID(ctx *gin.Context) {
	recruitmentID, err := strconv.Atoi(ctx.Param("id"))
	if err != nil {
		ctx.JSON(http.StatusBadRequest, domain.NewError(http.StatusBadRequest, err.Error()))
		return
	}

	id, exists := ctx.Get("userID")
	if !exists {
		ctx.JSON(http.StatusBadRequest, domain.NewError(http.StatusBadRequest, "user id is required"))
		return
	}

	err = r.service.DeleteByID(id.(string), recruitmentID)
	if err != nil {
		ctx.JSON(http.StatusInternalServerError, domain.NewError(http.StatusInternalServerError, err.Error()))
		return
	}

	ctx.Status(http.StatusOK)
	return
}

func (r *recruitmentController) Update(ctx *gin.Context) {
	recruitmentID, err := strconv.Atoi(ctx.Param("id"))
	if err != nil {
		ctx.JSON(http.StatusBadRequest, domain.NewError(http.StatusBadRequest, err.Error()))
		return
	}

	input := &domain.UpdateRecruitment{}
	err = ctx.ShouldBindJSON(input)
	if err != nil {
		ctx.JSON(http.StatusBadRequest, domain.NewError(http.StatusBadRequest, err.Error()))
		return
	}

	sub, exists := ctx.Get("userID")
	if !exists {
		ctx.JSON(http.StatusBadRequest, domain.NewError(http.StatusBadRequest, "user id is required"))
		return
	}

	err = r.service.Update(sub.(string), recruitmentID, input)
	if err != nil {
		ctx.JSON(http.StatusInternalServerError, domain.NewError(http.StatusInternalServerError, err.Error()))
		return
	}

	ctx.Status(http.StatusOK)
	return
}
