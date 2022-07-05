package com.produto.api.exceptions

open class CustomException(message: String): RuntimeException(message) {
    private val serialVersionUID = 15685850015825L
}