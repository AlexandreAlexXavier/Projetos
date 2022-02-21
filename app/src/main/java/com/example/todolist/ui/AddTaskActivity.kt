package com.example.todolist.ui

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.todolist.databinding.ActivityAddTaskBinding
import com.example.todolist.datasource.TaskDataSource
import com.example.todolist.extensions.format
import com.example.todolist.extensions.text
import com.example.todolist.model.Task
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*

class AddTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(intent.hasExtra(TASK_ID)) {
            val taskId = intent.getIntExtra(TASK_ID, 0)
            TaskDataSource.findById(taskId)?.let {
                binding.taskTitle.text = it.title
                binding.boxDate.text = it.date
                binding.boxHour.text = it.hour
                binding.tagStateDescription.text = it.desc
            }

        }

        insertListeners()
    }

    private fun insertListeners() {

        binding.boxDate.editText?.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()

            datePicker.addOnPositiveButtonClickListener {
                val timeZone = TimeZone.getDefault()
                val offSet = timeZone.getOffset(Date().time) * -1
                binding.boxDate.text = Date(it + offSet).format()
            }

            datePicker.show(supportFragmentManager, "DATE_PICKED_TAG")
       }
       binding.boxHour.editText?.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .build()

            timePicker.addOnPositiveButtonClickListener {
                val minute = if (timePicker.minute in 0..9) "0${timePicker.minute}" else timePicker.minute
                val hour = if (timePicker.hour in 0..9) "0${timePicker.hour}" else timePicker.hour
                binding.boxHour.text = "${hour}:${minute}"
            }
            timePicker.show(supportFragmentManager, null)
        }
        binding.cancelButton.setOnClickListener {
            finish()

        }
        binding.createTaskButton.setOnClickListener {
            val task = Task(
                title = binding.taskTitle.text,
                date = binding.boxDate.text,
                hour = binding.boxHour.text,
                desc = binding.tagStateDescription.text,
                id = intent.getIntExtra(TASK_ID, 0)
            )
            TaskDataSource.insertTask(task)
            setResult(Activity.RESULT_OK)
            finish()
        }
   }
    companion object {
        const val TASK_ID = "task_id"
    }
}