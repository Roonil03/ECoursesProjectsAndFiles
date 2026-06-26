const products = [
    ["Laptop", 1000],
    ["Phone", 500],
    ["Tablet", 700]
];
// Destructure the second product's name and price from the array
const [, [secondProductName, secondProductPrice]] = products;
console.log(`Second product: ${secondProductName}`);
console.log(`Second product price: ${secondProductPrice}`);
