package com.example.events.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.events.R
import com.example.events.databinding.FragmentRvWithSearchToolbarBinding

class FragmentMain : Fragment() {

    private lateinit var binding: FragmentRvWithSearchToolbarBinding

    private lateinit var adapter: EventAdapter

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRvWithSearchToolbarBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        viewModel.getEvents()
        setUI()
        setObservers()
    }

    private fun setObservers() {
        viewModel.events.observe(viewLifecycleOwner) { events ->
            binding.swipeRefresh.isRefreshing = false
            binding.tvEmpty.isVisible = events.isEmpty()
            adapter.updateItems(events)
        }
    }

    private fun setUI() {
        binding.toolbarTitle.text = getString(R.string.main)
        binding.tvEmpty.text = getString(R.string.events_empty)
        adapter = EventAdapter {

        }
        binding.rv.adapter = adapter
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getEvents()
        }
    }

}