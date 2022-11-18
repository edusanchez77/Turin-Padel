/**
 * Created by CbaElectronics by Eduardo Sanchez on 17/11/22 22:21.
 *  www.cbaelectronics.com.ar
 */

package com.cbaelectronics.turinpadel.usecases.matches

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.cbaelectronics.turinpadel.R
import com.cbaelectronics.turinpadel.databinding.FragmentMatchesBinding
import com.cbaelectronics.turinpadel.usecases.addMatch.AddMatchRouter
import com.cbaelectronics.turinpadel.usecases.grandtable.GrandtableViewModel
import com.cbaelectronics.turinpadel.util.FontSize
import com.cbaelectronics.turinpadel.util.FontType
import com.itdev.nosfaltauno.util.extension.font

class MatchesFragment : Fragment() {

    private lateinit var _binding: FragmentMatchesBinding
    private val binding get() = _binding!!
    private lateinit var viewModel: MatchesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Content
        _binding = FragmentMatchesBinding.inflate(inflater, container, false)

        // ViewModel
        viewModel = ViewModelProvider(this).get(MatchesViewModel::class.java)

        // Setup
        localize()
        setup()

        return binding.root
    }

    private fun localize() {

        binding.textViewMatchesTitle.text = getString(viewModel.title)
        binding.textViewMatchesInfo.text = getString(viewModel.info)
        binding.textViewMatchesFooterInfo.text = getString(viewModel.fotterInfo)
        binding.buttonCreateMatch.text = getString(viewModel.button)

    }

    private fun setup() {

        // UI
        binding.textViewMatchesTitle.font(
            FontSize.BODY,
            FontType.REGULAR,
            ContextCompat.getColor(binding.root.context, R.color.text)
        )

        binding.textViewMatchesInfo.font(
            FontSize.SUBHEAD,
            FontType.REGULAR,
            ContextCompat.getColor(binding.root.context, R.color.text)
        )

        binding.textViewMatchesFooterInfo.font(
            FontSize.BODY,
            FontType.REGULAR,
            ContextCompat.getColor(binding.root.context, R.color.text)
        )

        buttons()

    }

    private fun buttons() {
        binding.buttonCreateMatch.setOnClickListener {
            AddMatchRouter().launch(binding.root.context)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }

}