package com.amm.harvest4us.items

import org.json.JSONArray

data class ProducerItem(
    val name: String,
    val description: String,
    val city: String,
    val state: String
)

fun jsonArrToProducerItem(jsonArray: JSONArray): ProducerItem {
    return ProducerItem(
        jsonArray.get(0) as String, // name
        jsonArray.get(1) as String, // description
        jsonArray.get(2) as String, // city
        jsonArray.get(3) as String // state
    )
}