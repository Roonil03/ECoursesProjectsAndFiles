import React from 'react';
import Navbar from './components/Navbar';
import Footer from './components/Footer';
import BookingPage from './pages/BookingPage';
import './styles.css';

function App() {
  return (
    <>
      <Navbar />
      <main>
        <BookingPage />
      </main>
      <Footer />
    </>
  );
}

export default App;
