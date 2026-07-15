import * as React from 'react';
import { View, Text, Image, TextInput, Pressable, Alert, StyleSheet } from 'react-native';
import { validateEmail } from '../utils';

const SubscribeScreen = () => {
  const [email, setEmail] = React.useState('');
  const isEmailValid = validateEmail(email);

  const handleSubscribe = () => {
    Alert.alert("Thanks for subscribing, stay tuned!");
    setEmail('');
  };

  return (
    <View style={styles.container}>
      <Image
        source={require('../assets/little-lemon-logo.png')}
        style={styles.logo}
        resizeMode="contain"
      />
      <Text style={styles.subscribeText}>
        Subscribe to our newsletter for our latest delicious recipes!
      </Text>
      
      <TextInput
        style={styles.input}
        value={email}
        onChangeText={setEmail}
        placeholder="Type your email"
        keyboardType="email-address"
        autoCapitalize="none"
      />
      
      <Pressable
        style={[styles.button, !isEmailValid && styles.disabledButton]}
        onPress={handleSubscribe}
        disabled={!isEmailValid}
      >
        <Text style={styles.buttonText}>Subscribe</Text>
      </Pressable>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 24,
    backgroundColor: '#fff',
    alignItems: 'center',
  },
  logo: {
    width: 120,
    height: 120,
    marginTop: 20,
    marginBottom: 20,
  },
  subscribeText: {
    fontSize: 18,
    color: '#333333',
    textAlign: 'center',
    marginBottom: 24,
    paddingHorizontal: 10,
  },
  input: {
    width: '100%',
    height: 44,
    borderColor: '#000000',
    borderWidth: 1,
    borderRadius: 8,
    paddingHorizontal: 12,
    fontSize: 16,
    marginBottom: 20,
    color: '#333333',
  },
  button: {
    backgroundColor: '#495E57',
    paddingVertical: 12,
    borderRadius: 8,
    alignItems: 'center',
    width: '100%',
  },
  disabledButton: {
    backgroundColor: '#BDBDBD',
  },
  buttonText: {
    color: '#FFFFFF',
    fontSize: 16,
    fontWeight: 'bold',
  },
});

export default SubscribeScreen;
