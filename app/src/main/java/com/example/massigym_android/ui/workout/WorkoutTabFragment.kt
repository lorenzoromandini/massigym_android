package com.example.massigym_android.ui.workout

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.massigym_android.R
import com.example.massigym_android.databinding.FragmentWorkoutBinding
import com.google.android.material.tabs.TabLayoutMediator

// classe che gestisce la schermata dei Workout, che funge da contenitore per le schermate
// CardioFragment, LegsFragment e ArmsFragment
class WorkoutTabFragment : Fragment() {

    private lateinit var binding: FragmentWorkoutBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = FragmentWorkoutBinding.inflate(inflater, container, false)

        setupViewPagerWithTabs()

        binding.fabWorkout.setOnClickListener {
            val intent = Intent(context, AddWorkout::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    // metodo che setta la visualizzazione delle schermate attraverso l'adapter definito
    // in un'altra classe
    private fun setupViewPagerWithTabs() {
        val tabLayout = binding.tabBarWorkout
        val viewPager = binding.pager

        viewPager.adapter = WorkoutTabAdapter(this)

        // Set the title for each tab
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = getTabTitle(position)
        }.attach()
    }

    // metodo che setta il nome delle schermate nella TabBar
    private fun getTabTitle(position: Int): String? {
        return when (position) {
            CARDIO_PAGE_INDEX -> getString(R.string.cardioCategory)
            LEGS_PAGE_INDEX -> getString(R.string.legsCategory)
            ARMS_PAGE_INDEX -> getString(R.string.armsCategory)
            else -> null
        }
    }

}