import { render, screen, fireEvent } from '@testing-library/react';
import '@testing-library/jest-dom';
import BookingForm from '../components/BookingForm';

describe('BookingForm Validation', () => {
  test('renders form input fields correctly', () => {
    render(<BookingForm />);
    expect(screen.getByLabelText(/Full Name/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/Email Address/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/Phone Number/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/Choose Date/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/Choose Time/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/Number of Guests/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/Occasion/i)).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /Book Now/i })).toBeInTheDocument();
  });

  test('displays error triggers when required input data fields are blank', () => {
    render(<BookingForm />);
    const submitButton = screen.getByRole('button', { name: /Book Now/i });
    fireEvent.click(submitButton);
    
    expect(screen.getByText(/Full name is required/i)).toBeInTheDocument();
    expect(screen.getByText(/Please provide a valid email address/i)).toBeInTheDocument();
    expect(screen.getByText(/Phone number is required/i)).toBeInTheDocument();
    expect(screen.getByText(/Please select a valid future date/i)).toBeInTheDocument();
    expect(screen.getByText(/Please choose a dining time/i)).toBeInTheDocument();
  });

  test('submits reservation form successfully when data payload satisfies criteria', () => {
    render(<BookingForm />);
    
    fireEvent.change(screen.getByLabelText(/Full Name/i), { target: { value: 'Alex Mercer' } });
    fireEvent.change(screen.getByLabelText(/Email Address/i), { target: { value: 'alex@example.com' } });
    fireEvent.change(screen.getByLabelText(/Phone Number/i), { target: { value: '5551234567' } });
    fireEvent.change(screen.getByLabelText(/Choose Date/i), { target: { value: '2026-12-31' } });
    fireEvent.change(screen.getByLabelText(/Choose Time/i), { target: { value: '19:00' } });
    fireEvent.change(screen.getByLabelText(/Number of Guests/i), { target: { value: '4' } });

    fireEvent.click(screen.getByRole('button', { name: /Book Now/i }));
    expect(screen.getByRole('heading', { name: /Booking Confirmed!/i })).toBeInTheDocument();
  });
});
