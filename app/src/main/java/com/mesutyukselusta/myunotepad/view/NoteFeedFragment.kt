package com.mesutyukselusta.myunotepad.view

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mesutyukselusta.myunotepad.R
import com.mesutyukselusta.myunotepad.adapter.NoteAdapter
import com.mesutyukselusta.myunotepad.databinding.FragmentNotepadFeedBinding
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
        setHasOptionsMenu(true);

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

    }

    private fun observeLiveData() {
        viewModel.noteListLiveData.observe(viewLifecycleOwner) { payers ->

            payers?.let {
                recyclerView.visibility = View.VISIBLE
                noteAdapter.updateNoteList(it)
            }

        }

    }

    private fun adapterView(){

        _binding!!.recyclerView.layoutManager = LinearLayoutManager(context)
        _binding!!.recyclerView.adapter = noteAdapter

        val swipeGesture = object : SwipeGesture(requireContext()){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                when(direction){
                    ItemTouchHelper.LEFT ->{
                        val item = noteAdapter.getNoteFromPosition(viewHolder.adapterPosition)
                        viewModel.deleteNoteFromRoom(item.uuid)
                    }
                    ItemTouchHelper.RIGHT ->{
                        val item = noteAdapter.getNoteFromPosition(viewHolder.adapterPosition)
                        viewModel.deleteNoteFromRoom(item.uuid)
                    }
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.feed_note_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.add_note){
            val action = NotepadFeedFragmentDirections.actionNotepadFeedFragmentToCreateNotepadFragment()
            Navigation.findNavController(requireView()).navigate(action)
        }

        return super.onOptionsItemSelected(item)
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}