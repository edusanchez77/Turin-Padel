/**
 * Created by CbaElectronics by Eduardo Sanchez on 15/6/22 17:36.
 *  www.cbaelectronics.com.ar
 */

package com.cbaelectronics.turinpadel.usecases.common.rows

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cbaelectronics.turinpadel.R
import com.cbaelectronics.turinpadel.databinding.ContentItemUserBinding
import com.cbaelectronics.turinpadel.model.domain.Turn
import com.cbaelectronics.turinpadel.model.domain.User
import com.cbaelectronics.turinpadel.util.FontSize
import com.cbaelectronics.turinpadel.util.FontType
import com.itdev.nosfaltauno.util.extension.font

class UsersRecyclerViewAdapter(private val context: Context, private val itemClickListener: UsersRecyclerViewAdapter.onClickUserListener) :
    RecyclerView.Adapter<UsersRecyclerViewAdapter.ViewHolder>(), Filterable {

    interface onClickUserListener{
        fun onItemClick(user: User)
    }

    private var dataList = mutableListOf<User>()
    private var dataListFilter = mutableListOf<User>()

    fun setDataList(data: MutableList<User>) {
        dataList = data
        dataListFilter = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.content_item_user, parent, false)
        val viewHolder = ViewHolder(view)

        val binding = viewHolder.binding

        // UI

        binding.textViewItemUsername.font(FontSize.SUBHEAD, FontType.REGULAR, ContextCompat.getColor(context, R.color.text))
        binding.textViewItemUsermail.font(FontSize.BUTTON, FontType.LIGHT, ContextCompat.getColor(context, R.color.text))

        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = dataList[position]
        holder.bindView(user)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val binding = ContentItemUserBinding.bind(itemView)

        fun bindView(user: User) {

            binding.textViewItemUsername.text = user.displayName
            binding.textViewItemUsermail.text = user.email
            Glide.with(context).load(user.photoProfile).into(binding.imageViewItemUserAvatar)

            itemView.setOnClickListener {
                itemClickListener.onItemClick(user)
            }

        }

    }

    override fun getFilter(): Filter {

        val filter = object : Filter(){

            override fun performFiltering(CharSequence: CharSequence?): FilterResults {
                var filterResults = FilterResults()

                if(CharSequence == null || CharSequence.isEmpty()){
                    filterResults.count = dataListFilter.size
                    filterResults.values = dataListFilter
                }else{
                    val searchChr = CharSequence.toString().lowercase()
                    var resultData = mutableListOf<User>()

                    for(i in dataList){
                        if(i.displayName!!.lowercase().contains(searchChr)){
                            resultData.add(i)
                        }
                    }
                    filterResults.count = resultData.size
                    filterResults.values = resultData
                }

                return filterResults

            }

            override fun publishResults(CharSequence: CharSequence?, FilterResults: FilterResults?) {

                dataList = FilterResults!!.values as MutableList<User>
                notifyDataSetChanged()

            }
        }

        return filter

    }
}