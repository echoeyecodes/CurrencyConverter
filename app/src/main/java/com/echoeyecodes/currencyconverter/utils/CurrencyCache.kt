package com.echoeyecodes.currencyconverter.utils

import android.content.Context

class CurrencyCache(private val context: Context) {

    companion object {
        const val CURRENCY_CONVERSION = "CURRENCY_CONVERSION"
    }

    fun saveTimestamp(timestamp: Long) {
        context.getSharedPreferences(CURRENCY_CONVERSION, Context.MODE_PRIVATE).run {
            edit().apply {
                putLong("timestamp", timestamp)
                apply()
            }
        }
    }

    fun saveConversionSelections(conversion1: String, conversion2: String) {
        context.getSharedPreferences(CURRENCY_CONVERSION, Context.MODE_PRIVATE).run {
            edit().apply {
                putString("conversion1", conversion1)
                putString("conversion2", conversion2)
                apply()
            }
        }
    }

    fun getTimestamp(): Long {
        return context.getSharedPreferences(CURRENCY_CONVERSION, Context.MODE_PRIVATE).run {
            getLong("timestamp", 0)
        }
    }

    fun getConversionSelections(): Pair<String, String>? {
        return context.getSharedPreferences(CURRENCY_CONVERSION, Context.MODE_PRIVATE).run {
            val conversion1 = getString("conversion1", null)
            val conversion2 = getString("conversion2", null)

            if (conversion1 == null || conversion2 == null) {
                null
            } else {
                Pair(conversion1, conversion2)
            }
        }
    }
}