package com.produto.api.exceptions

import com.produto.api.enum.CodeError
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler

class EntityExecption(message: String, var codeError: CodeError): CustomException(message){
    private val serialVersionUID = 15600000015825L
}