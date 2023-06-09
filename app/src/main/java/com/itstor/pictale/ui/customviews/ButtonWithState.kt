package com.itstor.pictale.ui.customviews

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.itstor.pictale.R


class ButtonWithState : AppCompatButton {
    private lateinit var enabledBackground: Drawable
    private lateinit var disabledBackground: Drawable
    private var txtColorEnabled: Int = 0
    private var txtColorDisabled: Int = 0

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()

    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        background = if (isEnabled) enabledBackground else disabledBackground
        setTextColor(if (isEnabled) txtColorEnabled else txtColorDisabled)
        textSize = 14f
        gravity = Gravity.CENTER
    }

    private fun init() {
        txtColorEnabled = ContextCompat.getColor(context, android.R.color.white)
        txtColorDisabled = ContextCompat.getColor(context, R.color.grey)
        enabledBackground =
            ContextCompat.getDrawable(context, R.drawable.bg_rounded_button) as Drawable
        disabledBackground =
            ContextCompat.getDrawable(context, R.drawable.bg_rounded_button_disabled) as Drawable
    }
}