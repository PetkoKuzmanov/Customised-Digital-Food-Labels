package com.example.project.addFood

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.project.R
import com.example.project.diary.FoodInfoActivity
import com.example.project.diary.FoodModel
import java.util.*
import kotlin.collections.ArrayList

class AddFoodAdapter(private val foodModelsList: MutableList<FoodModel>) :
    RecyclerView.Adapter<AddFoodAdapter.ViewHolder>(), Filterable {

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
        holder.caloriesAmount.text = caloriesAmount

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, FoodInfoActivity::class.java)
            intent.putExtra("id", id)
            intent.putExtra("name", name)
            intent.putExtra("description", description)
            intent.putExtra("amount", amount)
            intent.putExtra("caloriesAmount", caloriesAmount)
            intent.putExtra("carbohydratesAmount", carbohydratesAmount)
            intent.putExtra("fatsAmount", fatsAmount)
            intent.putExtra("proteinsAmount", proteinsAmount)
            intent.putExtra("meal", meal)
            ContextCompat.startActivity(holder.itemView.context, intent, null)
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
}