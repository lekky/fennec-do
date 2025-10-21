package com.fennecdo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fennecdo.adapter.TodoAdapter
import com.fennecdo.database.AppDatabase
import com.fennecdo.database.TodoWithTags
import com.fennecdo.repository.LocalTodoRepository
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import android.widget.Spinner

class MainActivity : AppCompatActivity() {

    private lateinit var repository: LocalTodoRepository
    private lateinit var todoInput: TextInputEditText
    private lateinit var prioritySpinner: Spinner
    private lateinit var addButton: MaterialButton
    private lateinit var refreshFab: FloatingActionButton

    private lateinit var highPriorityAdapter: TodoAdapter
    private lateinit var mediumPriorityAdapter: TodoAdapter
    private lateinit var lowPriorityAdapter: TodoAdapter

    private val allTodos = mutableListOf<TodoWithTags>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize database and repository
        val database = AppDatabase.getDatabase(applicationContext)
        repository = LocalTodoRepository(
            database.todoDao(),
            database.tagDao(),
            database.todoTagDao()
        )

        // Initialize views
        todoInput = findViewById(R.id.todoInput)
        prioritySpinner = findViewById(R.id.prioritySpinner)
        addButton = findViewById(R.id.addButton)
        refreshFab = findViewById(R.id.syncFab)

        // Setup RecyclerViews
        setupRecyclerViews()

        // Setup listeners
        addButton.setOnClickListener {
            addTodo()
        }

        refreshFab.setOnClickListener {
            loadData()
        }

        // Load initial data
        loadData()
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
            try {
                val newTodo = repository.createTodo(title, priority)
                val todoWithTags = TodoWithTags(newTodo, emptyList())

                runOnUiThread {
                    allTodos.add(0, todoWithTags)
                    updateAdapters()
                    todoInput.text?.clear()
                    Toast.makeText(this@MainActivity, "Todo added!", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
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

    private fun toggleTodo(todoWithTags: TodoWithTags) {
        lifecycleScope.launch {
            try {
                val updatedTodo = todoWithTags.todo.copy(
                    completed = !todoWithTags.todo.completed,
                    updatedAt = System.currentTimeMillis()
                )
                repository.updateTodo(updatedTodo, todoWithTags.tags.map { it.id })

                runOnUiThread {
                    val index = allTodos.indexOfFirst { it.todo.id == updatedTodo.id }
                    if (index != -1) {
                        allTodos[index] = TodoWithTags(updatedTodo, todoWithTags.tags)
                        updateAdapters()
                    }
                }
            } catch (e: Exception) {
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

    private fun deleteTodo(todoId: String) {
        lifecycleScope.launch {
            try {
                repository.deleteTodo(todoId)

                runOnUiThread {
                    allTodos.removeIf { it.todo.id == todoId }
                    updateAdapters()
                    Toast.makeText(this@MainActivity, "Todo deleted!", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
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

    private fun loadData() {
        lifecycleScope.launch {
            try {
                val todos = repository.getAllTodosWithTags()

                runOnUiThread {
                    allTodos.clear()
                    allTodos.addAll(todos)
                    updateAdapters()
                    Toast.makeText(this@MainActivity, "Refreshed!", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(
                        this@MainActivity,
                        "Failed to load data: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun updateAdapters() {
        highPriorityAdapter.updateTodos(allTodos.filter { it.todo.priority == "high" })
        mediumPriorityAdapter.updateTodos(allTodos.filter { it.todo.priority == "medium" })
        lowPriorityAdapter.updateTodos(allTodos.filter { it.todo.priority == "low" })
    }
}
