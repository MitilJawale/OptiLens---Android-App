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
    lateinit var cardContactLens: CardView
    lateinit var cardAccessories: CardView

    lateinit var cardEyeCare: CardView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cardEyeGlasses = view.findViewById(R.id.cardEyeGlasses)
        cardSunGlasses = view.findViewById(R.id.cardSunGlasses)
        cardContactLens = view.findViewById(R.id.cardContactLenses)
        cardAccessories = view.findViewById(R.id.cardAccessories)



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

        cardEyeCare = view.findViewById(R.id.cardEyeCare)
        cardEyeCare.setOnClickListener{
            val fragmentManager: FragmentManager = parentFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

            // Replace the existing fragment with the new one that has arguments set
            fragmentTransaction.replace(R.id.frame_layout_main, EyeCareFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }


        cardContactLens.setOnClickListener{
            val bundle = Bundle()
            bundle.putString("categoryId", "contactlens")

            val productListFragment = ProductListFragment()
            productListFragment.arguments = bundle

            val fragmentManager: FragmentManager = parentFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

            // Replace the existing fragment with the new one that has arguments set
            fragmentTransaction.replace(R.id.frame_layout_main, productListFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }


        cardAccessories.setOnClickListener{
            val bundle = Bundle()
            bundle.putString("categoryId", "accessories")

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