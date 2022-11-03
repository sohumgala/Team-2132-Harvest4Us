package com.amm.harvest4us.backend

import android.os.Handler
import com.amm.harvest4us.items.ProduceItem
import okhttp3.*

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

    /**
     * Make a request to the backend for a list of all produce for a particular producer.
     * @param producer The business_name field of the producer
     * @param responseHandler a Handler that processes the response message. The message's
     *                        'what' field will contain the HTTP response code and the
     *                        message's 'obj' field will contain the produce data, encoded
     *                        as a JSON array.
     */
    fun getProduceByProducer(producer: String, responseHandler: Handler)
    fun filterItem(
        maxPriceValue: Int,
        minPriceValue: Int,
        searchString: String,
        username: String?
    ): Response
    fun checkout(current: String, total: String): Response
    fun getProducers(name: String): Response

    /**
     * Make a request to the backend for the current user's cart.
     * @param username The user's username
     * @param responseHandler Handler to process response from backend. Format is identical
     *                        to getAllProduce.
     */
    fun getCart(username: String, responseHandler: Handler)

    /**
     * Make a request to the backend to change the quantity of an item in the cart.
     * This general operation also handles inserting and deleting items from the cart.
     * A cart insert is performed when the item is not already present in the cart,
     * and a delete is performed when newQuantity is 0.
     * @param username The user's username
     * @param item The ProduceItem to be modified in the cart
     * @param newQuantity The new quantity for the item in the cart (0 for deletions)
     * @param responseHandler Handler to process backend response. This backend call has an empty
     *                          response body.
     */
    fun changeCartQuantity(username: String, item: ProduceItem, newQuantity: Int, responseHandler: Handler)
    fun getById(producer: String, product_id: Int): Response
}
