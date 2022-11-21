package com.amm.harvest4us.items

import android.content.Context
import org.json.JSONArray

data class ProduceItem(
    val product_id: Int,
    val producer: String,
    val produceType: String,
    val produceCategory: String,
    val unit: String,
    val usdaGrade: String,
    val active: Int,
    val quantityInStock: Double,
    val dateEdited: String,
    val organic: Int,
    val price: Double,
    val image: Int,
    val quantityInOrder: Int,
    val consumerUsername: String
)

/** Convert a JSONArray to a ProduceItem. Items in the array must be given in the same order
 * as the ProduceItem fields. */
fun jsonArrToProduceItem(context: Context, jsonArray: JSONArray): ProduceItem {
    val imageString = jsonArray.get(12) as String
    val image = context.resources.getIdentifier(imageString, "drawable", context.packageName)
    return ProduceItem(
        jsonArray.get(0) as Int, // product_id
        jsonArray.get(1) as String, // producer
        jsonArray.get(2) as String, // produceType
        jsonArray.get(3) as String, // produceCategory
        jsonArray.get(4) as String, // unit
        jsonArray.get(5) as String, // usdaGrade
        jsonArray.get(6) as Int, // active
        jsonArray.get(7) as Double, // quantityInStock
        jsonArray.get(8) as String, // dateEdited
        jsonArray.get(9) as Int, // organic
        jsonArray.get(10) as Double, // price
        image, // image
        jsonArray.get(11) as Int, // quantityInOrder
        "" // consumerUsername
    )
}

/**
 * Convert a JSONArray containing multiple JSON-formatted produceItems
 * into a List of ProduceItems.
 */
fun jsonArrToProduceItemList(context: Context, jsonArray: JSONArray): List<ProduceItem> {
    val result = ArrayList<ProduceItem>()
    for (i in 0 until jsonArray.length()) {
        val arr = jsonArray.getJSONArray(i)
        result.add(jsonArrToProduceItem(context, arr))
    }
    return result
}
