package com.echoeyecodes.currencyconverter.api.dao

import com.echoeyecodes.currencyconverter.api.model.CurrencyResponseModel
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyDao {
    @GET("latest")
    suspend fun getCurrencies(@Query("apikey") apiKey:String, @Query("base_currency") baseCurrency:String):CurrencyResponseModel
}