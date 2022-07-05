package com.produto.api.controller.request

import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank

class ProductRequest(
    @field:NotBlank
    var nome: String,
    @field:Min(1)
    var quantidade: Int,
    @field:Min(1)
    var valor: Double,
    @field:NotBlank
    var descricao: String,
    var isActive: Boolean) {
}