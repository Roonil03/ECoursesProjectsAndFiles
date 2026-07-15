# Little Lemon Mobile Application

A cross-platform mobile application built for **Little Lemon** using **React Native** and **Expo**.

---

## Home Screen Interface

Below is the layout capture of the completed Little Lemon Home Screen featuring the header avatar, hero banner, search bar, interactive category filters (`Starters`, `Mains`, `Desserts`, `Drinks`), and the vertically scrollable food menu list:

![Little Lemon Home Screen](./homescreen.png)

---

## Project Structure

```text
├── assets/
│   ├── little-lemon-logo.png
│   ├── profile.png
│   ├── icon.png
│   └── splash.png
├── navigators/
│   └── RootNavigator.js       # Stack navigator (Onboarding, Home, Profile, Welcome, Subscribe)
├── screens/
│   ├── OnboardingScreen.js    # Registration and onboarding flow
│   ├── HomeScreen.js          # Hero banner, search, filters, menu FlatList
│   ├── ProfileScreen.js       # User profile details and persistence management
│   ├── WelcomeScreen.js       # Welcome landing screen
│   └── SubscribeScreen.js     # Newsletter subscription intake window
├── utils/
│   └── index.js               # Validation utilities (validateEmail, validateName, validatePhone)
├── App.js                     # Root component with NavigationContainer
├── app.json                   # Expo application configuration
├── babel.config.js            # Babel preset configuration
├── package.json               # Dependencies and scripts
├── homescreen.png             # Capture of the completed Home Screen layout
└── README.md                  # Project documentation
```

---

## Getting Started

### Prerequisites
- [Node.js](https://nodejs.org/) (v16+)
- [Expo CLI](https://docs.expo.dev/get-started/installation/)

### Installation & Execution

1. **Install Dependencies:**
   ```bash
   npm install
   ```

2. **Start the Expo Dev Server:**
   ```bash
   npm start
   ```

3. **Run on Target Device / Emulator:**
   - Press `a` to run on Android Emulator.
   - Press `i` to run on iOS Simulator.
   - Or scan the QR code with the **Expo Go** mobile app on your physical device.

