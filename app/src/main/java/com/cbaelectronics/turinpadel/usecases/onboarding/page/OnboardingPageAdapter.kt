package com.mouredev.twitimer.usecases.onboarding.page

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.cbaelectronics.turinpadel.model.domain.Onboarding

/**
 * Created by CbaElectronics on 20/1/2022
 * Copyright (c) 2022 . All rights reserved.
 */
class OnboardingPageAdapter(val context: AppCompatActivity, var pages: List<Onboarding>): FragmentStateAdapter(context) {

    override fun getItemCount(): Int {
        return pages.size
    }

    override fun createFragment(position: Int): Fragment {
        val page = pages[position]
        return OnboardingPageFragment.fragment(page)
    }

}