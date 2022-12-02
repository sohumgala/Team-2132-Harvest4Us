package com.amm.harvest4us

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.amm.harvest4us.databinding.ActivityProductBinding
import com.amm.harvest4us.items.ProduceItem
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductBinding
    val backend = FlaskBackend
    private lateinit var produceItem: ProduceItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_product)

        val username = intent.getStringExtra("username")!!

        // creating the bottom navigation functionality
        val myBottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        myBottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home_image -> {
                    val intent = Intent(this, MarketplaceActivity::class.java)
                    intent.putExtra("username", username)
                    startActivity(intent)
                }
                R.id.tractor_image -> {
                    val intent = Intent(this, FarmsActivity::class.java)
                    intent.putExtra("username", username)
                    startActivity(intent)
                }
                R.id.resource_image -> {
                    val intent = Intent(this, ResourceActivity::class.java)
                    intent.putExtra("username", username)
                    startActivity(intent)
                }
                R.id.account_image -> {
                    val intent = Intent(this, SettingsActivity::class.java)
                    intent.putExtra("username", username)
                    startActivity(intent)
                }
            }
            true
        }

        // Retrieve fields for the product
        val name: String = intent.getStringExtra("name")!!
        val category: String = intent.getStringExtra("category")!!
        val productID: Int = intent.getIntExtra("productID", 0)
        val producer: String = intent.getStringExtra("producer")!!
        val unit: String = intent.getStringExtra("unit")!!
        val usdaGrade: String = intent.getStringExtra("usdaGrade")!!
        val active: Int = intent.getIntExtra("active", 0)
        val availableQuantity: Double = intent.getDoubleExtra("availableQuantity", 0.0)
        val dateEdited: String = intent.getStringExtra("dateEdited")!!
        val organic: Int = intent.getIntExtra("organic", 0)
        val price: Double = intent.getDoubleExtra("price", 0.0)
        val image: Int = intent.getIntExtra("image", 0)
        val quantity: Int = intent.getIntExtra("quantity", 0)
        produceItem = ProduceItem(
            productID,
            producer,
            name,
            category,
            unit,
            usdaGrade,
            active,
            availableQuantity,
            dateEdited,
            organic,
            price,
            image,
            quantity,
            username
        )

        findViewById<ImageView>(R.id.image_view).setImageResource(image)
        findViewById<TextView>(R.id.name_text).text = name
        findViewById<TextView>(R.id.category_text).text = category
        findViewById<TextView>(R.id.usda_text).text = "USDA Grade " + usdaGrade
        findViewById<TextView>(R.id.quantity_text).text = "Stock: " + availableQuantity.toString() + " " + unit
        findViewById<TextView>(R.id.date_text).text = "Last Updated " + dateEdited
        if (organic == 1) {
            findViewById<TextView>(R.id.organic_text).text = getString(R.string.organic)
        } else {
            findViewById<TextView>(R.id.organic_text).text = getString(R.string.not_organic)
        }
        val priceString = String.format("%.02f", price)
        findViewById<TextView>(R.id.price_text).text = "$$priceString"

        var editQuantityInteger = 1
        val editQuantity = findViewById<EditText>(R.id.editQuantity)
        editQuantity.setText(editQuantityInteger.toString())

        val plus = findViewById<ImageButton>(R.id.plusButton)
        plus.setOnClickListener {
            if (editQuantityInteger < availableQuantity) {
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
            val newQuantity = editQuantity.text.toString().toIntOrNull()
            val responseHandler = object : Handler(Looper.getMainLooper()) {
                // defined response handler
                override fun handleMessage(msg: Message) {
                    if (msg.what == -1) {
                        return
                    } else if (msg.what == 200) {
                        // Passes the item attributes to the cart display screen
                        val i = Intent(this@ProductActivity, CartActivity::class.java)
                        i.putExtra("username", username)
                        startActivity(i)
                    } else if (msg.what == 403) {
                        return
                    }
                }
            }
            if (newQuantity != null) {
                backend.changeCartQuantity(username, produceItem, newQuantity, responseHandler)
            }
        }

        val producerButton = findViewById<Button>(R.id.producer_button)
        producerButton.text = "Producer Details"

        producerButton.setOnClickListener {
            val i = Intent(this, ProducerActivity::class.java)
            // TODO: Figure out how to get other producer info here - JC
            i.putExtra("name", producer)
            i.putExtra("username", username)
            startActivity(i)
        }
    }

    // handle cart activities
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.getItemId()
        if (id == R.id.nav_cart) {
            val intent = Intent(this, CartActivity::class.java)
            intent.putExtra("username", produceItem.consumerUsername)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
}
