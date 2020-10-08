package com.hrms.app.util

import android.app.AlertDialog
import android.content.Context
import com.hrms.app.R

class DialogUtil {
    fun showErrorDialog(
        context: Context?,
        title: String?,
        message: String?
    ) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(R.string.ok) { dialog, which -> dialog.dismiss() }
        builder.show()
    }
}