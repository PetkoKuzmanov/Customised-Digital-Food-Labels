package com.example.project.weight

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.project.R

class WeightAdapter(private val imageModelArrayList: MutableList<WeightModel>) :
    RecyclerView.Adapter<WeightAdapter.ViewHolder>() {

    var context: Context? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        context = recyclerView.context
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var date = itemView.findViewById<View>(R.id.dateTextView) as TextView
        var weight = itemView.findViewById<View>(R.id.weightTextView) as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.weight_row_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val info = imageModelArrayList[position]

        holder.date.text = info.getDate()
        holder.weight.text = info.getWeight().toString() + "kg"

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