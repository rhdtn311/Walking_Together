import {React,useState} from 'react'
import moment from 'moment'

function FindPassword() {
    const style = {
        display : "flex",
        justifyContent: "center",
        alignItems: "center",
        width: "100%",
        height: "100vh"
    }

    const [birth, setbirth] = useState("");
    const [stdId, setstdId] = useState("");
    const [Email, setEmail] = useState("");
    const [Name, setName] = useState("");

    const NameHandler = (e) => {
        setName(e.currentTarget.value)
    };

    const stdIdHandler = (e) => {
        setstdId(e.currentTarget.value)
    };

<<<<<<< Updated upstream
    const EmailHandler = (e) => {
        setEmail(e.currentTarget.value)
    };

=======
    const findpasswordHandler = () => {
        axios.post('/findpassword', {
            stdId : stdId,
            name : Name,
            birth : birth
        })
        .then(res => {if(res.data.status === "200") {
            if(window.confirm(res.data.message)) {
                history.push({
                    pathname : '/findresult',
                    state : {email : res.data.email}
                })
            }
        } else {
            alert(res.data.message)
        }}
            
        )
        .catch(err => err)
    }
    
>>>>>>> Stashed changes
    return (
    
        <div style = {style}>
        <form>
            <label>이름</label>
            <input type = "text" value = {Name} onChange = {NameHandler}/>
            
            <label>생년월일</label>
            <input type = "date" onChange = {(e)=> {setbirth(moment(e.target.value).format('YYYYMMDD'))}}/>
            
            <label>이메일</label>
            <input type = "email" value = {Email} onChange = {EmailHandler}/>

            <label>학번</label>
            <input type = "text" value = {stdId} onChange = {stdIdHandler}/>
            <button>임시 비밀번호 발송</button>
        </form>
        </div>
    )
}

export default FindPassword
