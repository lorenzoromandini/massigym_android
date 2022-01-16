package com.example.massigym_android.ui.workout

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

const val CARDIO_PAGE_INDEX = 0
const val LEGS_PAGE_INDEX = 1
const val ARMS_PAGE_INDEX = 2

// classe che gestisce la barra navigazione dei fragment posta nella parte superiore della schermata dei Workout
class WorkoutTabAdapter(fragment: Fragment) :
    FragmentStateAdapter(fragment) {

    // metodo che fornisce il numero di schermate presenti
    override fun getItemCount() = tabFragmentsCreator.size

    override fun createFragment(position: Int): Fragment {
        return tabFragmentsCreator[position]?.invoke() ?: throw IndexOutOfBoundsException()
    }

    // schermate presenti
    private val tabFragmentsCreator: Map<Int, () -> Fragment> = mapOf(
        CARDIO_PAGE_INDEX to { CardioFragment() },
        LEGS_PAGE_INDEX to { LegsFragment() },
        ARMS_PAGE_INDEX to { ArmsFragment() }
    )
}