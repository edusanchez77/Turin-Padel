/**
 * Created by CbaElectronics by Eduardo Sanchez on 19/5/22 16:03.
 *  www.cbaelectronics.com.ar
 */

package com.cbaelectronics.turinpadel.usecases.menu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.cbaelectronics.turinpadel.R
import com.cbaelectronics.turinpadel.databinding.ActivityMenuBinding
import com.cbaelectronics.turinpadel.usecases.makeAdmin.MakeAdminRouter
import com.cbaelectronics.turinpadel.usecases.onboarding.OnboardingRouter
import com.cbaelectronics.turinpadel.usecases.reserve.ReserveRouter
import com.cbaelectronics.turinpadel.util.Constants.ADMIN
import com.cbaelectronics.turinpadel.util.Constants.CREATOR
import com.cbaelectronics.turinpadel.util.Constants.USER
import com.cbaelectronics.turinpadel.util.FontSize
import com.cbaelectronics.turinpadel.util.FontType
import com.itdev.nosfaltauno.util.extension.addClose
import com.itdev.nosfaltauno.util.extension.font
import com.itdev.nosfaltauno.util.extension.navigation

class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding
    private lateinit var viewModel: MenuViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)

        // Content
        setContentView(binding.root)

        // ViewModel
        viewModel = ViewModelProvider(this).get(MenuViewModel::class.java)

        // Setup
        localize()
        setup()
    }

    private fun localize() {

        binding.buttonAdmin.text = getString(viewModel.admin)
        binding.buttonSchedule.text = getString(viewModel.schedule)
        binding.textViewBy.text = getString(viewModel.byText)
        binding.textViewInfo.text = getString(viewModel.infoText)
        binding.buttonSite.text = getString(viewModel.siteText)
        binding.buttonOnboarding.text = getString(viewModel.onboardingText)
        binding.textViewVersion.text = viewModel.versionText(this)

    }

    private fun setup() {
        addClose(this)

        // UI
        binding.textViewBy.font(FontSize.BODY, FontType.LIGHT, getColor(R.color.light))
        binding.textViewInfo.font(FontSize.BODY, FontType.LIGHT, getColor(R.color.light))
        binding.buttonAdmin.font(FontSize.BODY, FontType.LIGHT, getColor(R.color.text))
        binding.buttonSchedule.font(FontSize.BODY, FontType.LIGHT, getColor(R.color.text))
        binding.buttonSite.font(FontSize.BODY, FontType.LIGHT, getColor(R.color.text))
        binding.buttonOnboarding.font(FontSize.BODY, FontType.LIGHT, getColor(R.color.text))
        binding.textViewVersion.font(FontSize.CAPTION, FontType.LIGHT, getColor(R.color.text))

        when(viewModel.user.type){
            ADMIN -> {
                binding.buttonSchedule.visibility = View.VISIBLE
                binding.buttonAdmin.visibility = View.VISIBLE
                binding.line1.visibility = View.VISIBLE
                binding.line2.visibility = View.VISIBLE
            }
            CREATOR -> {
                binding.buttonSchedule.visibility = View.VISIBLE
                binding.buttonAdmin.visibility = View.GONE
                binding.line1.visibility = View.VISIBLE
                binding.line2.visibility = View.GONE
            }
            USER -> {
                binding.buttonSchedule.visibility = View.GONE
                binding.buttonAdmin.visibility = View.GONE
                binding.line1.visibility = View.GONE
                binding.line2.visibility = View.GONE
            }
        }

        // Buttons
        buttons()

    }

    private fun buttons() {

        binding.imageButtonInstagram.setOnClickListener {
            viewModel.open(this, Network.INSTAGRAM)
        }

        binding.imageButtonFacebook.setOnClickListener {
            viewModel.open(this, Network.FACEBOOK)
        }

        binding.imageButtonTwitter.setOnClickListener {
            viewModel.open(this, Network.TWITTER)
        }

        binding.buttonSchedule.setOnClickListener {
            ReserveRouter().launch(this)
        }

        binding.buttonAdmin.setOnClickListener {
            MakeAdminRouter().launch(this)
        }

        binding.buttonSite.navigation {
            viewModel.openSite(this)
        }

        binding.buttonOnboarding.setOnClickListener {
            OnboardingRouter().launch(this)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        this.onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_back_in_up, R.anim.slide_back_out_up)
    }

    override fun onResume() {
        super.onResume()

    }
}