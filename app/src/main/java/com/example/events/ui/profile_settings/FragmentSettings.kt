package com.example.events.ui.profile_settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.events.data.entities.User
import com.example.events.databinding.FragmentSettingsBinding
import com.example.events.ui.authorization.AuthorizationActivity
import com.example.events.utils.Resource
import com.example.events.utils.showAlert

class FragmentSettings : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    private lateinit var viewModelSettings: ViewModelSettings

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? AuthorizationActivity)?.binding?.ivBack?.isVisible = true
        viewModelSettings = ViewModelProvider(this)[ViewModelSettings::class.java]
        (arguments?.getSerializable("user") as? User)?.let{
            viewModelSettings.setUser(it)
        }
        setUI()
        setObservers()
    }

    private fun setObservers() {
        viewModelSettings.editResult.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    findNavController().popBackStack()
                }

                is Resource.Error -> {
                    requireContext().showAlert("Ошибка при попытке регистрации")
                }
            }
        }
        viewModelSettings.user.observe(viewLifecycleOwner){user->
            user.clas?.let{
                binding.etClass.setText(it)
            }
            binding.etFIO.setText(user.name)
            if(user.teacher){
                binding.tgPupilOrTeacher
            }
        }
    }

    private fun setUI() {
        binding.apply {
            etClass.addTextChangedListener {
                it?.let { text ->
                    isEditEnabled(
                        etFIO.text.toString(),
                        text.toString()
                    )
                }
            }
            etFIO.addTextChangedListener {
                it?.let { text ->
                    isEditEnabled(
                        text.toString(),
                        etClass.text.toString()
                    )
                }
            }
            tgPupilOrTeacher.addOnButtonCheckedListener { _, _, _ ->
                isEditEnabled(
                    etFIO.text.toString(),
                    etClass.text.toString()
                )
            }
        }
    }

    private fun isEditEnabled(fio: String, clasText: String) {
        if (fio.isNotEmpty() && (binding.tgPupilOrTeacher.checkedButtonId == binding.btTeacher.id || clasText.length >= 2)) {
            binding.btSave.isClickable = true
            binding.btSave.alpha = 1f
            binding.btSave.setOnClickListener {
                viewModelSettings.editAccount(
                    User(
                        name = fio,
                        clas = clasText,
                        teacher = binding.tgPupilOrTeacher.checkedButtonId == binding.btTeacher.id,
                        email = viewModelSettings.user.value!!.email
                    )
                )
            }
        } else {
            binding.btSave.isClickable = false
            binding.btSave.alpha = 0.4f
        }
    }
}