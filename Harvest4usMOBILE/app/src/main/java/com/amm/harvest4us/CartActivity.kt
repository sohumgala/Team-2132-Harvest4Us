package com.amm.harvest4us

import android.content.Intent
import android.os.*
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amm.harvest4us.items.CartItem
import com.amm.harvest4us.items.ProduceItem
import com.amm.harvest4us.items.jsonArrToCartItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.json.JSONArray

class CartActivity : AppCompatActivity(), CellClickListener {
    private lateinit var adapter: CustomAdapterCart
    private lateinit var username: String

    private lateinit var cart: CartItem
    private val backend = FlaskBackend
    private var cartList: List<CartItem> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        username = intent.getStringExtra("username")!!

        // creating the bottom navigation functionality
        val myBottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        val recyclerview = findViewById<RecyclerView>(R.id.lv_listView)

        recyclerview.layoutManager = LinearLayoutManager(this)

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

//        val logout = findViewById<FloatingActionButton>(R.id.fab_logout)
//        val settings = findViewById<FloatingActionButton>(R.id.fab_settings)
//        val marketplace = findViewById<FloatingActionButton>(R.id.fab_marketplace)
//        val resources = findViewById<FloatingActionButton>(R.id.fab_resources)
        val saveChangesButton = findViewById<Button>(R.id.button)
        saveChangesButton.setOnClickListener {
            onSaveChangesButtonClicked() }
        refreshCart()
//        logout.setOnClickListener {
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//        }
//
//        settings.setOnClickListener {
//            val intent = Intent(this, SettingsActivity::class.java)
//            intent.putExtra("username", username)
//            startActivity(intent)
//        }
//
//        marketplace.setOnClickListener {
//            val intent = Intent(this, MarketplaceActivity::class.java)
//            intent.putExtra("username", username)
//            startActivity(intent)
//        }
//
//        resources.setOnClickListener {
//            val intent = Intent(this, ResourceActivity::class.java)
//            intent.putExtra("username", username)
//            startActivity(intent)
//        }
    }

    // is called from Cart adapter to do backend call in Cart activity
    fun changeItemQuantity(produceItem: ProduceItem, quantity: Int) {
        // edit quantity to cart
        val responseHandlerQuantity = object : Handler(Looper.getMainLooper()) {
            // defined response handler
            override fun handleMessage(msg: Message) {
                if (msg.what == -1) {
                    return
                } else if (msg.what == 200) {
                    refreshCart()
                } else if (msg.what == 403) {
                    return
                }
            }
        }
        backend.changeCartQuantity(username, produceItem, quantity, responseHandlerQuantity)
    }

    fun refreshCart() {
        val responseHandler = object : Handler(Looper.getMainLooper()) { // defined response handler
            override fun handleMessage(msg: Message) {
                if (msg.what != 200) return // http response code 200 worked 403 didnt't, -1 couldn't connect
                val cartData = JSONArray(msg.obj as String)
                cart = jsonArrToCartItem(this@CartActivity, cartData)
                updateCartView()
            }
        }
        backend.getCart(username, responseHandler)
    }

    override fun onCellClickListener(data: ProduceItem) {
        Toast.makeText(this, data.produceType, Toast.LENGTH_SHORT).show()
    }

    private fun updateCartView() {
        adapter = CustomAdapterCart(cart, this, this)

        // Setting the Adapter with the recyclerview
        val recyclerview = findViewById<RecyclerView>(R.id.lv_listView)
        recyclerview.adapter = adapter

        val totalText = findViewById<TextView>(R.id.totalText)
        val priceString = String.format("%.02f", cart.totalPrice)
        totalText.setText("Total: $$priceString")
    }

    private fun onSaveChangesButtonClicked() {
        val recyclerview = findViewById<RecyclerView>(R.id.lv_listView)
        val requests = ArrayList<Pair<ProduceItem, Int>>()
        // Get all changes needed before executing any (to ensure later changes don't get messed up
        // by earlier changes in the list)
        for (i in 0 until recyclerview.childCount) {
            val holder = recyclerview.findViewHolderForAdapterPosition(i) as CustomAdapterCart.CartViewHolder
            val quantity = holder.itemQuantity.text.toString().toIntOrNull()
            if (quantity != null && quantity != (cart.items[i].quantityInOrder)) {
                requests.add(Pair(cart.items[i], quantity))
            }
        }

        // Execute all requests
        for (request in requests) {
            changeItemQuantity(request.first, request.second)
        }
    }
}
