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
import com.amm.harvest4us.items.jsonArrToProduceItem
import com.amm.harvest4us.items.jsonArrToProduceItemList
import com.google.android.material.bottomnavigation.BottomNavigationView
import okhttp3.*
import org.json.JSONArray

class MarketplaceActivity : AppCompatActivity(), CellClickListener {
    private lateinit var adapter: CustomAdapter

    // Creates a place to store the returned messages from the API
    private var backend = FlaskBackend
    private var produceList : List<ProduceItem> = ArrayList()

    var minPriceValue = 0
    var maxPriceValue = 10

    private var username: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_marketplace)

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
        val recyclerview = findViewById<RecyclerView>(R.id.lv_listView)
        recyclerview.layoutManager = LinearLayoutManager(this)

        // Populate the produce list with all produce (default view)
        updateProduceList("")

        val minPrice = findViewById<EditText>(R.id.editMinPrice)
        minPrice.setText(minPriceValue.toString())
        val maxPrice = findViewById<EditText>(R.id.editMaxPrice)
        maxPrice.setText(maxPriceValue.toString())
        val applyFilter = findViewById<Button>(R.id.applyFilter)
        applyFilter.setOnClickListener {
            if ((minPrice.text.toString() == "") || (maxPrice.text.toString() == "")) {
                Toast.makeText(
                    this@MarketplaceActivity,
                    "Enter two price values",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (minPrice.text.toString().toInt() > maxPrice.text.toString().toInt()) {
                println("swapped")
                Toast.makeText(this@MarketplaceActivity, "Invalid price range", Toast.LENGTH_SHORT).show()
            } else if ((minPrice.text.toString().toInt() < 0) || (maxPrice.text.toString().toInt() < 0)) {
                println("negative")
                Toast.makeText(this@MarketplaceActivity, "Enter positive price values", Toast.LENGTH_SHORT).show()
            } else {
                println("perfect")
                maxPriceValue = maxPrice.text.toString().toInt()
                minPriceValue = minPrice.text.toString().toInt()
                updateProduceList("")
                val adapter = CustomAdapter(produceList, this@MarketplaceActivity)
                recyclerview.adapter = adapter
            }
        }
    }

    override fun onCellClickListener(data: ProduceItem) {
        // Gets all the data for the item clicked
        val name = data.produceType
        val category = data.produceCategory
        val productID = data.product_id
        val producer = data.producer
        val unit = data.unit
        val usdaGrade = data.usdaGrade
        val active = data.active
        val availableQuantity = data.quantityInStock
        val dateEdited = data.dateEdited
        val organic = data.organic
        val price = data.price
        val image = data.image
        val quantity = data.quantityInOrder

        // Passes the item attributes to the product display screen?? is this necessary
        val i = Intent(this, ProductActivity::class.java)
        i.putExtra("name", name)
        i.putExtra("category", category)
        i.putExtra("productID", productID)
        i.putExtra("producer", producer)
        i.putExtra("unit", unit)
        i.putExtra("usdaGrade", usdaGrade)
        i.putExtra("active", active)
        i.putExtra("availableQuantity", availableQuantity)
        i.putExtra("dateEdited", dateEdited)
        i.putExtra("organic", organic)
        i.putExtra("price", price)
        i.putExtra("image", image)
        i.putExtra("quantity", quantity)
        i.putExtra("username", username)
        startActivity(i)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)

        // Initializes the search bar
        val search = menu?.findItem(R.id.nav_search)
        val searchView = search?.actionView as SearchView
        searchView.queryHint = "Search something!"

        val cartB = menu?.findItem(R.id.nav_cart)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // if there is something in the search bar,
                if (query != null) {
                    setContentView(R.layout.activity_marketplace)
                    val recyclerview = findViewById<RecyclerView>(R.id.lv_listView)
                    // this creates a vertical layout Manager
                    recyclerview.layoutManager = LinearLayoutManager(applicationContext)
                    // Display the items pertaining to the search string
                    updateProduceList(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    setContentView(R.layout.activity_marketplace)
                    // getting the recyclerview by its id
                    val recyclerview = findViewById<RecyclerView>(R.id.lv_listView)
                    // this creates a vertical layout Manager
                    recyclerview.layoutManager = LinearLayoutManager(applicationContext)
                    updateProduceList(newText)
                }
                return true
            }
        })

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

    /**
     * Make a backend call to update the produce list using the query searchString.
     * ProduceList will be updated asynchronously when the backend call returns.
     */
    fun updateProduceList(searchString: String) {
        val responseHandler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                if (msg.what == -1) return // backend call failed
                produceList = jsonArrToProduceItemList(JSONArray(msg.obj as String))
                updateProduceListView()
            }
        }
        backend.getAllProduce(responseHandler)
    }

    /**
     * Refresh the CustomAdapter and RecyclerView for displaying produce items.
     */
    fun updateProduceListView() {
        adapter = CustomAdapter(produceList, this@MarketplaceActivity)
        val recyclerview = findViewById<RecyclerView>(R.id.lv_listView)
        recyclerview.adapter = adapter
    }
}
