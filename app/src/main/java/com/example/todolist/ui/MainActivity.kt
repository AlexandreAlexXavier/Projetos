package com.example.todolist.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.todolist.databinding.ActivityMainBinding
import com.example.todolist.datasource.TaskDataSource

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val adapter by lazy { TaskListAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvTasks.adapter = adapter
        updateList()

        insertListeners()
    }

    private fun insertListeners() {
        binding.btnAddTask.setOnClickListener {
            resultLauncher.launch(Intent(this, AddTaskActivity::class.java))
        }

        adapter.listenerEdit = {
            val intent = Intent(this, AddTaskActivity::class.java)
            intent.putExtra(AddTaskActivity.TASK_ID, it.id)
        }
        adapter.listenerDelete = {
            TaskDataSource.deleteTask(it)
            updateList()

        }
    }

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == CREATE_NEW_TASK) {
            binding.rvTasks.adapter = adapter
            updateList()
        }
    }
    private fun updateList() {
        val list = TaskDataSource.getList()
        binding.includeEmpty.emptyState.visibility = if(list.isEmpty()) View.VISIBLE
        else View.GONE

        adapter.submitList(TaskDataSource.getList())
    }

    companion object {
        private const val CREATE_NEW_TASK = 1000
    }
}