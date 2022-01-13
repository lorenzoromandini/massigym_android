package com.example.massigym_android.ui.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.massigym_android.databinding.FragmentStatisticsBinding
import com.google.android.material.tabs.TabLayoutMediator

class StatisticsTabFragment : Fragment() {

    private lateinit var binding: FragmentStatisticsBinding

    private lateinit var toolbar: Toolbar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = FragmentStatisticsBinding.inflate(inflater, container, false)

        setupToolbarWithNavigation()

        setupViewPagerWithTabs()

        return binding.root
    }

    private fun setupViewPagerWithTabs() {
        val tabLayout = binding.tabBarStatistics
        val viewPager = binding.pager

        viewPager.adapter = StatisticsTabAdapter(this)

        // Set the title for each tab
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = getTabTitle(position)
        }.attach()
    }

    private fun getTabTitle(position: Int): String? {
        return when (position) {
            WORKOUT_PAGE_INDEX -> "Workouts"
            LIKES_PAGE_INDEX -> "Likes"
            else -> null
        }
    }

    private fun setupToolbarWithNavigation() {
        toolbar = binding.toolbarStatistics
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

}