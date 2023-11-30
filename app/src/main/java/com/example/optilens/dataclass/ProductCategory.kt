package com.example.optilens.dataclass

import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject


interface Product {
    val productId: String
    val productName: String
    val category: String
    val price: Double
    fun getImageUrls(): List<String> // Add this method
    val brand: String
    val ratings: Ratings
    val frameColor: String
    val productStyle: String
    val dimensions: Dimensions
    // Add other common properties/methods as needed
}


data class ConcreteProduct(
    override val productId: String,
    override val productName: String,
    override val category: String,
    override val price: Double,
    override val brand: String,
    override val ratings: Ratings,
    override val frameColor: String,
    override val productStyle: String,
    override val dimensions: Dimensions,
    val images: List<String>
) : Product {
    override fun getImageUrls(): List<String> {
        return images
    }
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
    override val brand: String,
    override val frameColor: String,
    override val productStyle: String,
    val gender: String,
    override val dimensions: Dimensions,
    val specifications: Specifications,
    override val price: Double,
    override val ratings: Ratings,
    val images: Images
) : Product {
    override fun getImageUrls(): List<String> {
        return listOf(images.frontView, images.sideView, images.backView)
    }
}

data class Sunglass(
    override val productId: String,
    override val productName: String,
    override val category: String,
    override val brand: String,
    override val frameColor: String,
    override val productStyle: String,
    val lensMaterial: String,
    val lensColor: String,
    val uvProtection: String,
    override val dimensions: Dimensions,
    val specifications: Specifications,
    override val price: Double,
    override val ratings: Ratings,
    val images: Images
) : Product {
    override fun getImageUrls(): List<String> {
        return listOf(images.frontView, images.sideView, images.backView)
    }
}

data class ContactLens(
    override val productId: String,
    override val productName: String,
    override val category: String,
    override val brand: String,
    override val productStyle: String,
    val material: String,
    val waterContent: Double,
    val spherePowerRange: String,
    val cylinderPowerRange: String,
    val availability: String,
    override val price: Double,
    override val ratings: Ratings,
    val images: Images,
    val productDetails: ProductDetails,

    override val frameColor: String
) : Product {
    override fun getImageUrls(): List<String> {
        return listOf(images.frontView, images.sideView, images.backView)
    }

    override val dimensions: Dimensions
        get() = Dimensions(
            width = -99,       // Replace with the actual width
            height = -99,       // Replace with the actual height
            bridgeSize = -99,   // Replace with the actual bridge size
            templeLength = -99 // Replace with the actual temple length
        )
}

data class Accessory(
    override val productId: String,
    override val productName: String,
    override val category: String,
    override val brand: String,
    override val productStyle: String,
    val color: String,
    override val price: Double,
    override val ratings: Ratings,
    val images: Images,
    val productDetails: ProductDetails,
    override val frameColor: String
) : Product {
    override fun getImageUrls(): List<String> {
        return listOf(images.frontView, images.sideView, images.backView)
    }

    override val dimensions: Dimensions
        get() = Dimensions(
            width = -99,       // Replace with the actual width
            height = -99,       // Replace with the actual height
            bridgeSize = -99,   // Replace with the actual bridge size
            templeLength = -99 // Replace with the actual temple length
        )
}

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



