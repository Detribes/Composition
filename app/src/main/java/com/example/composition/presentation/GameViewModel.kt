package com.example.composition.presentation

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.composition.R
import com.example.composition.data.GameRepositoryImplementation
import com.example.composition.domain.entity.GameResult
import com.example.composition.domain.entity.GameSettings
import com.example.composition.domain.entity.Level
import com.example.composition.domain.entity.Question
import com.example.composition.domain.usecases.GenerateQuestionUseCase
import com.example.composition.domain.usecases.GetGameSettingsUseCase
import kotlin.concurrent.timer

class GameViewModel(application: Application) : AndroidViewModel(application) {
    private lateinit var _gameSettings: GameSettings
    private lateinit var _level: Level

    private val _repository = GameRepositoryImplementation
    private val _context = application

    private val _generateQuestionUseCase = GenerateQuestionUseCase(_repository)
    private val _getGameSettingsUseCase = GetGameSettingsUseCase(_repository)
    private var _timer: CountDownTimer? = null

    private val _formattedTime = MutableLiveData<String>()
    val formattedTime: LiveData<String>
        get() = _formattedTime

    private val _question = MutableLiveData<Question>()
    val question: LiveData<Question>
        get() = _question

    private val _percentOfRightAnswers = MutableLiveData<Int>()
    val percentOfRightQuestion: LiveData<Int>
        get() = _percentOfRightAnswers

    private val _progressAnswers = MutableLiveData<String>()
    val progressAnswers: LiveData<String>
        get() = _progressAnswers

    private val _enoughCount = MutableLiveData<Boolean>()
    val enoughCount: LiveData<Boolean>
        get() = _enoughCount

    private val _enoughPercent = MutableLiveData<Boolean>()
    val enoughPercent: LiveData<Boolean>
        get() = _enoughPercent

    private val _minPercent = MutableLiveData<Int>()
    val minPercent: LiveData<Int>
        get() = _minPercent

    private val _gameResult = MutableLiveData<GameResult>()
    val gameResult: LiveData<GameResult>
        get() = _gameResult

    private var _countOfRightAnswers = 0
    private var _countOfQuestions = 0

    fun startGame(level: Level){
        _setGameSettings(level)
        _startTimer()
        _generateQuestion()
        _updateProgress()
    }
    fun chooseAnswer(number: Int){
        _checkAnswers(number)
        _updateProgress()
        _generateQuestion()
    }
    override fun onCleared() {
        super.onCleared()
        _timer?.cancel()
    }

    private fun _checkAnswers(number: Int){
        val rightAnswer = question.value?.rightAnswer

        if (number == rightAnswer) {
            _countOfRightAnswers++
        }
        _countOfQuestions++
    }
    private fun _updateProgress(){
        val percent = _calculatePercentOfRightAnswers()
        _percentOfRightAnswers.value = percent
        _progressAnswers.value = String.format(
            _context.resources.getString(R.string.progress_answers),
            _countOfRightAnswers,
            _gameSettings.minCountOfRightAnswers
        )
        _enoughCount.value = _countOfRightAnswers >= _gameSettings.minCountOfRightAnswers
        _enoughPercent.value = percent >= _gameSettings.minPercentOfRightAnswers

    }
    private fun _calculatePercentOfRightAnswers() :Int{
        if (_countOfQuestions == 0) {
            return 0
        }
        return ((_countOfRightAnswers / _countOfQuestions.toDouble())*100).toInt()
    }
    private fun _setGameSettings(level: Level){
        this._level = level
        this._gameSettings = _getGameSettingsUseCase(level)
        _minPercent.value = _gameSettings.minPercentOfRightAnswers
    }
    private fun _generateQuestion() {
        _question.value = _generateQuestionUseCase(_gameSettings.maxSumValue)
    }
    private fun _formatTime(millisUntilFinished: Long) : String{
        val seconds = millisUntilFinished / MILLIS_IN_SECONDS
        val minutes = seconds / SECONDS_IN_MINUTES
        val leftSeconds = seconds - (minutes * SECONDS_IN_MINUTES)

        return String.format("%02d:%02d", minutes, leftSeconds)
    }
    private fun _startTimer(){
        _timer = object: CountDownTimer(
            _gameSettings.gameTimeInSeconds* MILLIS_IN_SECONDS,
            MILLIS_IN_SECONDS
        ){
            override fun onTick(millisUntilFinished: Long) {
                _formattedTime.value = _formatTime(millisUntilFinished)
            }
            override fun onFinish() {
                _finishGame()
            }

        }
        _timer?.start()
    }
    private fun _finishGame() {
        val gameResult = GameResult(
            enoughCount.value == true && enoughPercent.value == true,
            _countOfRightAnswers,
            _countOfQuestions,
            _gameSettings
        )
    }

    companion object{
        private const val MILLIS_IN_SECONDS = 1000L
        private const val SECONDS_IN_MINUTES = 60
    }
}