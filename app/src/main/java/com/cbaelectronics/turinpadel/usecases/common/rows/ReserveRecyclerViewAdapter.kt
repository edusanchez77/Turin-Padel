/**
 * Created by CbaElectronics by Eduardo Sanchez on 15/6/22 09:51.
 *  www.cbaelectronics.com.ar
 */

package com.cbaelectronics.turinpadel.usecases.common.rows

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cbaelectronics.turinpadel.R
import com.cbaelectronics.turinpadel.databinding.ContentItemReserveBinding
import com.cbaelectronics.turinpadel.model.domain.Schedule
import com.cbaelectronics.turinpadel.util.FontSize
import com.cbaelectronics.turinpadel.util.FontType
import com.itdev.nosfaltauno.util.extension.font
import com.itdev.nosfaltauno.util.extension.mediumFormat

class ReserveRecyclerViewAdapter(private val context: Context): RecyclerView.Adapter<ReserveRecyclerViewAdapter.ViewHolder>() {

    private var dataList = mutableListOf<Schedule>()

    fun setDataList(data: MutableList<Schedule>){
        dataList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.content_item_reserve, parent, false)
        val viewHolder = ViewHolder(view)

        // UI
        val binding = viewHolder.binding

        binding.textViewReserveDate.font(FontSize.SUBHEAD, FontType.BOLD, ContextCompat.getColor(context, R.color.text))
        binding.textViewReserveCurt.font(FontSize.BUTTON, FontType.REGULAR, ContextCompat.getColor(context, R.color.text))
        binding.textViewReserveUser.font(FontSize.BUTTON, FontType.REGULAR, ContextCompat.getColor(context, R.color.text))

        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val reserve = dataList[position]
        holder.bindView(reserve)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val binding = ContentItemReserveBinding.bind(itemView)

        fun bindView(reserve: Schedule){
            binding.textViewReserveDate.text = reserve.date.mediumFormat()
            binding.textViewReserveCurt.text = reserve.curt
            binding.textViewReserveUser.text = reserve.user.displayName
            Glide.with(context).load(reserve.user.photoProfile).into(binding.imageViewReserveUser)
        }

    }
}