package com.itstor.pictale.ui.customviews

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.itstor.pictale.R

class NameEditText : TextInputEditText {
    private lateinit var personDrawable: Drawable
    private var doValidateName = false

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

    private fun validateName(s: String) {
        error = if (s.isEmpty()) {
            "Name is required"
        } else {
            null
        }
    }

    private fun init() {
        personDrawable = ContextCompat.getDrawable(context, R.drawable.ic_profile) as Drawable
        inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS

        setHint(R.string.name_hint)
        setCompoundDrawablesWithIntrinsicBounds(personDrawable, null, null, null)
        compoundDrawablePadding = 16

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (doValidateName) {
                    validateName(text.toString())
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        if (!focused) {
            doValidateName = true
            validateName(text.toString())
        }
    }
}