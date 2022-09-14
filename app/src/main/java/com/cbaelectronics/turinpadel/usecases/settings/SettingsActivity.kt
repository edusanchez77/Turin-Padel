/**
 * Created by CbaElectronics by Eduardo Sanchez on 17/5/22 16:36.
 *  www.cbaelectronics.com.ar
 */

package com.cbaelectronics.turinpadel.usecases.settings

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieAnimationView
import com.cbaelectronics.turinpadel.R
import com.cbaelectronics.turinpadel.databinding.ActivitySettingsBinding
import com.cbaelectronics.turinpadel.util.FontSize
import com.cbaelectronics.turinpadel.util.FontType
import com.itdev.nosfaltauno.util.extension.addClose
import com.itdev.nosfaltauno.util.extension.enable
import com.itdev.nosfaltauno.util.extension.font

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)

        // Content
        setContentView(binding.root)

        // ViewModel
        viewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)

        // Setup
        localize()
        setup()
    }

    private fun localize() {

        binding.textViewSettingsGeneral.text = getString(viewModel.notifications)

        binding.switchSettingsNotificationsTurn.text = getString(viewModel.notificationTurn)
        binding.switchSettingsNotificationsPost.text = getString(viewModel.notificationPost)

        binding.buttonCloseSession.text = getString(viewModel.closeSession)
        binding.buttonSaveSettings.text = getString(viewModel.saveSettings)

        binding.switchSettingsNotificationsTurn.isChecked = viewModel.user.settings?.notificationTurn!!
        binding.switchSettingsNotificationsPost.isChecked = viewModel.user.settings?.notificationPost!!

    }

    private fun setup() {

        addClose(this)

        // UI
        binding.textViewSettingsGeneral.font(
            FontSize.BODY,
            FontType.REGULAR,
            ContextCompat.getColor(this, R.color.text)
        )

        binding.switchSettingsNotificationsTurn.font(
            FontSize.BODY,
            FontType.LIGHT,
            ContextCompat.getColor(this, R.color.text)
        )

        binding.switchSettingsNotificationsPost.font(
            FontSize.BODY,
            FontType.LIGHT,
            ContextCompat.getColor(this, R.color.text)
        )

        setupInfo()
        setupFooter()

    }

    private fun setupInfo() {

        // Notification Match

        binding.switchSettingsNotificationsTurn.setOnCheckedChangeListener { _, isChecked ->
            viewModel.settings.notificationTurn = isChecked
            checkEnableSave()
        }

        // Notification Post

        binding.switchSettingsNotificationsPost.setOnCheckedChangeListener { _, isChecked ->
            viewModel.settings.notificationPost = isChecked
            checkEnableSave()
        }

    }

    private fun setupFooter() {
        binding.buttonCloseSession.setOnClickListener {
            loadAlertDialog()
        }

        binding.buttonSaveSettings.enable(false)

        binding.buttonSaveSettings.setOnClickListener {
            save()
        }
    }

    private fun loadAlertDialog() {
        val mDialog = Dialog(binding.root.context)
        val mWindows = mDialog.window!!

        mWindows.attributes.windowAnimations = R.style.DialogAnimation
        mDialog.setContentView(R.layout.custom_dialog_opciones)
        mDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mDialog.setCancelable(false)
        val mText = mDialog.findViewById<TextView>(R.id.txtDialog)
        val mBtnOK = mDialog.findViewById<Button>(R.id.btnDialogAcept)
        val mBtnCancel = mDialog.findViewById<Button>(R.id.btnDialogCancel)
        val lottieDialog = mDialog.findViewById<LottieAnimationView>(R.id.lottieDialog)

        mText.text = getString(viewModel.alertLogout)
        lottieDialog.setAnimation(R.raw.logout)


        lottieDialog.loop(true)
        lottieDialog.playAnimation()

        mBtnOK.text = getString(viewModel.alertButtonOk)
        mBtnCancel.text = getString(viewModel.alertButtonCancel)

        mDialog.show()

        mBtnOK.setOnClickListener {
            mDialog.cancel()
            viewModel.close(this)
        }
        mBtnCancel.setOnClickListener {
            mDialog.cancel()

        }
    }

    private fun checkEnableSave() {
        binding.buttonSaveSettings.enable(viewModel.enableSave(this))
    }

    private fun save() {
        viewModel.save(this)
        binding.buttonSaveSettings.enable(false)
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