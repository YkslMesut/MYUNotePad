package com.mesutyukselusta.myunotepad.view

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mesutyukselusta.myunotepad.R
import com.mesutyukselusta.myunotepad.adapter.NoteAdapter
import com.mesutyukselusta.myunotepad.databinding.FragmentNotepadFeedBinding
import com.mesutyukselusta.myunotepad.model.Note
import com.mesutyukselusta.myunotepad.util.SwipeGesture
import com.mesutyukselusta.myunotepad.viewmodel.NoteFeedViewModel
import kotlinx.android.synthetic.main.fragment_notepad_feed.*


class NotepadFeedFragment : Fragment() {
    private val TAG = "NoteFeedFragment"

    private lateinit var viewModel : NoteFeedViewModel


    private var _binding: FragmentNotepadFeedBinding? = null
    private val binding get() = _binding!!

    private val noteAdapter = NoteAdapter(arrayListOf())




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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbarView()

        viewModel = ViewModelProvider(this).get(NoteFeedViewModel::class.java)
        viewModel.getAllNotesFromRoom()

        adapterView()

        observeLiveData()

        binding.payerSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                noteAdapter.filter.filter(newText)
                return false
            }

        })

        binding.toolbar.btnAdd.setOnClickListener {
            val action = NotepadFeedFragmentDirections.actionNotepadFeedFragmentToCreateNotepadFragment()
            Navigation.findNavController(requireView()).navigate(action)
        }

    }

    private fun observeLiveData() {
        viewModel.noteListLiveData.observe(viewLifecycleOwner) { payers ->

            payers?.let {
                if (it.isNotEmpty()){
                    txtEmpty.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                    noteAdapter.updateNoteList(it)
                } else {
                    recyclerView.visibility = View.GONE
                    txtEmpty.visibility = View.VISIBLE
                }

            }

        }

    }

    private fun adapterView(){


        _binding!!.recyclerView.layoutManager = GridLayoutManager(context,2)
        _binding!!.recyclerView.adapter = noteAdapter

        val swipeGesture = object : SwipeGesture(requireContext()){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                when(direction){
                    ItemTouchHelper.LEFT ->{
                        val deleteNoteFragment = deleteNoteFragment(viewHolder.position)
                        deleteNoteFragment.show()
                    }
                    ItemTouchHelper.RIGHT ->{
                        val deleteNoteFragment = deleteNoteFragment(viewHolder.position)
                        deleteNoteFragment.show()                    }
                }
            }
        }

        val touchHelper = ItemTouchHelper(swipeGesture)
        touchHelper.attachToRecyclerView(binding.recyclerView)

        noteAdapter.setOnItemClickListener(object : NoteAdapter.onItemClickListener{
            override fun onItemClick(uuid: Int) {
                val action = NotepadFeedFragmentDirections.actionNotepadFeedFragmentToEditNoteFragment(uuid)
                Navigation.findNavController(requireView()).navigate(action)
            }
        })

    }

    private fun deleteNoteFragment(position : Int) : AlertDialog {
        val alert = AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.deleteMessage))
            .setPositiveButton(getString(R.string.yes)) { dialog, which ->
                val item = noteAdapter.getNoteFromPosition(position)
                viewModel.deleteNoteFromRoom(item.uuid)
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.no)) { dialog, which ->
                viewModel.getAllNotesFromRoom()
                dialog.dismiss()
            }
            .create()

        return alert
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setToolbarView(){
        binding.toolbar.toolbarTitle.text = resources.getText(R.string.my_note)
        binding.toolbar.btnAdd.visibility = View.VISIBLE
    }
}