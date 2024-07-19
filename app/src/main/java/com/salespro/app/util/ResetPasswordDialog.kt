package com.salespro.app.util

import android.app.Activity
import android.view.View
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.salespro.app.R

fun Activity.setupBottomSheetDialog(
    onsendClick: (String) -> Unit
){
    val dialog = BottomSheetDialog(this,R.style.DialogStyle)
    val view = layoutInflater.inflate(R.layout.reset_password_dialog, null)
    dialog.setContentView(view)
    dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
    dialog.show()

    val edEmail = view.findViewById<EditText>(R.id.edEmail)
    view.findViewById<View>(R.id.btn_send).setOnClickListener {
        val email = edEmail.text.toString()
        if (email.isEmpty()) {
            edEmail.error = "Please enter your email"
            return@setOnClickListener
        }
        onsendClick(email)
        dialog.dismiss()
    }

    view.findViewById<View>(R.id.btn_cancel).setOnClickListener {
        dialog.dismiss()
    }
}