package com.store.controllers

import com.store.models.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RestController
@RequestMapping("/products")
class ProductsController {
    private val products = mutableMapOf<Int, Product>()

    init {
        // Populate initial data
        products[1] = Product(id = 1, name = "XYZ Phone", type = ProductType.gadget, inventory = 2)
        products[2] = Product(id = 2, name = "ABC Book", type = ProductType.book, inventory = 10)
        products[3] = Product(id = 3, name = "DEF Food", type = ProductType.food, inventory = 5)
        products[4] = Product(id = 4, name = "GHI Other", type = ProductType.other, inventory = 1)
    }

    @GetMapping
    fun getProducts(@RequestParam(required = false) type: String?): ResponseEntity<Any> {
        return try {
            if (type == null || type !is String) {
                ResponseEntity.ok(emptyList<Any>())
            } else {
                val productType = ProductType.valueOf(type)
                val filteredProducts = products.values.filter { it.type == productType }
                ResponseEntity.ok(filteredProducts)
            }
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ErrorResponseBody(
                    timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                    status = HttpStatus.BAD_REQUEST.value(),
                    error = "Invalid product type",
                    path = "/products"
                )
            )
        }
    }

    @PostMapping
    fun createProduct(@RequestBody productDetails: ProductDetails): ResponseEntity<Any> {
        if (productDetails.inventory == null || productDetails.inventory !is Int ||
            !isValidString(productDetails.name) ||
            productDetails.type == null ||
            !enumValues<ProductType>().any { it.name.equals(productDetails.type) }
        ) {
            val errorMessage = "Invalid product details"

            val errorResponse = ErrorResponseBody(
                timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                status = HttpStatus.BAD_REQUEST.value(),
                error = errorMessage,
                path = "/products"
            )
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse)
        }

        val newId = products.keys.max() + 1
        val newProduct = Product(
            id = newId,
            name = productDetails.name,
            type = ProductType.valueOf(productDetails.type),
            inventory = productDetails.inventory
        )
        products[newId] = newProduct

        return ResponseEntity.status(HttpStatus.CREATED).body(ProductId(newId))
    }

    private fun isValidString(value: String?): Boolean {
        return value != null && value.matches(Regex("^[a-zA-Z\\s]+$")) && value !in listOf("true", "false")
    }
}
