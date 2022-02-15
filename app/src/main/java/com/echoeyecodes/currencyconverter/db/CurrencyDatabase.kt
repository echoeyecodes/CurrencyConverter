package com.echoeyecodes.currencyconverter.db

import android.content.Context
import androidx.room.*
import com.echoeyecodes.currencyconverter.db.daos.CurrencyDao
import com.echoeyecodes.currencyconverter.db.models.CurrencyModel

@Database(entities = [CurrencyModel::class], version = 1)
abstract class CurrencyDatabase : RoomDatabase() {
    abstract fun currencyDao():CurrencyDao

    companion object {
        @Volatile
        private var INSTANCE: CurrencyDatabase? = null
        fun getInstance(context: Context) = INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(context.applicationContext, CurrencyDatabase::class.java, "currency_db")
                .fallbackToDestructiveMigration().build()
            INSTANCE = instance
            return instance
        }
    }
}