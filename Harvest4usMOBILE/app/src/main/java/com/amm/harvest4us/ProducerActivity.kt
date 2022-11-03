package com.amm.harvest4us

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.amm.harvest4us.items.ProduceItem
import com.amm.harvest4us.items.jsonArrToProduceItemList
import org.json.JSONArray

class ProducerActivity : AppCompatActivity() {
    var inventory: List<ProduceItem> = ArrayList(0)
    private var username: String? = null
    private val backend = FlaskBackend

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_producer)

        username = intent.getStringExtra("username")

        val name = intent.getStringExtra("name")!!
        val address = intent.getStringExtra("city") + ", " + intent.getStringExtra("state")
        val description = intent.getStringExtra("description")
        val nameText = findViewById<TextView>(R.id.name_text)
        val addressText = findViewById<TextView>(R.id.address_text)
        val descriptionText = findViewById<TextView>(R.id.description_text)

        nameText.text = name
        addressText.text = address
        descriptionText.text = description

        // Initialize the inventory list
        val responseHandler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                if (msg.what == -1) return
                inventory = jsonArrToProduceItemList(JSONArray(msg.obj as String))
                // frontend code needs to update whatever view is displaying the items here - JC
            }
        }
        backend.getProduceByProducer(name, responseHandler)
    }
}
