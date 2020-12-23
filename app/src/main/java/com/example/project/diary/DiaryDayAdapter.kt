package com.example.project.diary

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.project.R

class DiaryDayAdapter (private val imageModelArrayList: MutableList<FoodModel>) :
    RecyclerView.Adapter<DiaryDayAdapter.ViewHolder>() {

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
        holder.amount.text = info.getAmount() + info.getAmountMeasurement()
        holder.caloriesAmount.text = info.getCaloriesAmount()

//        val url = info.getUrls()
//
//        holder.itemView.setOnClickListener {
//            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
//            context?.let { it1 -> ContextCompat.startActivity(it1, intent, null) }
//        }
    }

    override fun getItemCount(): Int {
        return imageModelArrayList.size
    }
}