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

enum class MonetizationCustomerAccountStatusType {
    OK, PENDING_ACTION, ERROR, NOT_CONNECTED
}
enum class RequirementStatus {
    PAST_DUE,
    CURRENTLY_DUE,
    DUE_FUTURE,
}
data class IntegrationRequirementActions(
    val requirement: String,
    val status: RequirementStatus
)

data class MonetizationAccountStatus(
    val accountType: String,
    val accountId: String,
    val status: MonetizationCustomerAccountStatusType,
    val livemode: Boolean?,
    val chargesEnabled: Boolean?,
    val payoutsEnabled: Boolean?,
    val actions: List<IntegrationRequirementActions> = emptyList(),
    val disabledReason: String? = null,
)

data class AccountLinkData(val userObjectId: String, val organisationObjectId: String)
