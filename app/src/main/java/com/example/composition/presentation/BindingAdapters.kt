package com.example.composition.presentation

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.composition.R
import com.example.composition.domain.entity.GameResult

@BindingAdapter("requiredAnswers")
fun bindRequireAnswers(textView: TextView, count: Int){
    textView.text = String.format(
        textView.context.getString(R.string.required_score),
        count
    )
}

@BindingAdapter("scoreAnswers")
fun bindScoreAnswers(textView: TextView, count: Int){
    textView.text = String.format(
        textView.context.getString(R.string.score_answers),
        count
    )
}

@BindingAdapter("requirePercentage")
fun bindRequirePercentage(textView: TextView, count: Int){
    textView.text = String.format(
       textView.context.getString(R.string.required_percentage),
        count
    )
}

@BindingAdapter("requirePercentage")
fun bindRequirePercentage(textView: TextView, gameResult: GameResult){
    textView.text = String.format(
        textView.context.getString(R.string.score_percentage),
        _getPercentOfRightAnswers(gameResult)
    )
}

@BindingAdapter("emoji")
fun bindEmojiResult(imageView: ImageView, winner: Boolean){
    imageView.setImageResource(_getSmileResId(winner))
}

private fun _getPercentOfRightAnswers(gameResult: GameResult) = with(gameResult) {
    if (countOfQuestions == 0) {
        0
    } else {
        ((countOfRightAnswers / countOfQuestions.toDouble()) * 100).toInt()
    }
}
private fun _getSmileResId(winner: Boolean): Int{
    return if (winner){
        R.drawable.ic_happy
    } else {
        R.drawable.ic_sad
    }
}