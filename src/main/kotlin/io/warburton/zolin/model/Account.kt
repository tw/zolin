package io.warburton.zolin.model

import org.joda.time.DateTime

/**
 * @author tw
 */
data class Account(val id: String, val type: String, val description: String, val created: DateTime)

data class AccountResponse(val accounts: List<Account>) {
    operator fun iterator(): Iterator<Account> {
        return accounts.iterator()
    }
}