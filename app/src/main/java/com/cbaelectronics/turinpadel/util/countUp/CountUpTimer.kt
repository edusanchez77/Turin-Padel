/**
 * Created by CbaElectronics by Eduardo Sanchez on 9/6/22 10:48.
 *  www.cbaelectronics.com.ar
 */

package com.cbaelectronics.turinpadel.util.countUp

import android.os.CountDownTimer

abstract class CountUpTimer(private val secondsInFuture: Int, countUpIntervalSeconds: Int) :
    CountDownTimer(secondsInFuture.toLong() * 1000, countUpIntervalSeconds.toLong() * 1000) {

    abstract fun onCount(count: Int)

    override fun onTick(msUntilFinished: Long) {
        onCount(((secondsInFuture.toLong() * 1000 - msUntilFinished) / 1000).toInt())
    }

}