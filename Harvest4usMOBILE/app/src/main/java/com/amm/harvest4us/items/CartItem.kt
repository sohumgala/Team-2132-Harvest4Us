package com.amm.harvest4us.items

import org.json.JSONArray

data class CartItem(
    val items: List<ProduceItem>,
    val totalPrice: Double
)

fun jsonArrToCartItem(jsonArray: JSONArray): CartItem {
    val items = ArrayList<ProduceItem>()
    var totalPrice = 0.0
    for (i in 0 until jsonArray.length()) {
        val produceItemJSON = jsonArray.getJSONArray(i)
        items.add(jsonArrToProduceItem(produceItemJSON))
        totalPrice += produceItemJSON.getDouble(produceItemJSON.length() - 1)
    }
    return CartItem(items, totalPrice)
}
