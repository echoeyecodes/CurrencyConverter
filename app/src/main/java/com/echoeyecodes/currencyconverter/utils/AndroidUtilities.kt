package com.echoeyecodes.currencyconverter.utils

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.snackbar.Snackbar

class AndroidUtilities {

    companion object {
        fun showSnackBar(view: View, message: String, action: (view: View) -> Unit = {}) {
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT).setAction("Dismiss") {
                action(view)
            }.show()
        }

        fun showFragment(fragmentManager: FragmentManager, fragment: DialogFragment, tag: String) {
            if (!fragment.isAdded) {
                fragment.show(fragmentManager, tag)
            }
        }

        fun dismissFragment(fragment: DialogFragment) {
            if (fragment.isAdded) {
                fragment.dismiss()
            }
        }

        fun showToastMessage(context: Context, message: String) =
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

        fun log(message: String) = Log.d("CARRR", message)
    }
}