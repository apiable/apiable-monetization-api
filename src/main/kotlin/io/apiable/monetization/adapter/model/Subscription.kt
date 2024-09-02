package io.apiable.monetization.adapter.model
/**
 * Apiable Oy
 * http://www.apiable.io/
 *
 * (c) Copyright Apiable Oy. All rights reserved.
 *
 * This product is the proprietary and sole property of Apiable Oy.
 * Use, duplication or dissemination is subject to prior written consent of
 * Apiable Oy.
 *
 * Created on 26.04.23
 * @author: Apiable Geeks <geeks@apiable.io>
 *
 */

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
