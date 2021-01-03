package com.example.project.addFood

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.project.R
import com.example.project.diary.FoodInfoActivity
import com.example.project.diary.FoodModel

class AddFoodAdapter (private val imageModelArrayList: MutableList<FoodModel>) :
    RecyclerView.Adapter<AddFoodAdapter.ViewHolder>() {

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

        holder.name.text = info.getName()
        holder.description.text = info.getDescription()
        holder.amount.text = info.getAmount() + info.getMeasurement()
        holder.caloriesAmount.text = info.getCaloriesAmount()

//        val id = info.getId()
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, FoodInfoActivity::class.java)
//        intent.putExtra("id", id)
            ContextCompat.startActivity(holder.itemView.context, intent, null)
        }
    }

    override fun getItemCount(): Int {
        return imageModelArrayList.size
    }
}