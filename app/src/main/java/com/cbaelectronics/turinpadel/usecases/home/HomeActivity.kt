/**
 * Created by CbaElectronics by Eduardo Sanchez on 10/5/22 15:14.
 *  www.cbaelectronics.com.ar
 */

package com.cbaelectronics.turinpadel.usecases.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.cbaelectronics.turinpadel.R
import com.cbaelectronics.turinpadel.databinding.ActivityHomeBinding
import com.cbaelectronics.turinpadel.model.domain.Post
import com.cbaelectronics.turinpadel.model.session.Session
import com.cbaelectronics.turinpadel.provider.preferences.PreferencesKey
import com.cbaelectronics.turinpadel.provider.preferences.PreferencesProvider
import com.cbaelectronics.turinpadel.provider.services.firebase.DatabaseField
import com.cbaelectronics.turinpadel.usecases.comments.CommentsRouter
import com.cbaelectronics.turinpadel.usecases.menu.MenuRouter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.itdev.nosfaltauno.util.extension.titleLogo

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var viewModel: HomeViewModel

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)

        // Content
        setContentView(binding.root)

        // ViewModel
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        // Setup
        initCrashlytics()
        //getData()
        setup()

    }

    private fun initCrashlytics() {
        FirebaseCrashlytics.getInstance().setUserId(viewModel.user.email.toString())
    }

    private fun getData(){
        val bundle: Bundle? = intent.extras
        if(bundle?.isEmpty == false){
            val postJSON = bundle.getString(DatabaseField.POST.key).toString()
            val post = Post.fromJson(postJSON)!!
            CommentsRouter().launch(this, post)
        }
    }


    private fun setup() {
        supportActionBar?.titleLogo(this)

        // Navigation
        val navView: BottomNavigationView = binding.homeBottomNavigationView
        navController = findNavController(R.id.homeContainer)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.home_menu_turn,
                R.id.home_menu_grandtable,
                R.id.home_menu_account
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_settings_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.home_menu -> {
                MenuRouter().launch(this)
            }
        }
        return super.onOptionsItemSelected(item)
    }

}