import { combineReducers } from 'redux';
import activityReducer from '../activity';
import feedReducer from '../feed';
import noticeReducer from '../notice';
import user from '../user';
import partner from '../partner';

const rootReducer = combineReducers({
  activityReducer,
  feedReducer,
  noticeReducer,
  user,
  partner
});

export default rootReducer;