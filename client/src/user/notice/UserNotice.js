import {React, useState, useEffect, useCallback} from 'react'
import {FaSearch} from 'react-icons/fa';
import ReactPaginate from 'react-paginate';
import {getNoticeList} from '../../modules/notice';
import {useSelector, useDispatch} from 'react-redux';
import { useHistory } from 'react-router-dom';
import { debounce } from "lodash";

import NoticeDetail from './NoticeDetail';
import { changeBar } from '../../modules/topbar';


function UserNotice() {
    const dispatch = useDispatch();
    const history = useHistory();

    let noticeList = useSelector(state => state.noticeReducer.list); //현재 페이지에 띄워질 공지 리스트

    //page
    const [current, setCurrent] = useState(0);  //현재 페이지
    const pageInfo = useSelector(state => state.noticeReducer.pageInfo);  //전체 페이지 정보
    const [keyword, setkeyword] = useState(null);       //키워드 state
    const [active, setactive] = useState("");

    //공지사항 검색함수
    const Search = debounce(() => {
        dispatch(getNoticeList(current+1,keyword));
    }, 800);
    
    //엔터키 사용시 실행되는 함수 Search함수와 같음
    const enterKey = () => { 
        if(window.event.keyCode === 13) {
            dispatch(getNoticeList(current+1,keyword));
        }
    };


    const changePage = (page) => {  //pagination 페이지 변경 시 실행
        setCurrent(page.selected);
    };


    const ChangeKeywordHandler = (e) => {
        setkeyword(e.target.value);
    };

    //param function
    const goBack = useCallback(() => {
        history.replace('/user/home');
    }, [history]);

    useEffect(() => {
        dispatch(changeBar("back", {title:"공지사항", data:null}, "null", goBack, "null", "small"));  //상단바 변경
        dispatch(getNoticeList(1));  //공지사항 목록 받아오기
    }, [dispatch, goBack]);

    return (
        <div id="noticeListWrap">
            <div id="noticeList">
                <div id="searchWrap">
                    <input className="input" id="input" type = "text" onKeyUp = {enterKey} onChange = {ChangeKeywordHandler}></input>
                    <span id="icon">
                    <FaSearch onClick = {Search}/>
                    </span>
                </div>
                {noticeList.length!==0 ?
                noticeList.map(item => {
                    return(
                        <NoticeDetail key = {item.noticeId} noticeId = {item.noticeId} title = {item.title} active = {active} setactive = {setactive} content = {item.content}/>
                    )
                }) : <div id="resMessage">검색 결과가 없습니다.</div> }
                <ReactPaginate 
                    pageCount={pageInfo.totalPage}  //총 페이지 수
                    pageRangeDisplayed={10}  //한 페이지에 표시할 게시글 수
                    initialPage={current}  //선택한 초기 페이지
                    previousLabel={"이전"}  //이전 라벨
                    nextLabel={"다음"}  //다음 라벨
                    onPageChange={changePage}  //클릭 할 때 호출 할 메서드
                    containerClassName={"pagination-ul"}  //페이지 매김 컨테이너의 클래스 이름
                    pageClassName={"page-li"}  //각 페이지 요소의 li태그에 있는 클래스 이름
                    activeClassName={"currentPage"}  //활성 페이지의 클래스 이름
                    previousClassName={"pageLabel-btn"}  //이전 라벨의 클래스 이름
                    nextClassName={"pageLabel-btn"}  //다음 라벨의 클래스 이름
                />
            </div>
        </div>
    );
};

export default UserNotice;