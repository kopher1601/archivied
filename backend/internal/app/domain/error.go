package domain

import "net/http"

type Error struct {
	Code    int    `json:"code"`
	Message string `json:"message"`
}

func NewError(code int, message ...string) *Error {
	err := &Error{
		Code:    code,
		Message: http.StatusText(code),
	}
	if len(message) > 0 {
		err.Message = message[0]
	}
	return err
}
