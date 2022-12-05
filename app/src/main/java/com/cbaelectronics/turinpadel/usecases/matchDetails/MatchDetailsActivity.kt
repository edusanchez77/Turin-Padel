/**
 * Created by CbaElectronics by Eduardo Sanchez on 30/11/22 14:21.
 *  www.cbaelectronics.com.ar
 */

package com.cbaelectronics.turinpadel.usecases.matchDetails

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.cbaelectronics.turinpadel.R
import com.cbaelectronics.turinpadel.databinding.ActivityMatchDetailsBinding
import com.cbaelectronics.turinpadel.model.domain.Match
import com.cbaelectronics.turinpadel.model.domain.Post
import com.cbaelectronics.turinpadel.provider.services.firebase.DatabaseField
import com.cbaelectronics.turinpadel.util.Constants
import com.cbaelectronics.turinpadel.util.FontSize
import com.cbaelectronics.turinpadel.util.FontType
import com.itdev.nosfaltauno.util.extension.addClose
import com.itdev.nosfaltauno.util.extension.font
import com.itdev.nosfaltauno.util.extension.longFormat

class MatchDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMatchDetailsBinding
    private lateinit var viewModel: MatchDetailsViewModel
    private lateinit var matchJSON: String
    private lateinit var match: Match

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMatchDetailsBinding.inflate(layoutInflater)

        // Context
        setContentView(binding.root)

        // ViewModel
        viewModel = ViewModelProvider(this).get(MatchDetailsViewModel::class.java)

        // Data
        val bundle = intent.extras!!
        matchJSON = bundle.getString(DatabaseField.MATCHES.key).toString()
        match = Match.fromJson(matchJSON)!!

        // Setup
        localize()
        setup()
    }

    private fun localize() {

        Glide.with(binding.root.context)
            .load(match.user.photoProfile)
            .into(binding.detalleFotoPerfil)
        binding.detalleNombreUser.text = match.user.displayName
        binding.textViewMatchDetailsUserCategory.text = match.user.settings?.category
        binding.textViewMatchDetailsUserPosition.text = match.user.settings?.position
        binding.detalleVacantes.text = viewModel.vacantesText(binding.root.context, match.vacantes)
        binding.detalleFecha.text = match.date.longFormat()
        binding.detalleCategoria.text = match.category
        binding.detalleGenero.text = match.genre
        binding.textViewTeam1.text = getString(viewModel.team1)
        binding.textViewTeam2.text = getString(viewModel.team2)

        binding.buttonMatchDetailsSolicitud.text = getString(viewModel.button)

        if (match.user.settings?.category.isNullOrEmpty()) {
            binding.textViewMatchDetailsUserCategory.visibility = View.GONE
            binding.textViewMatchDetailsSeparador.visibility = View.GONE
        }
        if (match.user.settings?.position.isNullOrEmpty()) {
            binding.textViewMatchDetailsUserPosition.visibility = View.GONE
            binding.textViewMatchDetailsSeparador.visibility = View.GONE
        }

        loadVacantes(match.vacantes)
        Log.d("LOGBUNDLE", match.id!!)
        viewModel.load(match.id!!)
    }

    private fun setup() {
        addClose(binding.root.context)

        // UI
        binding.detalleNombreUser.font(FontSize.HEAD, FontType.REGULAR, ContextCompat.getColor(this, R.color.text))
        binding.textViewMatchDetailsUserPosition.font(FontSize.BUTTON, FontType.LIGHT, ContextCompat.getColor(this, R.color.text))
        binding.textViewMatchDetailsUserCategory.font(FontSize.BUTTON, FontType.LIGHT, ContextCompat.getColor(this, R.color.text))
        binding.imageViewDate.setColorFilter(ContextCompat.getColor(this, R.color.text))
        binding.imageViewCategory.setColorFilter(ContextCompat.getColor(this, R.color.text))
        binding.imageViewGenre.setColorFilter(ContextCompat.getColor(this, R.color.text))


        binding.detalleVacantes.font(
            FontSize.BUTTON,
            FontType.REGULAR,
            ContextCompat.getColor(this, R.color.light)
        )
        binding.detalleFecha.font(
            FontSize.BODY,
            FontType.REGULAR,
            ContextCompat.getColor(this, R.color.text)
        )
        binding.detalleCategoria.font(
            FontSize.BUTTON,
            FontType.LIGHT,
            ContextCompat.getColor(this, R.color.text)
        )
        binding.detalleGenero.font(
            FontSize.BUTTON,
            FontType.LIGHT,
            ContextCompat.getColor(this, R.color.text)
        )
        binding.textViewTeam1.font(
            FontSize.SUBHEAD,
            FontType.REGULAR,
            ContextCompat.getColor(this, R.color.text)
        )
        binding.textViewTeam2.font(
            FontSize.SUBHEAD,
            FontType.REGULAR,
            ContextCompat.getColor(this, R.color.text)
        )
        binding.textViewPlayer1.font(
            FontSize.BUTTON,
            FontType.LIGHT,
            ContextCompat.getColor(this, R.color.text)
        )
        binding.textViewPlayer2.font(
            FontSize.BUTTON,
            FontType.LIGHT,
            ContextCompat.getColor(this, R.color.text)
        )
        binding.textViewPlayer3.font(
            FontSize.BUTTON,
            FontType.LIGHT,
            ContextCompat.getColor(this, R.color.text)
        )
        binding.textViewPlayer4.font(
            FontSize.BUTTON,
            FontType.LIGHT,
            ContextCompat.getColor(this, R.color.text)
        )

        binding.buttonMatchDetailsSolicitud.isEnabled = false

    }

    private fun loadVacantes(vacantes: Int) {

        var player1: String
        var player2: String
        var player3: String
        var player4: String
        var avatar1: Drawable?
        var avatar2: Drawable?
        var avatar3: Drawable?
        var avatar4: Drawable?

        when (vacantes) {
            1 -> {
                player1 = getString(viewModel.noAvailable)
                player2 = getString(viewModel.noAvailable)
                player3 = getString(viewModel.noAvailable)
                player4 = getString(viewModel.available)
                avatar1 = viewModel.iconNoAvailable(this)
                avatar2 = viewModel.iconNoAvailable(this)
                avatar3 = viewModel.iconNoAvailable(this)
                avatar4 = viewModel.iconAvailable(this)
            }
            2 -> {
                player1 = getString(viewModel.noAvailable)
                player2 = getString(viewModel.noAvailable)
                player3 = getString(viewModel.available)
                player4 = getString(viewModel.available)
                avatar1 = viewModel.iconNoAvailable(this)
                avatar2 = viewModel.iconNoAvailable(this)
                avatar3 = viewModel.iconAvailable(this)
                avatar4 = viewModel.iconAvailable(this)
            }
            3 -> {
                player1 = getString(viewModel.noAvailable)
                player2 = getString(viewModel.available)
                player3 = getString(viewModel.available)
                player4 = getString(viewModel.available)
                avatar1 = viewModel.iconNoAvailable(this)
                avatar2 = viewModel.iconAvailable(this)
                avatar3 = viewModel.iconAvailable(this)
                avatar4 = viewModel.iconAvailable(this)
            }
            4 -> {
                player1 = getString(viewModel.available)
                player2 = getString(viewModel.available)
                player3 = getString(viewModel.available)
                player4 = getString(viewModel.available)
                avatar1 = viewModel.iconAvailable(this)
                avatar2 = viewModel.iconAvailable(this)
                avatar3 = viewModel.iconAvailable(this)
                avatar4 = viewModel.iconAvailable(this)
            }
            else -> {
                player1 = getString(viewModel.noAvailable)
                player2 = getString(viewModel.noAvailable)
                player3 = getString(viewModel.noAvailable)
                player4 = getString(viewModel.noAvailable)
                avatar1 = viewModel.iconNoAvailable(this)
                avatar2 = viewModel.iconNoAvailable(this)
                avatar3 = viewModel.iconNoAvailable(this)
                avatar4 = viewModel.iconNoAvailable(this)
            }
        }


        binding.textViewPlayer1.text = player1
        binding.textViewPlayer2.text = player2
        binding.textViewPlayer3.text = player3
        binding.textViewPlayer4.text = player4
        binding.imageViewPlayer1.setImageDrawable(avatar1)
        binding.imageViewPlayer2.setImageDrawable(avatar2)
        binding.imageViewPlayer3.setImageDrawable(avatar3)
        binding.imageViewPlayer4.setImageDrawable(avatar4)


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