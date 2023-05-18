package com.itstor.pictale.ui.customviews

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.itstor.pictale.R

class EmailEditText : TextInputEditText {
    private lateinit var emailDrawable: Drawable

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

    private fun init() {
        emailDrawable = ContextCompat.getDrawable(context, R.drawable.ic_email) as Drawable
        inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS

        setHint(R.string.email_hint)
        setAutofillHints(AUTOFILL_HINT_EMAIL_ADDRESS)
        setCompoundDrawablesWithIntrinsicBounds(emailDrawable, null, null, null)
        compoundDrawablePadding = 16

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty() && !Patterns.EMAIL_ADDRESS.matcher(s).matches())
                    error = context.getString(R.string.email_error_message)
            }
        })
    }
}