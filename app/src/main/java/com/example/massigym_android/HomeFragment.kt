package com.example.massigym_android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.massigym_android.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.categoryCardio.categoryTitle.setText("Cardio")
        setCategoryImage(binding.categoryCardio.categoryImage, getString(R.string.cardioImageUrl))
        binding.categoryCardio.categoryOverview.setText(getString(R.string.cardioTopDescription))
        binding.categoryCardio.categoryDescription.setText(getString(R.string.cardioTopDescription))

        binding.categoryLegs.categoryTitle.setText("Legs")
        setCategoryImage(binding.categoryLegs.categoryImage, getString(R.string.legsImageUrl))
        binding.categoryLegs.categoryOverview.setText(getString(R.string.legsTopDescription))
        binding.categoryLegs.categoryDescription.setText(getString(R.string.legsBottomDescription))

        binding.categoryArms.categoryTitle.setText("Arms")
        setCategoryImage(binding.categoryArms.categoryImage, getString(R.string.armsImageUrl))
        binding.categoryArms.categoryOverview.setText(getString(R.string.armsTopDescription))
        binding.categoryArms.categoryDescription.setText(getString(R.string.armsBottomDescription))


        return binding.root
    }

    private fun setCategoryImage(destination: ImageView, url: String) {
        Glide.with(this)
            .load(url)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .dontAnimate()
            .into(destination);
    }

}