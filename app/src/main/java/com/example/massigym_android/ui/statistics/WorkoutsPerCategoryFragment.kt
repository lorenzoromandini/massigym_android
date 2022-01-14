package com.example.massigym_android.ui.statistics

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.example.massigym_android.databinding.FragmentWorkoutsPerCategoryBinding
import com.example.massigym_android.ui.auth.LoginActivity
import com.example.massigym_android.ui.common.BottomNavBar
import com.google.firebase.firestore.FirebaseFirestore


class WorkoutsPerCategoryFragment : Fragment() {

    private lateinit var binding: FragmentWorkoutsPerCategoryBinding

    private val categoryList: MutableList<String> = mutableListOf()
    private val totalWorkoutslist: MutableList<Int> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = FragmentWorkoutsPerCategoryBinding.inflate(inflater, container, false)

        Toast.makeText(context, "Caricamento in corso...", Toast.LENGTH_LONG).show()

        getData("cardio")
        getData("legs")
        getData("arms")

        @Suppress("DEPRECATION")
        Handler().postDelayed(
            {
                pieChart()
            },
            6000
        )

        return binding.root
    }

    private fun getData(category: String) {

        FirebaseFirestore.getInstance().collection("statistics").document(category)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val categoria = task.result

                    categoryList.add(categoria["category"].toString())
                    totalWorkoutslist.add((categoria["totalWorkouts"].toString()).toInt())
                }
            }
    }

    private fun pieChart() {

        val pie = AnyChart.pie()

        val dataPieChart: MutableList<DataEntry> = mutableListOf()

        for (index in totalWorkoutslist.indices) {
            dataPieChart.add(ValueDataEntry(categoryList.elementAt(index), totalWorkoutslist.elementAt(index)))
        }

        pie.data(dataPieChart)

        pie.animation(true)

        pie.title("Workouts per categoria")

        binding.pieChart!!.setChart(pie)

    }

}


