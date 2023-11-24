package com.example.optilens.dataclass

import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject


interface Product {
    val productId: String
    val productName: String
    val category: String
    val price: Double

    // Add other common properties/methods as needed
}



data class ProductCategory(
    val eyeglasses: List<Eyeglass>,
    val sunglasses: List<Sunglass>,
    val contactlens: List<ContactLens>,
    val accessories: List<Accessory>
)

data class Eyeglass(
    override val productId: String,
    override val productName: String,
    override val category: String,
    val brand: String,
    val frameColor: String,
    val frameStyle: String,
    val gender: String,
    val dimensions: Dimensions,
    val specifications: Specifications,
    override val price: Double,
    val ratings: Ratings,
    val images: Images
) : Product

data class Sunglass(
    override val productId: String,
    override val productName: String,
    override val category: String,
    val brand: String,
    val frameColor: String,
    val frameStyle: String,
    val lensMaterial: String,
    val lensColor: String,
    val uvProtection: String,
    val dimensions: Dimensions,
    val specifications: Specifications,
    override val price: Double,
    val ratings: Ratings,
    val images: Images
) : Product

data class ContactLens(
    override val productId: String,
    override val productName: String,
    override val category: String,
    val brand: String,
    val lensType: String,
    val material: String,
    val waterContent: Double,
    val spherePowerRange: String,
    val cylinderPowerRange: String,
    val availability: String,
    override val price: Double,
    val ratings: Ratings,
    val images: Images,
    val productDetails: ProductDetails
) : Product

data class Accessory(
    override val productId: String,
    override val productName: String,
    override val category: String,
    val brand: String,
    val accessoryType: String,
    val color: String,
    override val price: Double,
    val ratings: Ratings,
    val images: Images,
    val productDetails: ProductDetails
) : Product

data class Dimensions(
    val width: Int,
    val height: Int,
    val bridgeSize: Int,
    val templeLength: Int
)

data class Specifications(
    val prescription: Boolean,
    val uvProtection: Boolean,
    val antiReflectiveCoating: Boolean
)

data class Ratings(
    val averageRating: Double,
    val numberOfRatings: Int
)

data class Images(
    val frontView: String,
    val sideView: String,
    val backView: String
)

data class ProductDetails(
    val comfortDuration: String,
    val uvProtection: String,
    val tinted: Boolean,
    val availableColors: List<String>,
    val recommendedUse: String,
    val replacementSchedule: String
)




data class Glasses(
    val productId: String,
    val productName: String,
    val category: String,
    val brand: String,
    val frameColor: String,
    val frameStyle: String,
    val gender: String,
    val dimensions: Dimensions,
    val specifications: Specifications,
    val price: Double,
    val ratings: Ratings,
    val images: Images
)





fun parseProductList(json: String): List<Glasses> {
    val jsonObject = JSONObject(json)
    val eyeglassesArray = jsonObject.getJSONArray("eyeglasses")
    val sunglassesArray = jsonObject.getJSONArray("sunglasses")
    val contactLensArray = jsonObject.getJSONArray("contactlens")
    val accessoriesArray = jsonObject.getJSONArray("accessories")

    val productList = mutableListOf<Glasses>()

    productList.addAll(parseProducts(eyeglassesArray))
    productList.addAll(parseProducts(sunglassesArray))
    productList.addAll(parseProducts(contactLensArray))
    productList.addAll(parseProducts(accessoriesArray))

    return productList
}

private fun parseProducts(array: JSONArray): List<Glasses> {
    val gson = Gson()
    val productList = mutableListOf<Glasses>()

    for (i in 0 until array.length()) {
        val productObject = array.getJSONObject(i)
        val product = gson.fromJson(productObject.toString(), Glasses::class.java)
        productList.add(product)
    }

    return productList
}



