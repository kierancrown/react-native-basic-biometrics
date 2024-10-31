import { useEffect, useState } from 'react';
import { StyleSheet, View, Text, Button, Alert } from 'react-native';
import {
  canAuthenticate,
  getBiometryType,
  requestBioAuth,
  type BiometryType,
} from 'react-native-basic-biometrics';

export default function App() {
  const [authenticated, setAuthenticated] = useState(false);
  const [canAuthenticateResult, setCanAuthenticateResult] = useState(false);
  const [biometricType, setBiometricType] = useState<BiometryType>('Unknown');

  useEffect(() => {
    canAuthenticate().then((result) => {
      setCanAuthenticateResult(result);
    });

    getBiometryType().then((type) => {
      setBiometricType(type);
    });
  }, []);

  const handleRequestBioAuth = async () => {
    try {
      const res = await requestBioAuth('Title', 'Subtitle');
      setAuthenticated(res);
    } catch (e) {
      const error = e as Error;
      Alert.alert('Error', error.message);
    }
  };

  return (
    <View style={styles.container}>
      {canAuthenticateResult ? (
        <>
          <Text>Biometric Type: {biometricType}</Text>
          <Button title="Authenticate" onPress={handleRequestBioAuth} />
          {authenticated && <Text>Authenticated</Text>}
        </>
      ) : (
        <Text>Biometric authentication is not available</Text>
      )}
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
