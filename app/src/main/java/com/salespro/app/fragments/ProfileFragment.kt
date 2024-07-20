package com.salespro.app.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.salespro.app.R
import com.salespro.app.activities.LoginActivity
import com.salespro.app.activities.LoginActivity_GeneratedInjector
import com.salespro.app.databinding.FragmentProfileBinding
import com.salespro.app.util.Resource
import com.salespro.app.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest


@AndroidEntryPoint
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

        binding.tvVersionCode.text = "Version 1.0"

        binding.linearOrders.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToOrderFragment()
            findNavController().navigate(action)
        }

        binding.linearHelp.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToHelpFragment()
            findNavController().navigate(action)
        }

        lifecycleScope.launchWhenStarted {
           viewModel.user.collectLatest {
                when(it){
                     is Resource.Loading -> {
                          binding.progressbarSettings.visibility = View.VISIBLE
                     }
                     is Resource.Success -> {
                          binding.progressbarSettings.visibility = View.GONE
                          binding.tvUserName.text = it.data?.firstName + " " + it.data?.lastName
                            binding.tvEmail.text = it.data?.email

                     }
                     is Resource.Error -> {
                          binding.progressbarSettings.visibility = View.GONE
                          Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                     }
                    else -> Unit
                }
           }
        }

    }

    override fun onResume() {
        super.onResume()
        val bottomNavigation =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.visibility = View.VISIBLE
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