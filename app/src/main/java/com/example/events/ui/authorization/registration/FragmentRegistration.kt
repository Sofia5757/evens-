package com.example.events.ui.authorization.registration

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
import com.example.events.databinding.FragmentRegistrationBinding
import com.example.events.ui.authorization.AuthorizationActivity
import com.example.events.utils.Resource
import com.example.events.utils.showAlert
import com.google.firebase.auth.FirebaseAuth

class FragmentRegistration : Fragment() {

    private lateinit var binding: FragmentRegistrationBinding

    private lateinit var mAuth: FirebaseAuth

    private lateinit var viewModelRegistration: ViewModelRegistration

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegistrationBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? AuthorizationActivity)?.binding?.ivBack?.isVisible = true
        viewModelRegistration = ViewModelProvider(this)[ViewModelRegistration::class.java]
        mAuth = FirebaseAuth.getInstance()
        setUI()
        setObservers()
    }

    private fun setObservers(){
        viewModelRegistration.registrationResult.observe(viewLifecycleOwner){
            when(it){
                is Resource.Success->{
                    findNavController().popBackStack()
                }
                is Resource.Error->{
                    requireContext().showAlert("Ошибка при попытке регистрации")
                }
            }
        }
    }

    private fun setUI() {
        binding.apply {
            etEmail.addTextChangedListener {
                it?.let { text ->
                    isRegisterEnabled(
                        text.toString(),
                        etPassword.text.toString(),
                        etFIO.text.toString(),
                        etClass.text.toString()
                    )
                }
            }
            etPassword.addTextChangedListener {
                it?.let { text ->
                    isRegisterEnabled(
                        etEmail.text.toString(),
                        text.toString(),
                        etFIO.text.toString(),
                        etClass.text.toString()
                    )
                }
            }
            etClass.addTextChangedListener {
                it?.let { text ->
                    isRegisterEnabled(
                        etEmail.text.toString(),
                        etPassword.text.toString(),
                        etFIO.text.toString(),
                        text.toString()
                    )
                }
            }
            etFIO.addTextChangedListener {
                it?.let { text ->
                    isRegisterEnabled(
                        etEmail.text.toString(),
                        etPassword.text.toString(),
                        text.toString(),
                        etClass.text.toString()
                    )
                }
            }
            tgPupilOrTeacher.addOnButtonCheckedListener { _, _, _ ->
                isRegisterEnabled(
                    etEmail.text.toString(),
                    etPassword.text.toString(),
                    etFIO.text.toString(),
                    etClass.text.toString()
                )
            }
        }
    }

    private fun isRegisterEnabled(emailText: String, passwordText: String, fio: String, clasText: String) {
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(emailText)
                .matches() && passwordText.length >= 8 && fio.isNotEmpty() && (binding.tgPupilOrTeacher.checkedButtonId == binding.btTeacher.id || clasText.length >= 2)
        ) {
            binding.btRegister.isClickable = true
            binding.btRegister.alpha = 1f
            binding.btRegister.setOnClickListener {
                mAuth.createUserWithEmailAndPassword(emailText, passwordText)
                    .addOnSuccessListener {
                        viewModelRegistration.createAccount(
                            User(
                                name = fio,
                                clas = clasText,
                                teacher = binding.tgPupilOrTeacher.checkedButtonId == binding.btTeacher.id,
                                email = emailText
                            )
                        )
                    }.addOnFailureListener {
                        AlertDialog.Builder(requireContext())
                            .setMessage("Ошибка при попытке регистрации")
                            .show()
                    }
            }
        } else {
            binding.btRegister.isClickable = false
            binding.btRegister.alpha = 0.4f
        }
    }
}