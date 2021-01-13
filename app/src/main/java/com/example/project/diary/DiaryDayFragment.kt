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
import com.example.project.addToDiary.AddExerciseActivity
import com.example.project.addToDiary.AddFoodActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlin.math.roundToInt

class DiaryDayFragment(private val currentDate: String) : Fragment() {
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: DatabaseReference = Firebase.database.reference
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

        val addFoodToBreakfastTextView =
            requireView().findViewById<TextView>(R.id.addFoodToBreakfastTextView)
        val addFoodToLunchTextView =
            requireView().findViewById<TextView>(R.id.addFoodToLunchTextView)
        val addFoodToDinnerTextView =
            requireView().findViewById<TextView>(R.id.addFoodToDinnerTextView)
        val addFoodToSnacksTextView =
            requireView().findViewById<TextView>(R.id.addFoodToSnacksTextView)
        val addExerciseTextView =
            requireView().findViewById<TextView>(R.id.addExerciseTextView)

        addFoodToBreakfastTextView.setOnClickListener {
            val intent = Intent(requireView().context, AddFoodActivity::class.java)
            intent.putExtra("meal", "breakfast")
            intent.putExtra("date", currentDate)
            startActivity(intent)
        }
        addFoodToLunchTextView.setOnClickListener {
            val intent = Intent(requireView().context, AddFoodActivity::class.java)
            intent.putExtra("meal", "lunch")
            intent.putExtra("date", currentDate)
            startActivity(intent)
        }
        addFoodToDinnerTextView.setOnClickListener {
            val intent = Intent(requireView().context, AddFoodActivity::class.java)
            intent.putExtra("meal", "dinner")
            intent.putExtra("date", currentDate)
            startActivity(intent)
        }
        addFoodToSnacksTextView.setOnClickListener {
            val intent = Intent(requireView().context, AddFoodActivity::class.java)
            intent.putExtra("meal", "snacks")
            intent.putExtra("date", currentDate)
            startActivity(intent)
        }
        addExerciseTextView.setOnClickListener {
            val intent = Intent(requireView().context, AddExerciseActivity::class.java)
            intent.putExtra("date", currentDate)
            intent.putExtra("menu", "add")
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        println("onResume")
        getData()
    }

