import React, { useState } from 'react';

function BookingForm() {
  const [formData, setFormData] = useState({
    name: '', email: '', phone: '', date: '', time: '', guests: 1, occasion: 'None'
  });
  const [errors, setErrors] = useState({});
  const [submitted, setSubmitted] = useState(false);

  const validate = (data = formData) => {
    const newErrors = {};
    const today = new Date().toISOString().split('T')[0];

    if (!data.name.trim()) newErrors.name = 'Full name is required.';
    if (!data.email.match(/^[\w-.]+@([\w-]+\.)+[\w-]{2,4}$/)) newErrors.email = 'Please provide a valid email address.';
    if (!data.phone.trim()) newErrors.phone = 'Phone number is required.';
    if (!data.date || data.date < today) newErrors.date = 'Please select a valid future date.';
    if (!data.time) newErrors.time = 'Please choose a dining time.';
    if (data.guests < 1 || data.guests > 10) newErrors.guests = 'Number of guests must be between 1 and 10.';

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    const parsedValue = name === 'guests' ? (value ? parseInt(value, 10) : '') : value;
    const updatedData = { ...formData, [name]: parsedValue };
    setFormData(updatedData);

    if (Object.keys(errors).length > 0 || name === 'date' || name === 'guests') {
      const newErrors = { ...errors };
      const today = new Date().toISOString().split('T')[0];

      if (name === 'name') {
        if (!updatedData.name.trim()) newErrors.name = 'Full name is required.';
        else delete newErrors.name;
      } else if (name === 'email') {
        if (!updatedData.email.match(/^[\w-.]+@([\w-]+\.)+[\w-]{2,4}$/)) newErrors.email = 'Please provide a valid email address.';
        else delete newErrors.email;
      } else if (name === 'phone') {
        if (!updatedData.phone.trim()) newErrors.phone = 'Phone number is required.';
        else delete newErrors.phone;
      } else if (name === 'date') {
        if (!updatedData.date || updatedData.date < today) newErrors.date = 'Please select a valid future date.';
        else delete newErrors.date;
      } else if (name === 'time') {
        if (!updatedData.time) newErrors.time = 'Please choose a dining time.';
        else delete newErrors.time;
      } else if (name === 'guests') {
        if (updatedData.guests < 1 || updatedData.guests > 10) newErrors.guests = 'Number of guests must be between 1 and 10.';
        else delete newErrors.guests;
      }
      setErrors(newErrors);
    }
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (validate(formData)) {
      setSubmitted(true);
    }
  };

  if (submitted) {
    return (
      <section className="success-message" aria-live="polite">
        <h2>Booking Confirmed!</h2>
        <p>Thank you, {formData.name}. Your table for {formData.guests} on {formData.date} at {formData.time} is reserved.</p>
      </section>
    );
  }

  const isSubmitDisabled = Object.keys(errors).length > 0;

  return (
    <form onSubmit={handleSubmit} noValidate aria-label="Table Reservation Form">
      <fieldset className="form-fieldset">
        <legend><h2>Reserve a Table</h2></legend>
        
        <div className="form-group">
          <label htmlFor="name">Full Name</label>
          <input type="text" id="name" name="name" value={formData.name} onChange={handleChange} aria-required="true" aria-invalid={!!errors.name} />
          {errors.name && <span className="error" role="alert">{errors.name}</span>}
        </div>

        <div className="form-group">
          <label htmlFor="email">Email Address</label>
          <input type="email" id="email" name="email" value={formData.email} onChange={handleChange} aria-required="true" aria-invalid={!!errors.email} />
          {errors.email && <span className="error" role="alert">{errors.email}</span>}
        </div>

        <div className="form-group">
          <label htmlFor="phone">Phone Number</label>
          <input type="tel" id="phone" name="phone" value={formData.phone} onChange={handleChange} aria-required="true" aria-invalid={!!errors.phone} />
          {errors.phone && <span className="error" role="alert">{errors.phone}</span>}
        </div>

        <div className="form-group">
          <label htmlFor="date">Choose Date</label>
          <input type="date" id="date" name="date" value={formData.date} onChange={handleChange} aria-required="true" aria-invalid={!!errors.date} />
          {errors.date && <span className="error" role="alert">{errors.date}</span>}
        </div>

        <div className="form-group">
          <label htmlFor="time">Choose Time</label>
          <select id="time" name="time" value={formData.time} onChange={handleChange} aria-required="true" aria-invalid={!!errors.time}>
            <option value="">Select Time</option>
            <option value="17:00">5:00 PM</option>
            <option value="18:00">6:00 PM</option>
            <option value="19:00">7:00 PM</option>
            <option value="20:00">8:00 PM</option>
            <option value="21:00">9:00 PM</option>
          </select>
          {errors.time && <span className="error" role="alert">{errors.time}</span>}
        </div>

        <div className="form-group">
          <label htmlFor="guests">Number of Guests</label>
          <input type="number" id="guests" name="guests" min="1" max="10" value={formData.guests} onChange={handleChange} aria-required="true" aria-invalid={!!errors.guests} />
          {errors.guests && <span className="error" role="alert">{errors.guests}</span>}
        </div>

        <div className="form-group">
          <label htmlFor="occasion">Occasion</label>
          <select id="occasion" name="occasion" value={formData.occasion} onChange={handleChange}>
            <option value="None">None</option>
            <option value="Birthday">Birthday</option>
            <option value="Anniversary">Anniversary</option>
          </select>
        </div>

        <button type="submit" disabled={isSubmitDisabled} aria-label="Submit Reservation - Book Now">Book Now</button>
      </fieldset>
    </form>
  );
}

export default BookingForm;
