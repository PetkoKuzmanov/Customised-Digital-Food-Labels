package com.petko.project.addToDiary

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.recyclerview.widget.RecyclerView
import com.petko.project.R
import com.petko.project.diary.FoodInfoActivity
import com.petko.project.diary.FoodModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList

class SearchAllFoodAdapter(
    private val foodModelsList: MutableList<FoodModel>,
    private val currentDate: String,
    private val meal: String
) :
    RecyclerView.Adapter<SearchAllFoodAdapter.ViewHolder>(), Filterable {

    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var database: DatabaseReference

    var context: Context? = null
    var filteredFoodModelsList = ArrayList<FoodModel>()

    init {
        filteredFoodModelsList = foodModelsList as ArrayList<FoodModel>
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        context = recyclerView.context
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name = itemView.findViewById<View>(R.id.foodName) as TextView
        var description = itemView.findViewById<View>(R.id.foodDescription) as TextView
        var amount = itemView.findViewById<View>(R.id.foodAmount) as TextView
        var caloriesAmount = itemView.findViewById<View>(R.id.caloriesAmount) as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.food_row_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val info = filteredFoodModelsList[position]

        val id = info.getId()
        val name = info.getName()
        val description = info.getDescription()
        val amount = info.getAmount()
        val measurement = info.getMeasurement()
        val caloriesAmount = info.getCaloriesAmount()
        val carbohydratesAmount = info.getCarbohydratesAmount()
        val fatsAmount = info.getFatsAmount()
        val proteinsAmount = info.getProteinsAmount()
        val meal = info.getMeal()

        holder.name.text = name
        holder.description.text = description
        holder.amount.text = amount + measurement
        holder.caloriesAmount.text = ((caloriesAmount.toInt() * amount.toInt()) / 100).toString()

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, FoodInfoActivity::class.java)
            intent.putExtra(context?.getString(R.string.menu)?.toLowerCase(), context?.getString(R.string.addFood))
            intent.putExtra(context?.getString(R.string.id)?.toLowerCase(), id)
            intent.putExtra(context?.getString(R.string.name)?.toLowerCase(), name)
            intent.putExtra(context?.getString(R.string.description)?.toLowerCase(), description)
            intent.putExtra(context?.getString(R.string.amount)?.toLowerCase(), amount)
            intent.putExtra(context?.getString(R.string.caloriesAmount), caloriesAmount)
            intent.putExtra(context?.getString(R.string.carbohydratesAmount), carbohydratesAmount)
            intent.putExtra(context?.getString(R.string.fatsAmount), fatsAmount)
            intent.putExtra(context?.getString(R.string.proteinsAmount), proteinsAmount)
            intent.putExtra(context?.getString(R.string.meal)?.toLowerCase(), meal)
            intent.putExtra(context?.getString(R.string.date)?.toLowerCase(), currentDate)
            startActivityForResult(holder.itemView.context as Activity, intent, 123, null)
        }
    }

    override fun getItemCount(): Int {
        return filteredFoodModelsList.size
    }

    override fun getFilter(): Filter {
        return exampleFilter
    }


    private val exampleFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            var filteredList: MutableList<FoodModel> = ArrayList()
            if (constraint.isEmpty()) {
                filteredList = foodModelsList as ArrayList<FoodModel>
            } else {
                val filterPattern =
                    constraint.toString().toLowerCase(Locale.ROOT).trim { it <= ' ' }
                for (item in filteredFoodModelsList) {
                    if (item.getName().toLowerCase(Locale.ROOT).contains(filterPattern)) {
                        filteredList.add(item)
                    }
                }
                for (item in foodModelsList) {
                    if (item.getName().toLowerCase(Locale.ROOT)
                            .contains(filterPattern) && !filteredFoodModelsList.contains(item)
                    ) {
                        filteredList.add(item)
                    }
                }
            }

            val result = FilterResults()
            result.values = filteredList
            return result
        }


        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            filteredFoodModelsList = results?.values as ArrayList<FoodModel>
            notifyDataSetChanged()
        }
    }

    private fun getAllFoodData(): ArrayList<FoodModel> {
        val foodModelList = ArrayList<FoodModel>()

        database = Firebase.database.reference
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                foodModelList.clear()

                val foodListReference = mAuth.currentUser?.let {
                    snapshot.child("food")
                }

                for (index in foodListReference?.children!!) {
                    val foodId = index.key.toString()
                    val foodName = index.child("name").value.toString()
                    val foodDescription = index.child("description").value.toString()
                    val foodAmountMeasurement = index.child("measurement").value.toString()
                    val foodCalories = index.child("calories").value.toString()
                    val foodCarbohydrates = index.child("carbohydrates").value.toString()
                    val foodFats = index.child("fats").value.toString()
                    val foodProteins = index.child("proteins").value.toString()

                    val foodModel = FoodModel(
                        foodId,
                        foodName,
                        foodDescription,
                        "100",
                        foodAmountMeasurement,
                        foodCalories,
                        foodCarbohydrates,
                        foodFats,
                        foodProteins,
                        meal,
                        foodId
                    )
                    foodModelList.add(foodModel)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        return foodModelList
    }
}