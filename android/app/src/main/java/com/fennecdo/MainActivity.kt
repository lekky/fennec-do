package com.fennecdo

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fennecdo.adapter.TodoAdapter
import com.fennecdo.api.ApiService
import com.fennecdo.models.Todo
import com.fennecdo.repository.TodoRepository
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var repository: TodoRepository
    private lateinit var todoInput: TextInputEditText
    private lateinit var prioritySpinner: Spinner
    private lateinit var addButton: MaterialButton
    private lateinit var syncFab: FloatingActionButton

    private lateinit var highPriorityAdapter: TodoAdapter
    private lateinit var mediumPriorityAdapter: TodoAdapter
    private lateinit var lowPriorityAdapter: TodoAdapter

    private val allTodos = mutableListOf<Todo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize repository
        val apiService = ApiService.create()
        repository = TodoRepository(apiService)

        // Initialize views
        todoInput = findViewById(R.id.todoInput)
        prioritySpinner = findViewById(R.id.prioritySpinner)
        addButton = findViewById(R.id.addButton)
        syncFab = findViewById(R.id.syncFab)

        // Setup RecyclerViews
        setupRecyclerViews()

        // Setup listeners
        addButton.setOnClickListener {
            addTodo()
        }

        syncFab.setOnClickListener {
            syncData()
        }

        // Load initial data
        syncData()
    }

    private fun setupRecyclerViews() {
        val highRecyclerView: RecyclerView = findViewById(R.id.highPriorityRecyclerView)
        val mediumRecyclerView: RecyclerView = findViewById(R.id.mediumPriorityRecyclerView)
        val lowRecyclerView: RecyclerView = findViewById(R.id.lowPriorityRecyclerView)

        highPriorityAdapter = TodoAdapter(emptyList(), ::toggleTodo, ::deleteTodo)
        mediumPriorityAdapter = TodoAdapter(emptyList(), ::toggleTodo, ::deleteTodo)
        lowPriorityAdapter = TodoAdapter(emptyList(), ::toggleTodo, ::deleteTodo)

        highRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = highPriorityAdapter
        }

        mediumRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = mediumPriorityAdapter
        }

        lowRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = lowPriorityAdapter
        }
    }

    private fun addTodo() {
        val title = todoInput.text.toString().trim()
        if (title.isEmpty()) {
            Toast.makeText(this, "Please enter a todo", Toast.LENGTH_SHORT).show()
            return
        }

        val priorityIndex = prioritySpinner.selectedItemPosition
        val priority = when (priorityIndex) {
            0 -> "high"
            1 -> "medium"
            else -> "low"
        }

        lifecycleScope.launch {
            val result = repository.createTodo(title, priority)
            result.onSuccess { newTodo ->
                runOnUiThread {
                    allTodos.add(0, newTodo)
                    updateAdapters()
                    todoInput.text?.clear()
                    Toast.makeText(this@MainActivity, "Todo added!", Toast.LENGTH_SHORT).show()
                }
            }.onFailure { e ->
                runOnUiThread {
                    Toast.makeText(
                        this@MainActivity,
                        "Failed to add todo: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun toggleTodo(todo: Todo) {
        lifecycleScope.launch {
            val updatedTodo = todo.copy(completed = !todo.completed)
            val result = repository.updateTodo(updatedTodo)
            result.onSuccess { newTodo ->
                runOnUiThread {
                    val index = allTodos.indexOfFirst { it.id == newTodo.id }
                    if (index != -1) {
                        allTodos[index] = newTodo
                        updateAdapters()
                    }
                }
            }.onFailure { e ->
                runOnUiThread {
                    Toast.makeText(
                        this@MainActivity,
                        "Failed to update todo: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun deleteTodo(id: String) {
        lifecycleScope.launch {
            val result = repository.deleteTodo(id)
            result.onSuccess {
                runOnUiThread {
                    allTodos.removeIf { it.id == id }
                    updateAdapters()
                    Toast.makeText(this@MainActivity, "Todo deleted!", Toast.LENGTH_SHORT).show()
                }
            }.onFailure { e ->
                runOnUiThread {
                    Toast.makeText(
                        this@MainActivity,
                        "Failed to delete todo: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun syncData() {
        lifecycleScope.launch {
            val result = repository.sync()
            result.onSuccess { syncResponse ->
                runOnUiThread {
                    allTodos.clear()
                    allTodos.addAll(syncResponse.todos)
                    updateAdapters()
                    Toast.makeText(this@MainActivity, "Synced!", Toast.LENGTH_SHORT).show()
                }
            }.onFailure { e ->
                runOnUiThread {
                    Toast.makeText(
                        this@MainActivity,
                        "Failed to sync: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun updateAdapters() {
        highPriorityAdapter.updateTodos(allTodos.filter { it.priority == "high" })
        mediumPriorityAdapter.updateTodos(allTodos.filter { it.priority == "medium" })
        lowPriorityAdapter.updateTodos(allTodos.filter { it.priority == "low" })
    }
}
