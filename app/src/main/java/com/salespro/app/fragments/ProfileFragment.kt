package com.salespro.app.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.salespro.app.R
import com.salespro.app.activities.LoginActivity
import com.salespro.app.activities.LoginActivity_GeneratedInjector
import com.salespro.app.databinding.FragmentProfileBinding
import com.salespro.app.viewmodel.ProfileViewModel

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    val viewModel by viewModels<ProfileViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onLogoutClick()

    }

    private fun onLogoutClick() {
        binding.linearOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            requireActivity().finish()
        }
    }

}