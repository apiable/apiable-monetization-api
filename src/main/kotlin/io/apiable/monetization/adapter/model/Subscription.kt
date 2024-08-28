package io.apiable.monetization.adapter.model

enum class MonetizationSubscriptionStatus {
    ACTIVE, INACTIVE, CANCELLED, UNKNOWN
}
data class MonetizationSubscription(
    val integrationId: String,
    val currentPeriodEnd: Long,
    val status: MonetizationSubscriptionStatus,
)
data class StripeSubscriptionUsageItem(
    val subscriptionitemId: String,
    val usageRecords: List<Any> // TODO
)
