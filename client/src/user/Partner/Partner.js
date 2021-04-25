import React from 'react';
import { useSelector } from 'react-redux';
import { useHistory } from 'react-router-dom';
import PartnerItem from '../Partner/PartnerItem';
import MainContainer from '../../utils/MainContainer'

import '../../styles/partner.scss';

const Partner = () => {
    const history = useHistory();
    const partner = useSelector(state => state.partner.briefPartner)  //PARTNER-LIST

    //param function
    function goBack() {
        history.push('/user/mypage')
    }

    function goCreatePartner() {
        history.push(`/user/partner-insert`)
    }

    return (
        <MainContainer header = {{
            left : "back",
            center : {title : "파트너", data : null},
            right : "plus" ,
            lfunc : () => goBack(),
            rfunc : () => goCreatePartner(),
            size :"small"

        }}>
            
            <table id="partner_table">
                <tbody>
                    { partner.length!==0 ?
                    partner.map((res) => {
                        return (
                            <PartnerItem state={res} key={res.partnerId}/>  //PARTNER-INFO-ITEM
                        )
                    })
                    : "파트너 정보가 없습니다."
                    }
                </tbody>
            </table>
        </MainContainer>
    );
};

export default Partner;