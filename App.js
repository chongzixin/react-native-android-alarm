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

import { createStore, combineReducers, applyMiddleware } from 'redux';
import { Provider, useDispatch, useSelector } from 'react-redux';
import ReduxThunk from 'redux-thunk';
import myReducer from './store/reducer';
import * as myActions from './store/action';

const rootReducer = combineReducers({
  data: myReducer,
})
const store = createStore(rootReducer, applyMiddleware(ReduxThunk));

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
    dispatch(myActions.readFromStore());
  }, [dispatch]);

  return (
    <View style={styles.screen}>
      <View style={styles.listContainer}>
        <Text>Running on {deviceName}</Text>
        <FlatList 
          contentContainerStyle={styles.list}
          data={storeData}
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
    paddingHorizontal: 16,
    paddingVertical: 5,
  },
});

export default AppWrapper;
export { AppWrapper as App, store };