import {React, useState, useEffect} from 'react';
import {useDispatch, useSelector} from 'react-redux';
import {useHistory} from 'react-router-dom';
import {sort} from '../../utils/options';
import {getFeedList, selectFeed, selectErrorFeed} from '../../modules/feed';
import { debounce } from "lodash";
import { changeBar } from '../../modules/topbar';

import '../../styles/feed.scss';

function Feed() {
    const ID = useSelector(state => state.user.authResult.stdId);
    const history = useHistory();
    const dispatch = useDispatch();
    const [sor, setsor] = useState("asc");             // 정렬을 위한 state 지정
    



    useEffect(() => {
        dispatch(changeBar("null", {title:"피드", data:null}, "null", "null", "null", "small"));  //상단바 변경
        dispatch(getFeedList(ID,sor));
    },[sor,ID,dispatch]);

    let myFeed = useSelector(state => state.feedReducer.list);





    const sortList = sort.map(
        item => {
            return ( <option key = {item.label} value = {item.value}>{item.label}</option>);
        }
    );

    const sortHandler = (e) => {
        setsor(e.currentTarget.value);
    };


    const goDetail = debounce((activityId, activityStatus) => {
        if(activityStatus=== 0) {
            dispatch(selectFeed(activityId))
            .then(() => history.replace('/user/feeddetail'));
        } else if(activityStatus === 1) {
            dispatch(selectErrorFeed(activityId))
            .then(() => history.replace('/user/feeddetail'));
        }
    }, 800);


    return (
        <div id="feedWrap">
            <select className="inputSelect" selectedvalue="desc" onChange = {(e) => sortHandler(e)}>{sortList}</select>
            <div id="feedItemsWrap">
                {myFeed.length!==0 ? 
                myFeed.map(
                    (item,index) => {

                        return (
                        <table key = {index} onClick = {() => goDetail(item.activityId, item.activityStatus)}>
                            <tbody>
                            <tr id="tr1">
                                <td id="date">{item.activityDate}</td>
                                <td id="state">{item.activityStatus ? "진행중" : "종료"}</td>
                            </tr>
                            <tr id="tr2">
                                <td id="distance">{item.distance}{item.distance === null ? "" : "m"}</td>
                                <td id="name">파트너 {item.partnerName}</td>
                            </tr>
                            </tbody>
                        </table>
                        )}
                ) : <p id="notice">피드 정보가 없습니다.</p>
            }
            </div>
        </div>
    );
};

export default Feed;