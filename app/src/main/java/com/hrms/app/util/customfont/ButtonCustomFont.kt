package com.hrms.app.util.customfont

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import com.hrms.app.util.customfont.FontUtils.FontUtils

class ButtonCustomFont : AppCompatButton {

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        FontUtils.updateFont(this, context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        FontUtils.updateFont(this, context, attrs)
    }
}