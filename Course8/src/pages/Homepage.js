import React from 'react';

function Homepage() {
  return (
    <section className="homepage" aria-label="Restaurant Hero Banner">
      <div className="hero-banner">
        <div>
          <h2>Little Lemon Chicago</h2>
          <p>We are a family owned Mediterranean restaurant, focused on traditional recipes served with a modern twist.</p>
        </div>
        <div>
          <a href="#booking" className="cta-button">Reserve a Table</a>
        </div>
      </div>
    </section>
  );
}

export default Homepage;
