import db from './database.js';
import { v4 as uuidv4 } from 'uuid';

// Tag Model
export const TagModel = {
  getAll() {
    return db.prepare('SELECT * FROM tags ORDER BY created_at DESC').all();
  },

  getById(id) {
    return db.prepare('SELECT * FROM tags WHERE id = ?').get(id);
  },

  create(name, color) {
    const id = uuidv4();
    const now = Date.now();
    db.prepare(
      'INSERT INTO tags (id, name, color, created_at, updated_at) VALUES (?, ?, ?, ?, ?)'
    ).run(id, name, color, now, now);
    return this.getById(id);
  },

  update(id, name, color) {
    const now = Date.now();
    db.prepare(
      'UPDATE tags SET name = ?, color = ?, updated_at = ? WHERE id = ?'
    ).run(name, color, now, id);
    return this.getById(id);
  },

  delete(id) {
    db.prepare('DELETE FROM tags WHERE id = ?').run(id);
  }
};

// Todo Model
export const TodoModel = {
  getAll() {
    const todos = db.prepare('SELECT * FROM todos ORDER BY created_at DESC').all();
    return todos.map(todo => ({
      ...todo,
      completed: Boolean(todo.completed),
      tags: this.getTodoTags(todo.id)
    }));
  },

  getById(id) {
    const todo = db.prepare('SELECT * FROM todos WHERE id = ?').get(id);
    if (!todo) return null;
    return {
      ...todo,
      completed: Boolean(todo.completed),
      tags: this.getTodoTags(id)
    };
  },

  getTodoTags(todoId) {
    return db.prepare(`
      SELECT t.* FROM tags t
      JOIN todo_tags tt ON t.id = tt.tag_id
      WHERE tt.todo_id = ?
    `).all(todoId);
  },

  create(title, priority, tagIds = []) {
    const id = uuidv4();
    const now = Date.now();

    db.prepare(
      'INSERT INTO todos (id, title, priority, completed, created_at, updated_at) VALUES (?, ?, ?, 0, ?, ?)'
    ).run(id, title, priority, now, now);

    // Add tags
    if (tagIds.length > 0) {
      const stmt = db.prepare('INSERT INTO todo_tags (todo_id, tag_id) VALUES (?, ?)');
      for (const tagId of tagIds) {
        stmt.run(id, tagId);
      }
    }

    return this.getById(id);
  },

  update(id, title, priority, completed, tagIds) {
    const now = Date.now();
    db.prepare(
      'UPDATE todos SET title = ?, priority = ?, completed = ?, updated_at = ? WHERE id = ?'
    ).run(title, priority, completed ? 1 : 0, now, id);

    // Update tags
    if (tagIds !== undefined) {
      db.prepare('DELETE FROM todo_tags WHERE todo_id = ?').run(id);
      if (tagIds.length > 0) {
        const stmt = db.prepare('INSERT INTO todo_tags (todo_id, tag_id) VALUES (?, ?)');
        for (const tagId of tagIds) {
          stmt.run(id, tagId);
        }
      }
    }

    return this.getById(id);
  },

  delete(id) {
    db.prepare('DELETE FROM todos WHERE id = ?').run(id);
  }
};
