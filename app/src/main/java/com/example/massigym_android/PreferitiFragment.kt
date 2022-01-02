package com.example.massigym_android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.massigym_android.databinding.FragmentPreferitiBinding

class PreferitiFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentPreferitiBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_preferiti, container, false)


        return binding.root
    }
}