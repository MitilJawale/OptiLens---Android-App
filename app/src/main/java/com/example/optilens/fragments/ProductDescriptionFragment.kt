package com.example.optilens.fragments



import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.optilens.R
import com.example.optilens.adapter.ImageAdapter


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
        numberOfReviewsTextView.text = arguments?.getString("numberofreviews")
        productFrameStyle.text = arguments?.getString("productframestyle")
        if(category.equals("Contact Lenses") || category.equals("Accessories")) {
            val table = view.findViewById<TableLayout>(R.id.tableLayout_Dimensions)
            table.visibility = View.GONE



        }
        height.text = arguments?.getString("height")
        width.text = arguments?.getString("width")
        bridgeSize.text = arguments?.getString("bridgesize")
        templeLength.text = arguments?.getString("templelength")
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


