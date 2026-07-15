// Task 1
var dairy = ['cheese', 'sour cream', 'milk', 'yogurt', 'ice cream', 'milkshake'];

function logDairy() {
    for (const item of dairy) {
        console.log(item);
    }
}
logDairy();

// Starter code for Task 2 and Task 3
const animal = {
    canJump: true
};

const bird = Object.create(animal);
bird.canFly = true;
bird.hasFeathers = true;

// Task 2
function birdCan() {
    for (const [key, value] of Object.entries(bird)) {
        console.log(`${key}: ${value}`);
    }
}
birdCan();

// Task 3
function animalCan() {
    for (const prop in bird) {
        console.log(`${prop}: ${bird[prop]}`);
    }
}
animalCan();