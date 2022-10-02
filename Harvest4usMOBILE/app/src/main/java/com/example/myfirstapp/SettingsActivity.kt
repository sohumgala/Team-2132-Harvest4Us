package com.example.myfirstapp

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.preference.PreferenceFragmentCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class SettingsActivity : AppCompatActivity() {
    private var username: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

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
                    val intent = Intent(this, ProducerActivity::class.java)
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

        username = intent.getStringExtra("username")

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val myIntent = Intent(applicationContext, MarketplaceActivity::class.java)
        myIntent.putExtra("username", username)
        startActivityForResult(myIntent, 0)
        return true
    }
}
