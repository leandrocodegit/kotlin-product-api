package com.produto.api.mapper

import com.produto.api.build.ProductBuild
import com.produto.api.controller.request.ProductRequest
import com.produto.api.controller.response.ProductResponse
import com.produto.api.model.Product
import org.junit.jupiter.api.Test
import org.mapstruct.factory.Mappers

class ProductMapperTest {

    private val productMapper = Mappers.getMapper(ProductMapper::class.java)

    private lateinit var product: Product
    private lateinit var productResponse: ProductResponse
    private lateinit var productRequest: ProductRequest

    @Test
    fun `test mapper product entity to response`() {

        product = ProductBuild().createProduct()
        productResponse = productMapper.toResponse(product)

        assert(product.id == productResponse.id)
        assert(product.nome == productResponse.nome)
        assert(product.quantidade == productResponse.quantidade)
        assert(product.valor == productResponse.valor)
        assert(product.descricao == productResponse.descricao)
        assert(product.isActive == productResponse.isActive)

    }

    @Test
    fun `test mapper product requst to entity`() {

        productRequest = ProductBuild().createProductRequestValid()
        product = productMapper.toEntity(productRequest)

        assert(product.nome == productRequest.nome)
        assert(product.quantidade == productRequest.quantidade)
        assert(product.valor == productRequest.valor)
        assert(product.descricao == productRequest.descricao)
        assert(product.isActive == productRequest.isActive)

    }
}