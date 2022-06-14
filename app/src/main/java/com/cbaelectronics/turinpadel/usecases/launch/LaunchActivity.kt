package com.cbaelectronics.turinpadel.usecases.launch

import androidx.appcompat.app.AppCompatActivity
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import com.cbaelectronics.turinpadel.R
import com.cbaelectronics.turinpadel.databinding.ActivityLaunchBinding
import com.cbaelectronics.turinpadel.model.session.Session
import com.cbaelectronics.turinpadel.provider.preferences.PreferencesKey
import com.cbaelectronics.turinpadel.provider.preferences.PreferencesProvider
import com.cbaelectronics.turinpadel.usecases.home.HomeActivity
import com.cbaelectronics.turinpadel.usecases.home.HomeRouter
import com.cbaelectronics.turinpadel.usecases.login.LoginActivity
import com.cbaelectronics.turinpadel.usecases.login.LoginRouter
import com.cbaelectronics.turinpadel.usecases.onboarding.OnboardingActivity

/**
 * Created by CbaElectronics by Eduardo Sanchez on 10/05/2022.
 * www.cbaelectronics.com.ar
 */


class LaunchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLaunchBinding
    private lateinit var viewModel: LaunchViewModel


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityLaunchBinding.inflate(layoutInflater)

        // Content
        setContentView(binding.root)

        // ViewModel
        viewModel = ViewModelProvider(this).get(LaunchViewModel::class.java)

        // Setup
        setup()
        data()

    }

    private fun setup() {

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

    }


    private fun data() {

        // Session
        Session.instance.configure(this)
        validateSession()

    }

    private fun validateSession() {

        val session = PreferencesProvider.string(this, PreferencesKey.AUTH_USER)
        var nextActivity = if (session.isNullOrEmpty()) Intent(this, OnboardingActivity::class.java).also { it } else Intent(this, HomeActivity::class.java).also { it }

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(nextActivity)
            finish()
        }, 5000)
    }


}