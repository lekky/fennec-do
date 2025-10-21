package com.fennecdo.adapter

import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fennecdo.R
import com.fennecdo.models.Todo
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class TodoAdapter(
    private var todos: List<Todo>,
    private val onToggle: (Todo) -> Unit,
    private val onDelete: (String) -> Unit
) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    class TodoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val checkbox: CheckBox = view.findViewById(R.id.todoCheckbox)
        val title: TextView = view.findViewById(R.id.todoTitle)
        val tagsGroup: ChipGroup = view.findViewById(R.id.todoTagsGroup)
        val deleteButton: MaterialButton = view.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_todo, parent, false)
        return TodoViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val todo = todos[position]

        holder.checkbox.isChecked = todo.completed
        holder.title.text = todo.title

        // Apply strikethrough if completed
        if (todo.completed) {
            holder.title.paintFlags = holder.title.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            holder.title.paintFlags = holder.title.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }

        // Clear and add tags
        holder.tagsGroup.removeAllViews()
        todo.tags.forEach { tag ->
            val chip = Chip(holder.itemView.context).apply {
                text = tag.name
                chipBackgroundColor = android.content.res.ColorStateList.valueOf(
                    Color.parseColor(tag.color)
                )
                setTextColor(Color.WHITE)
            }
            holder.tagsGroup.addView(chip)
        }

        holder.checkbox.setOnCheckedChangeListener { _, _ ->
            onToggle(todo)
        }

        holder.deleteButton.setOnClickListener {
            onDelete(todo.id)
        }
    }

    override fun getItemCount() = todos.size

    fun updateTodos(newTodos: List<Todo>) {
        todos = newTodos
        notifyDataSetChanged()
    }
}
