package com.cbaelectronics.turinpadel.usecases.common.tabs

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.cbaelectronics.turinpadel.usecases.turn.curts.Curt1Fragment
import com.cbaelectronics.turinpadel.usecases.turn.curts.Curt2Fragment
import java.util.*


class tabCurtsAdapter(fa: Fragment, pDate: String): FragmentStateAdapter(fa){

    private val mDate = pDate

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        var mFragment: Fragment = Curt1Fragment(mDate)
        when(position){
            0 -> {
                mFragment = Curt1Fragment(mDate)
            }
            1 -> {
                mFragment = Curt2Fragment(mDate)
            }
        }

        return mFragment
    }

}