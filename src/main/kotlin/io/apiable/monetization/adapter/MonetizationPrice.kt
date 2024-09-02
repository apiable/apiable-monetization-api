package io.apiable.monetization.adapter

import io.apiable.monetization.adapter.model.BillingPrice
import io.apiable.monetization.adapter.model.BillingPriceCreate
import io.apiable.monetization.adapter.model.RevenueModel

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


interface PriceWrapper {
    /** Create a new price with the given data
     * @param monetizationData
     * @return BillingPrice
     * */
    fun create(monetizationData: BillingPriceCreate): BillingPrice
    /** Retrieve a price with the given integrationId
     * @param integrationId
     * @return BillingPrice
     * */
    fun retrieve(integrationId: String): BillingPrice
    /** Archive a price with the given integrationId. This will make the price unavailable for new subscriptions.
     * Old subscriptions will still be billed with the archived price.
     * @param integrationId
     * @return BillingPrice
     * */
    fun archive(integrationId: String): BillingPrice
    /** Retrieve price by it's lookup key instead of ID
     * @return List<BillingPrice>
     * */
    fun retrieveByLookupKey(lookupKey: String): BillingPrice
}
