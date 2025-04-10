package io.apiable.monetization.adapter

import io.apiable.monetization.adapter.model.*
import java.time.LocalDateTime

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
    val subscriptionId: String? = null,
    val url: String,
)

/**
 * @param total The total amount of the invoice as a long in it's smallest units (e.g. 1000 cents)
 * @param totalDouble The total amount of the invoice as a double in standard units (e.g. 10.00 €)  (optional, but recommended as it has higher precedence to be shown on the UI.)
 */
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
    val totalDouble: Double? = null,
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

    /** Perform the necessary steps to unlink the account on the payment provider side. Or mark that the account has been unlinked.
     * Context: Called when user clicks "unlink payment provider" on the Apiable Portal management Dashboard.
     */
    fun unlinkAccount()

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
     *
     * Context: End user clicks on a link to view their subscription details within payment provider.
     * End user generally only does this when they want to update their billing information.
     *
     * @param endCustomerProviderId The ID of the end-customer in the monetization provider.
     * @param subscriptionIntegrationId The ID of the subscription in the monetization provider.
     * @return The URL to the billing portal.
     */
    fun getEndCustomerBillingPortalLink(endCustomerProviderId: String, subscriptionIntegrationId: String): String

    /** Creates configuration for the end-user billing portal.
     * Only applicable if the end-user billing portal is relevant for the monetization provider.
     *
     * Context: TODO: Set up during initial integration
     * Set's up the billing portal for the end user by providing the privacy policy and terms of service URLs.
     *
     * @param privacyPolicyUrl The URL to the privacy policy of the service.
     * @param tosUrl The URL to the terms of service of the service.
     *
     * @return The URL to the billing portal.
     * @see getEndCustomerBillingPortalLink
     */
    fun createBillingPortalConfiguration(privacyPolicyUrl: String, tosUrl: String): String

    /* ### PRODUCT ### */
    /** Check if a product exists in the monetization provider.
     *
     * Context: When a plan is given monetization information. This is used to check if the product already exists in the monetization provider.
     * If the product exists, it's prices will be updated, otherwise the product will be created.
     * @see createProduct
     * @see updateProduct
     *
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
     *
     * Context: A plan on the Apiable infrastructure is given monetization information. This is used to create a product in the monetization provider.
     * An Apiable plan object should have one-to-one relationship with a monetization product.
     *
     * A product should contain at least one price.
     *
     * @return The product object, including the integration ID of the product--the ID of the product in the monetization provider.
     * The integration ID will be stored in the Apiable database and used to reference the product in the monetization provider.
     * @see MonetizationProduct
     * @see createPrice
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
     *
     * Context: A plan is edited and it's information is changed in the Apiable infrastructure.
     *
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
     *
     * @return The price object, including the integration ID of the price--the ID of the price in the monetization provider.
     * The integration ID will be stored in the Apiable database and used to reference the price in the monetization provider.
     * @see BillingPriceCreate
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
     *
     * Context: Used when creating/updating Apiable plans.
     * This is used to check if the lookup key is already used in the monetization provider.
     *
     * @return True if the lookup key is used, false otherwise.
     */
    fun isLookupKeyUsed(lookupKey: String): Boolean

    /* ### SUBSCRIPTION ### */
    /** Create a subscription checkout link
     * @param returnUrlBase The base URL to which the user will be redirected after the checkout.
     * @param monetizationProductId The ID of the product for which the subscription is created.
     * @param monetizationPriceIds The IDs of the prices for the subscription.
     * @param monetizationCustomerId The ID of the customer for which the subscription is created.
     *
     * Context: When a user wants to create a subscription on the Apiable portal, a checkout link is created.
     * The user is redirected to the checkout link to complete the subscription securely.
     *
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
     *
     * Context: Triggered when the user returns from checkout session without completing the subscription.
     * In most cases the earlier checkout session can be returned to and completed. But if a new checkout session is created, the old one should be expired to prevent double-checkout.
     *
     * @param checkoutSessionId The ID of the checkout session to expire.
     */
    fun expireCheckoutSession(checkoutSessionId: String)

    /**
     * Retrieves the checkout session associated with the given checkout session ID.
     *
     * @param checkoutSessionId The unique identifier of the checkout session to be retrieved.
     * @return A [MonetizationCheckoutSession] object if the session exists, or null if no session is found.
     */
    fun getCheckoutSession(checkoutSessionId: String): MonetizationCheckoutSession?

    /** Check if the customer can create a subscription with the given currency.
     *
     * Context: Payment providers might not allow one customer to have multiple subscriptions with different currencies.
     * This function is always called before subscription is created.
     * If the customer can't create a subscription with the given currency, the subscription creation is prevented.
     *
     * In case payment provider does not support multiple currencies, this function should always return true.
     *
     * @param customerIntegrationId The ID of the customer.
     * @param currency The currency to check.
     * @return True if the customer can create a subscription with the given currency, false otherwise.
     */
    fun canCreateSubscriptionWithCurrency(customerIntegrationId: String, currency: String): Boolean

    /** Get a subscription from the monetization provider and return the timestamp of the next invoice.
     *
     * Context: This information is used to show the next invoice date to the user on the portal
     *
     * @param subscriptionIntegrationId The ID of the subscription.
     * @return The timestamp of the next invoice date.
     */
    fun findNextInvoiceDateForSubscription(subscriptionIntegrationId: String): Long?

    /** List all invoices that have been generated for a subscription.
     *
     * Context: This information will be shown to the user on the portal.
     *
     * @param subscriptionIntegrationId The ID of the subscription.
     * @return The list of invoices.
     */
    fun findSubscriptionInvoices(subscriptionIntegrationId: String): List<MonetizationInvoice>

    /** Refresh the state of the subscription from the payment provider.
     *
     * Context: Most of the time the state of the subscription is updated in the background by webhooks.
     * However, network errors can happen, and the state of the subscription should be able to be manually refreshed.
     *
     * Alternatively, if webhooks are not set up, or are not supported by the payment provider, the state of the subscription should be manually refreshed.
     *
     * @param subscriptionIntegrationId The ID of the subscription.
     * @return The subscription object containing the updated states from the payment provider.
     * The updated data will be processed and reflected on the Apiable subscription object.
     *
     * @see refreshSubscriptionByCheckoutId in case the integration ID of the subscription is missing.
     */
    fun refreshSubscriptionById(subscriptionIntegrationId: String): MonetizationSubscription

    /** Refresh the state of the subscription from the payment provider.
     * This function is only used when the state of the subscription has to be validated and the integration ID of the subscription is missing.
     * @see refreshSubscriptionById in case the integration ID of the subscription is available.
     *
     * Context: Most of the time the state of the subscription is updated in the background by webhooks.
     * However, network errors can happen, and the state of the subscription should be able to be manually refreshed.
     *
     * Alternatively, if webhooks are not set up, or are not supported by the payment provider, the state of the subscription should be manually refreshed.
     *
     * @param checkoutSessionId ID of checkout session used with the subscription.
     * @return The subscription object containing the updated states from the payment provider.
     * The updated data will be processed and reflected on the Apiable subscription object.
     */
    fun refreshSubscriptionByCheckoutId(checkoutSessionId: String): MonetizationSubscription

    /** Cancel a subscription in the monetization provider.
     *
     * Context: When the user cancels their subscription on the Apiable portal, the subscription is cancelled in the monetization provider.
     * By default, it is assumed that subscriptions are cancelled at the end of their current billing period.
     *
     * @param subscriptionIntegrationId The ID of the subscription to cancel.
     * @param immediately True if the subscription should be cancelled immediately, false if the subscription should be cancelled at the end of the current period. Default is false if no value is provided.
     * @param dateTime Optional parameter to specify the datetime when the subscription should be cancelled. Defaults to end of current billing period if not specified, and 'immediately' is set to false. This should be ignored if immediately is set to true.
     * @return True if the subscription was successfully cancelled, false otherwise.
     */
    fun cancelSubscription(subscriptionIntegrationId: String, immediately: Boolean, dateTime: LocalDateTime? = null): Boolean

    /** Update subscription so that it points to a different product
     *
     * Context: User changes the plan their subscription on the Apiable portal.
     *
     * @param subscriptionIntegrationId The ID of the subscription in the monetization provider to update.
     * @param productIntegrationId The payment provider ID of the product to which the subscription should belong to.
     * @return True if the subscription was successfully updated, false otherwise.
     */
    fun updateSubscription(subscriptionIntegrationId: String, billingPrice: BillingPrice): Boolean

    /** Report metered usage
     *
     * Context: Used to report usage for metered billing.
     *
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

    fun getMeteredUsageTotal(subscriptionId: String): Long

    /** create a price in the billing provider
     *
     * Context: Plan monetization is changed, and no previous price exists.
     *
     * @param priceData The data for the price.
     * @see BillingPriceCreate
     */
    fun createPrice(priceData: BillingPriceCreate):BillingPrice

    /** Update a price in the billing provider. For a lot of providers, prices are immutable for large parts.
     *  Common work-around is to de-activate the old price for new purchases, and create a new price.
     *
     *  Context: Plan monetization is changed, and a previous price exists.
     *
     *  @param integrationId The id of the price to update.
     *  @param priceData The new price data.
     *  @return The updated price that will be used for new subscriptions.
     */
    fun updatePrice(integrationId: String, priceData: BillingPriceCreate): BillingPrice

    /** Retrieve a list of allowed currencies that will be shown on the dashboard.
     * If only one currency is allowed, the currency will be preselected and not shown on the dashboard.
     * The default currency is assumed to be the first one in the list.
     * @return List<String>
     * */
    fun retrieveCurrencyOptions(): List<String>



    // Credit packs
    /**
     * @param customerIntegrationId Integration ID of the customer to create credit invoice for.
     * @param amount Amount of credit to invoice for, marked in the smallest denomination of the given currency.
     * @param currency Currency of the credit to be invoiced.
     */
    fun createCreditPackInvoice(customerIntegrationId: String, amount: Long, currency: String): String
    fun checkoutCreditPack(customerIntegrationId: String, priceIntegrationId: String): String
    fun fulfillCreditPackCheckout(checkoutSessionId: String): String?

    fun retrieveInvoice(invoiceId: String):MonetizationInvoiceResponse?

    fun retrieveCreditBalances(customerIntegrationId: String): List<Balances>?


    // Credit packs
    /** Grants credit to customer. Ensure that you have billed the customer before or during this method.
     * @param customerIntegrationId Integration ID of the customer to create credit invoice for.
     * @param amount Amount of credit to invoice for, marked in the smallest denomination of the given currency.
     * @param currency Currency of the credit to be invoiced.
     * @param checkoutSessionId Optional ID of the checkout session that should be tied to this credit grant.
     */
    fun grantCreditToCustomer(customerIntegrationId: String, amount: Long, currency: String, checkoutSessionId: String?=null): String

    fun listCreditGrants(customerIntegrationId: String, limit: Long, before: String?=null, after: String?=null): List<CreditGrant>
}
