package com.example.smartspend2

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.example.smartspend2.adapters.TransactionAdapter
import com.example.smartspend2.models.Transaction
import com.example.smartspend2.DatabaseHelper
import java.text.SimpleDateFormat
import java.util.Locale


class HomeFragment : Fragment() {

    private lateinit var pieChart: PieChart
    private lateinit var recyclerView: RecyclerView
    private lateinit var dbHelper: DatabaseHelper
    private var transactions: MutableList<Transaction> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = DatabaseHelper(requireContext())

        pieChart = view.findViewById(R.id.pieChart)
        recyclerView = view.findViewById(R.id.rvTransactions)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        loadTransactions()
        setupPieChart()
    }


    private fun loadTransactions() {
        // Load transactions from SQLite
        transactions.clear()
        transactions.addAll(dbHelper.getAllTransactions())

        // Display only the latest 5 transactions
        val recentTransactions = transactions.sortedByDescending {
            SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).parse(it.date)
        }.take(5)

        recyclerView.adapter = TransactionAdapter(recentTransactions) { _, _ -> }
        setupPieChart()  // Refresh the pie chart with the latest data
    }


    // Pie chart styling

    private fun setupPieChart() {

        if (transactions.isNullOrEmpty()) {
            pieChart.clear() // Clear chart if no data
            return
        }

        val expenseTransactions = transactions.filter { it.isExpense }

        // Group expenses by category and sum them
        val categoryTotals = expenseTransactions
            .groupBy { it.category }
            .mapValues { entry -> entry.value.sumOf { it.amount.toDouble() } }

        val entries = categoryTotals.map { (category, total) ->
            PieEntry(total.toFloat(), category)
        }

        val dataSet = PieDataSet(entries, "")
        val colors = listOf(
            ContextCompat.getColor(requireContext(), R.color.primary),
            ContextCompat.getColor(requireContext(), R.color.accent),
            ContextCompat.getColor(requireContext(), R.color.border),
            Color.parseColor("#FFA726") // Add more if needed
        )
        dataSet.colors = colors
        dataSet.sliceSpace = 3f
        dataSet.selectionShift = 5f

        val data = PieData(dataSet)
        data.setValueTextSize(12f)
        data.setValueTextColor(Color.WHITE)

        // Apply modern styling
        pieChart.data = data
        pieChart.setUsePercentValues(true)
        pieChart.description.isEnabled = false
        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(Color.TRANSPARENT)
        pieChart.transparentCircleRadius = 55f
        pieChart.holeRadius = 50f
        pieChart.setCenterText("Expenses")
        pieChart.setCenterTextSize(16f)
        pieChart.setCenterTextColor(Color.DKGRAY)
        pieChart.setEntryLabelColor(Color.BLACK)
        pieChart.setEntryLabelTextSize(14f)

        val legend = pieChart.legend
        legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        legend.orientation = Legend.LegendOrientation.HORIZONTAL
        legend.setDrawInside(false)
        legend.textSize = 14f
        legend.form = Legend.LegendForm.CIRCLE

        pieChart.invalidate()
    }
}
