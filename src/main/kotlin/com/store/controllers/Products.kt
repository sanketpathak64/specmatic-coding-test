package com.store.controllers

import com.store.models.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/products")
class Products {
        private val products = mutableMapOf<Long, Product>(
        1L to Product(1, "XYZ Phone", ProductType.gadget, 2),
        2L to Product(2, "XYZ Book", ProductType.book, 10),
        3L to Product(3, "XYZ Food", ProductType.food, 5)
    )

    @GetMapping
    fun getProductsByType(@RequestParam(required = false) type: ProductType?): ResponseEntity<List<Product>> {
        val filteredProducts = if (type != null) {
            products.values.filter { it.type == type }
        } else {
            products.values.toList()
        }
        return ResponseEntity.ok(filteredProducts)
    }

     @PostMapping
    fun createProduct(@RequestBody productDetails: ProductDetails): ResponseEntity<ProductId> {
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
