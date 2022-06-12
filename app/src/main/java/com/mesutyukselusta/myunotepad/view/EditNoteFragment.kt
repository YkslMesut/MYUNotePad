package com.mesutyukselusta.myunotepad.view

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.mesutyukselusta.myunotepad.R
import com.mesutyukselusta.myunotepad.databinding.FragmentEditNoteBinding
import com.mesutyukselusta.myunotepad.model.Note
import com.mesutyukselusta.myunotepad.model.Reminder
import com.mesutyukselusta.myunotepad.util.AlarmReceiver
import com.mesutyukselusta.myunotepad.util.AlarmService
import com.mesutyukselusta.myunotepad.viewmodel.CreateNoteViewModel
import com.mesutyukselusta.myunotepad.viewmodel.EditNoteViewModel
import java.util.*

class EditNoteFragment : Fragment() {
    private  val TAG = "EditNoteFragment"

    private var _binding : FragmentEditNoteBinding ?= null
    private val binding get() = _binding!!


    private lateinit var calendar : Calendar

    private lateinit var viewModel : EditNoteViewModel

    private lateinit var selectedNote : Note

    private lateinit var reminder: Reminder

    lateinit var alarmService: AlarmService

    var isReminderExist = false

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

        alarmService = AlarmService(requireContext())

        setToolbarView()

        viewModel = ViewModelProvider(this).get(EditNoteViewModel::class.java)

        arguments?.let {
            uuid = EditNoteFragmentArgs.fromBundle(it).uuid
            Log.d(TAG, "<uuid>: $uuid")
            viewModel.getNote(uuid)
            viewModel.getReminder(uuid)
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

        binding.btnAddReminder.setOnClickListener {
            openDialog()
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
        viewModel.reminderLiveData.observe(viewLifecycleOwner) { it ->
            reminder = it
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
        viewModel.reminderExistControl.observe(viewLifecycleOwner) {
            if (it) {
                requireActivity().runOnUiThread {
                    binding.btnAddReminder.setText(R.string.set_reminder)
                }
            }
            isReminderExist = it
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

    private fun openCalendar(txtDate : TextView){
        calendar = Calendar.getInstance()

        val dialog = DatePickerDialog(requireContext(),
            { view, year, month, dayOfMonth ->
                val newDate = Calendar.getInstance()
                val newTime = Calendar.getInstance()
                val time = TimePickerDialog(requireContext(),
                    { view, hourOfDay, minute ->
                        newDate[year, month, dayOfMonth, hourOfDay, minute] = 0
                        val tem = Calendar.getInstance()
                        Log.w("TIME", System.currentTimeMillis().toString() + "")
                        if (newDate.timeInMillis - tem.timeInMillis > 0) {
                            calendar = newDate
                            txtDate.text = calendar.time.toString()
                        } else Toast.makeText(
                            requireContext(),
                            "Invalid time",
                            Toast.LENGTH_SHORT).show()
                    }, newTime[Calendar.HOUR_OF_DAY], newTime[Calendar.MINUTE], true)
                time.show()
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(
                Calendar.DAY_OF_MONTH))
        dialog.datePicker.minDate = System.currentTimeMillis()
        dialog.show()



    }

    private fun openDialog(){

        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.reminder_popup)

        val txtDate: TextView = dialog.findViewById(R.id.txtDate)
        val btnSelectDate: Button = dialog.findViewById(R.id.btnSelectDate)
        val btnAdd: Button = dialog.findViewById(R.id.btnAdd)

        if (isReminderExist){
            txtDate.text = reminder.reminder_date
        }

        btnSelectDate.setOnClickListener {
            openCalendar(txtDate)
        }
        btnAdd.setOnClickListener {
            if (binding.etNoteTitle.text.toString().isNotEmpty()){
                val reminderMessage = binding.etNoteTitle.text.toString()
                viewModel.setAlarm(calendar,reminderMessage,dialog,requireContext(),selectedNote.uuid,alarmService)
                observeLiveData()
            }
        }
        dialog.show()


    }





}