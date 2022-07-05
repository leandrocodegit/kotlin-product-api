package com.produto.api.controller

import com.produto.api.controller.request.ProductRequest
import com.produto.api.controller.response.ProductResponse
import com.produto.api.enum.StatusProduct
import com.produto.api.mapper.ProductMapper
import com.produto.api.model.Product
import com.produto.api.service.ProductService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiOperation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.stream.Collectors
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/products")
@Api(tags= ["Endpoints produtos"], description = "Documentação completa")
class ProductController(
    private val productService: ProductService,
    private val productMapper: ProductMapper
) {

    @GetMapping("/{id}")
    @ApiOperation(value="Find product by ID")
    fun buscaProduct(@PathVariable id: Long): ResponseEntity<ProductResponse>{
        return ResponseEntity.ok(productMapper.toResponse(productService.buscaProduct(id)))
    }

    @GetMapping
    @ApiOperation(value="List all products ")
    fun listAllProducts(): ResponseEntity<List<ProductResponse>>{
        var listResponse: List<ProductResponse> = productService
            .listAllPorducts()
            .stream()
            .map { product -> productMapper.toResponse(product) }
            .collect(Collectors.toList())
        return ResponseEntity.ok(listResponse)
    }

    @PostMapping
    @ApiOperation(value="Create new product request")
    fun createNewProduct(@RequestBody @Valid productRequest: ProductRequest): ResponseEntity<ProductResponse>{
        var product: Product = productService.createPorduct(productMapper.toEntity(productRequest))
        return ResponseEntity.ok(productMapper.toResponse(product))
    }

    @PatchMapping("/{id}/active")
    @ApiOperation(value="Active product")
    fun activeStatusProduct(@PathVariable id: Long): ResponseEntity<ProductResponse>{
        return ResponseEntity.ok(productMapper.toResponse(productService.updateStatusProduct(id, StatusProduct.ACTIVE)))
    }

    @PatchMapping("/{id}/desactive")
    @ApiOperation(value="Desactive product")
    fun inactiveStatusProduct(@PathVariable id: Long): ResponseEntity<ProductResponse>{
        return ResponseEntity.ok(productMapper.toResponse(productService.updateStatusProduct(id, StatusProduct.INACTIVE)))
    }

}