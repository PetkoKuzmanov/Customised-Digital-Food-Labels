package com.example.project.diary

class FoodModel {

    private lateinit var id: String
    private lateinit var name: String
    private lateinit var description: String
    private lateinit var amount: String
    private lateinit var measurement: String
    private lateinit var caloriesAmount: String
    private lateinit var carbohydratesAmount: String
    private lateinit var fatsAmount: String
    private lateinit var proteinsAmount: String

    fun getId(): String {
        return id
    }

    fun setId(id: String) {
        this.id = id
    }

    fun getName(): String {
        return name
    }

    fun setName(name: String) {
        this.name = name
    }

    fun getDescription(): String {
        return description
    }

    fun setDescription(description: String) {
        this.description = description
    }

    fun getAmount(): String {
        return amount
    }

    fun setAmount(amount: String) {
        this.amount = amount
    }

    fun getMeasurement(): String {
        return measurement
    }

    fun setMeasurement(amountMeasurement: String) {
        this.measurement = amountMeasurement
    }

    fun getCaloriesAmount(): String {
        return caloriesAmount
    }

    fun setCaloriesAmount(caloriesAmount: String) {
        this.caloriesAmount = caloriesAmount
    }

    fun getCarbohydratesAmount(): String {
        return carbohydratesAmount
    }

    fun setCarbohydratesAmount(carbohydratesAmount: String) {
        this.carbohydratesAmount = carbohydratesAmount
    }

    fun getProteinsAmount(): String {
        return proteinsAmount
    }

    fun setProteinsAmount(proteinsAmount: String) {
        this.proteinsAmount = proteinsAmount
    }

    fun getFatsAmount(): String {
        return fatsAmount
    }

    fun setFatsAmount(fatsAmount: String) {
        this.fatsAmount = fatsAmount
    }
}