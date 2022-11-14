package com.amm.harvest4us

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.amm.harvest4us.databinding.ActivityProductBinding
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.IOException

class ProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductBinding
    val JSON: MediaType? = "application/json; charset=utf-8".toMediaTypeOrNull()

    private val client = OkHttpClient()
    var responseString = ""

    private var username: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_product)

        username = intent.getStringExtra("username")

        val name: String? = intent.getStringExtra("name")
        val category: String? = intent.getStringExtra("category")
        val productID: Int? = intent.getIntExtra("productID", 0)
        val producer: String? = intent.getStringExtra("producer")
        val unit: String? = intent.getStringExtra("unit")
        val usdaGrade: String? = intent.getStringExtra("usdaGrade")
        val active: Int? = intent.getIntExtra("active", 0)
        val availableQuantity: Double? = intent.getDoubleExtra("availableQuantity", 0.0)
        val dateEdited: String? = intent.getStringExtra("dateEdited")
        val organic: Int? = intent.getIntExtra("organic", 0)
        val price: Double? = intent.getDoubleExtra("price", 0.0)
        val image: Int? = intent.getIntExtra("image", 0)
        val quantity: Int? = intent.getIntExtra("quantity", 0)
        val username: String? = intent.getStringExtra("username")

        findViewById<TextView>(R.id.name_text).text = name
        findViewById<TextView>(R.id.category_text).text = category
        findViewById<TextView>(R.id.usda_text).text = "USDA Grade " + usdaGrade
        // findViewById<TextView>(R.id.quantity_text).text = availableQuantity.toString() + " " + unit
        findViewById<TextView>(R.id.date_text).text = "Last Updated " + dateEdited
        if (organic == 1) {
            findViewById<TextView>(R.id.organic_text).text = getString(R.string.organic)
        } else {
            findViewById<TextView>(R.id.organic_text).text = getString(R.string.not_organic)
        }
        findViewById<TextView>(R.id.price_text).text = "$" + price.toString()
        findViewById<Button>(R.id.producer_button).text = "Producer Details"

        var editQuantityInteger = 1
        val editQuantity = findViewById<EditText>(R.id.editQuantity)
        editQuantity.setText(editQuantityInteger.toString())

        val plus = findViewById<ImageButton>(R.id.plusButton)
        plus.setOnClickListener {
            if ((availableQuantity == null) || (editQuantityInteger < availableQuantity)) {
                editQuantityInteger++
                editQuantity.setText(editQuantityInteger.toString())
            }
        }
        val minus = findViewById<ImageButton>(R.id.minusButton)
        minus.setOnClickListener {
            if (editQuantityInteger > 1) {
                editQuantityInteger--
                editQuantity.setText(editQuantityInteger.toString())
            }
        }

        val cartButton = findViewById<Button>(R.id.cart_button)
        cartButton.setOnClickListener {
            post(
                "https://crwpdbho85.execute-api.us-east-1.amazonaws.com/dev/add-to-cart",
                "\"consumer\" : \"$username\", \"producer\" : \"$producer\", \"product_id\" : \"$productID\", \"date_added\" : \"$dateEdited\", \"quantity\" : \"$editQuantityInteger\""
            )
            val i = Intent(this, CartActivity::class.java)
            // Passes the item attributes to the cart display screen
            i.putExtra("username", username)
            startActivity(i)
        }

        val producerButton = findViewById<Button>(R.id.producer_button)

        producerButton.text = "Go to producer profile"
        producerButton.setOnClickListener {
            val i = Intent(this, ProducerActivity::class.java)
            // TODO: Figure out how to get other producer info here - JC
            i.putExtra("name", producer)
            i.putExtra("username", username)
            startActivity(i)
        }
    }

    fun post(url: String, json: String) {
        val body = RequestBody.create(JSON, json)
        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) {
                responseString = response.body?.string()!!
                println(responseString)
            }
        })
    }
}
