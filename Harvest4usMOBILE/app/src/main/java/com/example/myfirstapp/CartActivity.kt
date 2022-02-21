package com.example.myfirstapp

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.*
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.math.BigDecimal
import java.math.RoundingMode
import com.paypal.checkout.PayPalCheckout
import com.paypal.checkout.config.CheckoutConfig
import com.paypal.checkout.config.Environment
import com.paypal.checkout.config.SettingsConfig
import com.paypal.checkout.createorder.CurrencyCode
import com.paypal.checkout.createorder.UserAction
import androidx.annotation.RequiresApi
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_cart.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull


class CartActivity : AppCompatActivity(), CellClickListener {
    private val clientIdWasUpdated by lazy {
        PAYPAL_CLIENT_ID != null
    }
    private lateinit var adapter: CustomAdapterCart
    val JSON: MediaType? = "application/json; charset=utf-8".toMediaTypeOrNull()

    private val client = OkHttpClient()
    var responseString = ""
    // ArrayList of class ItemsViewModel
    var data = ArrayList<ItemsViewModel>()

    @RequiresApi(Build.VERSION_CODES.M)
    private var username: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PayPalCheckout.setConfig(
            checkoutConfig = CheckoutConfig(
                application = application,
                clientId = PAYPAL_CLIENT_ID,
                environment = Environment.SANDBOX,
                returnUrl = "${BuildConfig.APPLICATION_ID}://paypalpay",
                currencyCode = CurrencyCode.USD,
                userAction = UserAction.PAY_NOW,
                settingsConfig = SettingsConfig(
                    loggingEnabled = true,
                    shouldFailEligibility = false
                )
            )
        )
        setContentView(R.layout.activity_cart)

        username = intent.getStringExtra("username")

        val logout = findViewById<FloatingActionButton>(R.id.fab_logout)
        val settings = findViewById<FloatingActionButton>(R.id.fab_settings)
        val marketplace = findViewById<FloatingActionButton>(R.id.fab_marketplace)
        val resources = findViewById<FloatingActionButton>(R.id.fab_resources)
        val checkout = findViewById<Button>(R.id.checkoutButton)
        val totalText = findViewById<TextView>(R.id.totalText)
        var total = 0.00;
        val username: String? = intent.getStringExtra("username")

        // getting the recyclerview by its id
        val recyclerview = findViewById<RecyclerView>(R.id.lv_listView)

        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(this)

        //Get the cart

//         if (username == null) {
//             username = "fff"
//         }

        run("https://crwpdbho85.execute-api.us-east-1.amazonaws.com/dev/get-cart/$username")
        //Wait for a response
        Thread.sleep(1500)
        if (responseString != null) {
            println("\n\n\nHERE IS THE ORIGINAL STRING: " + responseString)
            //Format the response string
            responseString = responseString.removePrefix("{\"message\":")
            responseString = responseString.removeSuffix("}")

            //Make the string into a JSON array
            var jsonArray = JSONArray(responseString)
            println("HERE IS THE JSON ARRAY: " + jsonArray)
            // This loop will add each item and the attributes to the inventory list
            for (i in 0..jsonArray.length()) {
                if (i < jsonArray.length()) {
                    val jsonObject: JSONObject = jsonArray.getJSONObject(i)
                    val product_id = jsonObject.get("product_id") as Int
                    val producer = jsonObject.get("producer").toString()
                    val quantity = jsonObject.get("quantity") as Int

                    run("https://f6e1mmza5c.execute-api.us-east-1.amazonaws.com/dev/get-by-id/$producer||$product_id")
                    Thread.sleep(1500)
                    //Format the response string
                    responseString = responseString.removePrefix("{\"message\":")
                    responseString = responseString.removeSuffix("}")
                    println("\n\nHERE IS THE NEW STRING, SHOULD BE OBJECT: " + responseString)

                    //Make the string into a JSON object
                    if (!responseString.contains(" not found")) {
//                        var jsonArray2 = JSONArray(responseString)
//                        val jsonObjectProduct: JSONObject = jsonArray2.getJSONObject(0)
                        var jsonObjectProduct = JSONObject(responseString)

                        val produceType = jsonObjectProduct.get("produceType").toString()
                        val unit = jsonObjectProduct.get("unit").toString()
                        val usdaGrade = jsonObjectProduct.get("usdaGrade").toString()
                        val active = jsonObjectProduct.get("active") as Int
                        val availableQuantity = jsonObjectProduct.get("availableQuantity") as Double
                        val dateEdited = jsonObjectProduct.get("dateEdited").toString()
                        val organic = jsonObjectProduct.get("organic") as Int
                        val price = jsonObjectProduct.get("price") as Double
                        val produceCategory = jsonObjectProduct.get("produceCategory").toString()
                        data.add(
                            ItemsViewModel(
                                product_id,
                                producer,
                                produceType,
                                unit,
                                usdaGrade,
                                active,
                                availableQuantity,
                                dateEdited,
                                organic,
                                price,
                                produceCategory,
                                R.drawable.ic_android_black_24dp,
                                quantity,
                                username!!
                            )
                        )
                        total += price * quantity;
                    }
                }
            }
        }
        val decimal = BigDecimal(3.14159265359).setScale(2, RoundingMode.HALF_EVEN)
        totalText.setText("Total: " + BigDecimal(total.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString())

        // This will pass the ArrayList to our Adapter
        val adapter = CustomAdapterCart(data, this)


        // Setting the Adapter with the recyclerview
        recyclerview.adapter = adapter

        logout.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        settings.setOnClickListener {
//            val mainFragment : SettingsFragment = SettingsFragment()
//            supportFragmentManager.beginTransaction().add(R.id.container, mainFragment).commit()

            val intent = Intent(this, SettingsActivity::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }

        marketplace.setOnClickListener {
            val intent = Intent(this, MarketplaceActivity::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }

        resources.setOnClickListener {
            val intent = Intent(this, ResourceActivity::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }

        checkout.setOnClickListener {
            if (clientIdWasUpdated) {
                println("in")
                val i = Intent(this, PaymentButtonQuickStartActivity::class.java)
                i.putExtra("total", total.toString())
                i.putExtra("username", username)
                startActivity(i)
            } else {
                displayErrorSnackbar("Please Update PAYPAL_CLIENT_ID In QuickStartConstants.")
            }
        }
    }

    override fun onCellClickListener(data : ItemsViewModel) {
        Toast.makeText(this, data.produceType, Toast.LENGTH_SHORT).show()
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

    private fun displayErrorSnackbar(errorMessage: String) {
        Snackbar.make(rootQuickStart, errorMessage, Snackbar.LENGTH_INDEFINITE)
            .apply { setAction("Got It 👍") { dismiss() } }
            .show()
    }
}