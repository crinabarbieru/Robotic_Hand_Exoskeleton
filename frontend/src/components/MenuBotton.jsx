import React from 'react';
import '../stylesheets/MenuButton.css';

function MenuButton({ text, onClick}) {

  return (
    <button className="menu_button" onClick={ onClick }>
      {text}
    </button>
  );
}

export default MenuButton;
