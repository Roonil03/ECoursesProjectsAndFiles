import * as React from "react";
import { createNativeStackNavigator } from "@react-navigation/native-stack";
import AsyncStorage from "@react-native-async-storage/async-storage";
import WelcomeScreen from "../screens/WelcomeScreen";
import SubscribeScreen from "../screens/SubscribeScreen";
import OnboardingScreen from "../screens/OnboardingScreen";
import HomeScreen from "../screens/HomeScreen";
import ProfileScreen from "../screens/ProfileScreen";

const Stack = createNativeStackNavigator();

const RootNavigator = () => {
  const [state, setState] = React.useState({
    isLoading: true,
    isOnboardingCompleted: false,
  });

  React.useEffect(() => {
    const checkOnboarding = async () => {
      try {
        const value = await AsyncStorage.getItem("isOnboardingCompleted");
        setState({
          isLoading: false,
          isOnboardingCompleted: value === "true",
        });
      } catch (e) {
        setState({
          isLoading: false,
          isOnboardingCompleted: false,
        });
      }
    };
    checkOnboarding();
  }, []);

  if (state.isLoading) {
    return null;
  }

  return (
    <Stack.Navigator
      initialRouteName={state.isOnboardingCompleted ? "Home" : "Onboarding"}
      screenOptions={{ headerTitleAlign: "center" }}
    >
      <Stack.Screen
        name="Onboarding"
        component={OnboardingScreen}
        options={{ headerShown: false }}
      />
      <Stack.Screen
        name="Home"
        component={HomeScreen}
        options={{ headerShown: false }}
      />
      <Stack.Screen
        name="Profile"
        component={ProfileScreen}
        options={{ title: "Profile" }}
      />
      <Stack.Screen name="Welcome" component={WelcomeScreen} />
      <Stack.Screen name="Subscribe" component={SubscribeScreen} />
    </Stack.Navigator>
  );
};

export default RootNavigator;
