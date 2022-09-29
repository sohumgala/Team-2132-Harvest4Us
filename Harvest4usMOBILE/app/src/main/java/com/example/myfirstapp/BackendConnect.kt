package com.example.myfirstapp

import android.os.Handler
import android.text.Editable
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody.Companion.toResponseBody
import java.io.IOException

// Generic Interface for handling calls to backend database.
interface BackendConnect {
    /**
     * Make a request to create a new user to the backend, and send a message to the given
     * responseHandler.
     * @param username the username to create
     * @param password the password associated with the user
     * @param first the user's first name
     * @param last the user's last name
     * @param responseHandler a Handler that processes the response message. The message's
     *                        'what' field will contain the HTTP response code and the message's
     *                        'obj' field will contain the username, if the user is created
     *                        successfully.
     */
    fun newUser(
        username: String,
        password: String,
        first: String,
        last: String,
        responseHandler: Handler
    )
    /**
     * Make a login request to the Flask backend, and send a message to the given
     * responseHandler.
     * @param username the username to query
     * @param password the password to query
     * @param responseHandler a Handler that will receive and process a response message.
     *                        The message's 'what' field will contain the HTTP response code
     *                        and the message's 'obj' field will contain the username, if
     *                        authentication is successful.
     */
    fun login(
        username: String,
        password: String,
        responseHandler: Handler
    )
    fun resetPassword(email: String, password: String): Response
    fun getByProducer(producer: String): Response
    fun filterItem(
        maxPriceValue: Int,
        minPriceValue: Int,
        searchString: String,
        username: String?
    ): Response
    fun checkout(current: String, total: String): Response
    fun getProducers(name: String): Response
    fun getCart(username: String): Response
    fun getById(producer: String, product_id: Int): Response
}

// Global Backend instances
object MockBackend : MockConnect()
object FlaskBackend : FlaskConnect()

// Returns mock responses for all database requests for debugging.
open class MockConnect : BackendConnect {
    private val mockUrl = HttpUrl.Builder().scheme("https").host("test.com").build()
    private val mockRequest = Request.Builder().url(mockUrl).build()
    private val mockProduceObject = "{product_id:0, producer:testProducer, produceType:testProduceType, unit:testUnit, usdaGrade:testusdaGrade, active:0, availableQuantity:0.0, dateEdited:testDate, organic:0, price:0.0, produceCategory:testCategory, quantity:0}"
    private val mockProduceList = "[$mockProduceObject]"
    private val mockProducerObject = "{business_name:testBusinessName, business_street:testStreet, business_state:testState, about:testAbout, business_city:testCity, business_zip:00000}"
    private val mockProducerList = "[$mockProducerObject]"

    // Create a fake response with a 200 code and desired fake message/body
    fun mockResponse(mockMessage: String): Response {
        val body = "{\"message\":$mockMessage}"
        return Response.Builder()
            .request(mockRequest)
            .code(200)
            .protocol(Protocol.HTTP_1_1)
            .message(mockMessage)
            .body(mockMessage.toResponseBody(null))
            .build()
    }

    override fun newUser(
        username: String,
        password: String,
        first: String,
        last: String,
        responseHandler: Handler
    ) {
        val msg = responseHandler.obtainMessage(200, username)
        responseHandler.sendMessage(msg)
    }

    override fun login(
        username: String,
        password: String,
        responseHandler: Handler
    ) {
        val msg = responseHandler.obtainMessage(200, username)
        responseHandler.sendMessage(msg)
    }

    override fun resetPassword(email: String, password: String): Response {
        return mockResponse("")
    }

    override fun getByProducer(producer: String): Response {
        return mockResponse(mockProduceList)
    }

    override fun filterItem(
        maxPriceValue: Int,
        minPriceValue: Int,
        searchString: String,
        username: String?
    ): Response {
        return mockResponse(mockProduceList)
    }

    override fun checkout(current: String, total: String): Response {
        return mockResponse("")
    }

    override fun getProducers(name: String): Response {
        return mockResponse(mockProducerObject)
    }

    override fun getCart(username: String): Response {
        return mockResponse(mockProduceList)
    }

    override fun getById(producer: String, product_id: Int): Response {
        return mockResponse(mockProduceObject)
    }
}

/* Connect to Flask backend running on local dev machine. */
open class FlaskConnect : BackendConnect {
    private val client = OkHttpClient()
    private val mediaType = "application/json".toMediaType()

    // 10.0.2.2 is an alias for 127.0.0.1 in the Android emulator
    private val urlPrefix = "http://10.0.2.2:5000"
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
        client.newCall(request).enqueue(object: Callback {
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

    override fun resetPassword(email: String, password: String): Response {
        throw NotImplementedError()
    }

    override fun getByProducer(producer: String): Response {
        throw NotImplementedError()
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

    override fun getCart(username: String): Response {
        throw NotImplementedError()
    }

    override fun getById(producer: String, product_id: Int): Response {
        throw NotImplementedError()
    }
}
