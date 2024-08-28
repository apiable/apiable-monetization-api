package io.apiable.monetization.adapter.model

data class MonetizationProduct(
    val integrationId: String,
    val name: String,
    val description: String
)
data class MonetizationProductCreate(
    val integrationId: String,
    val name: String,
    val description: String,
    val imageUrl: String?,
)
