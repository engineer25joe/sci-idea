package com.example.util

import java.text.NumberFormat
import java.util.Locale

enum class SupportedCurrency(val code: String, val symbol: String, val displayName: String) {
    KES("KES", "KSh", "Kenyan Shilling"),
    USD("USD", "$", "US Dollar"),
    EUR("EUR", "€", "Euro"),
    TZS("TZS", "TSh", "Tanzanian Shilling"),
    UGX("UGX", "USh", "Ugandan Shilling")
}

data class Money(
    val amount: Long,
    val currency: SupportedCurrency = SupportedCurrency.KES
) {
    fun formatted(): String = CurrencyFormatter.format(amount, currency)
}

object CurrencyFormatter {
    private val numberFormat = NumberFormat.getNumberInstance(Locale.US)

    fun format(amount: Number, currency: SupportedCurrency = SupportedCurrency.KES): String {
        val formattedNumber = numberFormat.format(amount.toLong())
        return "${currency.symbol} $formattedNumber"
    }

    fun formatRange(min: Number, max: Number, currency: SupportedCurrency = SupportedCurrency.KES): String {
        val formattedMin = numberFormat.format(min.toLong())
        val formattedMax = numberFormat.format(max.toLong())
        return "${currency.symbol} $formattedMin - $formattedMax"
    }

    fun formatCode(amount: Number, currency: SupportedCurrency = SupportedCurrency.KES): String {
        val formattedNumber = numberFormat.format(amount.toLong())
        return "${currency.code} $formattedNumber"
    }
}
