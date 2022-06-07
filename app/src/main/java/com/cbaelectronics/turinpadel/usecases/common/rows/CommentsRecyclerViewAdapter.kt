/**
 * Created by CbaElectronics by Eduardo Sanchez on 2/6/22 10:12.
 *  www.cbaelectronics.com.ar
 */

package com.cbaelectronics.turinpadel.usecases.common.rows

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cbaelectronics.turinpadel.R
import com.cbaelectronics.turinpadel.databinding.ContentItemCommentBinding
import com.cbaelectronics.turinpadel.model.domain.Comment
import com.cbaelectronics.turinpadel.util.FontSize
import com.cbaelectronics.turinpadel.util.FontType
import com.itdev.nosfaltauno.util.extension.font
import com.itdev.nosfaltauno.util.extension.mediumFormat


class CommentsRecyclerViewAdapter(private val context: Context): RecyclerView.Adapter<CommentsRecyclerViewAdapter.ViewHolder>() {

    private var dataList = mutableListOf<Comment>()

    fun setDataList(data: MutableList<Comment>){
        dataList = data
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CommentsRecyclerViewAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.content_item_comment, parent, false)
        val viewHolder = ViewHolder(view)

        // UI
        val binding = viewHolder.binding

        binding.textViewCommentUsername.font(FontSize.SUBHEAD, FontType.LIGHT, ContextCompat.getColor(context, R.color.primary_shadow))
        binding.textViewCommentDate.font(FontSize.BODY, FontType.LIGHT, ContextCompat.getColor(context, R.color.text))
        binding.textViewCommentMessage.font(FontSize.SUBHEAD, FontType.REGULAR, ContextCompat.getColor(context, R.color.text))

        return viewHolder
    }

    override fun onBindViewHolder(holder: CommentsRecyclerViewAdapter.ViewHolder, position: Int) {
        val comment = dataList[position]
        holder.bindView(comment)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val binding = ContentItemCommentBinding.bind(itemView)

        fun bindView(comment: Comment){

            Glide.with(context).load(comment.user.photoProfile).into(binding.imageViewCommentItemUserAvatar)
            binding.textViewCommentDate.text = comment.date.mediumFormat()
            binding.textViewCommentMessage.text = comment.message
            binding.textViewCommentUsername.text = comment.user.displayName


        }

    }
}