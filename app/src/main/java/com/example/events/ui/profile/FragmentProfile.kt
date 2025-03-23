package com.example.events.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.events.R
import com.example.events.databinding.FragmentProfileBinding
import com.example.events.ui.authorization.AuthorizationActivity
import com.example.events.utils.Resource
import com.example.events.utils.showAlert
import com.google.firebase.auth.FirebaseAuth

class FragmentProfile: Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ProfileViewModel::class.java)
        viewModel.getUser()
        setUI()
        setObservers()
    }

    private fun setUI() {
        binding.tvMyEvents.setOnClickListener {

        }
        binding.btSettings.setOnClickListener {
            viewModel.userInfo.value?.data?.let {user->
                val action =
                    FragmentProfileDirections.actionFragmentProfileToFragmentSettings(user)
                findNavController().navigate(action)
            }
        }
        binding.btSignOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(requireContext(), AuthorizationActivity::class.java))
            requireActivity().finish()
        }
    }

    private fun setObservers(){
        viewModel.userInfo.observe(viewLifecycleOwner){
            when(it){
                is Resource.Success->{
                    binding.progress.isVisible = false
                    it.data?.let { user ->
                        binding.llContent.isVisible = true
                        binding.tvName.text = user.name
                        binding.tvEmail.text = user.email
                        binding.tvClass.text = user.clas
                        binding.tvMyEvents.isVisible = user.teacher
                        binding.ivProfile.setImageResource(
                            if (user.teacher) {
                                R.drawable.ic_teacher
                            } else {
                                R.drawable.ic_pupil
                            }
                        )
                    }
                }
                is Resource.Error->{
                    requireContext().showAlert()
                }
            }
        }
    }

}