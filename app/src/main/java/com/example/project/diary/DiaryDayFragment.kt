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
            intent.putExtra(
                getString(R.string.meal).toLowerCase(),
                getString(R.string.breakfast).toLowerCase()
            )
            intent.putExtra(getString(R.string.date).toLowerCase(), currentDate)
            startActivity(intent)
        }
        addFoodToLunchTextView.setOnClickListener {
            val intent = Intent(requireView().context, AddFoodActivity::class.java)
            intent.putExtra(
                getString(R.string.meal).toLowerCase(),
                getString(R.string.lunch).toLowerCase()
            )
            intent.putExtra(getString(R.string.date).toLowerCase(), currentDate)
            startActivity(intent)
        }
        addFoodToDinnerTextView.setOnClickListener {
            val intent = Intent(requireView().context, AddFoodActivity::class.java)
            intent.putExtra(
                getString(R.string.meal).toLowerCase(),
                getString(R.string.dinner).toLowerCase()
            )
            intent.putExtra(getString(R.string.date).toLowerCase(), currentDate)
            startActivity(intent)
        }
        addFoodToSnacksTextView.setOnClickListener {
            val intent = Intent(requireView().context, AddFoodActivity::class.java)
            intent.putExtra(
                getString(R.string.meal).toLowerCase(),
                getString(R.string.snacks).toLowerCase()
            )
            intent.putExtra(getString(R.string.date).toLowerCase(), currentDate)
            startActivity(intent)
        }
        addExerciseTextView.setOnClickListener {
            val intent = Intent(requireView().context, AddExerciseActivity::class.java)
            intent.putExtra(getString(R.string.date).toLowerCase(), currentDate)
            intent.putExtra(
                getString(R.string.menu).toLowerCase(),
                getString(R.string.add).toLowerCase()
            )
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
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

                if (snapshot.child(getString(R.string.users).toLowerCase()).child(mAuth.uid!!)
                        .exists()
                ) {

                    val caloriesReference =
                        snapshot.child(getString(R.string.users).toLowerCase()).child(mAuth.uid!!)
                            .child(getString(R.string.goals).toLowerCase())
                            .child(getString(R.string.calories).toLowerCase())

                    var caloriesGoal = 0
                    if (caloriesReference.exists()) {
                        caloriesGoal = snapshot.child(getString(R.string.users).toLowerCase())
                            .child(mAuth.uid!!).child(getString(R.string.goals).toLowerCase())
                            .child(getString(R.string.calories).toLowerCase()).value.toString()
                            .toInt()
                    }

                    val diaryReference =
                        snapshot.child(getString(R.string.users).toLowerCase()).child(mAuth.uid!!)
                            .child(getString(R.string.dates).toLowerCase()).child(currentDate)
                            .child(getString(R.string.diary).toLowerCase())


                    //Get the data for the meals
                    for (meal in diaryReference.children) {
                        if (meal.key == getString(R.string.exercise).toLowerCase()) {
                            for (index in meal.children) {
                                val exercise = index.value.toString()

                                val exerciseModel = ExerciseModel()
                                exerciseModel.setExercise(exercise)
                                exerciseModel.setKey(index.key!!)
                                exerciseList.add(exerciseModel)
                            }
                        } else {
                            for (index in meal.children) {
                                if (index.key?.contains(getString(R.string.food).toLowerCase())!!) {
                                    val foodId =
                                        index.child(getString(R.string.id).toLowerCase()).value.toString()
                                    val foodAmount =
                                        index.child(getString(R.string.amount).toLowerCase()).value.toString()

                                    val foodReference =
                                        snapshot.child(getString(R.string.food).toLowerCase())
                                            .child(foodId)

                                    val foodName =
                                        foodReference.child(getString(R.string.name).toLowerCase()).value.toString()
                                    val foodDescription =
                                        foodReference.child(getString(R.string.description).toLowerCase()).value.toString()
                                    val foodAmountMeasurement =
                                        foodReference.child(getString(R.string.measurement).toLowerCase()).value.toString()
                                    val foodCalories =
                                        foodReference.child(getString(R.string.calories).toLowerCase()).value.toString()
                                            .toInt()
                                    val foodCarbohydrates =
                                        foodReference.child(getString(R.string.carbohydrates).toLowerCase()).value.toString()
                                    val foodFats =
                                        foodReference.child(getString(R.string.fats).toLowerCase()).value.toString()
                                    val foodProteins =
                                        foodReference.child(getString(R.string.proteins).toLowerCase()).value.toString()

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
                                        meal.key.toString() == getString(R.string.breakfast).toLowerCase() -> {
                                            breakfastFoodList.add(foodModel)
                                        }
                                        meal.key.toString() == getString(R.string.lunch).toLowerCase() -> {
                                            lunchFoodList.add(foodModel)
                                        }
                                        meal.key.toString() == getString(R.string.dinner).toLowerCase() -> {
                                            dinnerFoodList.add(foodModel)
                                        }
                                        meal.key.toString() == getString(R.string.snacks).toLowerCase() -> {
                                            snacksFoodList.add(foodModel)
                                        }
                                    }
                                } else if (index.key?.contains(getString(R.string.calories).toLowerCase())!!) {
                                    val calories = index.value.toString()

                                    val foodModel = FoodModel()
                                    foodModel.setName(getString(R.string.quick_add))
                                    foodModel.setId(getString(R.string.quick_add))
                                    foodModel.setDescription(getString(R.string.empty_space))
                                    foodModel.setAmount(getString(R.string.empty_space))
                                    foodModel.setMeasurement(getString(R.string.empty_space))
                                    foodModel.setCaloriesAmount(calories)
                                    foodModel.setCarbohydratesAmount(getString(R.string.zero))
                                    foodModel.setFatsAmount(getString(R.string.zero))
                                    foodModel.setProteinsAmount(getString(R.string.zero))
                                    foodModel.setMeal(meal.key.toString())
                                    foodModel.setKey(index.key!!)

                                    when {
                                        meal.key.toString() == getString(R.string.breakfast).toLowerCase() -> {
                                            breakfastFoodList.add(foodModel)
                                        }
                                        meal.key.toString() == getString(R.string.lunch).toLowerCase() -> {
                                            lunchFoodList.add(foodModel)
                                        }
                                        meal.key.toString() == getString(R.string.dinner).toLowerCase() -> {
                                            dinnerFoodList.add(foodModel)
                                        }
                                        meal.key.toString() == getString(R.string.snacks).toLowerCase() -> {
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
                        val calories = if (food.getName() == getString(R.string.quick_add)) {
                            food.getCaloriesAmount().toInt()
                        } else {
                            food.getCaloriesAmount().toInt() * food.getAmount().toInt() / 100
                        }
                        breakfastCalories += calories
                    }
                    for (food in lunchFoodList) {
                        val calories = if (food.getName() == getString(R.string.quick_add)) {
                            food.getCaloriesAmount().toInt()
                        } else {
                            food.getCaloriesAmount().toInt() * food.getAmount().toInt() / 100
                        }
                        lunchCalories += calories
                    }
                    for (food in dinnerFoodList) {
                        val calories = if (food.getName() == getString(R.string.quick_add)) {
                            food.getCaloriesAmount().toInt()
                        } else {
                            food.getCaloriesAmount().toInt() * food.getAmount().toInt() / 100
                        }
                        dinnerCalories += calories
                    }
                    for (food in snacksFoodList) {
                        val calories = if (food.getName() == getString(R.string.quick_add)) {
                            food.getCaloriesAmount().toInt()
                        } else {
                            food.getCaloriesAmount().toInt() * food.getAmount().toInt() / 100
                        }
                        snacksCalories += calories
                    }

                    val totalCalories =
                        breakfastCalories + lunchCalories + dinnerCalories + snacksCalories
                    val remainingCalories = caloriesGoal - totalCalories

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
                    val caloriesGoalTextView =
                        requireView().findViewById<TextView>(R.id.goalCalories)

                    breakfastCaloriesTextView.text = breakfastCalories.toString()
                    lunchCaloriesTextView.text = lunchCalories.toString()
                    dinnerCaloriesTextView.text = dinnerCalories.toString()
                    snacksCaloriesTextView.text = snacksCalories.toString()
                    totalCaloriesTextView.text = totalCalories.toString()
                    remainingCaloriesTextView.text = remainingCalories.toString()
                    caloriesGoalTextView.text = caloriesGoal.toString()
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}
