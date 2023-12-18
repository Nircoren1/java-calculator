import React, { useState } from 'react';
import NumBtn from './NumBtn.js';

const Calculator = () => {
  const [display, setDisplay] = useState(null);
  const [active, setActive] = useState("");
  const calcActions = ['%', '/', '*', '+', '-'];

  const handleClick = (e, value) => {
    setDisplay(value);
    const targetValue = e.target.value;

    if (calcActions.includes(targetValue)) {
      setActive(targetValue);
    } else if (targetValue === 'C' || targetValue === '=') {
      setActive(null);
    }
  };

  const rows = [
    ['C',      '%', '/'],
    ['7', '8', '9', '*'],
    ['4', '5', '6', '-'],
    ['1', '2', '3', '+'],
    ['0',      '.', '=']
  ];

  return (
    <div className="calculator">
      <div className="calculator-display" data-testid="calculator-display">{display}</div>
      {rows.map((row, rowIndex) => (
        <div className="row" key={rowIndex}>
          {row.map((buttonValue, buttonIndex) => (
            <NumBtn
              key={buttonIndex}
              value={buttonValue}
              isActive={active === buttonValue && calcActions.includes(buttonValue)}
              handleClick={handleClick}
              classes={buttonValue === 'C' || buttonValue === '0' ? 'double-width' : ''}
            />
          ))}
        </div>
      ))}
    </div>
  );
};

export default Calculator;

