/**
 * Created by CbaElectronics by Eduardo Sanchez on 19/5/22 16:03.
 *  www.cbaelectronics.com.ar
 */

package com.cbaelectronics.turinpadel.usecases.menu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.cbaelectronics.turinpadel.R
import com.cbaelectronics.turinpadel.databinding.ActivityMenuBinding
import com.cbaelectronics.turinpadel.util.FontSize
import com.cbaelectronics.turinpadel.util.FontType
import com.itdev.nosfaltauno.util.extension.addClose
import com.itdev.nosfaltauno.util.extension.font

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
        binding.buttonSite.font(FontSize.BODY, FontType.LIGHT, getColor(R.color.text))
        binding.buttonOnboarding.font(FontSize.BODY, FontType.LIGHT, getColor(R.color.text))
        binding.textViewVersion.font(FontSize.CAPTION, FontType.LIGHT, getColor(R.color.text))

        // Buttons
        buttons()
    }

    private fun buttons() {
        binding.buttonSite.setOnClickListener {
            //TODO: Crear hipervinculo
        }

        binding.buttonOnboarding.setOnClickListener {
            //TODO: Crear hipervinculo
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
}