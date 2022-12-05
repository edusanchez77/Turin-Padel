/**
 * Created by CbaElectronics by Eduardo Sanchez on 18/11/22 16:25.
 *  www.cbaelectronics.com.ar
 */

package com.cbaelectronics.turinpadel.usecases.common.rows

import android.content.Context
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cbaelectronics.turinpadel.R
import com.cbaelectronics.turinpadel.databinding.ContentItemMatchBinding
import com.cbaelectronics.turinpadel.model.domain.Match
import com.cbaelectronics.turinpadel.model.domain.Post
import com.cbaelectronics.turinpadel.usecases.comments.CommentsRouter
import com.cbaelectronics.turinpadel.usecases.matchDetails.MatchDetailsActivity
import com.cbaelectronics.turinpadel.usecases.matchDetails.MatchDetailsRouter
import com.cbaelectronics.turinpadel.util.Constants
import com.cbaelectronics.turinpadel.util.FontSize
import com.cbaelectronics.turinpadel.util.FontType
import com.cbaelectronics.turinpadel.util.Util
import com.itdev.nosfaltauno.util.extension.enable
import com.itdev.nosfaltauno.util.extension.font
import com.itdev.nosfaltauno.util.extension.longFormat
import java.util.*

class MatchesRecyclerViewAdapter(
    private val context: Context
) : RecyclerView.Adapter<MatchesRecyclerViewAdapter.ViewHolder>() {

    private var dataList = mutableListOf<Match>()

    fun setDataList(data: MutableList<Match>) {
        dataList = data
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MatchesRecyclerViewAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.content_item_match, parent, false)
        val viewHolder = ViewHolder(view)

        // UI
        val binding = viewHolder.binding

        binding.textViewUser.font(
            FontSize.BUTTON,
            FontType.LIGHT,
            ContextCompat.getColor(context, R.color.light)
        )

        binding.textViewSchedule.font(
            FontSize.BODY,
            FontType.REGULAR,
            ContextCompat.getColor(context, R.color.light)
        )

        binding.textViewVacantes.font(
            FontSize.BUTTON,
            FontType.REGULAR,
            ContextCompat.getColor(context, R.color.light)
        )

        binding.textViewGenre.font(
            FontSize.BUTTON,
            FontType.LIGHT,
            ContextCompat.getColor(context, R.color.text)
        )

        binding.textViewCategory.font(
            FontSize.BUTTON,
            FontType.LIGHT,
            ContextCompat.getColor(context, R.color.text)
        )

        binding.textViewTimer.font(
            FontSize.BUTTON,
            FontType.REGULAR,
            ContextCompat.getColor(context, R.color.text)
        )

        return viewHolder
    }

    override fun onBindViewHolder(holder: MatchesRecyclerViewAdapter.ViewHolder, position: Int) {
        val match = dataList[position]
        holder.bindView(match)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val binding = ContentItemMatchBinding.bind(itemView)
        private var timer: CountDownTimer? = null

        fun bindView(match: Match) {

            var vacantesText = ""

            when (match.vacantes) {
                0 -> {
                    vacantesText = context.getString(R.string.matchDetails_vacantes_completed)
                    binding.lottieMatchItemVacantes.visibility = View.GONE
                }
                1 -> {
                    vacantesText = context.getString(R.string.matchDetails_vacantes_1)
                    binding.lottieMatchItemVacantes.visibility = View.VISIBLE
                }
                else -> {
                    vacantesText =
                        context.getString(R.string.matchDetails_vacantes_n, match.vacantes)
                    binding.lottieMatchItemVacantes.visibility = View.GONE
                }
            }

            binding.textViewUser.text = match.user.displayName
            binding.textViewSchedule.text = match.date.longFormat()
            binding.textViewCategory.text = match.category
            binding.textViewGenre.text = match.genre
            binding.textViewVacantes.text = vacantesText
            Glide.with(context)
                .load(match.user.photoProfile)
                .into(binding.imageViewAvatar)

            // Timer
            timer(context, match)

            // OnItemClick
            itemView.setOnClickListener {
                MatchDetailsRouter().launch(context, match)
            }

        }

        private fun timer(context: Context, match: Match) {

            timer?.cancel()
            val endDate = match.date?.time!! - Date().time

            timer = object : CountDownTimer(endDate, Constants.SECOND.toLong()) {

                override fun onTick(millisUntilFinished: Long) {

                    binding.textViewTimer.text = Util.countdown(context, millisUntilFinished)

                    /*if(millisUntilFinished < Util.hourToMilliseconds(Constants.TIME_UNTIL_CANCEL)){
                        binding.buttonScheduleCancel.visibility = View.GONE
                        binding.buttonScheduleCancel.enable(false)
                        binding.layoutConfirmFixedTurnButtons.visibility = View.GONE
                        binding.buttonDeleteFixedTurn.enable(false)
                        binding.buttonCancelFixedTurn.enable(false)
                        binding.buttonSaveFixedTurn.enable(false)

                    }*/

                }

                override fun onFinish() {

                    binding.textViewTimer.text =
                        context.getText(R.string.schedule_countDown_live).toString().uppercase()
                    /*binding.imageViewScheduleCountdown.setImageResource(R.drawable.button_record)
                    binding.imageViewScheduleCountdown.setColorFilter(ContextCompat.getColor(context, R.color.live))
                    binding.buttonScheduleCancel.visibility = View.GONE
                    binding.buttonScheduleCancel.enable(false)
                    binding.layoutConfirmFixedTurnButtons.visibility = View.GONE
                    binding.buttonDeleteFixedTurn.enable(false)
                    binding.buttonCancelFixedTurn.enable(false)
                    binding.buttonSaveFixedTurn.enable(false)*/

                    timer?.cancel()

                }

            }

            timer?.start()

        }

    }
}