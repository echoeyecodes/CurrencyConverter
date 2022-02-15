package com.echoeyecodes.currencyconverter.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currencies")
data class CurrencyModel(@PrimaryKey val currency:String, val rate: Double)