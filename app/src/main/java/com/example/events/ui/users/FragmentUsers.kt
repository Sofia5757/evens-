package com.example.events.ui.users

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
import com.example.events.R
import com.example.events.databinding.FragmentRvWithSearchToolbarBinding
import com.example.events.utils.Resource
import com.example.events.utils.showAlert

class FragmentUsers : Fragment() {

    private lateinit var binding: FragmentRvWithSearchToolbarBinding

    private lateinit var viewModel: ViewModelUsers

    private lateinit var adapterUser: UserAdapter

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
        viewModel = ViewModelProvider(requireActivity())[ViewModelUsers::class.java]
        setUI()
        setObservers()
        viewModel.getUsers("")
    }

    private fun setObservers() {
        viewModel.users.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    it.data?.let { users ->
                        adapterUser.updateItems(users)
                        binding.tvEmpty.isVisible = users.isEmpty()
                    }
                }

                is Resource.Error -> {
                    requireContext().showAlert()
                }
            }
        }
    }

    private fun setUI() {
        binding.toolbarTitle.text = getString(R.string.pupils_and_teachers)
        binding.tvEmpty.text = getString(R.string.list_empty)
        adapterUser = UserAdapter(requireContext())
        binding.rv.adapter = adapterUser
        binding.rv.itemAnimator = null
        (binding.toolbar.menu.findItem(R.id.itemSearch).actionView as SearchView).apply {
            queryHint = getString(R.string.users_search_hint)
            findViewById<LinearLayout>(androidx.appcompat.R.id.search_plate).setBackgroundColor(
                Color.TRANSPARENT
            )
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    viewModel.getUsers(newText?:"")
                    return true
                }
            })
        }
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getUsers(viewModel.curQuery)
            binding.swipeRefresh.isRefreshing = false
        }
    }


}