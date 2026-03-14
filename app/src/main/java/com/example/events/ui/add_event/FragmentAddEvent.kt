package com.example.events.ui.add_event

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.events.R
import com.example.events.databinding.FragmentAddEventBinding
import com.example.events.ui.my_events.ViewModelMyEvents
import com.example.events.utils.Resource
import com.example.events.utils.toEventCreateDate
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.Calendar
import java.util.Date

class FragmentAddEvent : Fragment() {

    private lateinit var binding: FragmentAddEventBinding

    private lateinit var viewModel: ViewModelAddEvent

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddEventBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(ViewModelAddEvent::class.java)
        setUI()
        setObservers()
    }

    private fun setObservers() {
        viewModel.isPostBtEnabled.observe(viewLifecycleOwner) {
            binding.btPost.isEnabled = it
            binding.btPost.backgroundTintList =
                ColorStateList.valueOf(resources.getColor(if (it) R.color.blue_super_light else R.color.gray_inactive))
        }
        viewModel.result.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    findNavController().popBackStack()
                }

                is Resource.Error -> {
                    Toast.makeText(requireContext(), it.error?.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setUI() {
        binding.apply {
            tietName.addTextChangedListener {
                viewModel.enterName(it.toString())
            }
            tietDescription.addTextChangedListener {
                viewModel.enterDescription(it.toString())
            }
            tietPlace.addTextChangedListener {
                viewModel.enterPlace(it.toString())
            }
            tietBonus.addTextChangedListener {
                viewModel.enterBonus(it.toString())
            }
            tietClas.addTextChangedListener {
                viewModel.enterClas(it.toString())
            }
            btPost.setOnClickListener {
                viewModel.createEvent()
            }
            tietDate.setOnClickListener {
                selectDate()
            }
            tilDate.setOnClickListener {
                selectDate()
            }
        }
    }

    private fun selectDate() {
        MaterialDatePicker.Builder.datePicker()
            .setTitleText(getString(R.string.choose_event_date))
            .build()
            .apply {
                addOnPositiveButtonClickListener { date ->
                    val picker = MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_24H)
                        .setTitleText(getString(R.string.choose_event_time))
                        .build()
                    picker.addOnPositiveButtonClickListener {
                        val calendar = Calendar.getInstance().apply {
                            timeInMillis = date
                            set(Calendar.HOUR_OF_DAY, picker.hour)
                            set(Calendar.MINUTE, picker.minute)
                        }
                        viewModel.enterDate(calendar.time)
                        binding.tietDate.setText(calendar.time.toEventCreateDate())
                    }
                    picker.show(parentFragmentManager, null)
                }
            }.show(parentFragmentManager, null)
    }

}