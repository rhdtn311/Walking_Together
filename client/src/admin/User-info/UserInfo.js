import React, { useState } from 'react';
import axios from 'axios';
import { debounce } from "lodash";

import '../../styles/admin.scss';

const UserInfo = () => {
    const [res,setRes] = useState([]);
    const url = process.env.REACT_APP_SERVER;
    
    //filter state
    const [keyword, setKeyword] = useState("");

    //button
    const search = debounce(() => {
        axios.get(`${url}/admin/userinfo?keyword=${keyword}`, {
            headers : {'Authorization' : `Bearer ${localStorage.getItem("token")}`}
        }).then((response) => {
            if(response.data.length!==0) {
                setRes(response.data.data);
            }
            else {
                setRes([]);
            }
        });
    }, 800);

    //change action
    const changeKeyword = (e) => {
        setKeyword(e.target.value);
    };

    return (
        <div>
            <div id="headerWrap">
                <span className="admin_title" id="title"># 회원 정보</span>
            </div>

            {/* filter */}
            <div id="filter">
                <div id="filterWrap">
                    <label>이름/학번</label>
                    <input type="text" name="keyword" id="keyword" value={keyword} onChange={changeKeyword}/>
                </div>

                <button onClick={search} className="admin_btn_blue" id="r1">조회</button>
            </div>
            
            
            {/* search */}
            <div id="userTableWrap" className="wrapper">
                <table id="userTable" className="table">
                    <thead>
                        <tr>
                            <th id="thNo">no</th>
                            <th id="thName">이름</th>
                            <th id="thId">학번</th>
                            <th id="thDept">학과</th>
                            <th id="thMail">이메일</th>
                            <th id="thBirth">생년월일</th>
                            <th id="thPh">연락처</th>
                        </tr>
                    </thead>
                    <tbody>
                    {   res.length!==0 ?
                            res.map((data, index)=>{
                                return (
                                    <tr key={data.stdId}>
                                        <td>{index+1}</td>
                                        <td>{data.name}</td>
                                        <td>{data.stdId}</td>
                                        <td>{data.department}</td>
                                        <td>{data.email}</td>
                                        <td>{data.birth}</td>
                                        <td>{data.phoneNumber}</td>
                                    </tr>
                                )
                            })
                        :
                        <tr>
                        <td colSpan="7">
                            회원 정보가 없습니다.
                        </td>
                    </tr>
                    }
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default UserInfo;