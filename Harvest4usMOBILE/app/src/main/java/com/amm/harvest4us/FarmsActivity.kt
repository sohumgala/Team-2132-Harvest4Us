package com.amm.harvest4us

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amm.harvest4us.items.ProduceItem
import com.amm.harvest4us.items.ProducerItem
import com.amm.harvest4us.items.jsonArrToProduceItemList
import com.amm.harvest4us.items.jsonArrToProducerItem
import okhttp3.*
import org.json.JSONArray
import com.google.android.material.bottomnavigation.BottomNavigationView

class FarmsActivity : AppCompatActivity(), CellClickListenerFarms {
    private lateinit var adapter: FarmsAdapter

    private var backend = FlaskBackend
    private val producerList = ArrayList<ProducerItem>()

    private var username: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_farms)

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
        val recyclerView = findViewById<RecyclerView>(R.id.farmView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        updateProducerList("")
    }

    override fun onCellClickListener(data: ProducerItem) {
        val name = data.name
        val city = data.city
        val state = data.state
        val description = data.description

        val i = Intent(this, ProducerActivity::class.java)
        i.putExtra("username", username)
        i.putExtra("name", name)
        i.putExtra("city", city)
        i.putExtra("state", state)
        i.putExtra("description", description)
        startActivity(i)
    }

    /**
     * Make a backend call to update the producer list using the query searchString.
     * ProducerList will be updated asynchronously when the backend call returns.
     */
    fun updateProducerList(searchString: String) {
        val responseHandler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                if (msg.what == -1) return // backend call failed
                producerList.clear()
                val newProducerData = JSONArray(msg.obj as String)
                for (i in 0 until newProducerData.length()) {
                    val arr = newProducerData.getJSONArray(i)
                    producerList.add(jsonArrToProducerItem(arr))
                }
                updateProducerListView()
            }
        }
        backend.getAllFarms(responseHandler)
    }

    /**
     * Refresh the CustomAdapter and RecyclerView for displaying producer items.
     */
    fun updateProducerListView() {
        adapter = FarmsAdapter(producerList, this@FarmsActivity)
        val recyclerview = findViewById<RecyclerView>(R.id.farmView)
        recyclerview.adapter = adapter
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
//                    updateProduceList(query)
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
//                    updateProduceList(newText)
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
//    fun updateProduceList(searchString: String) {
//        val responseHandler = object : Handler(Looper.getMainLooper()) {
//            override fun handleMessage(msg: Message) {
//                if (msg.what == -1) return // backend call failed
//                produceList = jsonArrToProduceItemList(JSONArray(msg.obj as String))
//                updateProduceListView()
//            }
//        }
//        backend.getAllProduce(responseHandler)
//    }




}
