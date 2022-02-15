package com.echoeyecodes.currencyconverter.db.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.echoeyecodes.currencyconverter.db.models.CurrencyModel

@Dao
abstract class CurrencyDao : BaseDao<CurrencyModel>() {

    @Query("SELECT * FROM currencies")
    abstract fun getCurrencies():LiveData<List<CurrencyModel>>

    @Query("DELETE FROM currencies")
    abstract suspend fun deleteCurrencies()

    @Query("SELECT * FROM currencies WHERE currency=:currency")
    abstract suspend fun getCurrency(currency:String):CurrencyModel?
}