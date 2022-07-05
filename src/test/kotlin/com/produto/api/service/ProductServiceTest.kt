package com.produto.api.service

import com.produto.api.build.ProductBuild
import com.produto.api.enum.StatusProduct
import com.produto.api.exceptions.EntityExecption
import com.produto.api.model.Product
import com.produto.api.repository.ProductRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import java.util.*

@ExtendWith(MockKExtension::class)
class ProductServiceTest {

    @InjectMockKs
    private lateinit var productService: ProductService
    @MockK
    private lateinit var productRepository: ProductRepository
    private lateinit var product: Product
    private lateinit var productActive: Product
    private lateinit var productInactive: Product

    @BeforeEach
    fun setup(){

        productActive = ProductBuild().createProductActive()
        productInactive = ProductBuild().createProductInactive()
        product = ProductBuild().createProduct()
        every { productRepository.findByNome(product.nome) } returns Optional.empty()
        every { productRepository.save(product) } returns product

        every { productRepository.saveAll(listOf(productActive,productInactive)) }
    }

    @Test
    fun `test create new produto`(){

        var newProduct: Product = productService.createPorduct(product)
        assert(newProduct.id == product.id)
        assert(newProduct.nome == product.nome)

    }

    @Test
    fun `test create produto already exist`(){

        every { productRepository.findByNome(product.nome) } returns Optional.of(product)
        assertThrows<EntityExecption> { productService.createPorduct(product) }

    }

    @Test
    fun `test active product active`(){

        every { productRepository.findById(productInactive.id) } returns Optional.of(productInactive)
        every { productRepository.save(productInactive) } returns productInactive
        assert( productService.updateStatusProduct(productInactive.id,StatusProduct.ACTIVE).isActive )

    }

    @Test
    fun `test active product inactive`(){

        every { productRepository.findById(productActive.id) } returns Optional.of(productActive)
        every { productRepository.save(productActive) } returns productActive
        assert( !productService.updateStatusProduct(productActive.id,StatusProduct.INACTIVE).isActive )

    }

    @Test
    fun `test active product already active`(){

        every { productRepository.findById(productActive.id) } returns Optional.of(productActive)
        assertThrows<EntityExecption> { productService.updateStatusProduct(productActive.id,StatusProduct.ACTIVE) }

    }

    @Test
    fun `test active product already inactive`(){

        every { productRepository.findById(productInactive.id) } returns Optional.of(productInactive)
        assertThrows<EntityExecption> { productService.updateStatusProduct(productInactive.id,StatusProduct.INACTIVE) }

    }
}