import express from 'express';
import { TodoModel, TagModel } from './models.js';

const router = express.Router();

// Tag Routes
router.get('/tags', (req, res) => {
  try {
    const tags = TagModel.getAll();
    res.json(tags);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
});

router.post('/tags', (req, res) => {
  try {
    const { name, color } = req.body;
    if (!name || !color) {
      return res.status(400).json({ error: 'Name and color are required' });
    }
    const tag = TagModel.create(name, color);
    res.status(201).json(tag);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
});

router.put('/tags/:id', (req, res) => {
  try {
    const { name, color } = req.body;
    if (!name || !color) {
      return res.status(400).json({ error: 'Name and color are required' });
    }
    const tag = TagModel.update(req.params.id, name, color);
    if (!tag) {
      return res.status(404).json({ error: 'Tag not found' });
    }
    res.json(tag);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
});

router.delete('/tags/:id', (req, res) => {
  try {
    TagModel.delete(req.params.id);
    res.status(204).send();
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
});

// Todo Routes
router.get('/todos', (req, res) => {
  try {
    const todos = TodoModel.getAll();
    res.json(todos);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
});

router.post('/todos', (req, res) => {
  try {
    const { title, priority, tags } = req.body;
    if (!title || !priority) {
      return res.status(400).json({ error: 'Title and priority are required' });
    }
    if (!['high', 'medium', 'low'].includes(priority)) {
      return res.status(400).json({ error: 'Priority must be high, medium, or low' });
    }
    const todo = TodoModel.create(title, priority, tags || []);
    res.status(201).json(todo);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
});

router.put('/todos/:id', (req, res) => {
  try {
    const { title, priority, completed, tags } = req.body;
    if (!title || !priority || completed === undefined) {
      return res.status(400).json({ error: 'Title, priority, and completed are required' });
    }
    if (!['high', 'medium', 'low'].includes(priority)) {
      return res.status(400).json({ error: 'Priority must be high, medium, or low' });
    }
    const todo = TodoModel.update(req.params.id, title, priority, completed, tags);
    if (!todo) {
      return res.status(404).json({ error: 'Todo not found' });
    }
    res.json(todo);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
});

router.delete('/todos/:id', (req, res) => {
  try {
    TodoModel.delete(req.params.id);
    res.status(204).send();
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
});

// Sync Route - Get all data
router.get('/sync', (req, res) => {
  try {
    const todos = TodoModel.getAll();
    const tags = TagModel.getAll();
    res.json({ todos, tags });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
});

export default router;
