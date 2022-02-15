package com.echoeyecodes.currencyconverter.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.echoeyecodes.currencyconverter.db.models.CurrencyModel
import com.echoeyecodes.currencyconverter.repository.CurrencyRepository
import com.echoeyecodes.currencyconverter.utils.CurrencyCache
import com.echoeyecodes.currencyconverter.utils.NetworkState
import com.echoeyecodes.currencyconverter.utils.toTimeFormat
import kotlinx.coroutines.launch

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val currencyRepository = CurrencyRepository(application)
    val fromCurrency = MutableLiveData("USD")
    val toCurrency = MutableLiveData("GBP")
    var fromValue = 0.0
    var toValue = MutableLiveData(0.00)

    init {
        getCachedCurrencies()?.let {
            fromCurrency.value = it.first
            toCurrency.value = it.second
        }
        fetchCurrencies()
    }

    private fun getCachedCurrencies(): Pair<String, String>? {
        val currencyCache = CurrencyCache(getApplication())
        return currencyCache.getConversionSelections()
    }

    fun getLastChecked(): String {
        val currencyCache = CurrencyCache(getApplication())
        return currencyCache.getTimestamp().toTimeFormat()
    }

    fun setBaseCurrency(currency: String) {
        this.fromCurrency.value = currency
        fetchCurrencies()
    }

    fun setToCurrency(currency: String) {
        this.toCurrency.value = currency
        convertValue()
    }

    fun fetchCurrencies() {
        val baseCurrency = fromCurrency.value!!
        viewModelScope.launch(currencyRepository.errorHandler) {
            currencyRepository.fetchCurrencies(baseCurrency)
            convertValue()
        }
    }

    fun getCurrenciesLiveData(): LiveData<List<CurrencyModel>> {
        return currencyRepository.getCurrenciesLiveData()
    }

    fun getNetworkState(): LiveData<NetworkState<Any>> {
        return currencyRepository.getNetworkState()
    }

    fun getConvertedValue(): LiveData<Double> {
        return toValue
    }

    fun updateFromValue(value: Double) {
        this.fromValue = value
        convertValue()
    }

    private fun convertValue() {
        val value = this.fromValue
        val _toCurrency = toCurrency.value
        val _fromCurrency = fromCurrency.value

        if (_toCurrency == _fromCurrency) {
            toValue.value = value
        } else {
            if (_toCurrency != null) {
                viewModelScope.launch {
                    val currency2 = currencyRepository.getCurrency(_toCurrency)
                    if (currency2 != null) {
                        toValue.value = value * currency2.rate
                    }
                }
            }
        }
    }

    override fun onCleared() {
        val currencyCache = CurrencyCache(getApplication())
        val fromCurrency = fromCurrency.value
        val toCurrency = toCurrency.value

        if (fromCurrency != null && toCurrency != null) {
            currencyCache.saveConversionSelections(fromCurrency, toCurrency)
        }
        super.onCleared()
    }

    fun toggleCurrencies() {
        val temp = this.toCurrency.value

        setToCurrency(fromCurrency.value!!)
        setBaseCurrency(temp!!)
    }
}