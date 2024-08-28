package io.apiable.monetization.adapter

import io.apiable.monetization.adapter.model.*

data class MonetizationCheckoutSession(
    val id: String,
    val url: String,
)
data class MonetizationInvoice(
    val id: String,
    val subscription: String,
    val customer: String,
    val amountDue: Long,
    val amountPaid: Long,
    val amountRemaining: Long,
    val created: Long,
    val currency: String,
    val dueDate: Long? = null,
    val status: String,
    val total: Long,
    val hostedUrl: String
)
data class SubscriptionUsageReport(
    val subscriptionId: String,
    val usageEventId: String,
    val quantity: Long,
    val timestamp: Long,
    val action: String,
    val rawEventJson: Any,
)

interface Monetization {

    // billingSubscriptionId

    /* ### ACCOUNT ### */
    fun createAccount(data: AccountLinkData): String
    fun getAccountStatus(): MonetizationAccountStatus
    fun getAccountDashboardLoginLink(): String

    /* ### USER ### */
    fun getUser(userIntegrationId: String): MonetizationCustomer
    fun createUser(email: String, name: String): MonetizationCustomer
    fun getEndCustomerBillingPortalLink(endCustomerProviderId: String, subscriptionIntegrationId: String): String
    fun createBillingPortalConfiguration(privacyPolicyUrl: String, tosUrl: String): String

    /* ### PRODUCT ### */
    fun doesProductExist(productIntegrationId: String): Boolean
    fun createProduct(
        productIntegrationId: String,
        name: String,
        description: String,
        imageUrl: String
    ): MonetizationProduct

    fun updateProduct(
        productIntegrationId: String,
        name: String,
        description: String,
        imageUrl: String
    ): MonetizationProduct

    fun getProduct(productIntegrationId: String): MonetizationProduct

    fun getPriceById(priceIntegrationId: String): BillingPrice
    fun findPriceByLookupKey(lookupKey: String): BillingPrice?
    fun isLookupKeyUsed(lookupKey: String): Boolean

    /* ### SUBSCRIPTION ### */
    fun subscriptionCheckout(
        returnUrlBase: String,
        monetizationProductId: String,
        monetizationPriceIds: List<String>,
        monetizationCustomerId: String
    ): MonetizationCheckoutSession?
    fun expireCheckoutSession(checkoutSessionId: String)
    fun canCreateSubscriptionWithCurrency(customerIntegrationId: String, currency: String): Boolean
    fun findNextInvoiceDateForSubscription(subscriptionIntegrationId: String): Long?
    fun findSubscriptionInvoices(subscriptionIntegrationId: String): List<MonetizationInvoice>
    fun refreshSubscriptionById(subscriptionIntegrationId: String): MonetizationSubscription
    fun refreshSubscriptionByCheckoutId(checkoutSessionId: String): MonetizationSubscription
    fun cancelSubscription(subscriptionIntegrationId: String, immediately: Boolean): Boolean
    fun updateSubscription(subscriptionIntegrationId: String, productIntegrationId: String): Boolean

    fun reportMeteredUsage(
        subscriptionId: String,
        quantity: Long,
        timestamp: Long,
        setInsteadOfIncrement: Boolean,
        lookupKey: String?
    ): SubscriptionUsageReport?

    // TODO: Implement usage as generic wrapper.
    fun getMeteredUsageSummary(subscriptionId: String): List<StripeSubscriptionUsageItem>?
    fun getMeteredUsageSummaryForSubscriptionItem(subscriptionItemId: String): StripeSubscriptionUsageItem


    fun createPrice(priceData: BillingPriceCreate):BillingPrice
    /** Update a price in the billing provider. For a lot of providers, prices are immutable for large parts.
     *  Common work-around is to de-activate the old price for new purchases, and create a new price.
     *  @param integrationId The id of the price to update.
     *  @param priceData The new price data.
     *  @return The updated price that will be used for new subscriptions.
     */
    fun updatePrice(integrationId: String, priceData: BillingPriceCreate): BillingPrice
}
