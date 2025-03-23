package com.example.events.ui.authorization.phone

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.events.databinding.FragmentAuthorizationBinding
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


class FragmentPhone: Fragment() {

    private lateinit var binding: FragmentAuthorizationBinding

    private lateinit var formatWatcher: FormatWatcher

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
            clEnterPhoneNumber.setOnClickListener {
                etPhoneNumber.isFocusableInTouchMode = true
                etPhoneNumber.requestFocus()
                val inputMethodManager: InputMethodManager = requireActivity().getSystemService(
                    AppCompatActivity.INPUT_METHOD_SERVICE
                ) as InputMethodManager
                inputMethodManager.showSoftInput(etPhoneNumber, 0)
            }
            val slots = UnderscoreDigitSlotsParser().parseSlots("(___) ___-__-__")
            formatWatcher = MaskFormatWatcher(MaskImpl.createTerminated(slots))
            formatWatcher.installOn(etPhoneNumber)
            etPhoneNumber.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(text: Editable?) {
                    if (formatWatcher.mask.filled()) {
                        btChoose.alpha = 1f
                        btChoose.setOnClickListener {
                            sendVerificationCode(
                                "+7${
                                    binding.etPhoneNumber.text.toString().replace(
                                        Regex("[()\\-+\\s]"),
                                        ""
                                    )
                                }"
                            )
                        }
                    } else {
                        btChoose.alpha = 0.5f
                        btChoose.isClickable = false
                    }
                }
            })
        }
    }

    private fun sendVerificationCode(number: String) {
        // this method is used for getting
        // OTP on user phone number.
        val options: PhoneAuthOptions =
            PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(number) // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(requireActivity()) // Activity (for callback binding)
                .setCallbacks(mCallBack) // OnVerificationStateChangedCallbacks
                .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val mCallBack: OnVerificationStateChangedCallbacks =
        object : OnVerificationStateChangedCallbacks() {
            // below method is used when
            // OTP is sent from Firebase
            override fun onCodeSent(s: String, forceResendingToken: ForceResendingToken) {
                super.onCodeSent(s, forceResendingToken)
                // when we receive the OTP it
                // contains a unique id which
                // we are storing in our string
                // which we have already created.

                val action = FragmentPhoneDirections.actionFragmentAuthToFragmentSmsCode(s)
                findNavController().navigate(action)
            }

            // this method is called when user
            // receive OTP from Firebase.
            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                // below line is used for getting OTP code
                // which is sent in phone auth credentials.
//                val code = phoneAuthCredential.smsCode
//
//
//                // checking if the code
//                // is null or not.
//                if (code != null) {
//                    // if the code is not null then
//                    // we are setting that code to
//                    // our OTP edittext field.
//                    edtOTP.setText(code)
//
//
//                    // after setting this code
//                    // to OTP edittext field we
//                    // are calling our verifycode method.
//                    verifyCode(code)
//                }
            }

            // this method is called when firebase doesn't
            // this method is called when firebase doesn't
            // sends our OTP code due to any error or issue.
            override fun onVerificationFailed(e: FirebaseException) {
                // displaying error message with firebase exception.
                Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
            }
        }

//    private fun verifyCode(code: String) {
//        // below line is used for getting
//        // credentials from our verification id and code.
//        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
//
//
//        // after getting credential we are
//        // calling sign in method.
//        signInWithCredential(credential)
//    }

}