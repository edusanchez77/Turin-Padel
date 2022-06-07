/**
 * Created by CbaElectronics by Eduardo Sanchez on 1/6/22 09:21.
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
import com.cbaelectronics.turinpadel.databinding.ContentItemPostBinding
import com.cbaelectronics.turinpadel.model.domain.Post
import com.cbaelectronics.turinpadel.model.domain.Schedule
import com.cbaelectronics.turinpadel.usecases.comments.CommentsRouter
import com.cbaelectronics.turinpadel.util.FontSize
import com.cbaelectronics.turinpadel.util.FontType
import com.itdev.nosfaltauno.util.extension.font
import com.itdev.nosfaltauno.util.extension.mediumFormat
import com.itdev.nosfaltauno.util.extension.shortFormat

class GrandtableRecyclerViewAdapter(private val context: Context, private val itemClickListener: onClickPostClickListener) :
    RecyclerView.Adapter<GrandtableRecyclerViewAdapter.ViewHolder>() {

    interface onClickPostClickListener{
        fun onItemClick(post: Post)
    }

    private var dataList = mutableListOf<Post>()

    fun setDataList(data: MutableList<Post>) {
        dataList = data
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GrandtableRecyclerViewAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.content_item_post, parent, false)
        val viewHolder = ViewHolder(view)

        // UI
        val binding = viewHolder.binding

        binding.textViewPostUsername.font(FontSize.SUBHEAD, FontType.REGULAR, ContextCompat.getColor(context, R.color.text))
        binding.textViewPostDate.font(FontSize.BODY, FontType.LIGHT, ContextCompat.getColor(context, R.color.text))
        binding.textViewPostMessage.font(FontSize.SUBHEAD, FontType.REGULAR, ContextCompat.getColor(context, R.color.text))
        binding.imageViewPostComment.setColorFilter(ContextCompat.getColor(context, R.color.text))
        binding.textViewPostComment.font(FontSize.BUTTON, FontType.REGULAR, ContextCompat.getColor(context, R.color.text))

        return viewHolder
    }

    override fun onBindViewHolder(holder: GrandtableRecyclerViewAdapter.ViewHolder, position: Int) {
        val post = dataList[position]
        holder.bindView(post)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val binding = ContentItemPostBinding.bind(itemView)

        fun bindView(post: Post) {

            Glide.with(context).load(post.user.photoProfile).into(binding.imageViewPostAvatar)
            binding.textViewPostUsername.text = post.user.displayName
            binding.textViewPostMessage.text = post.message
            binding.textViewPostDate.text = post.date.mediumFormat()
            binding.textViewPostComment.text = post.comments.toString()

            itemView.setOnClickListener {
                CommentsRouter().launch(context, post)
            }

        }

    }
}