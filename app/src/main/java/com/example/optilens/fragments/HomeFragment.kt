package com.example.optilens.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.optilens.R
import com.example.optilens.activities.HomePageActivity
import android.content.SharedPreferences


class HomeFragment : Fragment(R.layout.fragment_home) {

    lateinit var cardEyeGlasses: CardView
    lateinit var cardSunGlasses: CardView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cardEyeGlasses = view.findViewById(R.id.cardEyeGlasses)
        cardEyeGlasses.setOnClickListener{
            val bundle = Bundle()
            bundle.putString("categoryId", "eyeglass")

            val productListFragment = ProductListFragment()
            productListFragment.arguments = bundle

            val fragmentManager: FragmentManager = parentFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

            // Replace the existing fragment with the new one that has arguments set
            fragmentTransaction.replace(R.id.frame_layout_main, productListFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        cardSunGlasses = view.findViewById(R.id.cardSunGlasses)
        cardSunGlasses.setOnClickListener{
            val bundle = Bundle()
            bundle.putString("categoryId", "sunglass")

            val productListFragment = ProductListFragment()
            productListFragment.arguments = bundle

            val fragmentManager: FragmentManager = parentFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

            // Replace the existing fragment with the new one that has arguments set
            fragmentTransaction.replace(R.id.frame_layout_main, productListFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }


    }

}