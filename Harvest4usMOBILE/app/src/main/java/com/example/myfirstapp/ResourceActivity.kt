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
            "National Farmers Market Directory",
            "The Farmers Market Directory provides convenient access to information about local produce including: local farms, market locations, directions, operating times, product offerings, accepted forms of payment, and more.",
            "https://www.ams.usda.gov/local-food-directories/farmersmarkets",
        ))
        data.add(ResourceViewModel(
            "Local Harvest - Farmers Market",
            "Real Food, Real Farmers, Real Community. Lists local farms, farmers markets, restaurants, and other produce related events.",
            "https://www.localharvest.org",
        ))
        data.add(ResourceViewModel(
            "Supplemental Nutrition Assistance Program (SNAP)",
            "Discover how SNAP provides nutrition benefits to supplement the food budget of low-income families so they can purchase healthy food and move towards self-sufficiency.",
            "https://www.fns.usda.gov/snap/supplemental-nutrition-assistance-program",
        ))
        data.add(ResourceViewModel(
            "SNAP for Women, Infants, and Children (WIC)",
            "Learn about supplemental SNAP benefits for pregnant women, infants, and children under the age of five.",
            "https://www.fns.usda.gov/wic",
        ))
        data.add(ResourceViewModel(
            "Seniors Farmers’ Market Nutrition Program (SFMNP)",
            "SFMNP provides support for low-income seniors in accessing local, fresh food.",
            "https://www.fns.usda.gov/sfmnp/senior-farmers-market-nutrition-program",
        ))
        data.add(ResourceViewModel(
            "Google Find Food Support",
            "Find food resources like food pantries, snap benefits, and stores that accept EBT with Google’s Find Food Support.",
            "https://findfoodsupport.withgoogle.com",
        ))
        data.add(ResourceViewModel(
            "USDA - Healthy Eating",
            "Get resources to help you eat a healthy diet with vegetables, fruits, protein, grains, and dairy foods.",
            "https://www.nutrition.gov/topics/basic-nutrition/healthy-eating",
        ))

        data.add(ResourceViewModel(
            "American Heart Association - Healthy Eating",
            "Learn what to look for at the grocery store, restaurants, your workplace, and any eating occasion.",
            "https://www.heart.org/en/healthy-living/healthy-eating",
        ))
        data.add(ResourceViewModel(
            "Community Fridge Locations",
            "Find a community fridge near you!",
            "https://freedge.org/locations/",
        ))
        data.add(ResourceViewModel(
            "The Black Church Food Security Network",
            "The Black Church Food Security Network (BCFSN) utilizes an asset- based approach in organizing and linking the vast resources of historically African american congregations in rural and urban communities.",
            "https://blackchurchfoodsecurity.net/become-a-member/",
        ))
        data.add(ResourceViewModel(
            "Black Farmers’ Network",
            "Black Farmers’ Network (BFN) is a site for rural, African-American farmers to share stories, products and services in a now digital-driven economy.",
            "https://blackfarmersnetwork.com/category/education/",
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