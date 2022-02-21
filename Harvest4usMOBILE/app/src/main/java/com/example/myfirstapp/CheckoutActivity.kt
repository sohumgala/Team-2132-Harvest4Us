package com.example.myfirstapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.*
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CheckoutActivity : AppCompatActivity(), CellClickListener {
    //private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var adapter: CustomAdapterCart
    // ArrayList of class ItemsViewModel
    val data = ArrayList<ItemsViewModel>()

    private var username: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        username = intent.getStringExtra("username")

        val name: String? = intent.getStringExtra("name")
        val category: String? = intent.getStringExtra("category")
        val productID: Int? = intent.getIntExtra("productID", 0)
        val producer: String? = intent.getStringExtra("producer")
        val unit: String? = intent.getStringExtra("unit")
        val usdaGrade: String? = intent.getStringExtra("usdaGrade")
        val active: Int? = intent.getIntExtra("active", 0)
        val availableQuantity: Double? = intent.getDoubleExtra("availableQuantity", 0.0)
        val dateEdited: String? = intent.getStringExtra("dateEdited")
        val organic: Int? = intent.getIntExtra("organic", 0)
        val price: Double? = intent.getDoubleExtra("price", 0.0)
        val quantity: Int? = intent.getIntExtra("quantity", 0)
        val consumerUsername = ""
        val image: Int? = intent.getIntExtra("image", 0)

        val logout = findViewById<FloatingActionButton>(R.id.fab_logout)
        val settings = findViewById<FloatingActionButton>(R.id.fab_settings)
        val totalText = findViewById<TextView>(R.id.totalText)

        // getting the recyclerview by its id
        val recyclerview = findViewById<RecyclerView>(R.id.lv_listView)

        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(this)

//         This loop will create 20 Views containing
//         the image with the count of view
        if ((productID != null) && (producer != null) && (name != null) && (unit != null) && (usdaGrade != null) && (active != null) && (availableQuantity != null) && (dateEdited != null) && (organic != null) && (price != null) && (category != null) && (quantity != null) && (consumerUsername != null)) {
            data.add(
                ItemsViewModel(productID, producer, name, unit, usdaGrade, active, availableQuantity, dateEdited, organic, price, category, R.drawable.ic_android_black_24dp, quantity, consumerUsername)
            )
            totalText.text = "Total: " + price.toString();
        }

        // This will pass the ArrayList to our Adapter
        val adapter = CustomAdapterCart(data, this)


        // Setting the Adapter with the recyclerview
        recyclerview.adapter = adapter

        logout.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        settings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }

        val marketplace = findViewById<FloatingActionButton>(R.id.fab_marketplace)
        marketplace.setOnClickListener {
            val intent = Intent(this, MarketplaceActivity::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }
    }

    override fun onCellClickListener(data : ItemsViewModel) {
        Toast.makeText(this, data.produceType, Toast.LENGTH_SHORT).show()
    }
}