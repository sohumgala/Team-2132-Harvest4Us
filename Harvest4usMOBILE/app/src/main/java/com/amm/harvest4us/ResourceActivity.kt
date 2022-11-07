package com.amm.harvest4us

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import okhttp3.*

class ResourceActivity : AppCompatActivity(), CellClickListenerResource {
    // Need adapters for each category (array)
    private lateinit var adapter: CustomAdapterResource
    private lateinit var adapter1: CustomAdapterResource
    private lateinit var adapter2: CustomAdapterResource
    private lateinit var adapter3: CustomAdapterResource

    // Creates a place to store the returned messages from the API
    private val client = OkHttpClient()
    var responseString = ""

    // Holds the items
    var data = ArrayList<ResourceViewModel>() // ArrayList for Farmers Market Resources
    var data1 = ArrayList<ResourceViewModel>() // ArrayList for Government Nutrition Programs Resources
    var data2 = ArrayList<ResourceViewModel>() // ArrayList for Healthy Eating Resources
    var data3 = ArrayList<ResourceViewModel>() // ArrayList for Community Food Support Resources


    private var username: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resource)

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
                    val intent = Intent(this, FarmsActivity::class.java)
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


        username =intent.getStringExtra("username")

        // getting the recyclerview by its id
        val recyclerview = findViewById<RecyclerView>(R.id.listView)
        val recyclerview1 = findViewById<RecyclerView>(R.id.listView1)
        val recyclerview2 = findViewById<RecyclerView>(R.id.listView2)
        val recyclerview3 = findViewById<RecyclerView>(R.id.listView3)

        // this creates a vertical layout Manager
        recyclerview.layoutManager= LinearLayoutManager(this)
        recyclerview1.layoutManager= LinearLayoutManager(this)
        recyclerview2.layoutManager= LinearLayoutManager(this)
        recyclerview3.layoutManager= LinearLayoutManager(this)

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
        data1.add(ResourceViewModel(
            "Supplemental Nutrition Assistance Program (SNAP)",
            "Discover how SNAP provides nutrition benefits to supplement the food budget of low-income families so they can purchase healthy food and move towards self-sufficiency.",
            "https://www.fns.usda.gov/snap/supplemental-nutrition-assistance-program",
        ))
        data1.add(ResourceViewModel(
            "SNAP for Women, Infants, and Children (WIC)",
            "Learn about supplemental SNAP benefits for pregnant women, infants, and children under the age of five.",
            "https://www.fns.usda.gov/wic",
        ))
        data1.add(ResourceViewModel(
            "Seniors Farmers’ Market Nutrition Program (SFMNP)",
            "SFMNP provides support for low-income seniors in accessing local, fresh food.",
            "https://www.fns.usda.gov/sfmnp/senior-farmers-market-nutrition-program",
        ))
        data1.add(ResourceViewModel(
            "Google Find Food Support",
            "Find food resources like food pantries, snap benefits, and stores that accept EBT with Google’s Find Food Support.",
            "https://findfoodsupport.withgoogle.com",
        ))
        data2.add(ResourceViewModel(
            "USDA - Healthy Eating",
            "Get resources to help you eat a healthy diet with vegetables, fruits, protein, grains, and dairy foods.",
            "https://www.nutrition.gov/topics/basic-nutrition/healthy-eating",
        ))

        data2.add(ResourceViewModel(
            "American Heart Association - Healthy Eating",
            "Learn what to look for at the grocery store, restaurants, your workplace, and any eating occasion.",
            "https://www.heart.org/en/healthy-living/healthy-eating",
        ))
        data3.add(ResourceViewModel(
            "Community Fridge Locations",
            "Find a community fridge near you!",
            "https://freedge.org/locations/",
        ))
        data3.add(ResourceViewModel(
            "The Black Church Food Security Network",
            "The Black Church Food Security Network (BCFSN) utilizes an asset- based approach in organizing and linking the vast resources of historically African american congregations in rural and urban communities.",
            "https://blackchurchfoodsecurity.net/become-a-member/",
        ))
        data3.add(ResourceViewModel(
            "Black Farmers’ Network",
            "Black Farmers’ Network (BFN) is a site for rural, African-American farmers to share stories, products and services in a now digital-driven economy.",
            "https://blackfarmersnetwork.com/category/education/",
        ))

        // This will pass the ArrayList to our Adapter
        adapter = CustomAdapterResource(data, this)
        adapter1 = CustomAdapterResource(data1, this)
        adapter2 = CustomAdapterResource(data2, this)
        adapter3 = CustomAdapterResource(data3, this)


        // Setting the Adapter with the recyclerview
        recyclerview.adapter= adapter
        recyclerview1.adapter= adapter1
        recyclerview2.adapter = adapter2
        recyclerview3.adapter = adapter3

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