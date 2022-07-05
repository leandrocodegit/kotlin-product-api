package com.produto.api.repository

import com.produto.api.model.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface ProductRepository: JpaRepository<Product, Long> {

    fun findByNome(nome: String):Optional<Product>
}