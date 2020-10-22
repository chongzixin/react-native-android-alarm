/**
 * @format
 */

import {AppRegistry} from 'react-native';
import App from './App';
import {name as appName} from './app.json';

const AlarmTask = async (data) => {
    console.log(data.ALARM_EVENT);
}

AppRegistry.registerComponent(appName, () => App);
AppRegistry.registerHeadlessTask('AlarmTask', () => AlarmTask);