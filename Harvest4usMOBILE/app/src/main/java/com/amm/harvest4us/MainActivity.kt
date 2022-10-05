package com.amm.harvest4us

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.amm.harvest4us.databinding.ActivityMainBinding


//class MainActivity : AppCompatActivity() {
//
//    private lateinit var appBarConfiguration: AppBarConfiguration
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        val firstFragment=Marketplace()
//        val secondFragment=ProducerActivity()
//        val thirdFragment=ResourceActivity()
//        val fourthFragment=SettingsFragment()
//
//        setCurrentFragment(firstFragment)
//
//        bottomNavigation.setOnNavigationItemSelectedListener {
//            when(it.itemId){
//                R.id.home_image->setCurrentFragment(firstFragment)
////                R.id.tractor_image->setCurrentFragment(secondFragment)
////                R.id.resource_image->setCurrentFragment(thirdFragment)
//                R.id.account_image->setCurrentFragment(fourthFragment)
//
//            }
//            true
//        }
//
//    }
//
//    override fun onSupportNavigateUp(): Boolean {
//        val navController = findNavController(R.id.nav_host_fragment_content_main)
//        return navController.navigateUp(appBarConfiguration)
//                || super.onSupportNavigateUp()
//    }
//
//    private fun setCurrentFragment(fragment:Fragment)=
//        supportFragmentManager.beginTransaction().apply {
//            replace(R.id.flFragment,fragment)
//            commit()
//        }
//}


//class MainActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        val firstFragment=Marketplace()
//        val secondFragment=ProducerActivity()
//        val thirdFragment=ResourceActivity()
//        val fourthFragment=SettingsFragment()
//
//        setCurrentFragment(firstFragment)
//
//        bottomNavigation.setOnNavigationItemSelectedListener {
//            when(it.itemId){
//                R.id.home_image->setCurrentFragment(firstFragment)
////                R.id.tractor_image->setCurrentFragment(secondFragment)
////                R.id.resource_image->setCurrentFragment(thirdFragment)
//                R.id.account_image->setCurrentFragment(fourthFragment)
//
//            }
//            true
//        }
//
//    }
//
//    private fun setCurrentFragment(fragment:Fragment)=
//        supportFragmentManager.beginTransaction().apply {
//            replace(R.id.flFragment,fragment)
//            commit()
//        }
//
//}









//class MainActivity : AppCompatActivity() {
//
//    private lateinit var appBarConfiguration: AppBarConfiguration
//    private lateinit var binding: ActivityMainBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
////        setCurrentFragment(Marketplace())
//        setContentView(R.layout.activity_main)
//
////        val firstFragment=Marketplace()
////        val fourFragment=SettingsFragment()
////
////        setCurrentFragment(firstFragment)
//
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        setSupportActionBar(binding.toolbar)
//
//        val navController = findNavController(R.id.nav_host_fragment_content_main)
//        appBarConfiguration = AppBarConfiguration(navController.graph)
//        setupActionBarWithNavController(navController, appBarConfiguration)
//
//        val home = findViewById<BottomNavigationItemView>(R.id.home_image)
//        home.setOnClickListener{
//            val intent = Intent(this, MarketplaceActivity::class.java)
//            startActivity(intent)
//        }
//
//        val edu = findViewById<BottomNavigationItemView>(R.id.resource_image)
//        edu.setOnClickListener{
//            val intent = Intent(this, ResourceActivity::class.java)
//            startActivity(intent)
//        }
////
////        bottomNavigation.setOnNavigationItemSelectedListener{
////            when(it.itemId){
////                R.id.home_image-> {
////                    val intent = Intent(this, MarketplaceActivity::class.java)
////                    startActivity(intent)
////                    true
////                }
////                R.id.resource_image-> {
////                    val intent = Intent(this, ResourceActivity::class.java)
////                    startActivity(intent)
////                    true
////                }
////            }
////            false
////        }
//
//
////        binding.fab.setOnClickListener { view ->
////            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
////                    .setAction("Action", null).show()
////        }
//    }
//
//    private fun setCurrentFragment(fragment:Fragment)=
//        supportFragmentManager.beginTransaction().apply{
//            replace(R.id.flFragment,fragment)
//            commit()
//        }
//
//}





class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

//        binding.fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
//        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) ||
                super.onSupportNavigateUp()
    }
}
