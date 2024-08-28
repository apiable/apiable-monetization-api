package io.apiable.monetization.adapter.model

enum class MonetizationCustomerAccountStatusType {
    OK, PENDING_ACTION, ERROR
}
data class MonetizationAccountStatus(
    val accountType: String,
    val accountId: String,
    val status: MonetizationCustomerAccountStatusType,
    val chargesEnabled: Boolean?,
    val payoutsEnabled: Boolean?,
)
data class AccountLinkData(val userObjectId: String, val organisationObjectId: String)
