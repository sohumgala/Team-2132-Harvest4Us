package com.amm.harvest4us.items

import android.content.Context
import org.json.JSONArray

data class CartItem(
    val items: List<ProduceItem>,
    val totalPrice: Double
)

/**
 * Convert a JSONArray to a CartItem. This function expects that each entry in the jsonArray contain
 * an extra field for the total price of each item type (should come from getCart backend
 * call).
 */
fun jsonArrToCartItem(context: Context, jsonArray: JSONArray): CartItem {
    val items = jsonArrToProduceItemList(context, jsonArray)
    var totalPrice = 0.0
    for (i in 0 until jsonArray.length()) {
        totalPrice += jsonArray.getJSONArray(i).getDouble(13)
    }
    return CartItem(items, totalPrice)
}