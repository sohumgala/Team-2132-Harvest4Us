package com.amm.harvest4us.backend

import android.os.Handler
import com.amm.harvest4us.items.ProduceItem
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

open class FlaskBackendConnect : BackendConnect {
    private val client = OkHttpClient()
    private val mediaType = "application/json".toMediaType()

    // For running in Android Studio emulator.
    // 10.0.2.2 is an alias for 127.0.0.1 in the Android emulator
    private val emulatorUrl = "http://10.0.2.2:5000"

    // For running on a local android device. Requires running
    // "adb reverse tcp:5000 tcp:5000" with the device connected
    private val adbUrl = "http://localhost:5000"

    private val urlPrefix = emulatorUrl
    private fun buildURL(endpoint: String): String {
        val url = StringBuilder(urlPrefix)
        url.append("/").append(endpoint).append("/")
        return url.toString()
    }

    override fun newUser(
        username: String,
        password: String,
        first: String,
        last: String,
        responseHandler: Handler
    ) {
        val url = buildURL("register")
        val data = buildJsonObject {
            put("username", username)
            put("password", password)
            put("first_name", first)
            put("last_name", last)
        }
        val requestBody = data.toString().toRequestBody(mediaType)
        val request = Request.Builder().url(url).post(requestBody).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                val msg = responseHandler.obtainMessage(-1, null)
                responseHandler.sendMessage(msg)
            }

            override fun onResponse(call: Call, response: Response) {
                val msg = responseHandler.obtainMessage(response.code, username)
                responseHandler.sendMessage(msg)
            }
        })
    }

    override fun login(
        username: String,
        password: String,
        responseHandler: Handler
    ) {
        // Construct the HTTP Request
        val url = buildURL("login")
        val credentials = buildJsonObject {
            put("username", username)
            put("password", password)
        }
        val requestBody = credentials.toString().toRequestBody(mediaType)
        val request = Request.Builder().url(url).post(requestBody).build()

        // Send the request and notify the Handler in the callback
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                val msg = responseHandler.obtainMessage(-1, null)
                responseHandler.sendMessage(msg)
            }
            override fun onResponse(call: Call, response: Response) {
                val msg = responseHandler.obtainMessage(response.code, username)
                responseHandler.sendMessage(msg)
            }
        })
    }

    override fun getAllProduce(responseHandler: Handler) {
        val url = buildURL("get_all_produce")
        val request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                val msg = responseHandler.obtainMessage(-1, null)
                responseHandler.sendMessage(msg)
            }

            override fun onResponse(call: Call, response: Response) {
                val msg = responseHandler.obtainMessage(response.code, response.body?.string())
                responseHandler.sendMessage(msg)
            }
        })
    }

    override fun getAllFarms(responseHandler: Handler) {
        val url = buildURL("get_all_farms")
        val request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                val msg = responseHandler.obtainMessage(-1, null)
                responseHandler.sendMessage(msg)
            }

            override fun onResponse(call: Call, response: Response) {
                val msg = responseHandler.obtainMessage(response.code, response.body?.string())
                responseHandler.sendMessage(msg)
            }
        })
    }

    override fun resetPassword(email: String, password: String): Response {
        throw NotImplementedError()
    }

    override fun getProduceByProducer(producer: String, responseHandler: Handler) {
        val url = buildURL("get_produce_by_producer")
        val requestData = buildJsonObject {
            put("business_name", producer)
        }
        val requestBody = requestData.toString().toRequestBody(mediaType)
        val request = Request.Builder().url(url).post(requestBody).build()
        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                val msg = responseHandler.obtainMessage(-1, null)
                responseHandler.sendMessage(msg)
            }

            override fun onResponse(call: Call, response: Response) {
                val msg = responseHandler.obtainMessage(response.code, response.body?.string())
                responseHandler.sendMessage(msg)
            }
        })
    }

    override fun filterItem(
        maxPriceValue: Int,
        minPriceValue: Int,
        searchString: String,
        username: String?
    ): Response {
        throw NotImplementedError()
    }

    override fun checkout(current: String, total: String): Response {
        throw NotImplementedError()
    }

    override fun getProducers(name: String): Response {
        throw NotImplementedError()
    }

    override fun getCart(username: String, responseHandler: Handler) {
        val url = buildURL("get_cart")
        val requestData = buildJsonObject {
            put("username", username)
        }
        val requestBody = requestData.toString().toRequestBody(mediaType)
        val request = Request.Builder().url(url).post(requestBody).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                val msg = responseHandler.obtainMessage(-1, null)
                responseHandler.sendMessage(msg)
            }

            override fun onResponse(call: Call, response: Response) {
                val msg = responseHandler.obtainMessage(response.code, response.body?.string())
                responseHandler.sendMessage(msg)
            }
        })
    }

    override fun changeCartQuantity(username: String, item: ProduceItem, newQuantity: Int, responseHandler: Handler) {
        val requestBody = buildJsonObject {
            put("username", username)
            put("business_name", item.producer)
            put("product_id", item.product_id)
            put("new_quantity", newQuantity)
        }.toString().toRequestBody(mediaType)
        makeRequest("change_quantity", requestBody, responseHandler)
    }

    override fun getById(producer: String, product_id: Int): Response {
        throw NotImplementedError()
    }

    /**
     * Helper to construct and make a HTTP request to the backend.
     * @param endpoint The Flask backend endpoint to post to
     * @param body the body of the HTTP request
     * @param responseHandler the Handler that will receive the response code/message. A code
     *                          of -1 indicates a failure to connect to the backend.
     */
    private fun makeRequest(endpoint: String, body: RequestBody, responseHandler: Handler) {
        val url = buildURL(endpoint)
        val request = Request.Builder().url(url).post(body).build()
        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                val msg = responseHandler.obtainMessage(-1, null)
                responseHandler.sendMessage(msg)
            }

            override fun onResponse(call: Call, response: Response) {
                val msg = responseHandler.obtainMessage(response.code, response.body?.string())
                responseHandler.sendMessage(msg)
            }
        })
    }
}
