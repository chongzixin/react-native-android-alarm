export const WRITE_FILE = 'WRITE_FILE';
export const READ_FILE = 'READ_FILE';

import { readFromFile, writeToFile } from '../helpers/file';

export const writeToStore = payload => {
    // TODO: write to file here. and make it async
    return async dispatch => {
        try {
            await writeToFile(payload);
            dispatch({ type: WRITE_FILE, data: payload });
        } catch (err) {
            throw err;
        }
    };
};

export const readFromStore = () => {
    // read from file then load into array.
    return async dispatch => {
        try {
            const dataFromFile = await readFromFile();
            dispatch({ type: READ_FILE, data: dataFromFile });
        } catch (err) {
            throw err;
        }
    };
};