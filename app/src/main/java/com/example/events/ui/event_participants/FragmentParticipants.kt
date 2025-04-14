package com.example.events.ui.event_participants

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
import com.example.events.data.entities.UserList
import com.example.events.databinding.FragmentRvWithSearchToolbarBinding
import com.example.events.ui.main.FragmentMain
import com.example.events.ui.main.MainViewModel
import com.example.events.ui.users.UserAdapter

class FragmentParticipants: Fragment() {

    private lateinit var binding: FragmentRvWithSearchToolbarBinding

    private lateinit var viewModel: ViewModelParticipants

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
        viewModel = ViewModelProvider(requireActivity())[ViewModelParticipants::class.java]
        (arguments?.getSerializable(USERS) as? UserList)?.let{
            viewModel.initList(it.users)
        }
        setObservers()
        setUI()
    }

    private fun setObservers() {
        viewModel.users.observe(viewLifecycleOwner){
            binding.tvEmpty.isVisible = it.isEmpty()
            adapterUser.updateItems(it)
        }
    }

    private fun setUI() {
        adapterUser = UserAdapter(requireContext())
        binding.tvEmpty.text = getString(R.string.list_empty)
        binding.rv.adapter = adapterUser
        binding.rv.itemAnimator = null
        binding.toolbarTitle.text = getString(R.string.info_participants)
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
    }

    companion object{
        const val USERS = "users"
    }
}