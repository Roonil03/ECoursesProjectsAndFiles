import React from 'react';

function Footer() {
  return (
    <footer aria-label="Application Footer">
      <p>&copy; {new Date().getFullYear()} Little Lemon Restaurant. All rights reserved.</p>
    </footer>
  );
}

export default Footer;
