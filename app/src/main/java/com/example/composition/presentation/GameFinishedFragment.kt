package com.example.composition.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.composition.R
import com.example.composition.databinding.FragmentGameFinishedBinding
import com.example.composition.databinding.FragmentWelcomeBinding
import com.example.composition.domain.entity.GameResult
import java.lang.RuntimeException

class GameFinishedFragment : Fragment() {
    private val args by navArgs<GameFinishedFragmentArgs>()
//    private lateinit var _gameResult: GameResult
    private  var _binding: FragmentGameFinishedBinding? = null
    private val binding: FragmentGameFinishedBinding
        get() = _binding ?: throw RuntimeException("Fragment is null")

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        _parseArgs()
//    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGameFinishedBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _setupOnClickListeners()
//        _bindViews()
        binding.gameResult = args.gameResult
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun _setupOnClickListeners(){
        binding.buttonRetry.setOnClickListener {
            _retryGame()
        }
//        val callback = object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                _retryGame()
//            }
//        }
//        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }
//    private fun _bindViews(){
//        with(binding) {
//            emojiResult.setImageResource(_getSmileResId())
////            tvRequiredAnswers.text = String.format(
////                getString(R.string.required_score),
////                args.gameResult.gameSettings.minCountOfRightAnswers
////            )
////            tvScoreAnswers.text = String.format(
////                getString(R.string.score_answers),
////                args.gameResult.countOfRightAnswers
////            )
////            tvRequiredPercentage.text = String.format(
////                getString(R.string.required_percentage),
////                args.gameResult.gameSettings.minPercentOfRightAnswers
////            )
////            tvScorePercentage.text = String.format(
////                getString(R.string.score_percentage),
////                _getPercentOfRightAnswers()
////            )
//        }
//    }
//    private fun _getSmileResId(): Int{
//        return if (args.gameResult.winner) {
//            R.drawable.ic_happy
//        } else {
//            R.drawable.ic_sad
//        }
//    }

//    private fun _getPercentOfRightAnswers() = with(args.gameResult) {
//        if (countOfQuestions == 0) {
//            0
//        } else {
//            ((countOfRightAnswers / countOfQuestions.toDouble()) * 100).toInt()
//        }
//    }
//    private fun _parseArgs(){
//        requireArguments().getParcelable<GameResult>(KEY_GAME_RESULT)?.let {
//            _gameResult = it
//        }
//    }
    private fun _retryGame(){
        findNavController().popBackStack()
    }
    companion object{
       private const val KEY_GAME_RESULT = "game_result"

        fun newInstance(gameResult: GameResult) :GameFinishedFragment{
            return GameFinishedFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_GAME_RESULT, gameResult)
                }
            }
        }
    }
}