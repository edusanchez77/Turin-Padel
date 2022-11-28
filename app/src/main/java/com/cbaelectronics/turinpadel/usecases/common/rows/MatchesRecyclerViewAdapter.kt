/**
 * Created by CbaElectronics by Eduardo Sanchez on 18/11/22 16:25.
 *  www.cbaelectronics.com.ar
 */

package com.cbaelectronics.turinpadel.usecases.common.rows

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cbaelectronics.turinpadel.R
import com.cbaelectronics.turinpadel.databinding.ContentItemMatchBinding
import com.cbaelectronics.turinpadel.model.domain.Match

class MatchesRecyclerViewAdapter(private val context: Context): RecyclerView.Adapter<MatchesRecyclerViewAdapter.ViewHolder>() {

    private var dataList = mutableListOf<Match>()

    fun setDataList(data: MutableList<Match>){
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

        return viewHolder
    }

    override fun onBindViewHolder(holder: MatchesRecyclerViewAdapter.ViewHolder, position: Int) {
        val match = dataList[position]
        holder.bindView(match)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val binding = ContentItemMatchBinding.bind(itemView)

        fun bindView(match: Match){

        }

    }
}