package io.apiable.monetization.adapter

import io.apiable.monetization.adapter.model.*

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

    /* ### ACCOUNT ### */
    /** Used when integration is for a connected account to a vendor. Example: Stripe Connect.
     * Example use case with Stripe: The user clicks on a button to create an account. The user is redirected to Stripe to create a new standard connected account.
     * The account creation is initialized on the Apiable portal--storing an ID reference to the account, and then the requested user is redirected to fill their account details on Stripe.
     *
     * If initial integration is done manually, this function is not necessary, and should return an empty string.
     *
     * @param data Contains the IDs of the organisation and the user that triggered the account creation.
     * @return URL to redirect the user to the monetization provider to create an account.
     */
    fun createAccount(data: AccountLinkData): String

    /** Create API integration.
     * Saves configuration data for the monetization provider using the provided keys.
     * @param data The configuration data for the monetization provider.
     */
    // TODO: fun createIntegration(data: MonetizationIntegrationData)

    /** Returns status of the monetization integration.
     * The status is used to determine if the integration is ready to be used.
     *
     * If values are not applicable to payment provider, they should be set to true, or OK
     *
     * @return The status of the account.
     * @see MonetizationAccountStatus
     */
    fun getAccountStatus(): MonetizationAccountStatus

    /** Returns the URL to the external dashboard login page for the account.
     *
     * If payment provider hosted account dashboard is not applicable in the provider, this function should return an empty string.
     *
     * @return URL to the dashboard login page.
     */
    fun getAccountDashboardLoginLink(): String

    /* ### USER ### */
    /** Get end-customer user by integration ID.
     * End customer user is the user who is paying for the subscription. Each such user can have one or multiple subscriptions.
     *
     * The context on when this function is called is:
     * 1) if the user already exists in the monetization provider, when the user creates/checks out a subscription.
     * 2) When the user updates their subscription on Apiable portal (change of subscription plan, cancel subscription, etc.)
     *
     * @param userIntegrationId The integration ID of the user as it appears in the monetization provider.
     * @return The user object.
     */
    fun getUser(userIntegrationId: String): MonetizationCustomer

    /** Creates a new end-customer user on the monetization provider.
     * @param email The email of the user.
     * @param name The name of the user.
     *
     * The context on when this function is called is:
     * 1) When the user creates a subscription on the Apiable portal, and the user does not exist in the monetization provider.
     * 2) When the user upgrades a free subscription to paid one on Apiable portal
     *
     * @return The user object including the integration ID of the user--the ID of the user in the monetization provider.
     */
    fun createUser(email: String, name: String): MonetizationCustomer

    /** Returns a link to the end user billing portal where the end user can view and manage their subscription.
     * Only applicable if the end-user billing portal is relevant for the monetization provider.
     * @param endCustomerProviderId The ID of the end-customer in the monetization provider.
     * @param subscriptionIntegrationId The ID of the subscription in the monetization provider.
     * @return The URL to the billing portal.
     */
    fun getEndCustomerBillingPortalLink(endCustomerProviderId: String, subscriptionIntegrationId: String): String

    /** Creates configuration for the end-user billing portal.
     * Only applicable if the end-user billing portal is relevant for the monetization provider.
     * @param endCustomerProviderId The ID of the end-customer in the monetization provider.
     * @return The URL to the billing portal.
     */
    fun createBillingPortalConfiguration(privacyPolicyUrl: String, tosUrl: String): String

    /* ### PRODUCT ### */
    /** Check if a product exists in the monetization provider.
     * @param productIntegrationId The integration ID of the product as it appears in the monetization provider.
     * @return True if the product exists, false otherwise.
     */
    fun doesProductExist(productIntegrationId: String): Boolean

    /** Create a product in the monetization provider.
     * @param planId The ID of the Apiable plan for which monetization product will be created.
     * Ideally this should used as ID or a reference to the product in the monetization provider.
     * @param name The name of the product.
     * @param description The description of the product.
     * @param imageUrl The URL to the image of the product.
     * @return The product object, including the integration ID of the product--the ID of the product in the monetization provider.
     * The integration ID will be stored in the Apiable database and used to reference the product in the monetization provider.
     * @see MonetizationProduct
     */
    fun createProduct(
        planId: String,
        name: String,
        description: String,
        imageUrl: String
    ): MonetizationProduct

    /** Update a product in the monetization provider.
     * @param productIntegrationId The integration ID of the product as it appears in the monetization provider.
     * @param name The new name of the product.
     * @param description The new description of the product.
     * @param imageUrl The new URL to the image of the product.
     * @return The updated product object.
     */
    fun updateProduct(
        productIntegrationId: String,
        name: String,
        description: String,
        imageUrl: String
    ): MonetizationProduct
    /** Get a product from the monetization provider.
     * @param productIntegrationId The integration ID of the product as it appears in the monetization provider.
     * @return The product object.
     */
    fun getProduct(productIntegrationId: String): MonetizationProduct

    /* ### PRICE ### */
    /** Create a price in the monetization provider.
     * @param priceData The data for the price.
     * @see BillingPriceCreate
     * @return The price object, including the integration ID of the price--the ID of the price in the monetization provider.
     * The integration ID will be stored in the Apiable database and used to reference the price in the monetization provider.
     * @see BillingPrice
     */
    fun getPriceById(priceIntegrationId: String): BillingPrice

    /** Get a price from the monetization provider.
     * @param lookupKey The lookup key of the price.
     * @return The price object.
     * @see BillingPrice
     */
    fun findPriceByLookupKey(lookupKey: String): BillingPrice?

    /** Check if a lookup key is used.
     * @param lookupKey The lookup key to check.
     * @return True if the lookup key is used, false otherwise.
     */
    fun isLookupKeyUsed(lookupKey: String): Boolean

    /* ### SUBSCRIPTION ### */
    /** Create a subscription checkout link
     * @param returnUrlBase The base URL to which the user will be redirected after the checkout.
     * @param monetizationProductId The ID of the product for which the subscription is created.
     * @param monetizationPriceIds The IDs of the prices for the subscription.
     * @param monetizationCustomerId The ID of the customer for which the subscription is created.
     * @return The checkout session object, including the ID of the checkout session in the monetization provider.
     * The checkout session ID will be stored in the Apiable database and used to reference the checkout session in the monetization provider.
     * @see MonetizationCheckoutSession
     */
    fun subscriptionCheckout(
        returnUrlBase: String,
        monetizationProductId: String,
        monetizationPriceIds: List<String>,
        monetizationCustomerId: String
    ): MonetizationCheckoutSession?

    /** Expires a checkout session link.
     * @param checkoutSessionId The ID of the checkout session to expire.
     */
    fun expireCheckoutSession(checkoutSessionId: String)

    /** Check if the customer can create a subscription with the given currency.
     * Payment providers might not allow one customer to have multiple subscriptions with different currencies.
     * This function is always called before subscription is created.
     * @param customerIntegrationId The ID of the customer.
     * @param currency The currency to check.
     * @return True if the customer can create a subscription with the given currency, false otherwise.
     */
    fun canCreateSubscriptionWithCurrency(customerIntegrationId: String, currency: String): Boolean

    /** Get a subscription from the monetization provider and return the timestamp of the next invoice.
     * This information is used to show the next invoice date to the user on the portal
     * @param subscriptionIntegrationId The ID of the subscription.
     * @return The timestamp of the next invoice date.
     */
    fun findNextInvoiceDateForSubscription(subscriptionIntegrationId: String): Long?

    /** List all invoices that have been generated for a subscription.
     * This information will be shown to the user on the portal.
     * @param subscriptionIntegrationId The ID of the subscription.
     * @return The list of invoices.
     */
    fun findSubscriptionInvoices(subscriptionIntegrationId: String): List<MonetizationInvoice>

    /** Refresh the state of the subscription from the payment provider.
     * @param subscriptionIntegrationId The ID of the subscription.
     * @return The subscription object containing the updated states from the payment provider.
     * The updated data will be processed and reflected on the Apiable subscription object.
     */
    fun refreshSubscriptionById(subscriptionIntegrationId: String): MonetizationSubscription

    /** Refresh the state of the subscription from the payment provider.
     * This function is only used when the state of the subscription has to be validated and the integration ID of the subscription is missing.
     * @param checkoutSessionId ID of checkout session used with the subscription.
     * @return The subscription object containing the updated states from the payment provider.
     * The updated data will be processed and reflected on the Apiable subscription object.
     */
    fun refreshSubscriptionByCheckoutId(checkoutSessionId: String): MonetizationSubscription

    /** Cancel a subscription in the monetization provider.
     * @param subscriptionIntegrationId The ID of the subscription to cancel.
     * @param immediately True if the subscription should be cancelled immediately, false if the subscription should be cancelled at the end of the current period.
     * @return True if the subscription was successfully cancelled, false otherwise.
     */
    fun cancelSubscription(subscriptionIntegrationId: String, immediately: Boolean): Boolean

    /** Update subscription so that it points to a different product
     * @param subscriptionIntegrationId The ID of the subscription in the monetization provider to update.
     * @param productIntegrationId The payment provider ID of the product to which the subscription should belong to.
     * @return True if the subscription was successfully updated, false otherwise.
     */
    fun updateSubscription(subscriptionIntegrationId: String, productIntegrationId: String): Boolean

    /** Report metered usage
     * @param subscriptionId The ID of the subscription to report usage for.
     * @param quantity The quantity of the usage to report
     * @param timestamp The timestamp of the usage in unix seconds.
     * @param setInsteadOfIncrement True if the quantity should be set instead of incremented.
     * @param lookupKey The lookup key of the price to report usage for. If null, the usage will be reported through subscription defaults.
     * @return The usage report object
     */
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


    /** create a price in the billing provider
     * @param priceData The data for the price.
     * @see BillingPriceCreate
     */
    fun createPrice(priceData: BillingPriceCreate):BillingPrice
    /** Update a price in the billing provider. For a lot of providers, prices are immutable for large parts.
     *  Common work-around is to de-activate the old price for new purchases, and create a new price.
     *  @param integrationId The id of the price to update.
     *  @param priceData The new price data.
     *  @return The updated price that will be used for new subscriptions.
     */
    fun updatePrice(integrationId: String, priceData: BillingPriceCreate): BillingPrice
}
