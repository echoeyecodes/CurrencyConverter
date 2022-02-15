package com.echoeyecodes.currencyconverter.utils

import com.echoeyecodes.currencyconverter.db.models.CurrencyModel
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs

fun Map<String, Double>.toCurrencyModel(): List<CurrencyModel> {
    val data = ArrayList<CurrencyModel>()
    this.forEach { (key, value) ->
        data.add(CurrencyModel(key, value))
    }
    return data
}

private fun delimitTime(value: Int, suffix: String): String {
    return if (value <= 1) {
        "$value $suffix"
    } else {
        "$value ${suffix}s"
    }
}

fun Long.toTimeFormat(): String {
    val from = this

    if (from == 0L) {
        return "N/A"
    }
    val now = Date().time
    val seconds = (abs(now - from)) / 1000L

    return when {
        seconds < 60L -> {
            "few seconds"
        }
        seconds / 60L < 60L -> {
            delimitTime((seconds / 60).toInt(), "minute")
        }
        seconds / 60L / 60L < 24L -> {
            delimitTime((seconds / 60 / 60).toInt(), "hour")
        }
        seconds / 60L / 60L / 24L < 8L -> {
            delimitTime((seconds / 60 / 60 / 24).toInt(), "day")
        }
        seconds / 60L / 60L / 24L / 7L < 5L -> {
            delimitTime((seconds / 60 / 60 / 24 / 7).toInt(), "week")
        }
        seconds / 60L / 60L / 24L / 7L / 4L < 13L -> {
            delimitTime((seconds / 60 / 60 / 24 / 7 / 4).toInt(), "month")
        }
        else -> {
            delimitTime((seconds / 60L / 60L / 24L / 7L / 4L / 12L).toInt(), "year")
        }
    }.plus(" ago")
}