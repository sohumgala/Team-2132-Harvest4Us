package com.amm.harvest4us

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import com.google.android.material.bottomnavigation.BottomNavigationView


class MarketplaceActivity : AppCompatActivity(), CellClickListener {
    private lateinit var adapter: CustomAdapter

    // Creates a place to store the returned messages from the API
    private val client = OkHttpClient()
    var responseString = ""

    // Holds the items
    var data = ArrayList<ItemsViewModel>()

    var minPriceValue = 0
    var maxPriceValue = 10

    private var username: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_marketplace)

        // creating the bottom navigation functionality
        val myBottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigation)

        myBottomNavigationView.setOnNavigationItemSelectedListener{
            when(it.itemId){
                R.id.home_image-> {
                    val intent = Intent(this, MarketplaceActivity::class.java)
                    intent.putExtra("username", username)
                    startActivity(intent)
                }
                R.id.tractor_image-> {
                    val intent = Intent(this, ProducerActivity::class.java)
                    intent.putExtra("username", username)
                    startActivity(intent)
                }
                R.id.resource_image-> {
                    val intent = Intent(this, ResourceActivity::class.java)
                    intent.putExtra("username", username)
                    startActivity(intent)
                }
                R.id.account_image-> {
                    val intent = Intent(this, SettingsActivity::class.java)
                    intent.putExtra("username", username)
                    startActivity(intent)
                }

            }
            true
        }


        username = intent.getStringExtra("username")

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

        val minPrice = findViewById<EditText>(R.id.editMinPrice)
        minPrice.setText(minPriceValue.toString())
        val maxPrice = findViewById<EditText>(R.id.editMaxPrice)
        maxPrice.setText(maxPriceValue.toString())
        val applyFilter = findViewById<Button>(R.id.applyFilter)
        applyFilter.setOnClickListener {
            println("i'm here")
            if ((minPrice.text.toString() == "") || (maxPrice.text.toString() == "")) {
                println("blank")
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
                displayItems("")
                val adapter = CustomAdapter(data, this@MarketplaceActivity)
                recyclerview.adapter = adapter
            }
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
        val quantity = data.quantity

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
                    val adapter = CustomAdapter(data, this@MarketplaceActivity)

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
                    val adapter = CustomAdapter(data, this@MarketplaceActivity)

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
        println(minPriceValue)
        println(maxPriceValue)
        // run("https://f6e1mmza5c.execute-api.us-east-1.amazonaws.com/dev/filter-item/%7Bfilters/?price_range=$maxPriceValue,$minPriceValue&search_key=$searchString&distance=X&consumer=$username")
        responseString = MockBackend.filterItem(maxPriceValue, minPriceValue, searchString, username).body?.string()!!
        // Wait for a response
        Thread.sleep(1700)

        if (responseString != null) {
            // Format the response string
            responseString = responseString.removePrefix("{\"message\":")
            responseString = responseString.removeSuffix("}")
            println(responseString)
            // Make the string into a JSON array
            var jsonArray = JSONArray(responseString)
            // This loop will add each item and the attributes to the inventory list
            for (i in 0..jsonArray.length()) {
                if (i < jsonArray.length()) {
                    val jsonObject: JSONObject = jsonArray.getJSONObject(i)
                    val product_id = jsonObject.get("product_id") as Int
                    val producer = jsonObject.get("producer").toString()
                    val produceType = jsonObject.get("produceType").toString()
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
                            product_id, producer, produceType, unit, usdaGrade, active, availableQuantity, dateEdited, organic, price, produceCategory, R.drawable.ic_android_black_24dp, 1, username!!
                        )
                    )
                }
            }
        }
        return data
    }

    // DEPRECATED
//    fun run(url: String) {
//        val request = Request.Builder()
//            .url(url)
//            .build()
//
//        client.newCall(request).enqueue(object : Callback {
//            override fun onFailure(call: Call, e: IOException) {}
//            override fun onResponse(call: Call, response: Response) {
//                responseString = response.body?.string()!!
//                println(response.body)
//            }
//        })
//    }
}
