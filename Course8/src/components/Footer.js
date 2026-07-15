import React from 'react';

function Footer() {
  return (
    <footer className="footer" aria-label="Application Footer">
      <div className="footer-content">
        <section className="footer-brand" aria-label="Footer Brand">
          <h3>Little Lemon</h3>
          <p>Traditional Mediterranean recipes served with a modern twist in the heart of Chicago.</p>
        </section>

        <nav className="footer-nav" aria-label="Footer Navigation">
          <h4>Sitemap</h4>
          <ul>
            <li><a href="/">Home</a></li>
            <li><a href="#about">About</a></li>
            <li><a href="#menu">Menu</a></li>
            <li><a href="#booking">Reservations</a></li>
            <li><a href="#order-online">Order Online</a></li>
            <li><a href="#login">Login</a></li>
          </ul>
        </nav>

        <section id="contact" className="footer-contact" aria-label="Contact Information">
          <h4>Contact Us</h4>
          <address>
            <p>123 Main Street, Chicago, IL 60601</p>
            <p>Phone: <a href="tel:+13125550199">(312) 555-0199</a></p>
            <p>Email: <a href="mailto:contact@littlelemon.com">contact@littlelemon.com</a></p>
          </address>
        </section>

        <section className="footer-social" aria-label="Social Media">
          <h4>Connect With Us</h4>
          <ul>
            <li><a href="https://facebook.com" target="_blank" rel="noopener noreferrer">Facebook</a></li>
            <li><a href="https://instagram.com" target="_blank" rel="noopener noreferrer">Instagram</a></li>
            <li><a href="https://twitter.com" target="_blank" rel="noopener noreferrer">Twitter</a></li>
            <li><a href="https://youtube.com" target="_blank" rel="noopener noreferrer">YouTube</a></li>
          </ul>
        </section>
      </div>
      <div className="footer-bottom">
        <p>&copy; {new Date().getFullYear()} Little Lemon Restaurant. All rights reserved.</p>
      </div>
    </footer>
  );
}

export default Footer;
