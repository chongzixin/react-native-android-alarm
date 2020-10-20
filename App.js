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

  // TODO: read flatlist data from a file and use it to init the timestampList
  const [timestampList, setTimestampList] = useState([]);

  useEffect(() => {
    // TODO: this seems to be causing memory leaks
    const eventEmitter = new NativeEventEmitter(NativeModules.AlarmModule);
    if(!this.eventListener) {
      this.eventListener = eventEmitter.addListener('ALARM_EVENT', (event) => {
        // alarm has occurred. add the event to a state
        console.log("current event is: " + event); 
        setTimestampList(currentList => [event, ...currentList]);
        // TODO: write to a file
      });
    }
  });

  return (
    <View style={styles.screen}>
      <View style={styles.listContainer}>
        <FlatList 
          contentContainerStyle={styles.list}
          data={timestampList}
          renderItem={itemData => (<Text>{itemData.item}</Text>)}
          keyExtractor={item => item}
        />
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  screen: {
    flex: 1,
  },
  listContainer: {
    paddingHorizontal: 20,
  },
});

export default App;
