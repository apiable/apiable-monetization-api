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
