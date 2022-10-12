package com.amm.harvest4us.backend

import android.os.Handler
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

    /**
     * Make a request to the backend for a list of all produce on the marketplace.
     * @param responseHandler a Handler that processes the response message. The message's
     *                        'what' field will contain the HTTP response code and the
     *                        message's 'obj' field will contain the produce data, encoded
     *                        as a JSON array.
     */
    fun getAllProduce(responseHandler: Handler)

    /**
     * Make a request to the backend for a list of all farms on the marketplace.
     * @param responseHandler a Handler that processes the response message. The message's
     *                        'what' field will contain the HTTP response code and the
     *                        message's 'obj' field will contain the produce data, encoded
     *                        as a JSON array.
     */
    fun getAllFarms(responseHandler: Handler)

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

