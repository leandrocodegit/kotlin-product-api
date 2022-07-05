package com.produto.api.controller.response

import com.fasterxml.jackson.annotation.JsonProperty

class ProductResponse(
    var id: Long,
    var nome: String,
    var quantidade: Int,
    var valor: Double,
    var descricao: String,
    @JsonProperty("is_active")
    var isActive: Boolean) {
}