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

enum class BillingCycle {
    MONTH, YEAR, MONTHLY, YEARLY, NONE
}
enum class RevenueModel {
    ONETIME, RECURRING, FLAT_FEE, GRADUATED, VOLUME, FREE, CONTRACT, FLAT_FEE_OVERAGE, PREPAID_BURN_DOWN
}
data class MonetizationVolumeTier(
    val min: Double,
    val max: Double,
    val perCall: Double,
    val flatFee: Double
)

/**
 * Represents a price for a product
 * @param amount The amount of the price in cents or smallest currency unit (e.g. 100 cents for 1 USD)
 * @param amountDouble The amount of the price as a double in the currency's standard unit (e.g. 1.0 for 1 USD)
 */
interface BillingPrice {
    val revenueModel: RevenueModel
    val cycle:BillingCycle
    val intervalCount: Long
    val integrationId: String
    val productIntegrationId: String
    val currency: String
    val recurring: Boolean
    val lookupKey: String?
    val includeTax: Boolean

    val amount: Long?
    val amountDouble: Double?
    val priceTiers: List<MonetizationVolumeTier>?
    val meteringIntegrationId: String?
    val linkedPriceIds: List<String>?
}
data class BillingPriceCreate (
    val revenueModel: RevenueModel,
    val cycle:BillingCycle,
    val intervalCount: Long,
    val productIntegrationId: String,
    val currency: String,
    val recurring: Boolean,
    val includeTax: Boolean = false,
    val lookupKey: String? = null,

    val amount: Long? = null,
    val priceTiers: List<MonetizationVolumeTier>? = emptyList(),
    val meteringIntegrationId: String? = null,
    val linkedPriceIds: List<String>? = null,
)
