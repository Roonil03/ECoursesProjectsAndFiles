import * as React from 'react';
import { View, Text, Image, TextInput, Pressable, StyleSheet, KeyboardAvoidingView, Platform, ScrollView } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { validateEmail, validateName } from '../utils';

const OnboardingScreen = ({ navigation, route }) => {
  const [firstName, setFirstName] = React.useState('');
  const [lastName, setLastName] = React.useState('');
  const [email, setEmail] = React.useState('');

  const isFormValid = validateName(firstName) && validateName(lastName) && validateEmail(email);

  const handleRegister = async () => {
    if (!isFormValid) return;
    try {
      const userData = {
        firstName: firstName.trim(),
        lastName: lastName.trim(),
        email: email.trim(),
        isOnboardingCompleted: true,
      };
      await AsyncStorage.multiSet([
        ['firstName', userData.firstName],
        ['lastName', userData.lastName],
        ['email', userData.email],
        ['isOnboardingCompleted', 'true'],
      ]);
      if (route.params && typeof route.params.onComplete === 'function') {
        route.params.onComplete();
      } else {
        navigation.replace('Home');
      }
    } catch (e) {
      console.error('Failed to save onboarding data:', e);
    }
  };

  return (
    <KeyboardAvoidingView
      style={styles.container}
      behavior={Platform.OS === 'ios' ? 'padding' : 'height'}
    >
      <ScrollView contentContainerStyle={styles.scrollContainer} keyboardShouldPersistTaps="handled">
        <View style={styles.headerContainer}>
          <Image
            source={require('../assets/little-lemon-logo.png')}
            style={styles.logo}
            resizeMode="contain"
          />
        </View>

        <View style={styles.heroContainer}>
          <Text style={styles.heroTitle}>Let us get to know you</Text>
        </View>

        <View style={styles.formContainer}>
          <Text style={styles.label}>First Name</Text>
          <TextInput
            style={styles.input}
            value={firstName}
            onChangeText={setFirstName}
            placeholder="First Name"
            autoCapitalize="words"
          />

          <Text style={styles.label}>Last Name</Text>
          <TextInput
            style={styles.input}
            value={lastName}
            onChangeText={setLastName}
            placeholder="Last Name"
            autoCapitalize="words"
          />

          <Text style={styles.label}>Email Address</Text>
          <TextInput
            style={styles.input}
            value={email}
            onChangeText={setEmail}
            placeholder="Email Address"
            keyboardType="email-address"
            autoCapitalize="none"
          />
        </View>

        <View style={styles.buttonContainer}>
          <Pressable
            style={[styles.registerButton, !isFormValid && styles.disabledButton]}
            onPress={handleRegister}
            disabled={!isFormValid}
          >
            <Text style={styles.registerButtonText}>Next / Register</Text>
          </Pressable>
        </View>
      </ScrollView>
    </KeyboardAvoidingView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#FFFFFF',
  },
  scrollContainer: {
    flexGrow: 1,
    justifyContent: 'space-between',
  },
  headerContainer: {
    alignItems: 'center',
    paddingVertical: 20,
    backgroundColor: '#DEE3E9',
  },
  logo: {
    width: 180,
    height: 60,
  },
  heroContainer: {
    backgroundColor: '#495E57',
    paddingVertical: 30,
    paddingHorizontal: 24,
    alignItems: 'center',
  },
  heroTitle: {
    fontSize: 24,
    fontWeight: 'bold',
    color: '#FFFFFF',
    textAlign: 'center',
  },
  formContainer: {
    paddingHorizontal: 24,
    paddingVertical: 20,
  },
  label: {
    fontSize: 16,
    fontWeight: '600',
    color: '#333333',
    marginBottom: 8,
  },
  input: {
    width: '100%',
    height: 48,
    borderColor: '#333333',
    borderWidth: 1.5,
    borderRadius: 8,
    paddingHorizontal: 16,
    fontSize: 16,
    marginBottom: 20,
    color: '#333333',
  },
  buttonContainer: {
    paddingHorizontal: 24,
    paddingBottom: 36,
    alignItems: 'flex-end',
  },
  registerButton: {
    backgroundColor: '#F4CE14',
    paddingVertical: 14,
    paddingHorizontal: 36,
    borderRadius: 8,
    alignItems: 'center',
  },
  disabledButton: {
    backgroundColor: '#BDC3C7',
  },
  registerButtonText: {
    color: '#333333',
    fontSize: 18,
    fontWeight: 'bold',
  },
});

export default OnboardingScreen;
