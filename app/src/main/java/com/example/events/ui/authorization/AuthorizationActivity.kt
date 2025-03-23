package com.example.events.ui.authorization

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import com.example.events.databinding.ActivityAuthorizationBinding

class AuthorizationActivity: AppCompatActivity() {

    lateinit var binding: ActivityAuthorizationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthorizationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.ivBack.setOnClickListener {
            findNavController(binding.container.id).popBackStack()
        }
    }

}