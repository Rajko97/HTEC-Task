package com.htec.task.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.htec.task.R
import com.htec.task.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController = findNavController(R.id.fragment)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        super.onPrepareOptionsMenu(menu)
        menu?.clear()
        if(isDetailsFragmentCurrentlyShown()) {
            menuInflater.inflate(R.menu.main_activity_menu, menu)
        }
        return true
    }

    private fun isDetailsFragmentCurrentlyShown() : Boolean {
        return navController.currentDestination?.id == R.id.postDetailsFragment
    }
}