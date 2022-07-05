package com.produto.api.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import com.produto.api.build.ProductBuild
import com.produto.api.constantes.URL_PRODUCTS
import com.produto.api.constantes.URL_PRODUCT_ACTIVE
import com.produto.api.constantes.URL_PRODUCT_DESACTIVE
import com.produto.api.constantes.URL_PRODUCT_ID
import com.produto.api.controller.request.ProductRequest
import com.produto.api.enum.CodeError
import com.produto.api.enum.StatusProduct
import com.produto.api.exceptions.EntityExecption
import com.produto.api.mapper.ProductMapper
import com.produto.api.model.Product
import com.produto.api.repository.ProductRepository
import com.produto.api.service.ProductService
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import net.bytebuddy.implementation.bytecode.Throw
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mapstruct.factory.Mappers
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import java.util.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith(MockKExtension::class)
class ActiveTest {

    @Autowired
    private lateinit var mockMvc: MockMvc
    @Autowired
    private lateinit var productService: ProductService
    @MockkBean
    private lateinit var productRepository: ProductRepository


    private var productMapper = Mappers.getMapper(ProductMapper::class.java)

    private lateinit var productOne: Product
    private lateinit var productTwo: Product
    private lateinit var productOneActive: Product
    private lateinit var productTwoInactive: Product
    private lateinit var productRequestValid: ProductRequest
    private lateinit var productRequestInvalid: ProductRequest

    @BeforeEach
    fun setup(){
        productOne = ProductBuild().createProduct()
        productTwo = ProductBuild().createProduct()
        productOneActive = ProductBuild().createProductActive()
        productTwoInactive = ProductBuild().createProductInactive()
        productRequestValid = ProductBuild().createProductRequestValid()
        productRequestInvalid = ProductBuild().createProductRequestInvalid()

        var list: List<Product> = listOf(productOne,productTwo)

        every { productRepository.saveAll(list) }
        every { productRepository.findAll() } returns list

    }

    @Test
    fun `update status product inactive to active`(){

        every { productRepository.save(productTwoInactive) } returns productTwoInactive
        every { productRepository.findById(productTwoInactive.id) } returns Optional.of(productTwoInactive)

        this.mockMvc.perform(MockMvcRequestBuilders.patch("${URL_PRODUCTS}$URL_PRODUCT_ACTIVE", productTwoInactive.id))
            .andDo (print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.is_active").value(true))
    }

}