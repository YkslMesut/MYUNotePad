package com.mesutyukselusta.myunotepad.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.mesutyukselusta.myunotepad.R
import com.mesutyukselusta.myunotepad.databinding.FragmentCreateNotepadBinding
import com.mesutyukselusta.myunotepad.databinding.FragmentNotepadFeedBinding
import com.mesutyukselusta.myunotepad.viewmodel.CreateNoteViewModel
import com.mesutyukselusta.myunotepad.viewmodel.NoteFeedViewModel

class CreateNotepadFragment : Fragment() {
    private  val TAG = "CreateNoteFragment"

    private var _binding: FragmentCreateNotepadBinding? = null
    private val binding get() = _binding!!

    private lateinit var noteTitle : String
    private lateinit var noteDesc : String

    private lateinit var viewModel : CreateNoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCreateNotepadBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbarView()

        viewModel = ViewModelProvider(this).get(CreateNoteViewModel::class.java)

        binding.btnSave.setOnClickListener {
            takeInfoFromView()
            viewModel.validateControl(noteTitle,noteDesc)
            Log.d(TAG, "btnSave: ")
            observeLiveData()
        }

        binding.toolbar.btnBack.setOnClickListener {
            NavHostFragment.findNavController(this@CreateNotepadFragment).navigateUp()
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeLiveData(){
        viewModel.createNoteInsertControl.observe(viewLifecycleOwner, Observer {isInsertDatabase->
            if (isInsertDatabase){
                NavHostFragment.findNavController(this@CreateNotepadFragment).navigateUp()
            }
        })
    }

    private fun takeInfoFromView() {

        noteTitle = binding.etNoteTitle.text.toString()
        noteDesc = binding.etNoteDesc.text.toString()

    }

    private fun setToolbarView(){
        binding.toolbar.toolbarTitle.text = resources.getText(R.string.add_note)
        binding.toolbar.btnBack.visibility = View.VISIBLE
    }
}