/**
 * @format
 */

import {AppRegistry} from 'react-native';
import App from './App';
import {name as appName} from './app.json';

import * as RNFS from 'react-native-fs';

const AlarmTask = async (data) => {
    console.log(data.ALARM_EVENT);

    const toWrite = data.ALARM_EVENT +"\n";
    const path = RNFS.DocumentDirectoryPath + '/timestamp.txt';
    RNFS.appendFile(path, toWrite, 'utf8')
    .then((success) => {
        console.log('FILE WRITTEN!');

        // write to redux
    })
    .catch((err) => {
        console.log(err.message);
    });
}

AppRegistry.registerComponent(appName, () => App);
AppRegistry.registerHeadlessTask('AlarmTask', () => AlarmTask);