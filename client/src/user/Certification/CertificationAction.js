import React from 'react';
import pdfMake from 'pdfmake';
import '../../../node_modules/pdfmake/build/vfs_fonts.js';
import { useLocation } from "react-router";
import TopBar from '../../utils/TopBar';
import '../../styles/certification.scss';
import { debounce } from "lodash";

import Files_And_Folder_Flatline from '../../source/Files_And_Folder_Flatline.svg';

import { certificationBack } from '../../source/certification';
import { certificationDefault } from '../../source/certificationDefault';

const format = (data) => {
    if(data.length===1) {
        return ([
            {text: data[0].activityDate, fontSize: 11, alignment: 'center'},
            {text: data[0].distance+"km", fontSize: 11, alignment: 'center'},
            {text: data[0].startTime.substring(11, 19), fontSize: 11, alignment: 'center'},
            {text: data[0].endTime.substring(11, 19), fontSize: 11, alignment: 'center'},
            {text: data[0].partnerName, fontSize: 11, alignment: 'center'},
            {text: data[0].careTime, fontSize: 11, alignment: 'center'},
            {text: data[0].ordinaryTime, fontSize: 11, alignment: 'center'}
        ]);
    } else {
        return(data.map((item) => {
            return ([
                {text: item.activityDate, fontSize: 11, alignment: 'center'},
                {text: item.distance+"km", fontSize: 11, alignment: 'center'},
                {text: item.startTime.substring(11, 19), fontSize: 11, alignment: 'center'},
                {text: item.endTime.substring(11, 19), fontSize: 11, alignment: 'center'},
                {text: item.partnerName, fontSize: 11, alignment: 'center'},
                {text: item.careTime, fontSize: 11, alignment: 'center'},
                {text: item.ordinaryTime, fontSize: 11, alignment: 'center'}
            ])
        }));
    }
};

const CertificationAction = () => {
    const location = useLocation();

    const data = location.state.res;
    const from = location.state.from;
    const to = location.state.to;

    const formatData = format(data.data);

    const documentDefinition = {
		pageSize: 'A4',
		pageOrientation: 'portrait',
        margin: [ 30, 20, 30, 20 ],
        info: {
            title: 'walking-together',
            author: 'computer-gks'
        },
        pageMargins: [ 30, 30, 30, 30 ],

        images: {
            certification: certificationBack,
            certificationDefault: certificationDefault
        },

        background: [
            {
                image: 'certificationDefault',
                width: 595,
                height: 842,
                absolutePosition: { x: 0, y: 0 }
            }
        ],

		content: [
            //1page
            {
                image: 'certification',
                width: 595,
                height: 842,
                absolutePosition: { x: 0, y: 0 }
            },

            //USER INFO
            {text: data.data[0].name, fontSize: 15, absolutePosition: { x: 237, y: 289 }},
            {text: data.data[0].department, fontSize: 15, absolutePosition: { x: 237, y: 325 }},
            {text: data.data[0].stdId, fontSize: 15, absolutePosition: { x: 237, y: 362 }},

            //TOTAL INFO
            {text: from+'~'+to,  fontSize: 12, absolutePosition: { x: 237, y: 423.6 }},    //*******바꾸기 */
            {text: data.careTimes, fontSize: 12, absolutePosition: { x: 237, y: 445.8 }},
            {text: data.ordinaryTimes, fontSize: 12, absolutePosition: { x: 237, y: 467.5 }},
            {text: data.totalTime, fontSize: 12, absolutePosition: { x: 237, y: 489.5 }, pageBreak: 'after'},
            
            //activity table
            {  
                table: {
                    //heights: 20,
                    headerRows: 1,
                    dontBreakRows: true,
                    body: [
                        [ //table header
                            {text: '활동일', fontSize: 12, bold: true, alignment: 'center'}, 
                            {text: '활동거리', fontSize: 12, bold: true, alignment: 'center'}, 
                            {text: '시작시간', fontSize: 12, bold: true, alignment: 'center'}, 
                            {text: '종료시간', fontSize: 12, bold: true, alignment: 'center'}, 
                            {text: '파트너명', fontSize: 12, bold: true, alignment: 'center'},
                            {text: '돌봄활동', fontSize: 12, bold: true, alignment: 'center'},
                            {text: '일반활동', fontSize: 12, bold: true, alignment: 'center'}
                        ],
                        ...formatData
                    ],
                    alignment: "center"
                },
                absolutePosition: { x: 94, y: 80 }
            },
		],
        pageBreakBefore: function(currentNode, followingNodesOnPage) {
            return currentNode.headlineLevel === 1 && followingNodesOnPage.length === 0;
        }
    };

	const func = debounce(() => {
        pdfMake.createPdf(documentDefinition).open();  //please change to "download" open
    }, 800);
        
    return (
        <div id="certificationAction">
            <header>
                <TopBar
                    left="null" 
                    center={{title:"인증서 발급", data:null}} 
                    right="null" 
                    lfunc={null}
                    rfunc={null}
                    size="small"/>
            </header>
            <div id="buttonWrap">
                <button id="pdfmake" className="user_btn_blue" onClick={func}>인증서 다운로드</button>
            </div>
            
            <div id="imageWrap">
                <img src={Files_And_Folder_Flatline} alt=""/>
            </div>
        </div>
    );
};

export default CertificationAction;