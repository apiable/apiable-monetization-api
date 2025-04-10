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
 * Created on 04.04.25
 * @author: Apiable Geeks <geeks@apiable.io>
 *
 */

data class MonetizationInvoiceResponse(
    val integrationId: String,
    val hostedUrl: String,
    val customerIntegrationId: String,
    val status: String,
    val amount: Long,
    val currency: String,
)

/**
 * Available credit is credit that is currently available for use, while total credit might contain payments
 * that are still in transit or of which payment is still being validated.
 *
 * Balance represents the actual balance on the user's account that is result of balance adjustments from cancelled subscriptions,
 * or changes in final invoice values.
 *
 * @param customerIntegrationId IntegrationID of the customer
 * @param currency Code of the currency
 *
 * @param availableCredit Credit currently available to be used in the currency's lowest denomination.
 * @param totalCredit Total credit currently granted to the user in the currency's lowest denomination.
 * @param balance Actual balance on the user account in the currency's lowest denomination.
 *
 * @param availableCreditDouble availableCredit converted into currency's standard denomination for print.
 * @param totalCreditDouble totalCredit converted into currency's standard denomination for print.
 * @param balanceDouble balance converted into currency's standard denomination for print.
 */
data class Balances(
    val customerIntegrationId: String,
    val currency: String,
    val availableCredit: Long?,
    val totalCredit: Long?,
    val balance: Long?,
    val availableCreditDouble: Double?,
    val totalCreditDouble: Double?,
    val balanceDouble: Double?
)

data class CreditGrant(
    val integrationId: String,
    val amount: Long,
    val amountDouble: Double,
    val currency: String,
    val status: String,
    val prices: List<String>?

)
