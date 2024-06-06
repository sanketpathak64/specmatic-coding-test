package com.store.models

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class ProductDetails @JsonCreator constructor(
    @JsonProperty("name") val name: String,
    @JsonProperty("type") val type: ProductType,
    @JsonProperty("inventory") val inventory: Int
)

data class ProductId(
    val id: Long
)

data class Product(
    val id: Long,
    val name: String,
    val type: ProductType,
    val inventory: Int
)

enum class ProductType {
    book, food, gadget, other
}

data class ErrorResponseBody(
    val timestamp: String,
    val status: Int,
    val error: String,
    val path: String
)
