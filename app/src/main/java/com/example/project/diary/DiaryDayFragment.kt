package com.example.project.diary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project.R

class DiaryDayFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_diary_day, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        processFood()
    }

    private fun processFood() {
        val breakfastFoodList = ArrayList<FoodModel>()

        val breakfastNamesList = arrayListOf(
            "Oats", "Milk", "Orange juice"
        )
        val breakfastDescriptionsList = arrayListOf(
            "Quaker", "Tesco", "Juicerino"
        )
        val breakfastAmountsList = arrayListOf(
            "50", "200", "250"
        )
        val breakfastAmountMeasurementsList = arrayListOf(
            "g", "ml", "ml"
        )
        val breakfastCaloriesAmountsList = arrayListOf(
            "150", "150", "125"
        )

        for (i: Int in 0 until 3) {
            val foodModel = FoodModel()
            foodModel.setDate(breakfastNamesList[i])
            foodModel.setWeight(breakfastDescriptionsList[i])
            foodModel.setAmount(breakfastAmountsList[i])
            foodModel.setAmountMeasurement(breakfastAmountMeasurementsList[i])
            foodModel.setCaloriesAmount(breakfastCaloriesAmountsList[i])
            breakfastFoodList.add(foodModel)
        }

        val breakfastLayoutManager = LinearLayoutManager(activity)
        val breakfastRecyclerView =
            requireView().findViewById(R.id.breakfastRecyclerView) as RecyclerView
        breakfastRecyclerView.layoutManager = breakfastLayoutManager
        val breakfastAdapter = DiaryDayAdapter(breakfastFoodList)
        breakfastRecyclerView.adapter = breakfastAdapter

        //lunch
        val lunchFoodList = ArrayList<FoodModel>()

        val lunchNamesList = arrayListOf(
            "Pasta", "Tomato sauce"
        )
        val lunchDescriptionsList = arrayListOf(
            "Italiano", "Tesco"
        )
        val lunchAmountsList = arrayListOf(
            "100", "200g"
        )
        val lunchAmountMeasurementsList = arrayListOf(
            "g", "g"
        )
        val lunchCaloriesAmountsList = arrayListOf(
            "350", "125"
        )

        for (i: Int in 0 until 2) {
            val foodModel = FoodModel()
            foodModel.setDate(lunchNamesList[i])
            foodModel.setWeight(lunchDescriptionsList[i])
            foodModel.setAmount(lunchAmountsList[i])
            foodModel.setAmountMeasurement(lunchAmountMeasurementsList[i])
            foodModel.setCaloriesAmount(lunchCaloriesAmountsList[i])
            lunchFoodList.add(foodModel)
        }

        val lunchLayoutManager = LinearLayoutManager(activity)
        val lunchRecyclerView = requireView().findViewById(R.id.lunchRecyclerView) as RecyclerView
        lunchRecyclerView.layoutManager = lunchLayoutManager
        val lunchAdapter = DiaryDayAdapter(lunchFoodList)
        lunchRecyclerView.adapter = lunchAdapter


        //dinner
        val dinnerFoodList = ArrayList<FoodModel>()

        val dinnerNamesList = arrayListOf(
            "Steak", "Olive oil", "Potatoes"
        )
        val dinnerDescriptionsList = arrayListOf(
            "M&S", "Greek extra virgin", "Welsh"
        )
        val dinnerAmountsList = arrayListOf(
            "100", "10", "300"
        )
        val dinnerAmountMeasurementsList = arrayListOf(
            "g", "ml", "g"
        )
        val dinnerCaloriesAmountsList = arrayListOf(
            "250", "120", "230"
        )

        for (i: Int in 0 until 3) {
            val foodModel = FoodModel()
            foodModel.setDate(dinnerNamesList[i])
            foodModel.setWeight(dinnerDescriptionsList[i])
            foodModel.setAmount(dinnerAmountsList[i])
            foodModel.setAmountMeasurement(dinnerAmountMeasurementsList[i])
            foodModel.setCaloriesAmount(dinnerCaloriesAmountsList[i])
            dinnerFoodList.add(foodModel)
        }

        val dinnerLayoutManager = LinearLayoutManager(activity)
        val dinnerRecyclerView = requireView().findViewById(R.id.dinnerRecyclerView) as RecyclerView
        dinnerRecyclerView.layoutManager = dinnerLayoutManager
        val dinnerAdapter = DiaryDayAdapter(dinnerFoodList)
        dinnerRecyclerView.adapter = dinnerAdapter
    }
}
