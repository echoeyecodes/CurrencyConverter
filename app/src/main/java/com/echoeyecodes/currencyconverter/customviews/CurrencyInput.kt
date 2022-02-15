package com.echoeyecodes.currencyconverter.customviews

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.TextView
import com.echoeyecodes.currencyconverter.R
import com.google.android.material.textfield.TextInputEditText

class CurrencyInput:FrameLayout {
    private lateinit var currencyInput:TextInputEditText
    private lateinit var currencyTextView: TextView

    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        inflate(context, R.layout.layout_currency_input, this)
        val typedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CurrencyInput,
            0,
            0
        )
        initViews();

        try {
            val currencyText = typedArray.getString(R.styleable.CurrencyInput_currencyText) ?: ""
            val currencyValue = typedArray.getString(R.styleable.CurrencyInput_currencyValue) ?: ""
            val enableInput = typedArray.getBoolean(R.styleable.CurrencyInput_enableInput, true)
            setCurrencyText(currencyText)
            setCurrencyValue(currencyValue)
            enableInput(enableInput)
        } finally {
            typedArray.recycle()
        }

    }

    private fun initViews(){
        currencyInput = findViewById(R.id.currency_input)
        currencyTextView = findViewById(R.id.currency)
    }

    fun setCurrencyText(value: String){
        currencyTextView.text = value
        refreshLayout()
    }

    fun setCurrencyValue(value: String){
        currencyInput.setText(value)
        refreshLayout()
    }

    fun enableInput(value: Boolean){
        currencyInput.isEnabled = value
        refreshLayout()
    }

    fun getInputLayout():TextInputEditText{
        return currencyInput
    }

    private fun refreshLayout() {
        invalidate()
        requestLayout()
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )
}