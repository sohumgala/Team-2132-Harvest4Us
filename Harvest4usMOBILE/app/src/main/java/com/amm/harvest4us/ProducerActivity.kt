package com.amm.harvest4us

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.amm.harvest4us.items.ProduceItem
import com.google.android.material.floatingactionbutton.FloatingActionButton
import okhttp3.*
import org.json.JSONObject

class ProducerActivity : AppCompatActivity() {
    private val client = OkHttpClient()
    var responseString = ""
    var data = ArrayList<ProduceItem>()
    private var username: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_producer)

        username = intent.getStringExtra("username")

        val name: String? = intent.getStringExtra("producer")
        val nameText = findViewById<TextView>(R.id.name_text)
        val addressText = findViewById<TextView>(R.id.address_text)
        val descriptionText = findViewById<TextView>(R.id.description_text)

//        run("https://ohn09n3sii.execute-api.us-east-1.amazonaws.com/dev/get-producers/$name")
        responseString = MockBackend.getProducers(name!!).body?.string()!!
        Thread.sleep(1500) // they probably put this in as a hack... - Jose
        if (responseString != null) {
//            responseString = responseString.removePrefix("{\"message\":")
//            responseString = responseString.removeSuffix("}")
            val jsonObject = JSONObject(responseString)
            nameText.text = jsonObject.get("business_name").toString()
            addressText.text = jsonObject.get("business_city").toString() + ", " + jsonObject.get("business_state").toString()
            descriptionText.text = jsonObject.get("about").toString()
        }

        val inventoryBtn = findViewById<Button>(R.id.inventory_button)
        inventoryBtn.setOnClickListener {
            val i = Intent(this, InventoryActivity::class.java)
            i.putExtra("producer", name)
            i.putExtra("username", username)
            startActivity(i)
        }
    }

}
