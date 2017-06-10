package io.warburton.zolin.model

import org.joda.time.DateTime

/**
 * @author tw
 */
data class Transaction(val id: String, val currency: String, val amount: Long, val accountBalance: Long,
                       val localCurrency: String, val localAmount: Long, val merchant: Merchant?,
                       val description: String, val notes: String, val metadata: Map<String, Any>,
                       val isLoad: Boolean, val created: DateTime, val settled: DateTime?)

data class TransactionResponse(val transactions: List<Transaction>) {
    operator fun iterator(): Iterator<Transaction> {
        return transactions.iterator()
    }
}