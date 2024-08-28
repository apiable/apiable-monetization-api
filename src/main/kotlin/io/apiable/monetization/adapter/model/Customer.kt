package io.apiable.monetization.adapter.model

data class MonetizationCustomer(
    val integrationId: String,
    val name: String,
    val email: String,
    val currency: String = "NONE",
)

data class MonetizationCustomerCreate(
    val name: String,
    val email: String,
)
