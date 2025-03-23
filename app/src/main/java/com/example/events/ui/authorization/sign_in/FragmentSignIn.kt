package com.example.events.ui.authorization.sign_in

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.events.databinding.FragmentAuthorizationBinding
import com.example.events.ui.MainActivity
import com.example.events.ui.authorization.AuthorizationActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import ru.tinkoff.decoro.MaskImpl
import ru.tinkoff.decoro.parser.UnderscoreDigitSlotsParser
import ru.tinkoff.decoro.watchers.FormatWatcher
import ru.tinkoff.decoro.watchers.MaskFormatWatcher
import java.util.concurrent.TimeUnit


class FragmentSignIn: Fragment() {

    private lateinit var binding: FragmentAuthorizationBinding

    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAuthorizationBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        setUI()
    }

    private fun setUI() {
        binding.apply {
            (activity as? AuthorizationActivity)?.binding?.ivBack?.isVisible = false
            etEmail.addTextChangedListener {
                it?.let {text->
                    isAuthorizeEnabled(text.toString(), etPassword.text.toString())
                }
            }
            etPassword.addTextChangedListener {
                it?.let {text->
                    isAuthorizeEnabled(etEmail.text.toString(), text.toString())
                }
            }
            tvRegister.setOnClickListener{
                val action = FragmentSignInDirections.actionFragmentAuthToFragmentRegistration()
                findNavController().navigate(action)
            }
        }
    }

    private fun isAuthorizeEnabled(emailText: String, passwordText: String){
        if(android.util.Patterns.EMAIL_ADDRESS.matcher(emailText).matches() && passwordText.length>=8) {
            binding.btAuthorize.isClickable = true
            binding.btAuthorize.alpha = 1f
            binding.btAuthorize.setOnClickListener {
                mAuth.signInWithEmailAndPassword(emailText, passwordText).addOnSuccessListener {
                    startActivity(Intent(requireActivity(), MainActivity::class.java))
                    requireActivity().finish()
                }.addOnFailureListener {e->
                    MaterialAlertDialogBuilder(requireContext())
                        .setMessage("Ошибка при попытке авторизации")
                        .show()
                }
            }
        }else{
            binding.btAuthorize.isClickable = false
            binding.btAuthorize.alpha = 0.4f
        }
    }

}