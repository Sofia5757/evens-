package com.example.events.ui.class_events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.events.R
import com.example.events.databinding.FragmentRvWithFilterBinding
import com.example.events.ui.my_events.MyEventAdapter
import com.example.events.ui.my_events.ViewModelMyEvents
import com.example.events.utils.Resource
import com.example.events.utils.showAlert

class FragmentClassEvents: Fragment() {

    private lateinit var binding: FragmentRvWithFilterBinding

    private lateinit var adapter: MyEventAdapter

    private lateinit var viewModel: ViewModelClassEvents

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRvWithFilterBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(ViewModelClassEvents::class.java)
        viewModel.getProfile()
        setUI()
        setObservers()
    }

    private fun setObservers() {
        viewModel.events.observe(viewLifecycleOwner) { data ->
            binding.swipeRefresh.isRefreshing = false
            when (data) {
                is Resource.Success -> {
                    binding.tvEmpty.isVisible = data.data.isNullOrEmpty()
                    data.data?.let {
                        adapter.updateItems(it)
                    }
                }
                is Resource.Error -> {
                    requireContext().showAlert(data.error?.message)
                }
            }
        }
        viewModel.userInfo.observe(viewLifecycleOwner){
            viewModel.getEvents()
        }
    }

    private fun setUI() {
        binding.toolbarTitle.text = getString(R.string.class_events)
        binding.tvEmpty.text = getString(R.string.events_empty)
        adapter = MyEventAdapter {
            val action = FragmentClassEventsDirections.actionFragmentClassEventsToFragmentEventDetails(it)
            findNavController().navigate(action)
        }
        binding.rv.adapter = adapter
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getEvents()
        }
        binding.fabAddEvent.setOnClickListener {
            val action = FragmentClassEventsDirections.actionFragmentClassEventsToFragmentAddEvent()
            findNavController().navigate(action)
        }
    }
}
