import React, { useState, useEffect } from 'react';
import { api } from './services/api';
import PrioritySection from './components/PrioritySection';
import TagManager from './components/TagManager';
import './styles/App.css';

function App() {
  const [todos, setTodos] = useState([]);
  const [tags, setTags] = useState([]);
  const [loading, setLoading] = useState(true);
  const [newTodo, setNewTodo] = useState('');
  const [newPriority, setNewPriority] = useState('medium');
  const [selectedTags, setSelectedTags] = useState([]);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      setLoading(true);
      const data = await api.sync();
      setTodos(data.todos);
      setTags(data.tags);
    } catch (error) {
      console.error('Failed to load data:', error);
      alert('Failed to connect to server. Make sure the backend is running.');
    } finally {
      setLoading(false);
    }
  };

  const handleAddTodo = async (e) => {
    e.preventDefault();
    if (!newTodo.trim()) return;

    try {
      const todo = await api.createTodo(newTodo, newPriority, selectedTags);
      setTodos([todo, ...todos]);
      setNewTodo('');
      setSelectedTags([]);
    } catch (error) {
      console.error('Failed to create todo:', error);
      alert('Failed to create todo');
    }
  };

  const handleToggleTodo = async (todo) => {
    try {
      const updated = await api.updateTodo(todo.id, {
        ...todo,
        completed: !todo.completed,
        tags: todo.tags.map(t => t.id)
      });
      setTodos(todos.map(t => t.id === updated.id ? updated : t));
    } catch (error) {
      console.error('Failed to update todo:', error);
      alert('Failed to update todo');
    }
  };

  const handleDeleteTodo = async (id) => {
    try {
      await api.deleteTodo(id);
      setTodos(todos.filter(t => t.id !== id));
    } catch (error) {
      console.error('Failed to delete todo:', error);
      alert('Failed to delete todo');
    }
  };

  const handleCreateTag = async (name, color) => {
    try {
      const tag = await api.createTag(name, color);
      setTags([...tags, tag]);
    } catch (error) {
      console.error('Failed to create tag:', error);
      alert('Failed to create tag');
    }
  };

  const handleDeleteTag = async (id) => {
    try {
      await api.deleteTag(id);
      setTags(tags.filter(t => t.id !== id));
    } catch (error) {
      console.error('Failed to delete tag:', error);
      alert('Failed to delete tag');
    }
  };

  const handleSync = () => {
    loadData();
  };

  if (loading) {
    return <div className="loading">Loading...</div>;
  }

  return (
    <div className="app">
      <div className="header">
        <h1>Fennec Do</h1>
        <p>Your colorful, organized todo list</p>
      </div>

      <div className="container">
        <TagManager
          tags={tags}
          onCreateTag={handleCreateTag}
          onDeleteTag={handleDeleteTag}
        />

        <form className="add-todo-form" onSubmit={handleAddTodo}>
          <input
            type="text"
            placeholder="What needs to be done?"
            value={newTodo}
            onChange={(e) => setNewTodo(e.target.value)}
          />
          <select
            value={newPriority}
            onChange={(e) => setNewPriority(e.target.value)}
          >
            <option value="high">High Priority</option>
            <option value="medium">Medium Priority</option>
            <option value="low">Low Priority</option>
          </select>
          <button type="submit">Add Todo</button>
        </form>

        <PrioritySection
          priority="high"
          todos={todos}
          onToggle={handleToggleTodo}
          onDelete={handleDeleteTodo}
        />

        <PrioritySection
          priority="medium"
          todos={todos}
          onToggle={handleToggleTodo}
          onDelete={handleDeleteTodo}
        />

        <PrioritySection
          priority="low"
          todos={todos}
          onToggle={handleToggleTodo}
          onDelete={handleDeleteTodo}
        />
      </div>

      <button className="sync-button" onClick={handleSync} title="Sync">
        🔄
      </button>
    </div>
  );
}

export default App;
