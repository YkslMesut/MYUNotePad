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
import com.mesutyukselusta.myunotepad.databinding.FragmentEditNoteBinding
import com.mesutyukselusta.myunotepad.model.Note
import com.mesutyukselusta.myunotepad.viewmodel.CreateNoteViewModel
import com.mesutyukselusta.myunotepad.viewmodel.EditNoteViewModel

class EditNoteFragment : Fragment() {
    private  val TAG = "EditNoteFragment"

    private var _binding : FragmentEditNoteBinding ?= null
    private val binding get() = _binding!!

    private lateinit var viewModel : EditNoteViewModel

    private lateinit var selectedNote : Note

    var uuid : Int = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEditNoteBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbarView()

        viewModel = ViewModelProvider(this).get(EditNoteViewModel::class.java)

        arguments?.let {
            uuid = EditNoteFragmentArgs.fromBundle(it).uuid
            Log.d(TAG, "<uuid>: $uuid")
            viewModel.getNote(uuid)
            observeLiveData()

        }

        binding.btnUpdate.setOnClickListener {
            if (selectedNote != null) {
                val  noteTitle = (binding.etNoteTitle.text.toString())
                val noteDesc = (binding.etNoteDesc.text.toString())
                viewModel.updateNote(selectedNote,noteTitle,noteDesc)
                observeLiveData()
            }
        }

        binding.toolbar.btnDelete.setOnClickListener {
            viewModel.deleteNoteFromRoom(uuid)
            observeLiveData()
        }

        binding.toolbar.btnBack.setOnClickListener {
            NavHostFragment.findNavController(this@EditNoteFragment).navigateUp()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeLiveData() {
        viewModel.noteLiveData.observe(viewLifecycleOwner) { note ->
            selectedNote = note
            updateView(note)
        }
        viewModel.updateControl.observe(viewLifecycleOwner) {
            if (it) {
                NavHostFragment.findNavController(this@EditNoteFragment).navigateUp()
            }
        }
        viewModel.deleteControl.observe(viewLifecycleOwner) {
            if (it) {
                NavHostFragment.findNavController(this@EditNoteFragment).navigateUp()
            }
        }
    }

    private fun updateView(note : Note){

        val noteTitle = note.note_title
        val noteDesc = note.note_desc

        binding.etNoteDesc.setText(noteDesc)
        binding.etNoteTitle.setText(noteTitle)

    }

    private fun setToolbarView(){
        binding.toolbar.toolbarTitle.text = resources.getText(R.string.edit_note)
        binding.toolbar.btnBack.visibility = View.VISIBLE
        binding.toolbar.btnDelete.visibility = View.VISIBLE
    }

}