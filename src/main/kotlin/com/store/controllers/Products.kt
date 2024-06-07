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

    @GetMapping
    fun getProducts(@RequestParam(required = false) type: String?): ResponseEntity<Any> {
        return try {
            if (type == null) {
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ErrorResponseBody(
                        timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                        status = HttpStatus.BAD_REQUEST.value(),
                        error = "Missing product type",
                        path = "/products"
                    )
                )
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
        if (productDetails.name.isBlank() || productDetails.inventory <= 0) {
            val errorResponse = ErrorResponseBody(
                timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                status = HttpStatus.BAD_REQUEST.value(),
                error = "Invalid product details",
                path = "/products"
            )
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse)
        }

        val newId = (products.keys.maxOrNull() ?: 0) + 1
        val newProduct = Product(
            id = newId,
            name = productDetails.name,
            type = productDetails.type,
            inventory = productDetails.inventory
        )
        products[newId] = newProduct

        return ResponseEntity.status(HttpStatus.CREATED).body(ProductId(newId))
    }
}
