package com.example.composition.presentation

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewTreeLifecycleOwner
import com.example.composition.R
import com.example.composition.databinding.FragmentGameBinding
import com.example.composition.domain.entity.GameResult
import com.example.composition.domain.entity.GameSettings
import com.example.composition.domain.entity.Level
import java.lang.RuntimeException

class GameFragment : Fragment() {
    private lateinit var _level: Level

    private var _binding: FragmentGameBinding? = null
    private val binding: FragmentGameBinding
        get() = _binding ?: throw RuntimeException("Fragment is null")
    private val tvOptions by lazy{
        mutableListOf<TextView>().apply {
            add(binding.tvOption1)
            add(binding.tvOption2)
            add(binding.tvOption3)
            add(binding.tvOption4)
            add(binding.tvOption5)
            add(binding.tvOption6)
        }
    }
    private val _viewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[GameViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _parseArgs()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGameBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _viewModel.startGame(_level)
       _observeViewModel()
        _setClickListenersToOptions()
        _viewModel.startGame(_level)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun _parseArgs(){
        requireArguments().getParcelable<Level>(KEY_GAME_LEVEL)?.let {
            _level = it
        }
    }
    private fun _launchGameFinishedFragment(gameResult: GameResult){
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, GameFinishedFragment.newInstance(gameResult))
            .addToBackStack(null)
            .commit()
    }
    private fun _observeViewModel() {
        _viewModel.question.observe(viewLifecycleOwner) {
            binding.tvSum.text = it.sum.toString()
            binding.tvLeftNumber.text = it.visibleNumber.toString()
            for (i in 0 until tvOptions.size) {
                tvOptions[i].text = it.options[i].toString()
            }
        }
        _viewModel.percentOfRightQuestion.observe(viewLifecycleOwner) {
            binding.progressBar.setProgress(it, true)
        }
        _viewModel.enoughCount.observe(viewLifecycleOwner) {
            binding.tvAnswersProgress.setTextColor(_getColorByState(it))
        }
        _viewModel.enoughPercent.observe(viewLifecycleOwner){
            val color = _getColorByState(it)
            binding.progressBar.progressTintList = ColorStateList.valueOf(color)
        }
        _viewModel.formattedTime.observe(viewLifecycleOwner){
            binding.tvTimer.text = it
        }
        _viewModel.minPercent.observe(viewLifecycleOwner){
            binding.progressBar.secondaryProgress = it
        }
        _viewModel.gameResult.observe(viewLifecycleOwner){
            _launchGameFinishedFragment(it)
        }
        _viewModel.progressAnswers.observe(viewLifecycleOwner) {
            binding.tvAnswersProgress.text = it
        }
    }
    private fun _setClickListenersToOptions() {
        for (tvOption in tvOptions) {
            tvOption.setOnClickListener {
                _viewModel.chooseAnswer(tvOption.text.toString().toInt())
            }
        }
    }
    private fun _getColorByState(state: Boolean): Int{
        val colorResId = if(state){
            android.R.color.holo_green_light
        } else {
            android.R.color.holo_red_light
        }
        return ContextCompat.getColor(requireContext(), colorResId)
    }
    companion object{
        const val NAME = "GameFragment"
        private const val KEY_GAME_LEVEL = "level"

        fun newInstance(level: Level) :GameFragment{
            return GameFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_GAME_LEVEL, level)
                }
            }
        }
    }
}