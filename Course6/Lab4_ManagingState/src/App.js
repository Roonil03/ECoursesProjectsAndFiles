import React, { useState } from "react";

function App() {
  const [giftCard, setGiftCard] = useState({
    customer: "Jennifer Smith",
    text: "Free dinner for 4 guests",
    valid: true,
    instructions: "To use your coupon, click the button below.",
  });

  function spendGiftCard() {
    setGiftCard((prevState) => {
      return {
        ...prevState,
        text: "Your coupon has been used.",
        valid: false,
        instructions: "Please visit our restaurant to renew your gift card.",
      };
    });
  }

  return (
    <div className="App">
      <h1>Gift Card Page</h1>
      <h2>Customer: {giftCard.customer}</h2>
      <p>{giftCard.text}</p>
      <p>{giftCard.instructions}</p>
      {giftCard.valid && (
        <button onClick={spendGiftCard}>Spend Gift Card</button>
      )}
    </div>
  );
}

export default App;
