package com.example.optilens.dataclass

import java.util.*

data class User(
    val userId: String?=null,
    val name: String? = null,
    val address: String? = null,
    val phoneNumber: String? = null,
    val email: String? = null,
    val password: String? = null,
    val gender: String? = null,
    val profilePicture: String? = null,
    val wishlist: List<WishlistItem> = emptyList(),
    val cart: List<CartItem> = emptyList(),
    val orders: List<Order> = emptyList(),
    val lensPrescription: LensPrescription? = null

){
    fun updateGender(newGender: String?): User {
        return this.copy(gender = newGender)
    }
}

data class WishlistItem(
    val productId: Int,
    val productName: String
)

data class CartItem(
    val productId: Int,
    val productName: String,
    val price: Double,
    val subtotal: Double
)

data class Order(
    val orderId: String,
    val products: List<OrderedProduct>,
    val totalAmount: Double,
    val orderDate: Date,
    val status: String
)

data class OrderedProduct(
    val productId: Int,
    val productName: String,
    val price: Double,
    val subtotal: Double
)

data class LensPrescription(
    val userId: String?=null,
    val sphRight: String? = null,
    val cylRight: String? = null,
    val axisRight: String? = null,
    val sphLeft: String? = null,
    val cylLeft: String? = null,
    val axisLeft: String? = null
)
