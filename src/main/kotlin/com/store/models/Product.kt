package com.store.models

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty


enum class ProductType {
    book, food, gadget, other
}

data class ProductDetails @JsonCreator constructor(
    @JsonProperty("name") val name: String,
    @JsonProperty("type") val type: String,
    @JsonProperty("inventory") val inventory: Int?,
    @JsonProperty("cost") val cost: Int?,
)

data class Product @JsonCreator constructor(
    @JsonProperty("id") val id: Int,
    @JsonProperty("name") val name: String,
    @JsonProperty("type") val type: ProductType,
    @JsonProperty("inventory") val inventory: Int,
    @JsonProperty("cost") val cost: Int,
)

data class ProductId @JsonCreator constructor(
    @JsonProperty("id") val id: Int
)

data class ErrorResponseBody @JsonCreator constructor(
    @JsonProperty("timestamp") val timestamp: String,
    @JsonProperty("status") val status: Int,
    @JsonProperty("error") val error: String,
    @JsonProperty("path") val path: String
)
