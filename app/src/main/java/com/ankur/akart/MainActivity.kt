package com.ankur.akart

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.PopupMenu
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.findNavController
import com.ankur.akart.Activity.LoginActivity
import com.ankur.akart.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var i =0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (FirebaseAuth.getInstance().currentUser==null)
        {
            Intent(this,LoginActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }

        val navHostFragment =supportFragmentManager.findFragmentById(R.id.fragmentContainer)
        val navController = navHostFragment!!.findNavController()

        val popupMenu=PopupMenu(this,null)

        popupMenu.inflate(R.menu.bottom_nav)
        binding.bottomNav.setupWithNavController(popupMenu.menu,navController)

        binding.bottomNav.onItemSelected ={
            when(it)
            {
                0->{
                    i=0
                    navController.navigate(R.id.homeFragment)
                }
                1->i=1
                2->i=2
            }
        }


        navController.addOnDestinationChangedListener(object :NavController.OnDestinationChangedListener{
            override fun onDestinationChanged(
                controller: NavController,
                destination: NavDestination,
                arguments: Bundle?
            ) {
               title=when(destination.id)
               {
                   R.id.cartFragment -> "My Cart"
                   R.id.moreFragment -> "DashBoard"
                   else ->"AKart"
               }
            }


        })

    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (i==0)
        {
            finish()
        }
    }
}