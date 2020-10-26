import * as RNFS from 'react-native-fs';

const path = RNFS.DocumentDirectoryPath + '/timestamp.txt';

export const writeToFile = async (payload) => {
    try {
        const toWrite = payload + "\n";
        await RNFS.appendFile(path, toWrite, 'utf8');
    } catch (err) {
        throw err;
    }
}

export const readFromFile = async () => {
    try {
        // read the lines from the file, then return them in an array sorted by latest date first.
        const response = await RNFS.readFile(path, 'ascii');
        const dataFromFile = response.split("\n").reverse();
        return dataFromFile;
    } catch (err) {
        // WHEN APP IS LAUNCHED FOR THE FIRST TIME, this will throw an error because the file is not found.
        // we return an empty array so that the redux store will initialise with empty array.
        return [];
    }
}