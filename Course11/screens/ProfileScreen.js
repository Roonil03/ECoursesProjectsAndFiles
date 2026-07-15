import * as React from 'react';
import { View, Text, Image, TextInput, Pressable, ScrollView, StyleSheet, Alert } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';

const ProfileScreen = ({ navigation, route }) => {
  const [firstName, setFirstName] = React.useState('');
  const [lastName, setLastName] = React.useState('');
  const [email, setEmail] = React.useState('');
  const [phone, setPhone] = React.useState('');

  const [orderStatuses, setOrderStatuses] = React.useState(true);
  const [passwordChanges, setPasswordChanges] = React.useState(true);
  const [specialOffers, setSpecialOffers] = React.useState(true);
  const [newsletter, setNewsletter] = React.useState(true);

  const loadProfile = React.useCallback(async () => {
    try {
      const storedFirstName = await AsyncStorage.getItem('firstName');
      const storedLastName = await AsyncStorage.getItem('lastName');
      const storedEmail = await AsyncStorage.getItem('email');
      const storedPhone = await AsyncStorage.getItem('phone');

      if (storedFirstName) setFirstName(storedFirstName);
      if (storedLastName) setLastName(storedLastName);
      if (storedEmail) setEmail(storedEmail);
      if (storedPhone) setPhone(storedPhone);
    } catch (e) {
      console.error('Failed to load profile:', e);
    }
  }, []);

  React.useEffect(() => {
    loadProfile();
  }, [loadProfile]);

  const handleSaveChanges = async () => {
    try {
      await AsyncStorage.multiSet([
        ['firstName', firstName.trim()],
        ['lastName', lastName.trim()],
        ['email', email.trim()],
        ['phone', phone.trim()],
      ]);
      Alert.alert('Success', 'Profile changes saved successfully.');
    } catch (e) {
      console.error('Failed to save profile changes:', e);
      Alert.alert('Error', 'Could not save profile changes.');
    }
  };

  const handleDiscardChanges = () => {
    loadProfile();
  };

  const handleLogout = async () => {
    try {
      await AsyncStorage.clear();
      if (route.params && typeof route.params.onLogout === 'function') {
        route.params.onLogout();
      } else {
        navigation.reset({
          index: 0,
          routes: [{ name: 'Onboarding' }],
        });
      }
    } catch (e) {
      console.error('Failed to logout:', e);
    }
  };

  return (
    <ScrollView style={styles.container} contentContainerStyle={styles.content}>
      <Text style={styles.sectionTitle}>Personal information</Text>

      <View style={styles.avatarSection}>
        <Text style={styles.avatarLabel}>Avatar</Text>
        <View style={styles.avatarControls}>
          <Image
            source={require('../assets/profile.png')}
            style={styles.avatarImage}
          />
          <Pressable style={styles.changeButton}>
            <Text style={styles.changeButtonText}>Change</Text>
          </Pressable>
          <Pressable style={styles.removeButton}>
            <Text style={styles.removeButtonText}>Remove</Text>
          </Pressable>
        </View>
      </View>

      <Text style={styles.label}>First Name</Text>
      <TextInput
        style={styles.input}
        value={firstName}
        onChangeText={setFirstName}
        placeholder="First Name"
      />

      <Text style={styles.label}>Last Name</Text>
      <TextInput
        style={styles.input}
        value={lastName}
        onChangeText={setLastName}
        placeholder="Last Name"
      />

      <Text style={styles.label}>Email</Text>
      <TextInput
        style={styles.input}
        value={email}
        onChangeText={setEmail}
        placeholder="Email"
        keyboardType="email-address"
        autoCapitalize="none"
      />

      <Text style={styles.label}>Phone number</Text>
      <TextInput
        style={styles.input}
        value={phone}
        onChangeText={setPhone}
        placeholder="Phone number"
        keyboardType="phone-pad"
      />

      <Text style={styles.sectionTitle}>Email notifications</Text>

      <Pressable style={styles.checkboxRow} onPress={() => setOrderStatuses(!orderStatuses)}>
        <View style={[styles.checkbox, orderStatuses && styles.checkboxChecked]}>
          {orderStatuses && <Text style={styles.checkmark}>✓</Text>}
        </View>
        <Text style={styles.checkboxLabel}>Order statuses</Text>
      </Pressable>

      <Pressable style={styles.checkboxRow} onPress={() => setPasswordChanges(!passwordChanges)}>
        <View style={[styles.checkbox, passwordChanges && styles.checkboxChecked]}>
          {passwordChanges && <Text style={styles.checkmark}>✓</Text>}
        </View>
        <Text style={styles.checkboxLabel}>Password changes</Text>
      </Pressable>

      <Pressable style={styles.checkboxRow} onPress={() => setSpecialOffers(!specialOffers)}>
        <View style={[styles.checkbox, specialOffers && styles.checkboxChecked]}>
          {specialOffers && <Text style={styles.checkmark}>✓</Text>}
        </View>
        <Text style={styles.checkboxLabel}>Special offers</Text>
      </Pressable>

      <Pressable style={styles.checkboxRow} onPress={() => setNewsletter(!newsletter)}>
        <View style={[styles.checkbox, newsletter && styles.checkboxChecked]}>
          {newsletter && <Text style={styles.checkmark}>✓</Text>}
        </View>
        <Text style={styles.checkboxLabel}>Newsletter</Text>
      </Pressable>

      <Pressable style={styles.logoutButton} onPress={handleLogout}>
        <Text style={styles.logoutButtonText}>Log out</Text>
      </Pressable>

      <View style={styles.actionsRow}>
        <Pressable style={styles.discardButton} onPress={handleDiscardChanges}>
          <Text style={styles.discardButtonText}>Discard changes</Text>
        </Pressable>
        <Pressable style={styles.saveButton} onPress={handleSaveChanges}>
          <Text style={styles.saveButtonText}>Save changes</Text>
        </Pressable>
      </View>
    </ScrollView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#FFFFFF',
  },
  content: {
    padding: 20,
    paddingBottom: 40,
  },
  sectionTitle: {
    fontSize: 20,
    fontWeight: 'bold',
    color: '#333333',
    marginVertical: 16,
  },
  avatarSection: {
    marginBottom: 20,
  },
  avatarLabel: {
    fontSize: 14,
    color: '#333333',
    marginBottom: 8,
  },
  avatarControls: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  avatarImage: {
    width: 70,
    height: 70,
    borderRadius: 35,
    marginRight: 16,
  },
  changeButton: {
    backgroundColor: '#495E57',
    paddingVertical: 10,
    paddingHorizontal: 16,
    borderRadius: 8,
    marginRight: 12,
  },
  changeButtonText: {
    color: '#FFFFFF',
    fontWeight: 'bold',
  },
  removeButton: {
    backgroundColor: '#FFFFFF',
    paddingVertical: 10,
    paddingHorizontal: 16,
    borderRadius: 8,
    borderWidth: 1,
    borderColor: '#495E57',
  },
  removeButtonText: {
    color: '#495E57',
    fontWeight: 'bold',
  },
  label: {
    fontSize: 14,
    fontWeight: '600',
    color: '#333333',
    marginBottom: 6,
  },
  input: {
    width: '100%',
    height: 44,
    borderColor: '#CCCCCC',
    borderWidth: 1,
    borderRadius: 8,
    paddingHorizontal: 12,
    fontSize: 16,
    marginBottom: 16,
    color: '#333333',
  },
  checkboxRow: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 16,
  },
  checkbox: {
    width: 22,
    height: 22,
    borderWidth: 1.5,
    borderColor: '#495E57',
    borderRadius: 4,
    marginRight: 12,
    justifyContent: 'center',
    alignItems: 'center',
  },
  checkboxChecked: {
    backgroundColor: '#495E57',
  },
  checkmark: {
    color: '#FFFFFF',
    fontSize: 14,
    fontWeight: 'bold',
  },
  checkboxLabel: {
    fontSize: 16,
    color: '#333333',
  },
  logoutButton: {
    backgroundColor: '#F4CE14',
    paddingVertical: 14,
    borderRadius: 8,
    alignItems: 'center',
    marginVertical: 24,
    borderWidth: 1,
    borderColor: '#EE9972',
  },
  logoutButtonText: {
    color: '#333333',
    fontSize: 16,
    fontWeight: 'bold',
  },
  actionsRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
  },
  discardButton: {
    flex: 1,
    backgroundColor: '#FFFFFF',
    paddingVertical: 14,
    borderRadius: 8,
    alignItems: 'center',
    borderWidth: 1,
    borderColor: '#495E57',
    marginRight: 8,
  },
  discardButtonText: {
    color: '#495E57',
    fontSize: 16,
    fontWeight: 'bold',
  },
  saveButton: {
    flex: 1,
    backgroundColor: '#495E57',
    paddingVertical: 14,
    borderRadius: 8,
    alignItems: 'center',
    marginLeft: 8,
  },
  saveButtonText: {
    color: '#FFFFFF',
    fontSize: 16,
    fontWeight: 'bold',
  },
});

export default ProfileScreen;
