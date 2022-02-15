package com.echoeyecodes.currencyconverter.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.echoeyecodes.currencyconverter.R
import com.echoeyecodes.currencyconverter.adapters.callbacks.CurrencyListAdapterCallback
import com.echoeyecodes.currencyconverter.databinding.LayoutCurrencyItemBinding
import com.echoeyecodes.currencyconverter.db.models.CurrencyModel
import com.echoeyecodes.currencyconverter.utils.CurrencyModelItemCallBack

class CurrencyListAdapter(private val callback: CurrencyListAdapterCallback) :
    ListAdapter<CurrencyModel, CurrencyListAdapter.CurrencyListAdapterViewHolder>(
        CurrencyModelItemCallBack()
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CurrencyListAdapterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_currency_item, parent, false)
        return CurrencyListAdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: CurrencyListAdapterViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CurrencyListAdapterViewHolder(private val view: View) :
        RecyclerView.ViewHolder(view) {
        private val binding = LayoutCurrencyItemBinding.bind(view)
        private val textView = binding.root

        fun bind(model: CurrencyModel) {
            textView.text = model.currency
            view.setOnClickListener { callback.onCurrencySelected(model.currency) }
        }
    }
}