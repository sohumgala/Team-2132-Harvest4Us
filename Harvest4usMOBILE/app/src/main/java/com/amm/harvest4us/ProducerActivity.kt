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
    var data = ArrayList<ProduceItem>()
    private var username: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_producer)

        username = intent.getStringExtra("username")

        val name: String? = intent.getStringExtra("name")
        val address = intent.getStringExtra("city") + ", " + intent.getStringExtra("state")
        val description = intent.getStringExtra("description")
        val nameText = findViewById<TextView>(R.id.name_text)
        val addressText = findViewById<TextView>(R.id.address_text)
        val descriptionText = findViewById<TextView>(R.id.description_text)

        nameText.text = name
        addressText.text = address
        descriptionText.text = description
    }

}
