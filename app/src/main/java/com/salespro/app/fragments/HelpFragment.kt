package com.salespro.app.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.salespro.app.R
import com.salespro.app.databinding.FragmentHelpBinding


class HelpFragment : Fragment() {

    private lateinit var binding:FragmentHelpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHelpBinding.inflate(inflater)
        activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation)?.visibility = View.GONE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imgEmail.setOnClickListener { openEmailApp() }
        binding.imgPhone.setOnClickListener { openCallApp() }
        binding.imgCloseHelp.setOnClickListener { findNavController().navigateUp() }
    }

    private fun openCallApp() {
        val callIntent: Intent = Uri.parse("tel:123123123123").let { number ->
            Intent(Intent.ACTION_DIAL, number)
        }.also { startActivity(it) }
    }

    private fun openEmailApp() {
        Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_EMAIL, arrayOf("contact@supunsathsara.com")) // recipients
            putExtra(Intent.EXTRA_SUBJECT, "")
            putExtra(Intent.EXTRA_TEXT, "")
            putExtra(Intent.EXTRA_STREAM, Uri.parse("content://path/to/email/attachment"))
        }.also { startActivity(it) }
    }

}