package com.echoeyecodes.currencyconverter.utils

import androidx.recyclerview.widget.DiffUtil
import com.echoeyecodes.currencyconverter.db.models.CurrencyModel

class DefaultItemCallBack : DiffUtil.ItemCallback<String>() {

    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

}

class CurrencyModelItemCallBack : DiffUtil.ItemCallback<CurrencyModel>() {

    override fun areItemsTheSame(oldItem: CurrencyModel, newItem: CurrencyModel): Boolean {
        return oldItem.currency == newItem.currency
    }

    override fun areContentsTheSame(oldItem: CurrencyModel, newItem: CurrencyModel): Boolean {
        return oldItem == newItem
    }

}