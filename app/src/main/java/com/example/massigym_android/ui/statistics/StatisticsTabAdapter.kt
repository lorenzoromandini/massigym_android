package com.example.massigym_android.ui.statistics

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

const val WORKOUT_PAGE_INDEX = 0
const val LIKES_PAGE_INDEX = 1

class StatisticsTabAdapter(fragment: Fragment) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount() = tabFragmentsCreator.size

    override fun createFragment(position: Int): Fragment {
        return tabFragmentsCreator[position]?.invoke() ?: throw IndexOutOfBoundsException()
    }

    /**
     * Mapping of the ViewPager page indexes to their respective Fragments
     */
    private val tabFragmentsCreator : Map<Int, ()-> Fragment> = mapOf(
        WORKOUT_PAGE_INDEX to { WorkoutsPerCategoryFragment() },
        LIKES_PAGE_INDEX to { LikesPerCategoryFragment() },
    )
}
