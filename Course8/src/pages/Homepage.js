import React from 'react';

function Homepage() {
  return (
    <section className="homepage" aria-label="Restaurant Hero Banner">
      <article className="hero-banner">
        <header>
          <h2>Little Lemon Chicago</h2>
          <p>We are a family owned Mediterranean restaurant, focused on traditional recipes served with a modern twist.</p>
        </header>
        <div>
          <a href="#booking" className="cta-button">Reserve a Table</a>
        </div>
      </article>
    </section>
  );
}

export default Homepage;
