package com.produto.api.mapper

import com.produto.api.controller.request.ProductRequest
import com.produto.api.controller.response.ProductResponse
import com.produto.api.model.Product
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper
interface ProductMapper {

    @Mappings(
        Mapping(source = "active", target = "isActive"),
        Mapping(target = "id", ignore = true)
    )
    fun toEntity(productRequest: ProductRequest): Product
    @Mapping(source = "active", target = "isActive")
    fun toResponse(product: Product): ProductResponse
}