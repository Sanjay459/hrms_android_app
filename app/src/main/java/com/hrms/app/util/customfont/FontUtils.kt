package com.hrms.app.util.customfont

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.widget.TextView
import androidx.annotation.FontRes
import androidx.core.content.res.ResourcesCompat
import com.hrms.app.R

class FontUtils {
    object FontUtils {
        fun updateFont(textView: TextView, context: Context, attrs: AttributeSet) {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                context.obtainStyledAttributes(attrs, R.styleable.CustomFont).apply {
                    var fontWeight = getInteger(R.styleable.CustomFont_textFontWeight, 300)

                    val isItalic = textView.typeface.isItalic

                    /*Update font weight to bold*/
                    if (textView.typeface.isBold && fontWeight < 700) {
                        Log.w("CustomFont", "fontWeight is updated: $fontWeight -> 700")
                        fontWeight = 700
                    }

                    val type = findClosestFontType(fontWeight)


                    textView.typeface = ResourcesCompat.getFont(context, type.getResId(isItalic))
//                textView.setLineSpacing(0f, 1.4f)

                    recycle()
                }
            }
        }


        private fun findClosestFontType(fontWeight: Int):  FontType {
            val type = FontType.values().find { it.weight == fontWeight }

            if (null != type) {
                return type
            } else {
                Log.w("CustomFont", "didn't find appropriate font for weight=$fontWeight")
                var closestType = FontType.values()[0]
                FontType.values().forEach { font ->
                    if (font.weight < fontWeight) {
                        closestType = font
                    } else {
                        return closestType
                    }
                }
                return closestType
            }
        }
    }

    private enum class FontType(
        val weight: Int,
        @FontRes private val italicResId: Int,
        @FontRes private val normalResId: Int
    ) {
        X_LIGHT(100, R.font.gotham_ss_m_x_light_italic, R.font.gotham_ss_m_x_light),
        LIGHT(200, R.font.gotham_ss_m_light_italic, R.font.gotham_ss_m_light),
        BOOK(300, R.font.gotham_ss_m_book_italic, R.font.gotham_ss_m_book),
        MEDIUM(500, R.font.gotham_ss_m_medium_italic, R.font.gotham_ss_m_medium),
        BOLD(700, R.font.gotham_ss_m_bold_italic, R.font.gotham_ss_m_bold),
        BLACK(800, R.font.gotham_ss_m_black_italic, R.font.gotham_ss_m_black);

        fun getResId(isItalic: Boolean): Int {
            return if (isItalic) italicResId else normalResId
        }
    }
}