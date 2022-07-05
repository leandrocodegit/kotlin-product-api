package com.produto.api.build

import com.produto.api.controller.request.ProductRequest
import com.produto.api.model.Product
import java.util.Random


class ProductBuild(var id: Long = 0,
                   var nome: String = "Produto de teste",
                   var quantidade: Int = 10,
                   var valor: Double = 19.90,
                   var descricao: String = "Descricao de teste",
                   var isActive: Boolean = true) {

    fun createProduct(): Product{
        return Product(
            Random().nextLong(100000),
            "${nome} one ${Random().nextLong(100000)}",
            quantidade,
            valor,
            descricao,
            isActive)
    }

    fun createProductActive(): Product{
        return Product(
            Random().nextLong(100000),
            "${nome} active",
            quantidade,
            valor,
            descricao,
            true)
    }

    fun createProductInactive(): Product{
        return Product(
            Random().nextLong(100000),
            "${nome} inactive",
            quantidade,
            valor,
            descricao,
            false)
    }

    fun createProductRequestValid(): ProductRequest{
        return ProductRequest(
            "${nome} valid",
            quantidade,
            valor,
            descricao,
            isActive)
    }

    fun createProductRequestInvalid(): ProductRequest{
        return ProductRequest(
            "",
            0,
            valor,
            descricao,
            isActive)
    }
}