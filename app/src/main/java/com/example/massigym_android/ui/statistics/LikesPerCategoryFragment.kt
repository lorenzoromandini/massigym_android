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
import com.anychart.core.cartesian.series.Column
import com.anychart.enums.Anchor
import com.anychart.enums.HoverMode
import com.anychart.enums.Position
import com.anychart.enums.TooltipPositionMode
import com.example.massigym_android.databinding.FragmentLikesPerCategoryBinding
import com.google.firebase.firestore.FirebaseFirestore

// classe che gestisce l'istogramma sul numero di likes agli allenamenti di ciascuna categoria
class LikesPerCategoryFragment : Fragment() {

    private lateinit var binding: FragmentLikesPerCategoryBinding

    private val categoryList: MutableList<String> = mutableListOf()
    private val totalLikeslist: MutableList<Int> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = FragmentLikesPerCategoryBinding.inflate(inflater, container, false)

        Toast.makeText(context, "Caricamento in corso...", Toast.LENGTH_LONG).show()

        getData("cardio")
        getData("legs")
        getData("arms")

        @Suppress("DEPRECATION")
        // metodo che permette di eseguire i comandi al suo interno dopo un tempo prestabilito
        Handler().postDelayed(
            {
                barChart()
            },
            6000
        )

        return binding.root
    }

    // metodo che serve ad ottenere le statistiche relative al numero di likes per categoria dal database Cloud Firestore
    private fun getData(category: String) {

        FirebaseFirestore.getInstance().collection("statistics").document(category)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val categoria = task.result

                    categoryList.add(categoria["category"].toString())
                    totalLikeslist.add((categoria["totalLikes"].toString()).toInt())
                }
            }
    }

    // metodo che serve a costruire l'istogramma con le statistiche relative al numero di likes per categoria
    // attraverso la libreria AnyChart
    private fun barChart() {

        val cartesian = AnyChart.cartesian()

        val dataBarChart: MutableList<DataEntry> = mutableListOf()

        // inserisce categoria e numero di likes per ciascuna categoria negli appositi array
        for (index in totalLikeslist.indices) {
            dataBarChart.add(ValueDataEntry(categoryList.elementAt(index),
                totalLikeslist.elementAt(index)))
        }

        val column: Column = cartesian.column(dataBarChart)

        column.tooltip()
            .titleFormat("{%X}")
            .position(Position.CENTER_BOTTOM)
            .anchor(Anchor.CENTER_BOTTOM)
            .offsetX(0.0)
            .offsetY(5.0)
            .format("{%Value}{groupsSeparator: }")

        cartesian.animation(true)

        cartesian.title("Likes per categoria")

        cartesian.yScale().minimum(0.0)

        cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }")

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT)
        cartesian.interactivity().hoverMode(HoverMode.BY_X)

        cartesian.xAxis(0).title("Categoria")
        cartesian.yAxis(0).title("Numero di Like")



        // mostra il grafico con le informazioni contenute nel database
        binding.barChart!!.setChart(cartesian)

    }

}