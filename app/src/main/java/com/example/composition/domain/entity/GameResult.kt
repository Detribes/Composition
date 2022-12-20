package com.example.composition.domain.entity

import com.example.composition.domain.entity.GameSettings

data class GameResult(
    val winner: Boolean,
    val countOfRightAnswers: Int,
    val countOfQuestions: Int,
    val gameSettings: GameSettings
)