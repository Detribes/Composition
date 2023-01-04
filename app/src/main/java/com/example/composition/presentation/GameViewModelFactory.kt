package com.example.composition.presentation

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.composition.domain.entity.Level
import java.lang.RuntimeException

class GameViewModelFactory(
    private val _level: Level,
    private val _application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            return GameViewModel(_application, _level) as T
        }
        throw RuntimeException("Unknown ViewModel $modelClass")
    }
}