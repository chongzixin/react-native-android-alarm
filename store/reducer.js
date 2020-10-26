import { WRITE_FILE, READ_FILE } from './action';

const initialState = {
    data: [],
};

export default (state=initialState, action) => {
    switch(action.type) {
        case WRITE_FILE:
            return { data: [action.data, ...state.data] };
        case READ_FILE:
            return { data: action.data }
        default:
            return state;
    }
};