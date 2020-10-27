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
  PermissionsAndroid,
  Button,
} from 'react-native';

import { createStore, combineReducers, applyMiddleware } from 'redux';
import { Provider, useDispatch, useSelector } from 'react-redux';
import ReduxThunk from 'redux-thunk';
import myReducer from './store/reducer';
import * as myActions from './store/action';

import RNDisableBatteryOptimizationsAndroid from 'react-native-disable-battery-optimizations-android';

const rootReducer = combineReducers({
  data: myReducer,
})
const store = createStore(rootReducer, applyMiddleware(ReduxThunk));

// ask for location permissions
const requestLocationPermission = async () => {
  try {
    const granted = await PermissionsAndroid.request(
      PermissionsAndroid.PERMISSIONS.ACCESS_FINE_LOCATION,
      {
        title: 'RNAlarmTester',
        message: 'This app requires location permission.',
      }
    )
    if (granted === PermissionsAndroid.RESULTS.GRANTED) {
      console.log("Location Permission Granted")
      alert("Location Permission granted, starting foreground service");
      // also start the foreground service
      NativeModules.AlarmModule.startService();
    } else {
      console.log("location permission denied")
      alert("Location permission denied");
    }
  } catch (err) {
    console.warn(err)
  }
}

// ask for whitelisting permissions
const requestWhitelistPermission = () => {
  RNDisableBatteryOptimizationsAndroid.enableBackgroundServicesDialogue();
}

// created a wrapper because we are using the store inside App itself.
const AppWrapper = () => {
  return (
    <Provider store={store}>
      <App />
    </Provider>
  )
}

const App = () => {
  const [deviceName, setDeviceName] = useState('');
  NativeModules.Device.getDeviceName((err, name) => setDeviceName(name));

  const storeData = useSelector(state => state.data.data);
  const dispatch = useDispatch();
  
  useEffect(() => {
    // read the records from the store
    dispatch(myActions.readFromStore());
  }, [dispatch]);

  return (
    <View style={styles.screen}>
      <View style={styles.listContainer}>
          <Text>Running on {deviceName}</Text>
          <View style={styles.buttonStyle}>
            <Button title="Request Location Permissions" onPress={requestLocationPermission} />
          </View>
          <View style={styles.buttonStyle}>
            <Button title="Add to Whitelist" onPress={requestWhitelistPermission} />
          </View>
        <FlatList 
          contentContainerStyle={styles.list}
          data={storeData}
          renderItem={itemData => (<Text style={styles.listItem}>{itemData.item}</Text>)}
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
    paddingHorizontal: 16,
    paddingVertical: 5,
  },
  listItem: {
    fontSize: 12,
  },
  buttonStyle: {
    paddingBottom: 10,
  }
});

export default AppWrapper;
export { AppWrapper as App, store };