    private fun getData() {
        val breakfastFoodList = ArrayList<FoodModel>()
        val lunchFoodList = ArrayList<FoodModel>()
        val dinnerFoodList = ArrayList<FoodModel>()
        val snacksFoodList = ArrayList<FoodModel>()
        val exerciseList = ArrayList<ExerciseModel>()

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                breakfastFoodList.clear()
                lunchFoodList.clear()
                dinnerFoodList.clear()
                snacksFoodList.clear()
                exerciseList.clear()

                if (snapshot.child("users").child(mAuth.uid!!).exists()) {
                    val caloriesGoal = mAuth.currentUser?.let {
                        snapshot.child("users").child(it.uid).child("goals")
                            .child("calories").value.toString().toInt()
                    }

                    val caloriesGoalTextView =
                        requireView().findViewById<TextView>(R.id.goalCalories)
                    caloriesGoalTextView.text = caloriesGoal.toString()

                    val diaryReference = mAuth.currentUser?.let {
                        snapshot.child("users").child(it.uid).child("dates").child(currentDate)
                            .child("diary")
                    }

                    //Get the data for the meals
                    for (meal in diaryReference?.children!!) {
                        if (meal.key == "exercise") {
                            for (index in meal.children) {
                                val exercise = index.value.toString()

                                val exerciseModel = ExerciseModel()
                                exerciseModel.setExercise(exercise)
                                exerciseModel.setKey(index.key!!)
                                exerciseList.add(exerciseModel)
                            }
                        } else {
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
                                    val foodProteins =
                                        foodReference.child("proteins").value.toString()

//                                val foodInstanceCalories =
//                                    ((foodCalories * foodAmount.toDouble()) / 100).roundToInt()
//                                        .toString()

                                    val foodModel = FoodModel()
                                    foodModel.setName(foodName)
                                    foodModel.setId(foodId)
                                    foodModel.setDescription(foodDescription)
                                    foodModel.setAmount(foodAmount)
                                    foodModel.setMeasurement(foodAmountMeasurement)
                                    foodModel.setCaloriesAmount(foodCalories.toString())
                                    foodModel.setCarbohydratesAmount(foodCarbohydrates)
                                    foodModel.setFatsAmount(foodFats)
                                    foodModel.setProteinsAmount(foodProteins)
                                    foodModel.setMeal(meal.key.toString())
                                    foodModel.setKey(index.key!!)

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
                                    foodModel.setId("Quick Add")
                                    foodModel.setDescription(" ")
                                    foodModel.setAmount(" ")
                                    foodModel.setMeasurement(" ")
                                    foodModel.setCaloriesAmount(calories)
                                    foodModel.setCarbohydratesAmount("0")
                                    foodModel.setFatsAmount("0")
                                    foodModel.setProteinsAmount("0")
                                    foodModel.setMeal(meal.key.toString())
                                    foodModel.setKey(index.key!!)

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
                    }

                    val breakfastLayoutManager = LinearLayoutManager(activity)
                    val breakfastRecyclerView =
                        requireView().findViewById(R.id.breakfastRecyclerView) as RecyclerView
                    breakfastRecyclerView.layoutManager = breakfastLayoutManager
                    val breakfastAdapter = DiaryDayAdapter(breakfastFoodList, currentDate)
                    breakfastRecyclerView.adapter = breakfastAdapter

                    val lunchLayoutManager = LinearLayoutManager(activity)
                    val lunchRecyclerView =
                        requireView().findViewById(R.id.lunchRecyclerView) as RecyclerView
                    lunchRecyclerView.layoutManager = lunchLayoutManager
                    val lunchAdapter = DiaryDayAdapter(lunchFoodList, currentDate)
                    lunchRecyclerView.adapter = lunchAdapter

                    val dinnerLayoutManager = LinearLayoutManager(activity)
                    val dinnerRecyclerView =
                        requireView().findViewById(R.id.dinnerRecyclerView) as RecyclerView
                    dinnerRecyclerView.layoutManager = dinnerLayoutManager
                    val dinnerAdapter = DiaryDayAdapter(dinnerFoodList, currentDate)
                    dinnerRecyclerView.adapter = dinnerAdapter

                    val snacksLayoutManager = LinearLayoutManager(activity)
                    val snacksRecyclerView =
                        requireView().findViewById(R.id.snacksRecyclerView) as RecyclerView
                    snacksRecyclerView.layoutManager = snacksLayoutManager
                    val snacksAdapter = DiaryDayAdapter(snacksFoodList, currentDate)
                    snacksRecyclerView.adapter = snacksAdapter

                    val exerciseLayoutManager = LinearLayoutManager(activity)
                    val exerciseRecyclerView =
                        requireView().findViewById(R.id.exerciseRecyclerView) as RecyclerView
                    exerciseRecyclerView.layoutManager = exerciseLayoutManager
                    val exerciseAdapter = ExerciseAdapter(exerciseList, currentDate)
                    exerciseRecyclerView.adapter = exerciseAdapter

                    //Write the calories for the meals and the day
                    var breakfastCalories = 0
                    var lunchCalories = 0
                    var dinnerCalories = 0
                    var snacksCalories = 0

                    for (food in breakfastFoodList) {
                        val calories =
                            food.getCaloriesAmount().toInt() * food.getAmount().toInt() / 100
                        breakfastCalories += calories
                    }
                    for (food in lunchFoodList) {
                        val calories =
                            food.getCaloriesAmount().toInt() * food.getAmount().toInt() / 100
                        lunchCalories += calories
                    }
                    for (food in dinnerFoodList) {
                        val calories =
                            food.getCaloriesAmount().toInt() * food.getAmount().toInt() / 100
                        dinnerCalories += calories
                    }
                    for (food in snacksFoodList) {
                        val calories =
                            food.getCaloriesAmount().toInt() * food.getAmount().toInt() / 100
                        snacksCalories += calories
                    }

                    val totalCalories =
                        breakfastCalories + lunchCalories + dinnerCalories + snacksCalories
                    val remainingCalories = caloriesGoal!! - totalCalories

                    val breakfastCaloriesTextView =
                        requireView().findViewById<TextView>(R.id.breakfastCalories)
                    val lunchCaloriesTextView =
                        requireView().findViewById<TextView>(R.id.lunchCalories)
                    val dinnerCaloriesTextView =
                        requireView().findViewById<TextView>(R.id.dinnerCalories)
                    val snacksCaloriesTextView =
                        requireView().findViewById<TextView>(R.id.snacksCalories)
                    val totalCaloriesTextView =
                        requireView().findViewById<TextView>(R.id.caloriesConsumed)
                    val remainingCaloriesTextView =
                        requireView().findViewById<TextView>(R.id.caloriesRemaining)

                    breakfastCaloriesTextView.text = breakfastCalories.toString()
                    lunchCaloriesTextView.text = lunchCalories.toString()
                    dinnerCaloriesTextView.text = dinnerCalories.toString()
                    snacksCaloriesTextView.text = snacksCalories.toString()
                    totalCaloriesTextView.text = totalCalories.toString()
                    remainingCaloriesTextView.text = remainingCalories.toString()

                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}
