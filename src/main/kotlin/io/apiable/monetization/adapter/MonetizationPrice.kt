package io.apiable.monetization.adapter

import io.apiable.monetization.adapter.model.BillingPrice
import io.apiable.monetization.adapter.model.BillingPriceCreate
import io.apiable.monetization.adapter.model.RevenueModel

interface PriceWrapper {
    fun create(monetizationData: BillingPriceCreate): BillingPrice
    fun retrieve(integrationId: String, revenueModel: RevenueModel): BillingPrice
    fun update(integrationId: String, monetizationData: BillingPriceCreate): BillingPrice
    fun retrieveByLookupKey(lookupKey: String): BillingPrice
}
