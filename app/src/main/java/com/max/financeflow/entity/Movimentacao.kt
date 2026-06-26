package com.max.financeflow.entity

data class Movimentacao(
    val id: Int,
    val valor: Double,
    val descricao: String,
    val data: String,
    val tipo: String
)
