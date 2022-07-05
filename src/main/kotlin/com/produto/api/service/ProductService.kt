package com.produto.api.service

import com.produto.api.enum.CodeError
import com.produto.api.enum.StatusProduct
import com.produto.api.exceptions.EntityExecption
import com.produto.api.model.Product
import com.produto.api.repository.ProductRepository
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class ProductService(private var productRepository: ProductRepository) {

    fun createPorduct(produto: Product): Product{
        var optProduct: Optional<Product> = productRepository.findByNome(produto.nome)
            if(optProduct.isPresent) throw EntityExecption("Ja existe um produto com nome ${produto.nome}", CodeError.DUPLICATE)
        return productRepository.save(produto)
    }
    fun listAllPorducts(): List<Product>{
        return productRepository.findAll()
    }
    fun buscaProduct(id: Long):Product{
        var optProduct: Optional<Product> =  productRepository.findById(id)
        return optProduct.orElseThrow { throw EntityExecption("Id de produto nao encontrado", CodeError.NOT_FOUND) }
    }
    fun updateStatusProduct(id: Long, statusProduct: StatusProduct): Product{
        var product: Product = buscaProduct(id)
        if(product.isActive == statusProduct.value)
            when(product.isActive){
                StatusProduct.ACTIVE.value -> throw EntityExecption("Produto ja esta ativo", CodeError.ACTIVE)
                StatusProduct.INACTIVE.value -> throw EntityExecption("Produto ja esta inativo", CodeError.INACTIVE)
            }
        product.isActive = statusProduct.value
        return productRepository.save(product)
    }


}