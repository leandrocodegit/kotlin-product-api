package com.produto.api.exceptions

import com.produto.api.enum.CodeError
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.sql.Timestamp
import java.time.Instant

@ControllerAdvice
class CustomExceptionHandler(): ResponseEntityExceptionHandler() {

    @ExceptionHandler(EntityExecption::class)
    fun entitityCustomHandlerException(ex: EntityExecption): ResponseEntity<ResponseErro> {
        var error: ResponseErro =  ResponseErro(
            Timestamp.from(Instant.now()),
            "Erro na requisicao",
            ex.codeError.name,
            ex.codeError.value,
            ex.localizedMessage
        )
        return ResponseEntity(error,
            HttpHeaders(),
            HttpStatus.BAD_REQUEST)
    }

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        var error: ResponseErro =  ResponseErro(
            Timestamp.from(Instant.now()),
            "Formato invalido",
            CodeError.FORMAT_INVALID.name,
            CodeError.FORMAT_INVALID.value,
            ex.localizedMessage
        )
        return ResponseEntity.badRequest().body(error)
    }
}