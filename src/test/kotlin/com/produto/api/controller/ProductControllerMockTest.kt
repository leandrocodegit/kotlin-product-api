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
class ProductControllerMockTest {

    @Autowired
    private lateinit var mockMvc: MockMvc
    @MockkBean
    private lateinit var productService: ProductService
    @Autowired
    private lateinit var productServiceAuto: ProductService
    @MockK
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
    fun `test create new product valid`(){

        every { productService.createPorduct(any()) } returns productOne
        every { productRepository.save(any()) } returns productOne
        every { productRepository.findByNome(productRequestValid.nome) } returns Optional.empty()

        val id = productOne.id
        val body = ObjectMapper().writeValueAsString(productRequestValid)

        this.mockMvc.perform(
            post(URL_PRODUCTS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andDo(  print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(id))

    }

    @Test
    fun `test create new product invalid`(){

        val body = ObjectMapper().writeValueAsString(productRequestInvalid)

        this.mockMvc.perform(
            post(URL_PRODUCTS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andDo(  print())
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.containsError").value(true))

    }

    @Test
    fun `test create new product invalid contains error`(){

        val body = ObjectMapper().writeValueAsString(productRequestInvalid)

        this.mockMvc.perform(
            post(URL_PRODUCTS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andDo(  print())
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.containsError").value(true))

    }

    @Test
    fun `test create new product valid already`(){

        every { productService.createPorduct(any()) }  throws (EntityExecption("",CodeError.DUPLICATE))
        every { productRepository.findByNome(productRequestValid.nome) } returns Optional.of(productOne)

        val body = ObjectMapper().writeValueAsString(productRequestValid)

        this.mockMvc.perform(
            post(URL_PRODUCTS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andDo(  print())
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.containsError").value(true))

    }

    @Test
    fun `test get product id`(){

        every { productService.buscaProduct(productOne.id) } returns productOne
        every { productRepository.findById(productOne.id) } returns Optional.of(productOne)

        this.mockMvc.perform(
            MockMvcRequestBuilders.get(URL_PRODUCT_ID, productOne.id)
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.id").value(productOne.id))
    }

    @Test
    fun `test get product id not found`(){

        every { productService.buscaProduct(200) } throws (EntityExecption("Erro",CodeError.NOT_FOUND))
       // every { productRepository.findById(200) } returns Optional.empty()

        this.mockMvc.perform(
            MockMvcRequestBuilders.get(URL_PRODUCT_ID, 200)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.containsError").value(true))
    }

    @Test
    fun `test list all product`(){

        every { productService.listAllPorducts() } returns productRepository.findAll()

        this.mockMvc.perform(
            MockMvcRequestBuilders.get(URL_PRODUCTS)
            .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].id").exists())
            .andExpect(jsonPath("$[0].id").value(productOne.id))
    }

    @Test
    fun `update status product active to inactive`(){

        every { productService.updateStatusProduct(productOneActive.id, StatusProduct.INACTIVE) } returns productTwoInactive
        every { productRepository.findById(any()) } returns Optional.of(productTwoInactive)

        this.mockMvc.perform(
            MockMvcRequestBuilders.patch(
                "${URL_PRODUCTS}$URL_PRODUCT_DESACTIVE",
                productOneActive.id
            )
        )
            .andDo (print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.is_active").value(false))
    }

    @Test
    fun `update status product inactive to active`(){

        every { productService.updateStatusProduct(productTwoInactive.id, StatusProduct.ACTIVE) } returns productOneActive
        every { productRepository.findById(any()) } returns Optional.of(productOneActive)

        this.mockMvc.perform(MockMvcRequestBuilders.patch("${URL_PRODUCTS}$URL_PRODUCT_ACTIVE", productTwoInactive.id))
            .andDo (print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.is_active").value(true))
    }

    @Test
    fun `update status product active already active`(){

        every { productService.updateStatusProduct(productOneActive.id, StatusProduct.ACTIVE) } throws (EntityExecption("Erro",CodeError.ACTIVE))
        every { productRepository.findById(any()) } returns Optional.of(productOneActive)

        this.mockMvc.perform(
            MockMvcRequestBuilders.patch(
                "${URL_PRODUCTS}$URL_PRODUCT_ACTIVE",
                productOneActive.id
            )
        )
            .andDo (print())
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.containsError").value(true))
    }

    @Test
    fun `update status product inactive already inactive`(){

        every { productService.updateStatusProduct(productTwoInactive.id, StatusProduct.INACTIVE) } throws (EntityExecption("Erro",CodeError.INACTIVE))
        every { productRepository.findById(any()) } returns Optional.of(productTwoInactive)

        this.mockMvc.perform(MockMvcRequestBuilders.patch("${URL_PRODUCTS}$URL_PRODUCT_DESACTIVE", productTwoInactive.id))
            .andDo (print())
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.containsError").value(true))
    }

}