package com.example.optilens.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.optilens.R  // Replace with your actual package name
import com.example.optilens.adapter.ProductAdapter
import com.example.optilens.dataclass.ProductCategory
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader

class ProductListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_product_list, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(activity,2)



        val applicationContext: Context = requireContext()// Replace with the actual application context
        val productList = readAndParseJsonFile(applicationContext, "products.json")

        // Now, the 'productList' contains all the products from the JSON
        val categoryId = arguments?.getString("categoryId")


        if (categoryId.equals("eyeglass")) {
            val adapter = productList?.eyeglasses?.let {
                ProductAdapter(requireContext(), it)
            }
            recyclerView.adapter = adapter
        } else if(categoryId.equals("sunglass")) {
            val adapter = productList?.sunglasses?.let {
                ProductAdapter(requireContext(), it)
            }
            recyclerView.adapter = adapter
        }
        else if(categoryId.equals("contactlens")){
            val adapter = productList?.contactlens?.let {
                ProductAdapter(requireContext(), it)
            }
            recyclerView.adapter = adapter
        }
        else if(categoryId.equals("accessories")){
            val adapter = productList?.accessories?.let {
                ProductAdapter(requireContext(), it)
            }
            recyclerView.adapter = adapter
        }



        return view


        //val jsonData = requireContext().assets.open("movies.json").bufferedReader().use { it.readText() }
        //        val gson = Gson()
        //        val movieList = gson.fromJson(jsonData, MovieModels.MovieList::class.java)
        //
        //
        //        val recyclerView: RecyclerView = view.findViewById(R.id.rv_movie_tile)
        //
        //
        ////        recyclerView.itemAnimator = DefaultItemAnimator()
        //
        //
        //
        //        var adapter = MovieAdapter(movieList.results as ArrayList<MovieModels.Movie>)
    }



    // Function to read the content of a file and parse it into a list of products
    fun readAndParseJsonFile(context: Context, fileName: String): ProductCategory? {
        try {
            val inputStream = context.assets.open(fileName)
            val reader = BufferedReader(InputStreamReader(inputStream))
            val jsonString = StringBuilder()
            var line: String?

            while (reader.readLine().also { line = it } != null) {
                jsonString.append(line)
            }

            return Gson().fromJson(jsonString.toString(), ProductCategory::class.java)
        } catch (e: Exception) {
            Log.e("ProductListFragment", "Error reading JSON file", e)
        }

        return null
    }

}
