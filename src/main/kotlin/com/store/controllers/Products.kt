package com.store.controllers

import com.store.models.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.*
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.validation.Valid

@RestController
@RequestMapping("/products")
class ProductsController {
    private val products = mutableMapOf<Int, Product>()

    // this logic can be moved to service incase we are using DB
    // then service can call repo layer to fetch this data
    init {
        // Populate initial data
        products[1] = Product(id = 1, name = "XYZ Phone", type = ProductType.gadget, inventory = 2, cost = 10)
        products[2] = Product(id = 2, name = "ABC Book", type = ProductType.book, inventory = 10, cost = 10)
        products[3] = Product(id = 3, name = "DEF Food", type = ProductType.food, inventory = 5, cost = 10)
        products[4] = Product(id = 4, name = "GHI Other", type = ProductType.other, inventory = 1, cost = 10)
    }

    @GetMapping
    fun getProducts(@RequestParam(required = false) type: ProductType?): ResponseEntity<List<Product>> {
        return if (type == null) {
            ok(products.values.toList())
        } else {
            val filteredProducts = products.values.filter { it.type == type }
            ok(filteredProducts)
        }
    }

    @PostMapping
    fun createProduct(@Valid @RequestBody productDetails: ProductDetails): ResponseEntity<Any> {
        if (validateProduct(productDetails)) {
            val error = "Invalid product details";
            return status(HttpStatus.BAD_REQUEST).body(returnBadRequest(error))
        }

        val newId = products.keys.max() + 1
        products[newId] = newProduct(newId, productDetails)
        return status(HttpStatus.CREATED).body(ProductId(newId))
    }

    private fun newProduct(newId: Int, productDetails: ProductDetails): Product {
        return Product(
            id = newId,
            name = productDetails.name,
            type = ProductType.valueOf(productDetails.type),
            inventory = productDetails.inventory,
            cost = productDetails.cost!!
        )
    }

    private fun returnBadRequest(error: String) = ErrorResponseBody(
        timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
        status = HttpStatus.BAD_REQUEST.value(),
        error = error,
        path = "/products"
    )

    private fun validateProduct(productDetails: ProductDetails) =
        !isValidString(productDetails.name) ||
                !enumValues<ProductType>().any { it.name.equals(productDetails.type) }

    private fun isValidString(value: String?): Boolean {
        return value != null &&
                value !in listOf("true", "false") &&
                value.matches(Regex("^[a-zA-Z\\s]+$"))
    }
}


@ResponseStatus(HttpStatus.BAD_REQUEST)
class InvalidProductTypeException(message: String) : RuntimeException(message)
