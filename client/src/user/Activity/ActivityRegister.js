import { React, useState, useRef } from 'react'
import { finishActivity } from '../../modules/activity';
import { useDispatch } from "react-redux";
import { useHistory } from 'react-router-dom';
import TopBar from '../../utils/TopBar';

import '../../styles/activity.scss';

function ActivityRegister() {
    const history = useHistory()
    const dispatch = useDispatch()

    const stdId = localStorage.getItem('user_info')
    const partnerId = localStorage.getItem('partnerId')

    const [picture, setPicture] = useState([])
    const [buttonFirst, setButtonFirst] = useState(true)

    const camera = useRef();
    const frame = useRef();

    const takePhoto = (e) => {
        let reader = new FileReader();

        reader.onloadend = () => {
            const base64 = reader.result
            if (base64) {
              frame.current.src=base64;
            }
        }
        if (e.target.files[0]) {
            reader.readAsDataURL(e.target.files[0]) // 파일을 버퍼에 저장
            setPicture(e.target.files[0]) // 파일 상태 업데이트
            setButtonFirst(false)
        }
    }

    //param function
    function goBack() {
        history.goBack()
    }

    function createAction(e) {
        e.preventDefault();

        if(picture.length===0) {
            alert("사진 촬영 후 활동 등록이 가능합니다.")
        } else {
            submit()
        }
    }

    const submit = async() => {
        //create formdata
        const formData = new FormData();
        formData.append("stdId", stdId);
        formData.append("partnerId", partnerId);
        formData.append("startPhoto", picture);

        await dispatch(finishActivity(formData))
        .then(() => history.push('/activity'))
    }


    return (
        <div id="activityRegisterWrap">
            <TopBar 
            left="cancel" 
            center={{title:"사진 등록", data:null}} 
            right="create" 
            lfunc={goBack}
            rfunc={createAction}
            size="small"/>

            <div id="activityRegister">
                <div className = "picture_container">
                    {picture.length===0?
                    <div className="preview"></div>
                    :
                    <div className="preview">
                        <img ref={frame} alt="none"/>
                    </div>
                    }
                </div>

                <div id="pictureInput">
                    <form action="/activity/createActivity" className="imageForm" encType="multipart/form-data" method="post" onSubmit={(e) => createAction(e)}>
                        <input type="file" accept="image/*" capture="camera" ref={camera} id="inputFile" onChange={takePhoto}/>
                        
                        {buttonFirst===true ? 
                        <label htmlFor="inputFile" className="btn fileBtn">사진 촬영</label>
                        : <label htmlFor="inputFile" className="btn fileBtn">다시 촬영</label>
                        }
                        <br/>
                        {picture.length===0?
                        <span id="fileName">선택된 사진 없음</span>
                        : <span id="fileName">{picture.name}</span>
                        }
                    </form>
                </div>
            </div>
        </div>
    )
}

export default ActivityRegister
