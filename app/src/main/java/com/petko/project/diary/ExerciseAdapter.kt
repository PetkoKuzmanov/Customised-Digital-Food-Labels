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
import com.petko.project.addToDiary.AddExerciseActivity

class ExerciseAdapter(
    private val exerciseArrayList: MutableList<ExerciseModel>,
    private val currentDate: String
) :
    RecyclerView.Adapter<ExerciseAdapter.ViewHolder>() {
    var context: Context? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        context = recyclerView.context
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var exercise = itemView.findViewById<View>(R.id.exerciseTextView) as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.exercise_row_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val info = exerciseArrayList[position]

        val exercise = info.getExercise()
        val key = info.getKey()
        holder.exercise.text = exercise

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, AddExerciseActivity::class.java)
            intent.putExtra(context?.getString(R.string.exercise)?.toLowerCase(), exercise)
            intent.putExtra(context?.getString(R.string.key)?.toLowerCase(), key)
            intent.putExtra(context?.getString(R.string.date)?.toLowerCase(), currentDate)
            intent.putExtra(context?.getString(R.string.menu)?.toLowerCase(), context?.getString(R.string.edit)?.toLowerCase())
            startActivity(holder.itemView.context, intent, null)
        }
    }

    override fun getItemCount(): Int {
        return exerciseArrayList.size
    }
}