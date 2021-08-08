package com.petko.project.diary

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.petko.project.R

class DiaryDayAdapter(
    private val foodModelList: MutableList<FoodModel>,
    private val currentDate: String
) :
    RecyclerView.Adapter<DiaryDayAdapter.ViewHolder>() {
    var context: Context? = null
    private var rowCount = 0

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
        return if (foodModelList[rowCount].getName() == context?.getString(R.string.quick_add)) {
            val v = inflater.inflate(R.layout.quick_add_row_layout, parent, false)
            rowCount++
            ViewHolder(v)
        } else {
            val v = inflater.inflate(R.layout.food_row_layout, parent, false)
            rowCount++
            ViewHolder(v)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val info = foodModelList[position]

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
        val key = info.getKey()

        holder.name.text = name
        holder.description.text = description
        holder.amount.text = amount + measurement
        if (amount.isNotBlank()) {
            holder.caloriesAmount.text =
                ((caloriesAmount.toInt() * amount.toInt()) / 100).toString()
        } else {
            holder.caloriesAmount.text = caloriesAmount
        }

        if (name == context?.getString(R.string.quick_add)) {
            holder.itemView.setOnClickListener {
                val intent = Intent(holder.itemView.context, QuickAddInfoActivity::class.java)
                intent.putExtra(context?.getString(R.string.caloriesAmount), caloriesAmount)
                intent.putExtra(context?.getString(R.string.meal)?.toLowerCase(), meal)
                intent.putExtra(context?.getString(R.string.key)?.toLowerCase(), key)
                intent.putExtra(context?.getString(R.string.date)?.toLowerCase(), currentDate)
                startActivity(holder.itemView.context, intent, null)
            }
        } else {
            holder.itemView.setOnClickListener {
                val intent = Intent(holder.itemView.context, FoodInfoActivity::class.java)
                intent.putExtra(
                    context?.getString(R.string.menu)?.toLowerCase(),
                    context?.getString(R.string.diary)?.toLowerCase()
                )
                intent.putExtra(context?.getString(R.string.id)?.toLowerCase(), id)
                intent.putExtra(context?.getString(R.string.name)?.toLowerCase(), name)
                intent.putExtra(
                    context?.getString(R.string.description)?.toLowerCase(),
                    description
                )
                intent.putExtra(context?.getString(R.string.amount)?.toLowerCase(), amount)
                intent.putExtra(context?.getString(R.string.caloriesAmount), caloriesAmount)
                intent.putExtra(
                    context?.getString(R.string.carbohydratesAmount),
                    carbohydratesAmount
                )
                intent.putExtra(context?.getString(R.string.fatsAmount), fatsAmount)
                intent.putExtra(context?.getString(R.string.proteinsAmount), proteinsAmount)
                intent.putExtra(context?.getString(R.string.meal)?.toLowerCase(), meal)
                intent.putExtra(context?.getString(R.string.key)?.toLowerCase(), key)
                intent.putExtra(context?.getString(R.string.date)?.toLowerCase(), currentDate)
                startActivity(holder.itemView.context, intent, null)
            }
        }
    }

    override fun getItemCount(): Int {
        return foodModelList.size
    }
}