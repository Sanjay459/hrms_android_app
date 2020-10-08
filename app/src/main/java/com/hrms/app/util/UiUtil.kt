package com.hrms.app.util

import android.content.Context
import android.content.DialogInterface
import android.text.TextUtils
import com.hrms.app.util.customfont.AlertDialogCustomFont

class UiUtil {
    fun showAlertWithOkButton(
        context: Context,
        msg: String?,
        title: String?,
        listener: DialogInterface.OnClickListener?
    ) {
        val builder = AlertDialogCustomFont.Builder(context)
        builder.setMessage(msg)
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title)
        }
        builder.setPositiveButton(android.R.string.ok, listener)

        val dialog = builder.create()
        dialog.show()
    }

    fun showAlertWithTwoButtons(
        context: Context,
        titleResId: Int?,
        messageResId: Int,
        positiveResId: Int,
        positiveCallback: () -> Unit,
        negativeResId: Int,
        negativeCallback: () -> Unit
    ) {
        AlertDialogCustomFont.Builder(context).apply {
            titleResId?.let { setTitle(it) }
            setMessage(messageResId)
            setCancelable(false)
            setPositiveButton(positiveResId) { _, _ -> positiveCallback() }
            setNegativeButton(negativeResId) { _, _ -> negativeCallback() }
        }
            .show()
    }
}