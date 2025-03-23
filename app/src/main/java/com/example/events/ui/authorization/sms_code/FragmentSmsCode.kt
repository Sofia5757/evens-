package com.example.events.ui.authorization.sms_code

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.events.databinding.FragmentSmsCodeBinding
import com.example.events.ui.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class FragmentSmsCode: Fragment() {

    private lateinit var binding: FragmentSmsCodeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSmsCodeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        measureSmsViewSize()
        binding.smsCodeView.onChangeListener =
            SmsConfirmationView.OnChangeListener { code, isComplete ->
                if(isComplete) {
                    val rightCode = arguments?.getString("code")
                    val credential = PhoneAuthProvider.getCredential(rightCode!!, code)
                    signInWithCredential(credential)
                }
            }
    }

    private fun signInWithCredential(credential: PhoneAuthCredential){
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {task->
            if(task.isSuccessful){
                startActivity(Intent(requireContext(), MainActivity::class.java))
                requireActivity().finish()
            }else{
                if(task.exception is FirebaseAuthInvalidCredentialsException){
                    AlertDialog.Builder(requireContext())
                        .setMessage("Неправильный код")
                        .show()
                }
            }
        }
    }

    private fun measureSmsViewSize() {
        val observer = binding.smsCodeView.viewTreeObserver
        observer.addOnGlobalLayoutListener(object: ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val size = (binding.smsCodeView.measuredWidth - convertDpToPixels(12 * 3)) / 4
                binding.smsCodeView.symbolWidth = size
                binding.smsCodeView.symbolHeight = size - 16
                binding.smsCodeView.requestLayout()
                observer.removeOnGlobalLayoutListener(this)
            }
        })
    }

}