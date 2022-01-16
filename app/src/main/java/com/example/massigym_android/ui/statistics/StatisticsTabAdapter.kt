package com.example.massigym_android.ui.statistics

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

const val WORKOUT_PAGE_INDEX = 0
const val LIKES_PAGE_INDEX = 1

// classe che gestisce la barra navigazione dei fragment posta nella parte superiore della schermata delle Statistiche
class StatisticsTabAdapter(fragment: Fragment) :
    FragmentStateAdapter(fragment) {

    // metodo che fornisce il numero di schermate presenti
    override fun getItemCount() = tabFragmentsCreator.size

    override fun createFragment(position: Int): Fragment {
        return tabFragmentsCreator[position]?.invoke() ?: throw IndexOutOfBoundsException()
    }

    // schermate presenti
    private val tabFragmentsCreator: Map<Int, () -> Fragment> = mapOf(
        WORKOUT_PAGE_INDEX to { WorkoutsPerCategoryFragment() },
        LIKES_PAGE_INDEX to { LikesPerCategoryFragment() },
    )
}
