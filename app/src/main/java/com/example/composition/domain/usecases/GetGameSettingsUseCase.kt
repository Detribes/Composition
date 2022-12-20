package com.example.composition.domain.usecases

import com.example.composition.domain.entity.GameSettings
import com.example.composition.domain.entity.Level
import com.example.composition.domain.repository.GameRepository

class GetGameSettingsUseCase(private val _repository: GameRepository)  {
    operator fun invoke(level: Level): GameSettings {
        return _repository.getGameSettings(level)
    }
}