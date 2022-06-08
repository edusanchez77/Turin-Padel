/**
 * Created by CbaElectronics by Eduardo Sanchez on 26/5/22 16:25.
 *  www.cbaelectronics.com.ar
 */

package com.cbaelectronics.turinpadel.usecases.common.rows

import android.content.Context
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.cbaelectronics.turinpadel.R
import com.cbaelectronics.turinpadel.databinding.ContentItemTurnBinding
import com.cbaelectronics.turinpadel.model.domain.Turn
import com.cbaelectronics.turinpadel.util.Constants
import com.cbaelectronics.turinpadel.util.Constants.STATUS_DEFAULT
import com.cbaelectronics.turinpadel.util.Constants.STATUS_OUTOFTIME
import com.cbaelectronics.turinpadel.util.Constants.STATUS_RESERVED
import com.cbaelectronics.turinpadel.util.FontSize
import com.cbaelectronics.turinpadel.util.FontType
import com.itdev.nosfaltauno.util.extension.enable
import com.itdev.nosfaltauno.util.extension.font
import com.itdev.nosfaltauno.util.extension.shortFormat


class TurnsRecyclerViewAdapter(private val context: Context, private val itemClickListener: onClickTurnClickListener): RecyclerView.Adapter<TurnsRecyclerViewAdapter.ViewHolder>() {

    interface onClickTurnClickListener{
        fun onItemButtonClick(turn: Turn)
    }

    private var dataList = mutableListOf<Turn>()

    fun setDataList(data: MutableList<Turn>){
        dataList = if(data.isNotEmpty()){
            data.sortedBy { turn -> turn.date } as MutableList<Turn>
        }else{
            data
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TurnsRecyclerViewAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.content_item_turn, parent, false)
        val viewHolder = ViewHolder(view)

        // UI

        val binding = viewHolder.binding

        binding.txtHoraTurno.font(FontSize.HEAD, FontType.BOLD, ContextCompat.getColor(context, R.color.text))
        binding.txtTurnoStatus.font(FontSize.BUTTON, FontType.REGULAR, ContextCompat.getColor(context, R.color.text))
        binding.btnReservarTurno.font(FontSize.BUTTON, FontType.REGULAR, ContextCompat.getColor(context, R.color.light))

        return viewHolder
    }

    override fun onBindViewHolder(holder: TurnsRecyclerViewAdapter.ViewHolder, position: Int) {
        val turn = dataList[position]
        holder.bindView(turn)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnCreateContextMenuListener{

        val binding = ContentItemTurnBinding.bind(itemView)

        fun bindView(turn: Turn){

            binding.txtHoraTurno.text = turn.date.shortFormat()

            // Buttons

            when(turn.status){
                STATUS_DEFAULT -> {
                    binding.btnReservarTurno.visibility = View.VISIBLE
                    binding.txtTurnoStatus.text = context.getString(R.string.turn_available_yes)
                    binding.btnReservarTurno.text = context.getString(R.string.turn_button_reserve_available_yes)
                }
                STATUS_RESERVED -> {
                    binding.btnReservarTurno.visibility = View.VISIBLE
                    binding.txtTurnoStatus.text = context.getString(R.string.turn_available_not)
                    binding.btnReservarTurno.text = context.getString(R.string.turn_button_reserve_available_not)
                    binding.btnReservarTurno.enable(false)
                    binding.btnReservarTurno.setBackgroundResource(R.drawable.secondary_button_round)
                }
                STATUS_OUTOFTIME -> {
                    binding.btnReservarTurno.visibility = View.GONE
                    binding.txtTurnoStatus.text = context.getString(R.string.turn_outOfTime)
                }
            }

            binding.btnReservarTurno.setOnClickListener {
                itemClickListener.onItemButtonClick(turn)
            }

            binding.cardViewTurn.setOnCreateContextMenuListener(this)

        }

        override fun onCreateContextMenu(
            menu: ContextMenu,
            v: View,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            val edit = R.string.menu_context_turn_edit
            val delete = R.string.menu_context_turn_delete

            menu.add(adapterPosition, 1, 0, edit)
            menu.add(adapterPosition, 2, 1, delete)
        }



    }

    fun deleteTurn(position: Int){
        Toast.makeText(context, dataList[position].id, Toast.LENGTH_SHORT).show()
    }

}