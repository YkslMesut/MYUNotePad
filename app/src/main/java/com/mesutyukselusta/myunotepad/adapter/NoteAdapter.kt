package com.mesutyukselusta.myunotepad.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.mesutyukselusta.myunotepad.databinding.RowNoteBinding
import com.mesutyukselusta.myunotepad.model.Note
import java.util.*
import kotlin.collections.ArrayList


class NoteAdapter(private val noteList : ArrayList<Note>): RecyclerView.Adapter<NoteAdapter.NoteViewHolder>(),
    Filterable {

    private  val TAG = "NoteAdapter"

    private lateinit var mListener : onItemClickListener
    var noteFilterList = ArrayList<Note>()

    init {
        noteFilterList = noteList
    }

    interface onItemClickListener {
        fun onItemClick(uuid : Int)
    }

    fun setOnItemClickListener(listener : onItemClickListener){
        mListener = listener
    }

    class NoteViewHolder(private val itemBinding: RowNoteBinding,listener : onItemClickListener) : RecyclerView.ViewHolder(itemBinding.root) {
        @SuppressLint("SetTextI18n")
        var uuid : Int = 0
        fun bind(note: Note) {
            itemBinding.noteTitle.text = note.note_title
            itemBinding.noteDesc.text = note.note_desc.take(50)
            itemBinding.noteCreationDate.text = note.note_creation_date
            uuid = note.uuid
        }
        init{
            itemView.setOnClickListener {
                listener.onItemClick(uuid)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): NoteViewHolder {
        val itemBinding = RowNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(itemBinding,mListener)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note: Note = noteFilterList[position]
        holder.bind(note)
    }

    override fun getItemCount(): Int {
        return noteFilterList.size
    }

    fun updateNoteList(newNoteList : List<Note>){
        noteList.clear()
        noteList.addAll(newNoteList)
        noteFilterList = noteList
        notifyDataSetChanged()
    }

    fun getNoteFromPosition(position : Int) : Note {
        return noteFilterList[position]
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    noteFilterList = noteList
                } else {
                    val resultList = ArrayList<Note>()
                    for (note in noteList) {
                        if (note.note_desc.lowercase(Locale.ROOT).contains(charSearch.lowercase(Locale.ROOT)) ||
                            note.note_title!!.lowercase(Locale.ROOT).contains(charSearch.lowercase(Locale.ROOT)) ||
                            note.note_creation_date!!.lowercase(Locale.ROOT).contains(charSearch.lowercase(Locale.ROOT))) {
                            resultList.add(note)
                        }
                    }
                    noteFilterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = noteFilterList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                noteFilterList = results?.values as ArrayList<Note>
                for (note in noteFilterList){
                    Log.d(TAG, "publishResults: " + note.note_title)
                }
                notifyDataSetChanged()
            }

        }
    }

}