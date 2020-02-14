package fr.thomas.lefebvre.toutougo.ui

import android.content.Context
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import android.content.Intent
import android.content.DialogInterface
import android.location.LocationManager
import androidx.appcompat.app.AlertDialog
import fr.thomas.lefebvre.toutougo.R


class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
       



        navView.setupWithNavController(navController)

    }




}
