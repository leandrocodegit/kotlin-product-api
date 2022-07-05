package com.produto.api.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,
    var nome: String,
    var quantidade: Int,
    var valor: Double,
    var descricao: String,
    @Column(name = "is_active")
    var isActive: Boolean) {
}