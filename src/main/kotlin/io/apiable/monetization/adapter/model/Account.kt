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
