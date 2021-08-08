package com.petko.project.weight

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.petko.project.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class WeightAdapter(private val weightModelsList: MutableList<WeightModel>) :
    RecyclerView.Adapter<WeightAdapter.ViewHolder>() {

    var context: Context? = null
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var database: DatabaseReference

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
        val info = weightModelsList[position]

        holder.date.text = info.getDate()
        holder.weight.text =
            info.getWeight().toString() + context?.getString(R.string.kg)?.toLowerCase()
    }

    override fun getItemCount(): Int {
        return weightModelsList.size
    }

    fun addItem(weight: Double, date: String) {
        database = Firebase.database.reference
        mAuth.currentUser?.let {
            database.child(context?.getString(R.string.users)?.toLowerCase()!!).child(it.uid)
                .child(context?.getString(R.string.dates)?.toLowerCase()!!).child(date).child(
                    context?.getString(R.string.weight)?.toLowerCase()!!
                )
                .setValue(weight)
        }
    }

}