package com.store.models

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern


enum class ProductType {
    book, food, gadget, other
}

data class ProductDetails @JsonCreator constructor(
    @field:NotNull
    @field:Pattern(
        regexp = "^(?i)(?!true|false)[a-z\\s]*\$",
        message = "Name must not be a boolean value"
    )
    @JsonProperty("name") val name: String,
    @field:NotNull
    @JsonProperty("type") val type: ProductType,
    @field:NotNull
    @field:Min(value = 1, message = "Inventory must be at least 1")
    @JsonProperty("inventory") val inventory: Int,
    @field:NotNull
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
