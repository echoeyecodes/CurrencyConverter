package com.echoeyecodes.currencyconverter.utils

sealed class NetworkState<out T:Any>{
    data class Error(val data: String) : NetworkState<Nothing>()
    object Loading : NetworkState<Nothing>()
    data class Complete<T:Any>(val data:T) : NetworkState<T>()
}

enum class CurrencyConfig{
    BASE,
    TO
}