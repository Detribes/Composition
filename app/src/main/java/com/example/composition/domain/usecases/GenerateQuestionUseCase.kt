package com.example.composition.domain.usecases

import com.example.composition.domain.entity.Question
import com.example.composition.domain.repository.GameRepository

class GenerateQuestionUseCase(private val _repository: GameRepository) {
    operator fun invoke(maxSumValue: Int): Question{
        return _repository.generateQuestion(maxSumValue, COUNT_OF_OPTION)
    }

    companion object{
        private const val COUNT_OF_OPTION = 6
    }
}