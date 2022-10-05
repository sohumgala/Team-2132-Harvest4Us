package com.amm.harvest4us

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
class InventoryActivity : AppCompatActivity(), CellClickListener {
    private lateinit var adapter: CustomAdapter

    // Creates a place to store the returned messages from the API
    private val client = OkHttpClient()
    var responseString = ""

    // Holds the items
    var data = ArrayList<ItemsViewModel>()

    private var producer: String? = null
    private var username: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        producer = intent.getStringExtra("producer")
        username = intent.getStringExtra("username")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventory)

        // getting the recyclerview by its id
        val recyclerview = findViewById<RecyclerView>(R.id.lv_listView)

        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(this)

        // Displays all items, not using a specific search string
        data = displayItems("")

        // This will pass the ArrayList to our Adapter
        adapter = CustomAdapter(data, this)

        // Setting the Adapter with the recyclerview
        recyclerview.adapter = adapter

        // Logout button goes to Login screen when clicked
        val logout = findViewById<FloatingActionButton>(R.id.fab_logout)
        logout.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Settings button goes to Settings screen when clicked
        val settings = findViewById<FloatingActionButton>(R.id.fab_settings)
        settings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }

        // Cart button goes to cart screen when clicked
        val cart = findViewById<FloatingActionButton>(R.id.fab_cart)
        cart.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }

        // Resources button goes to resources screen when clicked
        val resources = findViewById<FloatingActionButton>(R.id.fab_resources)
        resources.setOnClickListener {
            val intent = Intent(this, ResourceActivity::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }

        val seeAll = findViewById<Button>(R.id.all_products)
        seeAll.setOnClickListener {
            val intent = Intent(this, MarketplaceActivity::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }
    }

    override fun onCellClickListener(data: ItemsViewModel) {
        // Gets all the data for the item clicked
        val name = data.produceType
        val category = data.produceCategory
        val productID = data.product_id
        val producer = data.producer
        val unit = data.unit
        val usdaGrade = data.usdaGrade
        val active = data.active
        val availableQuantity = data.availableQuantity
        val dateEdited = data.dateEdited
        val organic = data.organic
        val price = data.price
        val image = data.image

        // Passes the item attributes to the product display screen
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

        i.putExtra("username", username)
        startActivity(i)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)

        // Initializes the search bar
        val search = menu?.findItem(R.id.nav_search)
        val searchView = search?.actionView as SearchView
        searchView.queryHint = "Search something!"

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // if there is something in the search bar,
                if (query != null) {
                    setContentView(R.layout.activity_marketplace)
//                    val minPrice = findViewById<EditText>(R.id.editMinPrice)
//                    minPrice.setText(minPriceValue.toString())
//                    val maxPrice = findViewById<EditText>(R.id.editMaxPrice)
//                    maxPrice.setText(maxPriceValue.toString())
                    // getting the recyclerview by its id
                    val recyclerview = findViewById<RecyclerView>(R.id.lv_listView)

                    // this creates a vertical layout Manager
                    recyclerview.layoutManager = LinearLayoutManager(applicationContext)
                    // Display the items pertaining to the search string
                    displayItems(query)
                    // This will pass the ArrayList to our Adapter
                    val adapter = CustomAdapter(data, this@InventoryActivity)

                    // Setting the Adapter with the recyclerview
                    recyclerview.adapter = adapter
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
                    // Display the items pertaining to the search string
                    displayItems(newText)
                    // This will pass the ArrayList to our Adapter
                    val adapter = CustomAdapter(data, this@InventoryActivity)

                    // Setting the Adapter with the recyclerview
                    recyclerview.adapter = adapter
                }
                return true
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    fun displayItems(searchString: String): ArrayList<ItemsViewModel> {
        // reset the data array
        data.clear()
        // ArrayList of class ItemsViewModel
        data = ArrayList<ItemsViewModel>()

        // Get the list of items pertaining to the search string
        println("2 PRODUCER IS: " + producer)
//        run("https://f6e1mmza5c.execute-api.us-east-1.amazonaws.com/dev/get-by-producer/$producer")
        responseString = MockBackend.getByProducer(producer!!).body!!.string()
        // Wait for a response
        Thread.sleep(1500)

        if (responseString != null) {
            // Format the response string
//            responseString = responseString.removePrefix("{\"message\":")
//            responseString = responseString.removeSuffix("}")

            // Make the string into a JSON array
            var jsonArray = JSONArray(responseString)
            // This loop will add each item and the attributes to the inventory list
            for (i in 0..jsonArray.length()) {
                if (i < jsonArray.length()) {
                    val jsonObject: JSONObject = jsonArray.getJSONObject(i)
                    val product_id = jsonObject.get("product_id") as Int
                    val producer = jsonObject.get("producer").toString()
                    val productType = jsonObject.get("produceType").toString()
                    val unit = jsonObject.get("unit").toString()
                    val usdaGrade = jsonObject.get("usdaGrade").toString()
                    val active = jsonObject.get("active") as Int
                    val availableQuantity = jsonObject.get("availableQuantity") as Double
                    val dateEdited = jsonObject.get("dateEdited").toString()
                    val organic = jsonObject.get("organic") as Int
                    val price = jsonObject.get("price") as Double
                    val produceCategory = jsonObject.get("produceCategory").toString()
                    data.add(
                        ItemsViewModel(
                            product_id, producer, productType, unit, usdaGrade, active, availableQuantity, dateEdited, organic, price, produceCategory, R.drawable.ic_android_black_24dp, 1, username!!
                        )
                    )
                }
            }
        }
        return data
    }

    // DEPRECATED: Old Team method of running backend call
//    fun run(url: String) {
//        val request = Request.Builder()
//            .url(url)
//            .build()
//
//        client.newCall(request).enqueue(object : Callback {
//            override fun onFailure(call: Call, e: IOException) {}
//            override fun onResponse(call: Call, response: Response) {
//                responseString = response.body?.string()!!
//                println(responseString)
//            }
//        })
//    }
}
