/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow strict-local
 */

import React, { useEffect, useState } from 'react';
import {
  StyleSheet,
  View,
  Text,
  FlatList,
  NativeModules,
  NativeEventEmitter,
} from 'react-native';

const App = () => {
  const [deviceName, setDeviceName] = useState('');
  NativeModules.Device.getDeviceName((err, name) => setDeviceName(name));

  useEffect(() => {
    const eventEmitter = new NativeEventEmitter(NativeModules.AlarmModule);
    if(!this.eventListener) {
      this.eventListener = eventEmitter.addListener('ALARM_EVENT', (event) => {
        console.log(event); 

        // alarm has occurred. add the event to a state
      });
    }
  });

  return (
    <View style={styles.screen}>
      <Text>We are testing Alarm here from {deviceName}</Text>
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
