package com.example.massigym_android.ui.statistics

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.example.massigym_android.databinding.FragmentWorkoutsPerCategoryBinding
import com.google.firebase.firestore.FirebaseFirestore


// classe che gestisce il grafico a torta col numero di workouts di ciascuna categoria
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
        // metodo che permette di eseguire i comandi al suo interno dopo un tempo prestabilito
        Handler().postDelayed(
            {
                pieChart()
            },
            6000
        )

        return binding.root
    }

    // metodo che serve ad ottenere le statistiche relative al numero di workouts per categoria dal database Cloud Firestore
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

    // metodo che serve a costruire il grafico a torta con le statistiche relative al numero di workouts per categoria
    // attraverso la libreria AnyChart
    private fun pieChart() {

        val pie = AnyChart.pie()

        val dataPieChart: MutableList<DataEntry> = mutableListOf()

        // inserisce categoria e numero di workouts per ciascuna categoria negli appositi array
        for (index in totalWorkoutslist.indices) {
            dataPieChart.add(ValueDataEntry(categoryList.elementAt(index), totalWorkoutslist.elementAt(index)))
        }

        pie.data(dataPieChart)

        pie.animation(true)

        pie.title("Workouts per categoria")

        // mostra il grafico con le informazioni contenute nel database
        binding.pieChart!!.setChart(pie)

    }

}


