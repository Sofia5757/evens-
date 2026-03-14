package com.example.events.ui.my_events

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
import com.example.events.utils.Resource
import com.example.events.utils.showAlert

class FragmentMyEvents: Fragment() {

    private lateinit var binding: FragmentRvWithFilterBinding

    private lateinit var adapter: MyEventAdapter

    private lateinit var viewModel: ViewModelMyEvents

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
        viewModel = ViewModelProvider(this).get(ViewModelMyEvents::class.java)
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
            binding.fabAddEvent.isVisible = it.data?.teacher == true
            viewModel.getEvents()
        }
    }

    private fun setUI() {
        binding.toolbarTitle.text = getString(R.string.my_events)
        binding.tvEmpty.text = getString(R.string.events_empty)
        adapter = MyEventAdapter {
            val action = FragmentMyEventsDirections.actionFragmentMyEventsToFragmentEventDetails(it)
            findNavController().navigate(action)
        }
        binding.rv.adapter = adapter
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getEvents()
        }
        binding.fabAddEvent.setOnClickListener {
            val action = FragmentMyEventsDirections.actionFragmentMyEventsToFragmentAddEvent()
            findNavController().navigate(action)
        }
    }
}
