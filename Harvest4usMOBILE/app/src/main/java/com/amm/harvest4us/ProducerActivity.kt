package com.amm.harvest4us

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amm.harvest4us.items.ProduceItem
import com.amm.harvest4us.items.jsonArrToProduceItemList
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.json.JSONArray

class ProducerActivity : AppCompatActivity(), CellClickListener {
    private lateinit var adapter: CustomAdapter
    private lateinit var name: String

    var inventory: List<ProduceItem> = ArrayList(0)
    private var username: String? = null
    private val backend = FlaskBackend

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_producer)

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

        username = intent.getStringExtra("username")
        val recyclerview = findViewById<RecyclerView>(R.id.farmerProduceView)
        recyclerview.layoutManager = LinearLayoutManager(this)

        name = intent.getStringExtra("name")!!
        val address = intent.getStringExtra("city") + ", " + intent.getStringExtra("state")
        val description = intent.getStringExtra("description")
        val nameText = findViewById<TextView>(R.id.name_text)
        val addressText = findViewById<TextView>(R.id.address_text)
        val descriptionText = findViewById<TextView>(R.id.description_text)

        nameText.text = name
        addressText.text = address
        descriptionText.text = description

        updateProduceInventory("")
    }

    override fun onCellClickListener(data: ProduceItem) {
        // Passes the item attributes to the product display screen
        val i = Intent(this, ProductActivity::class.java)
        i.putExtra("name", data.produceType)
        i.putExtra("category", data.produceCategory)
        i.putExtra("productID", data.product_id)
        i.putExtra("producer", data.producer)
        i.putExtra("unit", data.unit)
        i.putExtra("usdaGrade", data.usdaGrade)
        i.putExtra("active", data.active)
        i.putExtra("availableQuantity", data.quantityInStock)
        i.putExtra("dateEdited", data.dateEdited)
        i.putExtra("organic", data.organic)
        i.putExtra("price", data.price)
        i.putExtra("image", data.image)
        i.putExtra("quantity", data.quantityInOrder)
        i.putExtra("username", username)
        startActivity(i)
    }

    // Initialize the inventory list
    fun updateProduceInventory(searchString: String) {
        val responseHandler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                if (msg.what == -1) return
                inventory = jsonArrToProduceItemList(this@ProducerActivity, JSONArray(msg.obj as String))
                // frontend code needs to update whatever view is displaying the items here - JC
                updateProduceInventoryListView()
            }
        }
        backend.getProduceByProducer(name, responseHandler)
    }

    fun updateProduceInventoryListView() {
        adapter = CustomAdapter(inventory, this@ProducerActivity)
        val recyclerview = findViewById<RecyclerView>(R.id.farmerProduceView)
        recyclerview.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)

//        // Initializes the search bar
//        val search = menu?.findItem(R.id.nav_search)
//        val searchView = search?.actionView as SearchView
//        searchView.queryHint = "Search something!"
//
//        val cartB = menu?.findItem(R.id.nav_cart)
//
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                // if there is something in the search bar,
//                if (query != null) {
//                    setContentView(R.layout.activity_marketplace)
//                    val recyclerview = findViewById<RecyclerView>(R.id.lv_listView)
//                    // this creates a vertical layout Manager
//                    recyclerview.layoutManager = LinearLayoutManager(applicationContext)
//                    // Display the items pertaining to the search string
////                    updateProduceList(query)
//                }
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                if (newText != null) {
//                    setContentView(R.layout.activity_marketplace)
//                    // getting the recyclerview by its id
//                    val recyclerview = findViewById<RecyclerView>(R.id.lv_listView)
//                    // this creates a vertical layout Manager
//                    recyclerview.layoutManager = LinearLayoutManager(applicationContext)
////                    updateProduceList(newText)
//                }
//                return true
//            }
//        })

        return super.onCreateOptionsMenu(menu)
    }

    // handle cart activities
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.getItemId()
        if (id == R.id.nav_cart) {
            val intent = Intent(this, CartActivity::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }


}
