package com.bs.sriwilis.ui.order

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bs.sriwilis.R
import com.bs.sriwilis.databinding.FragmentOrderBinding
import com.bs.sriwilis.databinding.FragmentOrderFailedBinding
import com.bs.sriwilis.databinding.FragmentOrderSuccessBinding

class OrderFailedFragment : Fragment() {

    private var _binding: FragmentOrderFailedBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderFailedBinding.inflate(inflater, container, false)
        return binding.root
    }
}