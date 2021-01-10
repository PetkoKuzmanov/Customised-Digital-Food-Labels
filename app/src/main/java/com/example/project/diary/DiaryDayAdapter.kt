package com.example.project.diary

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.project.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

class DiaryDayAdapter(
    private val imageModelArrayList: MutableList<FoodModel>,
    private val currentDate: String
) :
    RecyclerView.Adapter<DiaryDayAdapter.ViewHolder>() {

    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var database: DatabaseReference
    var context: Context? = null

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
        val info = imageModelArrayList[position]

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
        holder.caloriesAmount.text = caloriesAmount

        if (name == "Quick Add") {
            holder.itemView.setOnClickListener {
                val intent = Intent(holder.itemView.context, QuickAddInfoActivity::class.java)
                intent.putExtra("caloriesAmount", caloriesAmount)
                intent.putExtra("meal", meal)
                intent.putExtra("key", key)
                intent.putExtra("date", currentDate)
                startActivity(holder.itemView.context, intent, null)
            }
        } else {
            holder.itemView.setOnClickListener {
                val intent = Intent(holder.itemView.context, FoodInfoActivity::class.java)
                intent.putExtra("menu", "diary")
                intent.putExtra("id", id)
                intent.putExtra("name", name)
                intent.putExtra("description", description)
                intent.putExtra("amount", amount)
                intent.putExtra("caloriesAmount", caloriesAmount)
                intent.putExtra("carbohydratesAmount", carbohydratesAmount)
                intent.putExtra("fatsAmount", fatsAmount)
                intent.putExtra("proteinsAmount", proteinsAmount)
                intent.putExtra("meal", meal)
                intent.putExtra("key", key)
                intent.putExtra("date", currentDate)
                startActivity(holder.itemView.context, intent, null)
            }
        }
    }

    override fun getItemCount(): Int {
        return imageModelArrayList.size
    }
}