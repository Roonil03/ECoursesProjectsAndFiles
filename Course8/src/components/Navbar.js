import React from 'react';

function Navbar() {
  return (
    <header className="navbar" aria-label="Application Header">
      <h1>Little Lemon</h1>
      <nav aria-label="Main Navigation">
        <ul className="nav-links">
          <li><a href="/">Home</a></li>
          <li><a href="#booking">Book a Table</a></li>
          <li><a href="#menu">Menu</a></li>
          <li><a href="#contact">Contact</a></li>
        </ul>
      </nav>
    </header>
  );
}

export default Navbar;
