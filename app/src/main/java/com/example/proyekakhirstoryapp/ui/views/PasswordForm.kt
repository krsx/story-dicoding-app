package com.example.proyekakhirstoryapp.ui.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.proyekakhirstoryapp.R

class PasswordForm : AppCompatEditText, OnTouchListener {
    private lateinit var errorIcon: Drawable
    private lateinit var showPasswordButton: Drawable
    private var isVisible: Boolean = false

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

        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    private fun init() {

        showPasswordButton =
            ContextCompat.getDrawable(context, R.drawable.ic_visibility_24) as Drawable

        setOnTouchListener(this)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().isNotEmpty()) showShowPasswordButton() else hideDrawables()

                if (p0.toString().length <= 8) {
                    error = "Masukkan minimal 8 karakter"
                } else if (p0.toString().isEmpty()) {
                    error = null
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
    }

    private fun setIconDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }

    private fun showShowPasswordButton() {
        setIconDrawables(endOfTheText = showPasswordButton)
    }

    private fun showErrorIcon() {
        setIconDrawables(endOfTheText = errorIcon)
    }

    private fun hideDrawables() {
        setIconDrawables()
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (compoundDrawables[2] != null) {
            val showPasswordButtonStart: Float
            val showPasswordButtonEnd: Float
            var isShowPasswordButtonClicked = false

            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                showPasswordButtonEnd = (showPasswordButton.intrinsicWidth + paddingStart).toFloat()
                if (event != null) {
                    when {
                        event.x < showPasswordButtonEnd -> isShowPasswordButtonClicked = true
                    }
                }
            } else {
                showPasswordButtonStart =
                    (width - paddingEnd - showPasswordButton.intrinsicWidth).toFloat()
                if (event != null) {
                    when {
                        event.x > showPasswordButtonStart -> isShowPasswordButtonClicked = true
                    }
                }
            }

            if (isShowPasswordButtonClicked) {
                if (event != null) {
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            showPasswordButton =
                                ContextCompat.getDrawable(
                                    context,
                                    R.drawable.ic_remove_red_eye_24
                                ) as Drawable
                            isVisible = !isVisible
                            val inputType = if (isVisible) {
                                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                            } else {
                                InputType.TYPE_CLASS_TEXT
                            }
                            showPasswordButton = if (isVisible) {
                                ContextCompat.getDrawable(
                                    context,
                                    R.drawable.ic_visibility_24
                                ) as Drawable
                            } else {
                                ContextCompat.getDrawable(
                                    context,
                                    R.drawable.ic_visibility_off_24
                                ) as Drawable
                            }
                            setInputType(inputType)
                            return true
                        }
                    }
                }
            }
        }
        return false
    }
}