export const validateEmail = (email) => {
  return email.match(
    /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
  );
};

export const validateName = (name) => {
  return typeof name === 'string' && name.trim().length > 0 && /^[a-zA-Z\s]+$/.test(name.trim());
};

export const validatePhone = (phone) => {
  return typeof phone === 'string' && /^[0-9+()-\s]*$/.test(phone);
};
