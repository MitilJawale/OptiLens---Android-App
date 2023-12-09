package com.example.optilens.dataclass

data class User(
    val name: String? = null,
    val address: String? = null,
    val phoneNumber: String? = null,
    val email: String? = null,
    val password: String? = null,
    val gender: String? = null,
    val profilePicture: String? = "",
    val wishlist: List<WishlistItem> = emptyList(),
    val cart: List<CartItem> = emptyList(),
    val orders: List<Order> = emptyList(),
    val lensPrescription: LensPrescription? = null
)

data class WishlistItem(
    val productId: String,
    val productName: String
){
    // No-argument constructor required by Firebase
    constructor() : this("", "")
}

data class CartItem(
    val productId: String? = null,
    val productName: String? = null,
    val price: Double? = null
) {
    // Add a no-argument constructor
    constructor() : this(null, null, null)
}

//data class Order(
//    val orderId: MutableList<String>,
//    val products: List<CartItem>,
//    val totalAmount: Double,
//    val orderDate: Long,
//    val status: String
//)

data class Order(
    val orderId: String,
    val products: List<CartItem>,
    val totalAmount: Double,
    val orderDate: Long,
    val status: String
){
    // No-argument constructor required by Firebase
    constructor() : this("", emptyList(), 0.0, 0, "")
}

data class OrderedProduct(
    val productId: Int,
    val productName: String,
    val price: Double,
    val subtotal: Double
)

data class LensPrescription(
    val sphRight: String? = null,
    val cylRight: String? = null,
    val axisRight: String? = null,
    val sphLeft: String? = null,
    val cylLeft: String? = null,
    val axisLeft: String?=null
)
