package com.example.events.ui.event_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.events.R
import com.example.events.data.entities.Event
import com.example.events.data.entities.User
import com.example.events.databinding.FragmentEventDetailsBinding
import com.example.events.utils.Resource
import com.example.events.utils.showAlert
import com.example.events.utils.toEventDetailsDate

class FragmentEventDetails: Fragment() {

    private lateinit var binding: FragmentEventDetailsBinding

    private lateinit var viewModel: ViewModelEventDetails

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEventDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(ViewModelEventDetails::class.java)
        arguments?.getSerializable("event", Event::class.java)?.let{
            viewModel.event = it
            setObservers()
        }
    }

    private fun setObservers() {
        viewModel.result.observe(viewLifecycleOwner){
            findNavController().popBackStack()
        }
        viewModel.myProfile.observe(viewLifecycleOwner){
            when(it){
                is Resource.Success->{
                    it.data?.let {
                        setUI(viewModel.event!!, it)
                    }
                }
                is Resource.Error->{
                    requireContext().showAlert()
                }
            }
        }
    }

    private fun setUI(event: Event, myProfile: User) {
        binding.apply {
            tvName.text = event.name
            event.description?.let {
                tvDescription.isVisible = true
                tvDescription.text = it
            }
            tvPlace.text = event.place
            tvDate.text = event.date.toEventDetailsDate()
            tvAccompanist.text = getString(R.string.accompanist_value, event.accompanistName)
            event.clas?.let{
                tvClass.isVisible = true
                tvClass.text = getString(R.string.class_value, it)
            }
            event.bonus?.let{
                tvBonuses.isVisible = true
                tvBonuses.text = getString(R.string.bonus_value, it)
            }
            when{
                event.participants.contains(myProfile.id)->{
                    btAction.isVisible = true
                    btAction.text = getString(R.string.cancel_participation)
                    btAction.setOnClickListener {
                        viewModel.cancelParticipation()
                    }
                }
                event.accompanistId == myProfile.id->{
                    btAction.isVisible = true
                    btAction.text = getString(R.string.cancel_event)
                    btAction.setOnClickListener {
                        viewModel.cancelEvent()
                    }
                }
                event.clas!=null && event.clas != myProfile.clas->{
                    btAction.isVisible = false
                }
                else->{
                    btAction.isVisible = true
                    btAction.text = getString(R.string.by_myself)
                    btWithParent.isVisible = !myProfile.teacher
                    btAction.setOnClickListener {
                        viewModel.participateInEvent(false)
                    }
                    btWithParent.setOnClickListener {
                        viewModel.participateInEvent(true)
                    }
                }
            }
        }
    }

}