package com.produto.api.repository

import com.produto.api.build.ProductBuild
import com.produto.api.model.Product
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.*

@ExtendWith(MockKExtension::class)
class ProductRepositoryTest {

    @MockK
    private lateinit var productRepository: ProductRepository
    private lateinit var productOne: Product
    private lateinit var productTwo: Product

    @BeforeEach
    fun setup(){
        productOne = ProductBuild().createProduct()
        productTwo = ProductBuild().createProduct()

        every { productRepository.saveAll(listOf(productOne, productTwo)) }
    }

    @Test
    fun `test create new product`(){

        every { productRepository.save(productOne) } returns productOne
        var newProduct: Product = productRepository.save(productOne)
        assert(productOne.id == newProduct.id)

    }

    @Test
    fun `test find  product id`(){

        every { productRepository.findById(productTwo.id) } returns Optional.of(productTwo)
        every { productRepository.findById(4444444444444444444L) } returns Optional.empty()
        assert(productRepository.findById(productTwo.id).isPresent)
        assert(productRepository.findById(4444444444444444444L).isEmpty)

    }

}