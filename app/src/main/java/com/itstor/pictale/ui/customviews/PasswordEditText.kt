package com.itstor.pictale.ui.customviews

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.itstor.pictale.R

class PasswordEditText : TextInputEditText, View.OnTouchListener {
    private lateinit var lockDrawable: Drawable
    private lateinit var eyeOpenDrawable: Drawable
    private lateinit var eyeClosedDrawable: Drawable
    private var isPasswordVisible = false

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
        lockDrawable = ContextCompat.getDrawable(context, R.drawable.ic_lock) as Drawable
        eyeOpenDrawable = ContextCompat.getDrawable(context, R.drawable.ic_eye_open) as Drawable
        eyeClosedDrawable = ContextCompat.getDrawable(context, R.drawable.ic_eye_closed) as Drawable

        inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD

        setHint(R.string.password_hint)
        setCompoundDrawablesWithIntrinsicBounds(lockDrawable, null, eyeClosedDrawable, null)
        compoundDrawablePadding = 16

        transformationMethod = PasswordTransformationMethod.getInstance()

        setOnTouchListener(this)
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty() && s.length < 8)
                    error = context.getString(R.string.password_error_message)
            }
        })
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        if (compoundDrawables[2] != null) {
            val toggleButtonStart: Float
            val toggleButtonEnd: Float
            var isToggleClicked = false
            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                toggleButtonEnd = (eyeClosedDrawable.intrinsicWidth + paddingStart).toFloat()
                when {
                    event.x < toggleButtonEnd -> isToggleClicked = true
                }
            } else {
                toggleButtonStart =
                    (width - paddingEnd - eyeClosedDrawable.intrinsicWidth).toFloat()
                when {
                    event.x > toggleButtonStart -> isToggleClicked = true
                }
            }
            if (isToggleClicked) {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        return true
                    }

                    MotionEvent.ACTION_UP -> {
                        // I don't know why the eye drawable is not changing after error appears.

                        if (isPasswordVisible) {
                            setCompoundDrawablesWithIntrinsicBounds(
                                lockDrawable,
                                null,
                                eyeClosedDrawable,
                                null
                            )
                            transformationMethod = PasswordTransformationMethod.getInstance()
                            isPasswordVisible = false
                            setSelection(TextUtils.getTrimmedLength(text))
                        } else {
                            setCompoundDrawablesWithIntrinsicBounds(
                                lockDrawable,
                                null,
                                eyeOpenDrawable,
                                null
                            )
                            isPasswordVisible = true
                            transformationMethod = null
                            setSelection(TextUtils.getTrimmedLength(text))
                        }
                        return true
                    }

                    else -> return false
                }
            } else return false
        }
        return false
    }
}