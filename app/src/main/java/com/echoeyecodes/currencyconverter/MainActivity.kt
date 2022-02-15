package com.echoeyecodes.currencyconverter

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.echoeyecodes.currencyconverter.customviews.CurrencyInput
import com.echoeyecodes.currencyconverter.databinding.ActivityMainBinding
import com.echoeyecodes.currencyconverter.fragments.bottomsheets.CurrencyListBottomSheet
import com.echoeyecodes.currencyconverter.utils.AndroidUtilities
import com.echoeyecodes.currencyconverter.utils.CurrencyConfig
import com.echoeyecodes.currencyconverter.utils.NetworkState
import com.echoeyecodes.currencyconverter.viewmodel.MainActivityViewModel
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainActivityViewModel
    private var currencyListBottomSheet: CurrencyListBottomSheet? = null
    private lateinit var fromCurrency: TextView
    private lateinit var toCurrency: TextView
    private lateinit var timestamp: TextView
    private lateinit var retryBtn: MaterialButton
    private lateinit var updateBtn: MaterialButton
    private lateinit var progressBar: ProgressBar
    private lateinit var toggleBtn:ImageButton
    private lateinit var currencyInputEditText: CurrencyInput
    private lateinit var convertedInputEditText: CurrencyInput
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        currencyListBottomSheet =
            supportFragmentManager.findFragmentByTag(CurrencyListBottomSheet.TAG) as CurrencyListBottomSheet?

        updateBtn = binding.updateBtn
        retryBtn = binding.retry

        toggleBtn = binding.currencyConfig.toggleBtn

        currencyInputEditText = binding.currencyValueEditText
        convertedInputEditText = binding.convertedInputEditText

        timestamp = binding.timestamp
        progressBar = binding.progressBar
        fromCurrency = binding.currencyConfig.fromCurrency.root
        toCurrency = binding.currencyConfig.toCurrency.root

        fromCurrency.setOnClickListener { openCurrencyList(CurrencyConfig.BASE) }
        toCurrency.setOnClickListener { openCurrencyList(CurrencyConfig.TO) }
        retryBtn.setOnClickListener { viewModel.fetchCurrencies() }
        updateBtn.setOnClickListener { viewModel.fetchCurrencies() }

        getLastChecked()

        viewModel.getNetworkState().observe(this) {
            updateBtn.isEnabled = it != NetworkState.Loading
            updateBtn.isVisible = it !is NetworkState.Error
            retryBtn.isVisible = it is NetworkState.Error
            progressBar.isVisible = it is NetworkState.Loading

            when (it) {
                is NetworkState.Error -> {
                    AndroidUtilities.showSnackBar(binding.root, it.data)
                }
                is NetworkState.Complete -> {
                    getLastChecked()
                }
            }
        }

        viewModel.getConvertedValue().observe(this) {
            convertedInputEditText.setCurrencyValue(it.toString())
        }

        viewModel.fromCurrency.observe(this){
            fromCurrency.text = it
            currencyInputEditText.setCurrencyText(it)
        }

        viewModel.toCurrency.observe(this){
            toCurrency.text = it
            convertedInputEditText.setCurrencyText(it)
        }

        currencyInputEditText.getInputLayout().doOnTextChanged { text, _, _, _ ->
            val value = if (text.isNullOrEmpty()) {
                0.00
            } else {
                text.toString().toDouble()
            }
            viewModel.updateFromValue(value)
        }

        currencyInputEditText.setCurrencyValue(viewModel.fromValue.toString())
        toggleBtn.setOnClickListener { toggleCurrencies() }
    }

    private fun toggleCurrencies() {
        viewModel.toggleCurrencies()
    }

    private fun openCurrencyList(type:CurrencyConfig) {
        val fragment = currencyListBottomSheet ?: CurrencyListBottomSheet.getInstance(type)
        AndroidUtilities.showFragment(
            supportFragmentManager,
            fragment,
            CurrencyListBottomSheet.TAG
        )
    }

    @SuppressLint("SetTextI18n")
    private fun getLastChecked() {
        timestamp.text = "Last Checked: ${viewModel.getLastChecked()}"
    }
}