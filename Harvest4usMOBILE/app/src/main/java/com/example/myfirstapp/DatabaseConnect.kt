package com.example.myfirstapp

import android.text.Editable
import okhttp3.*
import okhttp3.ResponseBody.Companion.toResponseBody

// Generic Interface for handling calls to backend database.
interface DatabaseConnect {
    fun newUser(username: Editable, password: Editable): Response
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

// Global DatabaseConnect instance
object MockBackend : MockConnect()

// Returns mock responses for all database requests for debugging.
open class MockConnect : DatabaseConnect {
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

    override fun newUser(username: Editable, password: Editable): Response {
        return mockResponse("New user test")
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
