import React from 'react';
import TodoItem from './TodoItem';

export default function PrioritySection({ priority, todos, onToggle, onDelete }) {
  const priorityLabels = {
    high: 'High Priority',
    medium: 'Medium Priority',
    low: 'Low Priority'
  };

  const filteredTodos = todos.filter(todo => todo.priority === priority);

  return (
    <div className="priority-section">
      <div className="priority-header">
        <span className={`priority-badge ${priority}`}>{priority.toUpperCase()}</span>
        <h2>{priorityLabels[priority]}</h2>
        <span className="priority-count">({filteredTodos.length})</span>
      </div>
      <div className="todo-list">
        {filteredTodos.length === 0 ? (
          <div className="empty-state">No todos in this priority</div>
        ) : (
          filteredTodos.map(todo => (
            <TodoItem
              key={todo.id}
              todo={todo}
              onToggle={onToggle}
              onDelete={onDelete}
            />
          ))
        )}
      </div>
    </div>
  );
}
