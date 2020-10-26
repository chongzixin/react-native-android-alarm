/**
 * @format
 */

import {AppRegistry} from 'react-native';
import { App, store } from './App';
import {name as appName} from './app.json';

import * as myActions from './store/action';

const AlarmTask = async (data) => {
    console.log(data.ALARM_EVENT);

    const toWrite = data.ALARM_EVENT;
    store.dispatch(myActions.writeToStore(toWrite));
}

AppRegistry.registerComponent(appName, () => App);
AppRegistry.registerHeadlessTask('AlarmTask', () => AlarmTask);