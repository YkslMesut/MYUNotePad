package com.mesutyukselusta.myunotepad.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mesutyukselusta.myunotepad.R
import com.mesutyukselusta.myunotepad.databinding.FragmentNotepadFeedBinding


class NotepadFeedFragment : Fragment() {
    private val TAG = "NoteFeedFragment"

    private var _binding: FragmentNotepadFeedBinding? = null
    private val binding get() = _binding!!



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentNotepadFeedBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}