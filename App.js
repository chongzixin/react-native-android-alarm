/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow strict-local
 */

import React, { useState } from 'react';
import {
  StyleSheet,
  ScrollView,
  View,
  Text,
  NativeModules,
} from 'react-native';

const App = () => {
  const [deviceName, setDeviceName] = useState('');
  NativeModules.Device.getDeviceName((err, name) => setDeviceName(name));

  return (
    <View style={styles.screen}>
      <Text>We are starting here from {deviceName}</Text>
    </View>
  );
};

const styles = StyleSheet.create({
  screen: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
  }
});

export default App;
