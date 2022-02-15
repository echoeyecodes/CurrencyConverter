package com.echoeyecodes.currencyconverter.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.withTransaction
import com.echoeyecodes.currencyconverter.BuildConfig
import com.echoeyecodes.currencyconverter.api.ApiClient
import com.echoeyecodes.currencyconverter.api.dao.CurrencyDao
import com.echoeyecodes.currencyconverter.db.CurrencyDatabase
import com.echoeyecodes.currencyconverter.db.models.CurrencyModel
import com.echoeyecodes.currencyconverter.utils.AndroidUtilities
import com.echoeyecodes.currencyconverter.utils.CurrencyCache
import com.echoeyecodes.currencyconverter.utils.NetworkState
import com.echoeyecodes.currencyconverter.utils.toCurrencyModel
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.util.*

class CurrencyRepository(private val context: Context) {
    private val currencyDatabase = CurrencyDatabase.getInstance(context)
    private val currencyDao = currencyDatabase.currencyDao()

    private val remoteCurrencyRequest = ApiClient.getInstance().getClient(CurrencyDao::class.java)
    private val networkState = MutableLiveData<NetworkState<Any>>()

    val errorHandler = CoroutineExceptionHandler { _, exception ->
        AndroidUtilities.log(exception.message.toString())
        // run on main thread
        if (exception is HttpException) {
            when {
                exception.code() == 404 -> {
                    setErrorMessage("Resource not found")
                }
                exception.code() == 429 -> {
                    setErrorMessage("Rate limit reached")
                }
                exception.code() == 500 -> {
                    setErrorMessage("Interval server error")
                }
                else -> {
                    setErrorMessage("An unknown error has occurred")
                }
            }
        } else {
            setErrorMessage("An unknown error has occurred")
        }
    }

    private fun setErrorMessage(message: String) {
        networkState.postValue(NetworkState.Error(message))
    }

    suspend fun fetchCurrencies(baseCurrency: String) {
        networkState.value = NetworkState.Loading
        withContext(Dispatchers.IO) {
            val currencies = remoteCurrencyRequest.getCurrencies(
                BuildConfig.API_KEY,
                baseCurrency
            ).data.toCurrencyModel()
            currencyDatabase.withTransaction {
                currencyDao.deleteCurrencies()
                currencyDao.addItems(currencies)
            }
            val currencyCache = CurrencyCache(context)
            currencyCache.saveTimestamp(Date().time)
            networkState.postValue(NetworkState.Complete(0))
        }
    }

    fun getCurrenciesLiveData(): LiveData<List<CurrencyModel>> {
        return currencyDao.getCurrencies()
    }

    fun getNetworkState(): LiveData<NetworkState<Any>> {
        return networkState
    }

    suspend fun getCurrency(currency: String): CurrencyModel? {
        return withContext(Dispatchers.IO) { currencyDao.getCurrency(currency) }
    }

}