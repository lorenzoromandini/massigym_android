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

// classe che gestisce la schermata delle Statistiche, che funge da contenitore per le schermate
// LikesPerCategoryFragment e WorkoutsPerCategoryFragment
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

    // metodo che setta la visualizzazione delle schermate attraverso l'adapter definito
    // in un'altra classe
    private fun setupViewPagerWithTabs() {
        val tabLayout = binding.tabBarStatistics
        val viewPager = binding.pager

        viewPager.adapter = StatisticsTabAdapter(this)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = getTabTitle(position)
        }.attach()
    }

    // metodo che setta il nome delle schermate nella TabBar
    private fun getTabTitle(position: Int): String? {
        return when (position) {
            WORKOUT_PAGE_INDEX -> "Workouts"
            LIKES_PAGE_INDEX -> "Likes"
            else -> null
        }
    }

    // metodo che permette di tornare indietro alla schermata precedente premendo l'apposito pulsante sulla AppBar
    // in alto a sinistra, facendo uso del navigation
    private fun setupToolbarWithNavigation() {
        toolbar = binding.toolbarStatistics
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

}