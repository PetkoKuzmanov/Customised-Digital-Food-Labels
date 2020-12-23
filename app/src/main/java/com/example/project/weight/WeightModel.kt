package com.example.project.weight


class WeightModel {

    private lateinit var date: String
    private var weight: Double = 0.0

    fun getDate(): String {
        return date
    }

    fun setDate(date: String) {
        this.date = date
    }

    fun getWeight(): Double {
        return weight
    }

    fun setWeight(weight: Double) {
        this.weight = weight
    }
}