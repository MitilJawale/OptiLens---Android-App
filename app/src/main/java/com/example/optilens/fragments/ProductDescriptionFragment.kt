package com.example.optilens.fragments



import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.optilens.R
import com.example.optilens.adapter.ImageAdapter
import com.example.optilens.dataclass.Eyeglass


class ProductDescriptionFragment : Fragment() {

    private lateinit var viewPager: ViewPager2
    private lateinit var imageUrls: List<String>
    private lateinit var productNameTextView: TextView
    private lateinit var brandTextView: TextView
    private lateinit var priceTextView: TextView
    private lateinit var ratingsTextView: TextView
    private lateinit var frameColorTextView: TextView
    private lateinit var numberOfReviewsTextView: TextView
    private lateinit var productFrameStyle: TextView
    private lateinit var height: TextView
    private lateinit var width: TextView
    private lateinit var bridgeSize: TextView
    private lateinit var templeLength: TextView
    private lateinit var llSpecifications: LinearLayout
    private lateinit var productPrescriptionIV: ImageView
    private lateinit var productUVProtectionIV: ImageView
    private lateinit var productARC_IV: ImageView


    private lateinit var productMaterialTextView: TextView
    private lateinit var productWaterContentTextView: TextView
    private lateinit var productSpherePRTextView: TextView
    private lateinit var productCylinderPRTextView: TextView

    private lateinit var detail1TV: TextView
    private lateinit var detail2TV: TextView
    private lateinit var detail3TV: TextView
    private lateinit var detail11TV: TextView
    private lateinit var detail22TV: TextView
    private lateinit var detail33TV: TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_product_description, container, false)

        // Initialize TextViews
        productNameTextView = view.findViewById(R.id.tv_productName)
        brandTextView = view.findViewById(R.id.tv_productBrand)
        priceTextView = view.findViewById(R.id.tv_productPrice)
        ratingsTextView = view.findViewById(R.id.tv_productRatings)
        frameColorTextView = view.findViewById(R.id.tv_productColor)
        numberOfReviewsTextView = view.findViewById(R.id.tv_productViews)
        productFrameStyle = view.findViewById(R.id.tv_productFrameStyle)
        height = view.findViewById(R.id.tvText4)
        width = view.findViewById(R.id.tvText5)
        bridgeSize = view.findViewById(R.id.tvText6)
        templeLength = view.findViewById(R.id.tvText8)
        productPrescriptionIV = view.findViewById(R.id.iv_productPrescription)
        productUVProtectionIV = view.findViewById(R.id.iv_productuvProtection)
        productARC_IV = view.findViewById(R.id.iv_productantiReflectiveCoating)
        llSpecifications = view.findViewById(R.id.ll_eyeglass_sunglass)

        // Contact Lens Specifications
        productMaterialTextView = view.findViewById(R.id.tv_productMaterial)
        productWaterContentTextView = view.findViewById(R.id.tv_productWatercontent)
        productSpherePRTextView = view.findViewById(R.id.tv_productSpherePowerRange)
        productCylinderPRTextView = view.findViewById(R.id.tv_productCylinderPowerRange)


        detail1TV = view.findViewById(R.id.tv_accessoriesProductDetails1)
        detail2TV = view.findViewById(R.id.tv_accessoriesProductDetails2)
        detail3TV = view.findViewById(R.id.tv_accessoriesProductDetails3)
        detail11TV = view.findViewById(R.id.tv_accessoriesProductDetails11)
        detail22TV = view.findViewById(R.id.tv_accessoriesProductDetails22)
        detail33TV = view.findViewById(R.id.tv_accessoriesProductDetails33)


        // Get data from arguments
        val productName = arguments?.getString("productName")
        Log.d("SSS", productName.toString())
        val brand = arguments?.getString("brand")
        val price = arguments?.getDouble("price")
        val ratings = arguments?.getDouble("ratings")
        val frameColor = arguments?.getString("framecolor")
        val category = arguments?.getString("category")

        // Set data to TextViews
        productNameTextView.text = productName
        brandTextView.text = brand
        priceTextView.text = getString(R.string.price_format, price)
        ratingsTextView.text = getString(R.string.ratings_format, ratings)
        frameColorTextView.text = frameColor
        numberOfReviewsTextView.text = arguments?.getString("numberofreviews").toString().plus(" Reviews")
        productFrameStyle.text = arguments?.getString("productframestyle")



        if(arguments?.getString("identifier") == "accessories") {
            detail1TV.text = arguments?.getString("detail1")
            detail2TV.text = arguments?.getString("detail2")
            detail3TV.text = arguments?.getString("detail3")

            detail11TV.text = arguments?.getString("detail11")
            detail22TV.text = arguments?.getString("detail22")
            detail33TV.text = arguments?.getString("detail33")


            val table = view.findViewById<TableLayout>(R.id.tableLayout_Dimensions)
            table.visibility = View.GONE
            view.findViewById<View>(R.id.ll_accessories).visibility = View.VISIBLE



        }


        if(category.equals("Contact Lenses")) {
            val table = view.findViewById<TableLayout>(R.id.tableLayout_Dimensions)
            productMaterialTextView.text = arguments?.getString("material")
            productWaterContentTextView.text = arguments?.getString("watercontent")
            productSpherePRTextView.text = arguments?.getString("spherePR")
            productCylinderPRTextView.text = arguments?.getString("cylinderPR")

            table.visibility = View.GONE
            view.findViewById<View>(R.id.horizontal_line_below_gender).visibility = View.GONE
            view.findViewById<View>(R.id.ll_contactLens).visibility = View.VISIBLE


        }


        height.text = arguments?.getString("height")
        width.text = arguments?.getString("width")
        bridgeSize.text = arguments?.getString("bridgesize")
        templeLength.text = arguments?.getString("templelength")

        if(arguments?.getString("identifier") == "eyeglasses" || arguments?.getString("identifier") == "sunglasses") {
            val isPrescription = arguments?.getBoolean("Speci") == true
            productPrescriptionIV.setImageResource(if (isPrescription) R.drawable.baseline_check_circle_24 else R.drawable.baseline_close_24)

            val isUVProtected = arguments?.getString("uvProtection") == "true"
            productUVProtectionIV.setImageResource(if (isUVProtected) R.drawable.baseline_check_circle_24 else R.drawable.baseline_close_24)


            val isARC = arguments?.getString("antiReflectiveCoating") == "true"
            productARC_IV.setImageResource(if (isARC) R.drawable.baseline_check_circle_24 else R.drawable.baseline_close_24)

            view.findViewById<View>(R.id.ll_contactLens).visibility = View.GONE
            view.findViewById<View>(R.id.horizontal_line_below_gender).visibility = View.VISIBLE
            llSpecifications.visibility = View.VISIBLE

        }



        // Use imageUrls as needed, for example, with ViewPager



        return view
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve the image URLs from arguments
        imageUrls = requireArguments().getStringArrayList("imageUrls") ?: emptyList()

        // Initialize ViewPager and set up the adapter
        viewPager = view.findViewById(R.id.viewPager)
        val productImageAdapter = ImageAdapter(imageUrls)
        viewPager.adapter = productImageAdapter
    }
}


