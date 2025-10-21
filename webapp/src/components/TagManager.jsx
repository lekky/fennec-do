import React, { useState } from 'react';

export default function TagManager({ tags, onCreateTag, onDeleteTag }) {
  const [tagName, setTagName] = useState('');
  const [tagColor, setTagColor] = useState('#667eea');

  const handleSubmit = (e) => {
    e.preventDefault();
    if (tagName.trim()) {
      onCreateTag(tagName, tagColor);
      setTagName('');
      setTagColor('#667eea');
    }
  };

  return (
    <div className="tag-management">
      <h3>Manage Tags</h3>
      <form className="tag-form" onSubmit={handleSubmit}>
        <input
          type="text"
          placeholder="Tag name"
          value={tagName}
          onChange={(e) => setTagName(e.target.value)}
        />
        <input
          type="color"
          value={tagColor}
          onChange={(e) => setTagColor(e.target.value)}
        />
        <button type="submit">Add Tag</button>
      </form>
      <div className="tag-list">
        {tags.map(tag => (
          <div
            key={tag.id}
            className="tag-item"
            style={{ backgroundColor: tag.color }}
          >
            {tag.name}
            <button onClick={() => onDeleteTag(tag.id)}>×</button>
          </div>
        ))}
      </div>
    </div>
  );
}
