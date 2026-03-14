package com.example.events.ui.main

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.events.R
import com.example.events.databinding.FragmentRvWithSearchToolbarBinding
import com.example.events.utils.Resource
import com.example.events.utils.showAlert

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
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        setUI()
        setObservers()
        viewModel.getEvents("")
    }

    private fun setObservers() {
        viewModel.events.observe(viewLifecycleOwner) { data ->
            binding.swipeRefresh.isRefreshing = false
            when (data) {
                is Resource.Success -> {
                    data.data?.let {
                        adapter.myProfile = viewModel.myProfile
                        binding.tvEmpty.isVisible = it.isEmpty()
                        adapter.updateItems(it)
                    }
                }
                is Resource.Error -> {
                    requireContext().showAlert(data.error?.message)
                }
            }
        }
        viewModel.result.observe(viewLifecycleOwner){
            when (it) {
                is Resource.Success -> {
                    viewModel.getEvents(viewModel.curQuery)
                }
                is Resource.Error -> {
                    requireContext().showAlert(it.error?.message)
                }
            }
        }
    }

    private fun setUI() {
        binding.toolbarTitle.text = getString(R.string.main)
        binding.tvEmpty.text = getString(R.string.events_empty)
        adapter = EventAdapter(
            onParticipantsClick = {
                val action = FragmentMainDirections.actionFragmentMainToFragmentParticipants(
                    it.participants.toTypedArray(),
                    it.withParent.toBooleanArray()
                )
                findNavController().navigate(action)
            },
            onClick = {
                val action = FragmentMainDirections.actionFragmentMainToFragmentEventDetails(it)
                findNavController().navigate(action)
            },
            onDeleteClick = {
                viewModel.cancelEvent(it)
            },
            onCancelClick = {
                viewModel.cancelParticipation(it)
            }
        )
        binding.rv.adapter = adapter
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getEvents(viewModel.curQuery)
        }
        (binding.toolbar.menu.findItem(R.id.itemSearch).actionView as SearchView).apply {
            queryHint = getString(R.string.events_search_hint)
            findViewById<LinearLayout>(androidx.appcompat.R.id.search_plate).setBackgroundColor(
                Color.TRANSPARENT
            )
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    viewModel.getEvents(newText?:"")
                    return true
                }
            })
        }
    }

}