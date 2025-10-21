import React from 'react';

export default function TodoItem({ todo, onToggle, onDelete }) {
  return (
    <div className={`todo-item ${todo.completed ? 'completed' : ''}`}>
      <input
        type="checkbox"
        checked={todo.completed}
        onChange={() => onToggle(todo)}
      />
      <div className="todo-content">
        <div className="todo-title">{todo.title}</div>
        {todo.tags && todo.tags.length > 0 && (
          <div className="todo-tags">
            {todo.tags.map(tag => (
              <span
                key={tag.id}
                className="todo-tag"
                style={{ backgroundColor: tag.color }}
              >
                {tag.name}
              </span>
            ))}
          </div>
        )}
      </div>
      <button className="delete-btn" onClick={() => onDelete(todo.id)}>
        Delete
      </button>
    </div>
  );
}
