package com.example.myfirstapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class ResourceActivity : AppCompatActivity(), CellClickListenerResource {
    private lateinit var adapter: CustomAdapterResource

    //Creates a place to store the returned messages from the API
    private val client = OkHttpClient()
    var responseString = ""

    //Holds the items
    var data = ArrayList<ResourceViewModel>()

    private var username: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resource)


        username = intent.getStringExtra("username")

        // getting the recyclerview by its id
        val recyclerview = findViewById<RecyclerView>(R.id.lv_listView)

        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(this)

        //Displays all items, not using a specific search string
        data.add(ResourceViewModel(
            "Find Food Support",
            "Google",
            "https://findfoodsupport.withgoogle.com",
        ))
        data.add(ResourceViewModel(
            "AgLanta Resources",
            "AgLanta",
            "https://www.aglanta.org/aglanta-resources-index",
        ))
        data.add(ResourceViewModel(
            "Freedge: Public Fridges",
            "Freedge",
            "https://freedge.org",
        ))
        data.add(ResourceViewModel(
            "Freedge Locations",
            "Freedge",
            "https://freedge.org/locations/",
        ))
        data.add(ResourceViewModel(
            "Farming Education",
            "SARE",
            "https://southern.sare.org/?s=&custom-order=date-desc&cat_location%5B%5D=south&post_type=product&re=1",
        ))
        data.add(ResourceViewModel(
            "Black-owned Farm Map",
            "Black Food Justice",
            "https://www.blackfoodjustice.org/food-map-director",
        ))
        data.add(ResourceViewModel(
            "Apply for SNAP",
            "USDA",
            "https://www.fns.usda.gov/snap/apply-to-accept",
        ))

        // This will pass the ArrayList to our Adapter
        adapter = CustomAdapterResource(data, this)

        // Setting the Adapter with the recyclerview
        recyclerview.adapter = adapter

        //Logout button goes to Login screen when clicked
        val logout = findViewById<FloatingActionButton>(R.id.fab_logout)
        logout.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        //Settings button goes to Settings screen when clicked
        val settings = findViewById<FloatingActionButton>(R.id.fab_settings)
        settings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }

        //Marketplace button goes to marketplace screen when clicked
        val marketplace = findViewById<FloatingActionButton>(R.id.fab_marketplace)
        marketplace.setOnClickListener {
            val intent = Intent(this, MarketplaceActivity::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }

        //Cart button goes to cart screen when clicked
        val cart = findViewById<FloatingActionButton>(R.id.fab_cart)
        cart.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }
    }

    override fun onCellClickListener(data : ResourceViewModel) {
        //Passes the item attributes to the product display screen
        val uriUrl: Uri = Uri.parse(data.url)
        val launchBrowser = Intent(Intent.ACTION_VIEW, uriUrl)
        startActivity(launchBrowser)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    fun run(url: String) {
        val request = Request.Builder()
            .url(url)
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