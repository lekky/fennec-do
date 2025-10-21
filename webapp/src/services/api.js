const API_URL = 'http://localhost:3000/api';

export const api = {
  // Todos
  async getTodos() {
    const response = await fetch(`${API_URL}/todos`);
    return response.json();
  },

  async createTodo(title, priority, tags = []) {
    const response = await fetch(`${API_URL}/todos`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ title, priority, tags })
    });
    return response.json();
  },

  async updateTodo(id, data) {
    const response = await fetch(`${API_URL}/todos/${id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data)
    });
    return response.json();
  },

  async deleteTodo(id) {
    await fetch(`${API_URL}/todos/${id}`, { method: 'DELETE' });
  },

  // Tags
  async getTags() {
    const response = await fetch(`${API_URL}/tags`);
    return response.json();
  },

  async createTag(name, color) {
    const response = await fetch(`${API_URL}/tags`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ name, color })
    });
    return response.json();
  },

  async updateTag(id, name, color) {
    const response = await fetch(`${API_URL}/tags/${id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ name, color })
    });
    return response.json();
  },

  async deleteTag(id) {
    await fetch(`${API_URL}/tags/${id}`, { method: 'DELETE' });
  },

  // Sync
  async sync() {
    const response = await fetch(`${API_URL}/sync`);
    return response.json();
  }
};
