package com.example.project.diary

class FoodModel {

    private lateinit var id: String
    private lateinit var name: String
    private lateinit var description: String
    private lateinit var amount: String
    private lateinit var amountMeasurement: String
    private lateinit var caloriesAmount: String

    fun getId(): String {
        return id
    }

    fun setId(id: String) {
        this.id = id
    }

    fun getName(): String {
        return name
    }

    fun setDate(name: String) {
        this.name = name
    }

    fun getDescription(): String {
        return description
    }

    fun setWeight(description: String) {
        this.description = description
    }

    fun getAmount(): String {
        return amount
    }

    fun setAmount(amount: String) {
        this.amount = amount
    }

    fun getAmountMeasurement(): String {
        return amountMeasurement
    }

    fun setAmountMeasurement(amountMeasurement: String) {
        this.amountMeasurement = amountMeasurement
    }

    fun getCaloriesAmount(): String {
        return caloriesAmount
    }

    fun setCaloriesAmount(caloriesAmount: String) {
        this.caloriesAmount = caloriesAmount
    }
}