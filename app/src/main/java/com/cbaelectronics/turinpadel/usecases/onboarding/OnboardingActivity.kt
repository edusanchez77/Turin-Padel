/**
 * Created by CbaElectronics by Eduardo Sanchez on 14/6/22 10:16.
 *  www.cbaelectronics.com.ar
 */

package com.cbaelectronics.turinpadel.usecases.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.cbaelectronics.turinpadel.R
import com.cbaelectronics.turinpadel.databinding.ActivityOnboardingBinding
import com.cbaelectronics.turinpadel.provider.preferences.PreferencesKey
import com.cbaelectronics.turinpadel.provider.preferences.PreferencesProvider
import com.cbaelectronics.turinpadel.usecases.login.LoginRouter
import com.itdev.nosfaltauno.util.extension.primary
import com.itdev.nosfaltauno.util.extension.secondary
import com.mouredev.twitimer.usecases.onboarding.page.OnboardingPageAdapter

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var viewModel: OnboardingViewModel

    private var selection = 0
    private var dots: Array<TextView?> = arrayOfNulls(0)

    private var onboardingKey: Boolean? = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)

        // Content
        setContentView(binding.root)
        supportActionBar?.hide()

        // ViewModel
        viewModel = ViewModelProvider(this).get(OnboardingViewModel::class.java)

        // Setup
        localize()
        setup()
    }

    private fun localize() {
        binding.buttonPrev.text = getText(viewModel.previousText)
        binding.buttonNext.text = getText(viewModel.nextText)
    }

    private fun setup() {
        onboardingKey = PreferencesProvider.bool(this, PreferencesKey.ONBOARDING)

        // Adapter
        slider()

        // Dots
        dots(0)

        buttons()

    }

    private fun buttons() {

        binding.buttonPrev.secondary {
            selection -= 1
            binding.viewPagerOnboarding.setCurrentItem(selection, true)
        }

        binding.buttonNext.primary {
            if (selection == viewModel.pages - 1) {
                if (onboardingKey == false){
                    PreferencesProvider.set(this, PreferencesKey.ONBOARDING, true)
                    LoginRouter().launch(this)
                }
                finish()
            } else {
                selection += 1
                binding.viewPagerOnboarding.setCurrentItem(selection, true)
            }
        }
    }

    private fun slider() {
        binding.viewPagerOnboarding.adapter = OnboardingPageAdapter(this, viewModel.data)

        binding.viewPagerOnboarding.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                dots(position)
                selection = position

                binding.buttonPrev.visibility = if (position == 0) View.INVISIBLE else View.VISIBLE
                binding.buttonNext.text = getText(if (position == viewModel.pages - 1) viewModel.understoodText else viewModel.nextText)
            }
        })
    }

    private fun dots(position: Int) {
        dots = arrayOfNulls(viewModel.pages)
        binding.layoutDots.removeAllViews()
        for (index in dots.indices) {
            dots[index] = TextView(this)
            dots[index]?.text = Html.fromHtml("•")
            dots[index]?.textSize = 35f
            dots[index]?.setTextColor(getColor(if (index == position) R.color.primary else R.color.primary_shadow))
            binding.layoutDots.addView(dots[index])
        }
    }
}