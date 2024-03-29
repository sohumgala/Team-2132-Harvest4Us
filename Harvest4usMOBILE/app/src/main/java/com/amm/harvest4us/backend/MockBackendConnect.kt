package com.amm.harvest4us.backend

import android.os.Handler
import com.amm.harvest4us.items.CartItem
import com.amm.harvest4us.items.ProduceItem
import okhttp3.HttpUrl
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

open class MockBackendConnect : BackendConnect {
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

    override fun getAllProduce(responseHandler: Handler) {
        TODO("Not yet implemented")
    }

    override fun getAllFarms(responseHandler: Handler) {
        TODO("Not yet implemented")
    }

    override fun resetPassword(email: String, password: String): Response {
        return mockResponse("")
    }

    override fun getProduceByProducer(producer: String, responseHandler: Handler) {
        throw NotImplementedError()
    }

    override fun changeCartQuantity(username: String, item: ProduceItem, newQuantity:Int, responseHandler: Handler) {
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

    override fun getCart(username: String, responseHandler: Handler) {
        val items = ArrayList<ProduceItem>()
        items.add(
            ProduceItem(
                0,
                "mock_producer",
                "mock_type",
                "mock_category",
                "mock_unit",
                "mock_grade",
                1,
                0.0,
                "00/00/00",
                0,
                0.0,
                0,
                0,
                "mock_username"
            )
        )
        val msg = responseHandler.obtainMessage(0, CartItem(items, 0.0))
        responseHandler.sendMessage(msg)
    }

    override fun getById(producer: String, product_id: Int): Response {
        return mockResponse(mockProduceObject)
    }
}
