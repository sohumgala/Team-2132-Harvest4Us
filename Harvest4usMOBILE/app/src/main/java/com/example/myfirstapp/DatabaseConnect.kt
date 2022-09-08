package com.example.myfirstapp

import android.text.Editable
import okhttp3.*
import okhttp3.ResponseBody.Companion.toResponseBody


// Generic Interface for handling calls to backend database.
interface DatabaseConnect {
    fun newUser(username: Editable, password: Editable): Response;
    fun resetPassword(email: String, password: String): Response;
    fun getByProducer(producer: String): Response;
    fun filterItem(
        maxPriceValue: Int, minPriceValue: Int,
        searchString: String, username: String?,
    ): Response;
    fun checkout(current: String, total: String): Response;
    fun getProducers(name: String): Response;
    fun getCart(username: String): Response;
    fun getById(producer: String, product_id: String): Response;
}

// Global DatabaseConnect instance
object DB : MockDB() {
}

// Returns mock responses for all database requests for debugging.
open class MockDB: DatabaseConnect {
    val mockUrl = HttpUrl.Builder().scheme("https").host("test.com").build()
    val mockRequest = Request.Builder().url(mockUrl).build()

    // Create a fake response with a 200 code and desired fake message/body
    fun mockResponse(mockMessage: String): Response {
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
        return mockResponse("")
    }

    override fun filterItem(
        maxPriceValue: Int,
        minPriceValue: Int,
        searchString: String,
        username: String?,
    ): Response {
        return mockResponse(

            """
                [ {product_id:0, producer:testProducer, produceType:testProduceType, unit:testUnit, usdaGrade:testusdaGrade, active:0, availableQuantity:0.0, dateEdited:testDate, organic:0, price:0.0, produceCategory:testCategory} ]
            """)

    }

    override fun checkout(current: String, total: String): Response {
        return mockResponse("")
    }

    override fun getProducers(name: String): Response {
        return mockResponse("")
    }

    override fun getCart(username: String): Response {
        return mockResponse("")
    }

    override fun getById(producer: String, product_id: String): Response {
        return mockResponse("")
    }

}