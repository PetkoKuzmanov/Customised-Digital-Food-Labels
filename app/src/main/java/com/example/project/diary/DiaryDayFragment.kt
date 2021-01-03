package com.example.project.diary

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project.R
import com.example.project.addFood.AddFoodActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

class DiaryDayFragment : Fragment() {
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var database: DatabaseReference
    val context = this


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_diary_day, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        getData()
//        processFood()

        val addFoodToBreakfastTextView =
            requireView().findViewById<TextView>(R.id.addFoodToBreakfastTextView)
        val addFoodToLunchTextView =
            requireView().findViewById<TextView>(R.id.addFoodToLunchTextView)
        val addFoodToDinnerTextView =
            requireView().findViewById<TextView>(R.id.addFoodToDinnerTextView)
        val addFoodToSnacksTextView =
            requireView().findViewById<TextView>(R.id.addFoodToSnacksTextView)

        addFoodToBreakfastTextView.setOnClickListener {
            val intent = Intent(requireView().context, AddFoodActivity::class.java)
            intent.putExtra("meal", "breakfast")
            startActivity(intent)
        }
        addFoodToLunchTextView.setOnClickListener {
            val intent = Intent(requireView().context, AddFoodActivity::class.java)
            intent.putExtra("meal", "lunch")
            startActivity(intent)
        }
        addFoodToDinnerTextView.setOnClickListener {
            val intent = Intent(requireView().context, AddFoodActivity::class.java)
            intent.putExtra("meal", "dinner")
            startActivity(intent)
        }
        addFoodToSnacksTextView.setOnClickListener {
            val intent = Intent(requireView().context, AddFoodActivity::class.java)
            intent.putExtra("meal", "snacks")
            startActivity(intent)
        }
    }

    private fun getData() {
        val breakfastFoodList = ArrayList<FoodModel>()
        val lunchFoodList = ArrayList<FoodModel>()
        val dinnerFoodList = ArrayList<FoodModel>()
        val snacksFoodList = ArrayList<FoodModel>()

        database = Firebase.database.reference
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val caloriesGoal = mAuth.currentUser?.let {
                    snapshot.child("users").child(it.uid).child("goals")
                        .child("calories").value.toString()
                }

                val caloriesGoalTextView = requireView().findViewById<TextView>(R.id.goalCalories)
                caloriesGoalTextView.text = caloriesGoal

                //Set the current date as the date
                val current = LocalDateTime.now()
                val formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val formattedDate = current.format(formatterDate)

                val diaryReference = mAuth.currentUser?.let {
                    snapshot.child("users").child(it.uid).child("dates").child(formattedDate)
                        .child("diary")
                }

                //Get the data for the meals
                for (meal in diaryReference?.children!!) {
                    for (index in meal.children) {
                        if (index.key?.contains("food")!!) {
                            val foodId = index.child("id").value.toString()
                            val foodAmount = index.child("amount").value.toString()

                            val foodReference = snapshot.child("food").child(foodId)

                            val foodName = foodReference.child("name").value.toString()
                            val foodDescription =
                                foodReference.child("description").value.toString()
                            val foodAmountMeasurement =
                                foodReference.child("measurement").value.toString()
                            val foodCalories =
                                foodReference.child("calories").value.toString().toInt()
                            val foodCarbohydrates =
                                foodReference.child("carbohydrates").value.toString()
                            val foodFats = foodReference.child("fats").value.toString()
                            val foodProteins = foodReference.child("proteins").value.toString()

                            val foodInstanceCalories =
                                ((foodCalories * foodAmount.toDouble()) / 100).roundToInt()
                                    .toString()

                            val foodModel = FoodModel()
                            foodModel.setName(foodName)
                            foodModel.setId(foodId)
                            foodModel.setDescription(foodDescription)
                            foodModel.setAmount(foodAmount)
                            foodModel.setMeasurement(foodAmountMeasurement)
                            foodModel.setCaloriesAmount(foodInstanceCalories)
                            foodModel.setCarbohydratesAmount(foodCarbohydrates)
                            foodModel.setFatsAmount(foodFats)
                            foodModel.setProteinsAmount(foodProteins)
                            foodModel.setMeal(meal.key.toString())

                            when {
                                meal.key.toString() == "breakfast" -> {
                                    breakfastFoodList.add(foodModel)
                                }
                                meal.key.toString() == "lunch" -> {
                                    lunchFoodList.add(foodModel)
                                }
                                meal.key.toString() == "dinner" -> {
                                    dinnerFoodList.add(foodModel)
                                }
                                meal.key.toString() == "snacks" -> {
                                    snacksFoodList.add(foodModel)
                                }
                            }
                        } else if (index.key?.contains("calories")!!) {
                            val calories = index.value.toString()

                            val foodModel = FoodModel()
                            foodModel.setName("Quick Add")
                            foodModel.setId(" ")
                            foodModel.setDescription(" ")
                            foodModel.setAmount(" ")
                            foodModel.setMeasurement(" ")
                            foodModel.setCaloriesAmount(calories)
                            foodModel.setCarbohydratesAmount("0")
                            foodModel.setFatsAmount("0")
                            foodModel.setProteinsAmount("0")
                            foodModel.setMeal(meal.key.toString())

                            when {
                                meal.key.toString() == "breakfast" -> {
                                    breakfastFoodList.add(foodModel)
                                }
                                meal.key.toString() == "lunch" -> {
                                    lunchFoodList.add(foodModel)
                                }
                                meal.key.toString() == "dinner" -> {
                                    dinnerFoodList.add(foodModel)
                                }
                                meal.key.toString() == "snacks" -> {
                                    snacksFoodList.add(foodModel)
                                }
                            }
                        }
                    }
                }

                val breakfastLayoutManager = LinearLayoutManager(activity)
                val breakfastRecyclerView =
                    requireView().findViewById(R.id.breakfastRecyclerView) as RecyclerView
                breakfastRecyclerView.layoutManager = breakfastLayoutManager
                val breakfastAdapter = DiaryDayAdapter(breakfastFoodList)
                breakfastRecyclerView.adapter = breakfastAdapter

                val lunchLayoutManager = LinearLayoutManager(activity)
                val lunchRecyclerView =
                    requireView().findViewById(R.id.lunchRecyclerView) as RecyclerView
                lunchRecyclerView.layoutManager = lunchLayoutManager
                val lunchAdapter = DiaryDayAdapter(lunchFoodList)
                lunchRecyclerView.adapter = lunchAdapter

                val dinnerLayoutManager = LinearLayoutManager(activity)
                val dinnerRecyclerView =
                    requireView().findViewById(R.id.dinnerRecyclerView) as RecyclerView
                dinnerRecyclerView.layoutManager = dinnerLayoutManager
                val dinnerAdapter = DiaryDayAdapter(dinnerFoodList)
                dinnerRecyclerView.adapter = dinnerAdapter

                val snacksLayoutManager = LinearLayoutManager(activity)
                val snacksRecyclerView =
                    requireView().findViewById(R.id.snacksRecyclerView) as RecyclerView
                snacksRecyclerView.layoutManager = snacksLayoutManager
                val snacksAdapter = DiaryDayAdapter(snacksFoodList)
                snacksRecyclerView.adapter = snacksAdapter
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun processFood() {


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
            foodModel.setName(lunchNamesList[i])
            foodModel.setDescription(lunchDescriptionsList[i])
            foodModel.setAmount(lunchAmountsList[i])
            foodModel.setMeasurement(lunchAmountMeasurementsList[i])
            foodModel.setCaloriesAmount(lunchCaloriesAmountsList[i])
            lunchFoodList.add(foodModel)
        }


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
            foodModel.setName(dinnerNamesList[i])
            foodModel.setDescription(dinnerDescriptionsList[i])
            foodModel.setAmount(dinnerAmountsList[i])
            foodModel.setMeasurement(dinnerAmountMeasurementsList[i])
            foodModel.setCaloriesAmount(dinnerCaloriesAmountsList[i])
            dinnerFoodList.add(foodModel)
        }


    }
}
