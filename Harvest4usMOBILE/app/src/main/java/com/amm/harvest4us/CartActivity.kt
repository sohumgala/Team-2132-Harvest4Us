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
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONArray

class CartActivity : AppCompatActivity(), CellClickListener {
    private lateinit var adapter: CustomAdapterCart
    private lateinit var username: String


    private lateinit var cart: CartItem
    private val backend = FlaskBackend
    private var cartList : List<CartItem> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        username = intent.getStringExtra("username")!!




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

//        val logout = findViewById<FloatingActionButton>(R.id.fab_logout)
//        val settings = findViewById<FloatingActionButton>(R.id.fab_settings)
//        val marketplace = findViewById<FloatingActionButton>(R.id.fab_marketplace)
//        val resources = findViewById<FloatingActionButton>(R.id.fab_resources)

        val responseHandler = object : Handler(Looper.getMainLooper()) {// defined response handler
            override fun handleMessage(msg: Message) {
                if (msg.what == -1) return // http response code 200 worked 403 didnt't, -1 couldn't connect
                val cartData = JSONArray(msg.obj as String)
                cart = jsonArrToCartItem(cartData)
                updateCartView()
            }
        }
        backend.getCart(username!!, responseHandler)

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
    //is called from Cart adapter to do backend call in Cart activity
    fun changeItemQuantity(produceItem: ProduceItem, quantity: Int) {
        //backend.changeCartQuanity(username, produce item, new quantity of item, response handler)
        //same^ but for delete from cart set quantity to zero

        //edit quantity to cart
        val responseHandlerQuantity = object : Handler(Looper.getMainLooper()) {
            // defined response handler
            override fun handleMessage(msg: Message) {
                if (msg.what == -1) {
                    return
                } else if (msg.what == 200) {
                    //CALL REFRESHCART IN HERE
                    refreshCart()
                } else if (msg.what == 403) {
                    return
                }
                // http response code 200 worked 403 didnt't, -1 couldn't connect
                //val cartData = JSONArray(msg.obj as String)// not needed
                updateCartView()
            }
        }
        backend.changeCartQuantity(username,produceItem, quantity ,responseHandlerQuantity)
    }


    fun refreshCart() {
        val responseHandler = object : Handler(Looper.getMainLooper()) {// defined response handler
        override fun handleMessage(msg: Message) {
            if (msg.what == -1) return // http response code 200 worked 403 didnt't, -1 couldn't connect
            val cartData = JSONArray(msg.obj as String)
            cart = jsonArrToCartItem(cartData)
            updateCartView()
        }
        }
        backend.getCart(username!!, responseHandler)
    }

    override fun onCellClickListener(data: ProduceItem) {
        Toast.makeText(this, data.produceType, Toast.LENGTH_SHORT).show()
    }

    private fun updateCartView() {
        adapter = CustomAdapterCart(cart, this, this)

        // Setting the Adapter with the recyclerview
        val recyclerview = findViewById<RecyclerView>(R.id.lv_listView)
        recyclerview.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(this)

        val totalText = findViewById<TextView>(R.id.totalText)
        totalText.setText("Total: $" + cart.totalPrice.toString())
    }
}
