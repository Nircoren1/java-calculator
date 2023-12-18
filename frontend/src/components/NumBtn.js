import React from 'react';

const numBtn = ({handleClick, value, classes, isActive }) => {
  // Access the passed data using props

  const btnClicked = async (e,value) => {
      try {
        const response = await fetch('http://localhost:8080/calc', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({ value }),
        });

         if (!response.ok) {
            throw new Error('Network response was not ok');
         }
        const result = await response.text();
        handleClick(e,result);
        if (isActive) {

        }


      } catch (error) {
        console.error('Error:', error);
      }
    };

  return (
      <button
      value = {value}
      className={`num-btn ${classes} btn-${value} btn-${isActive} ? 'active' : ''}`}
      onClick={(e) => btnClicked(e,value)}
      data-testid={`btn-${value}`}
      >
      {value}
      </button>

  );
}

export default numBtn;
