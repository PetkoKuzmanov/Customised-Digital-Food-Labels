package com.example.project.addToDiary

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
import com.example.project.R
import com.example.project.diary.FoodInfoActivity
import com.example.project.diary.FoodModel
import java.util.*
import kotlin.collections.ArrayList

class AddFoodAdapter(
    private val historyFoodModelList: MutableList<FoodModel>,
    private val foodModelListToFilter: MutableList<FoodModel>,
    private val currentDate: String
) :
    RecyclerView.Adapter<AddFoodAdapter.ViewHolder>(), Filterable {

    var context: Context? = null
    var filteredFoodModelList = ArrayList<FoodModel>()

    init {
        filteredFoodModelList = historyFoodModelList as ArrayList<FoodModel>
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
        val v = inflater.inflate(R.layout.food_row_with_background_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val info = filteredFoodModelList[position]

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
            intent.putExtra(
                context?.getString(R.string.menu)?.toLowerCase(),
                context?.getString(R.string.addFood)
            )
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
        return filteredFoodModelList.size
    }

    override fun getFilter(): Filter {
        return exampleFilter
    }


    private val exampleFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            var filteredList: MutableList<FoodModel> = ArrayList()
            if (constraint.isEmpty()) {
                filteredList = foodModelListToFilter as ArrayList<FoodModel>
            } else {
                val filterPattern =
                    constraint.toString().toLowerCase(Locale.ROOT).trim { it <= ' ' }
                for (item in foodModelListToFilter) {
                    if (item.getName().toLowerCase(Locale.ROOT).contains(filterPattern)) {
                        filteredList.add(item)
                    }
                }
                for (item in foodModelListToFilter) {
                    if (item.getName().toLowerCase(Locale.ROOT)
                            .contains(filterPattern) && !foodModelListToFilter.contains(item)
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
            filteredFoodModelList = results?.values as ArrayList<FoodModel>
            notifyDataSetChanged()
        }
    }
}