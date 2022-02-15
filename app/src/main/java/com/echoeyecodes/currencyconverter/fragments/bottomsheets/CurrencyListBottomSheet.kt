package com.echoeyecodes.currencyconverter.fragments.bottomsheets

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.echoeyecodes.currencyconverter.R
import com.echoeyecodes.currencyconverter.adapters.CurrencyListAdapter
import com.echoeyecodes.currencyconverter.adapters.callbacks.CurrencyListAdapterCallback
import com.echoeyecodes.currencyconverter.utils.CurrencyConfig
import com.echoeyecodes.currencyconverter.viewmodel.MainActivityViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CurrencyListBottomSheet : BottomSheetDialogFragment(), CurrencyListAdapterCallback {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: MainActivityViewModel

    companion object {
        const val TAG = "CURRENCY_LIST_BOTTOM_SHEET"
        fun getInstance(type: CurrencyConfig) = CurrencyListBottomSheet().apply {
            val bundle = Bundle().apply {
                putSerializable("type", type)
            }
            arguments = bundle
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        dialog.setOnShowListener { dialog ->
            val bottom_dialog = dialog as BottomSheetDialog
            val bottomSheet =
                (bottom_dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?)!!

            val layoutParams = bottomSheet.layoutParams as ViewGroup.LayoutParams

            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            bottomSheet.layoutParams = layoutParams

            BottomSheetBehavior.from(bottomSheet).skipCollapsed = true
            BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
        }
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = LayoutInflater.from(requireContext()).inflate(
            R.layout.fragment_currency_bottomsheet,
            container,
            false
        )
        viewModel = ViewModelProvider(requireActivity())[MainActivityViewModel::class.java]
        recyclerView = view.findViewById(R.id.recycler_view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireContext())
        val adapter = CurrencyListAdapter(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        viewModel.getCurrenciesLiveData().observe(viewLifecycleOwner) {
            adapter.submitList(it.sortedBy { it.currency })
        }
    }

    override fun onCurrencySelected(currency: String) {
        val type = arguments?.getSerializable("type") as CurrencyConfig?
        if (type != null) {
            if (type == CurrencyConfig.BASE) {
                viewModel.setBaseCurrency(currency)
            } else {
                viewModel.setToCurrency(currency)
            }
        }
        dismiss()
    }
}