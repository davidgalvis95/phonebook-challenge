import phoneBookApiReducer from './phoneBookApiReducer';
import {combineReducers} from 'redux';

const rootReducer = combineReducers({
    phoneBookApi: phoneBookApiReducer
})

export default rootReducer;