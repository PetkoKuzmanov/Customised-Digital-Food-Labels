package com.petko.project.diary

class ExerciseModel {

    private lateinit var exercise: String
    private lateinit var key: String

    fun getExercise(): String {
        return exercise
    }

    fun setExercise(exercise: String) {
        this.exercise = exercise
    }

    fun getKey(): String {
        return key
    }

    fun setKey(key: String) {
        this.key = key
    }
}