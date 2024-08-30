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
    fun create(monetizationData: BillingPriceCreate): BillingPrice
    fun retrieve(integrationId: String, revenueModel: RevenueModel): BillingPrice
    fun update(integrationId: String, monetizationData: BillingPriceCreate): BillingPrice
    fun retrieveByLookupKey(lookupKey: String): BillingPrice
}
