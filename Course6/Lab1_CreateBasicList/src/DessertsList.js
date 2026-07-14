import React from 'react';

function DessertsList(props) {
  const lowCalorieDesserts = props.data
    .filter((dessert) => dessert.calories < 500)
    .sort((a, b) => a.calories - b.calories)
    .map((dessert) => {
      return (
        <li key={dessert.name}>
          {dessert.name} - {dessert.calories} cal
        </li>
      );
    });

  return <ul>{lowCalorieDesserts}</ul>;
}

export default DessertsList;