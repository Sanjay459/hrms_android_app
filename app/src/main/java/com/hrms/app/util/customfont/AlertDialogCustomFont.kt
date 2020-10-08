package com.hrms.app.util.customfont

import android.content.Context
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import com.hrms.app.R

object AlertDialogCustomFont {

    class Builder(context: Context) : AlertDialog.Builder(context) {
        var lineSpacingMult: Float = ResourcesCompat.getFloat(context.resources, R.dimen.dialog_msg_line_spacing_multiplier)
        override fun show(): AlertDialog {
            return super.show().apply {
                findViewById<TextView>(android.R.id.message)?.setLineSpacing(0f, lineSpacingMult)
            }
        }
    }
}