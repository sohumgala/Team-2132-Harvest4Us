package com.amm.harvest4us

import android.content.Intent
import android.os.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amm.harvest4us.items.CartItem
import com.amm.harvest4us.items.ProduceItem
import com.amm.harvest4us.items.jsonArrToCartItem
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONArray

class CartActivity : AppCompatActivity(), CellClickListener {
    private lateinit var adapter: CustomAdapterCart

    private lateinit var cart: CartItem
    private val backend = FlaskBackend

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        val username = intent.getStringExtra("username")

        val logout = findViewById<FloatingActionButton>(R.id.fab_logout)
        val settings = findViewById<FloatingActionButton>(R.id.fab_settings)
        val marketplace = findViewById<FloatingActionButton>(R.id.fab_marketplace)
        val resources = findViewById<FloatingActionButton>(R.id.fab_resources)

        val responseHandler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                if (msg.what == -1) return
                val cartData = JSONArray(msg.obj as String)
                cart = jsonArrToCartItem(cartData)
                updateCartView()
            }
        }
        backend.getCart(username!!, responseHandler)

        logout.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        settings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }

        marketplace.setOnClickListener {
            val intent = Intent(this, MarketplaceActivity::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }

        resources.setOnClickListener {
            val intent = Intent(this, ResourceActivity::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }
    }

    override fun onCellClickListener(data: ProduceItem) {
        Toast.makeText(this, data.produceType, Toast.LENGTH_SHORT).show()
    }

    private fun updateCartView() {
        adapter = CustomAdapterCart(cart, this)

        // Setting the Adapter with the recyclerview
        val recyclerview = findViewById<RecyclerView>(R.id.lv_listView)
        recyclerview.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(this)

        val totalText = findViewById<TextView>(R.id.totalText)
        totalText.setText("Total: " + cart.totalPrice.toString())
    }
}
