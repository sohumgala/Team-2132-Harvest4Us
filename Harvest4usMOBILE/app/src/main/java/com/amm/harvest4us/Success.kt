package com.amm.harvest4us

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Success : AppCompatActivity() {

    private var username: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success)

        username = intent.getStringExtra("username")

        val marketplace = findViewById<FloatingActionButton>(R.id.fab_marketplace)
        marketplace.setOnClickListener {
            val intent = Intent(this, MarketplaceActivity::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }
    }
}
