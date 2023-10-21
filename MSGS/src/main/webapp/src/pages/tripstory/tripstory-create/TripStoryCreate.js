import React, { useState, useEffect } from "react";
import { Navigate, useParams } from "react-router-dom";
import axios from "axios";

import styles from "./TripStoryCreate.module.css";
import Loading from "../../../components/common/Loading";
import TripStoryCreateDay from "./TripStoryCreateDay";
import GoogleMapPolyline from "../../../components/tripstory/tripstory-details/GoogleMapPolyline";
import StarClick from "../../../components/common/StarClick";
import UploadPhotoList from "../../../components/tripstory/tripstory-create/tripstory-create-upload/UploadPhotoList";
import UploadPhoto from "../../../components/tripstory/tripstory-create/tripstory-create-upload/UploadPhoto";



//const scheduleId = 35;  //이야기 생성 버튼 클릭 시 넘어오는 데이터

/*이 페이지 마운트 시 백에서 가져오는 정보 Start*/
const storySummary_sample = {
	cityName: '춘천·홍천',
	schedule_id: 13,
	title: '',
	rating: '',
	dateList: ['2023.6.22', '2023.6.23', '2023.6.24'],
	comment: '',
	img: [],
}


const storyList_sample = {
	/* 이 페이지에서 사용자가 입력을 통해 storyList의 각 Object에 추가할 데이터 (Nullable 하다)*/
	// rating = 4,
	// comment = "아 여기는 뷰가 멋지더라",
	// imgOriginName = "img origin name",
	// imgPath = "img path"
	1: [
		{
			/* /tripschedule/info 에서 get한 데이터(planList) */
			contentid: '133967',
			title: '수석정',
			mapx: 129.237308,
			mapy: 35.825712,
			location: '경주',
			type: '음식점',
			order: 1,
			placeOrder: 1,
			// areacode: 31,
			// sigungucode: 19,
			// contenttypeid: "32", -> 여기까지 3개의 데이터는 areaTitle로 CitiesData 필터링 해서 알 수 있는 정보들임.
			// firstimage2: "http://tong.visitkorea.or.kr/cms/resource/18/2613518_image2_1.jpg",
			// isChecked: false,    -> 여기까지 2개의 데이터는 이 페이지에서는 필요 없는 데이터임.

			/* 백엔드에서 fetch한 데이터 각각에 끼워넣어야 하는 데이터들 */
			rating: '',
			comment: '',
			img_origin_name: '',
			img_path: '',
			reviewImg: [],
		},
		{
			contentid: '1986674',
			title: '전통손칼국수',
			areacode: 31,
			sigungucode: 1,
			contenttypeid: '12',
			firstimage2: 'http://tong.visitkorea.or.kr/cms/resource/49/2690649_image3_1.jpg',
			mapx: 129.2848,
			mapy: 35.835334,
			location: '경주',
			isChecked: false,
			type: '음식점',
			order: 2,
			placeOrder: 2,

			rating: '',
			comment: '',
			img_origin_name: '',
			img_path: '',
			reviewImg: [],
		},
		{
			contentid: '2736662',
			title: '낙지마실',
			areacode: 31,
			sigungucode: 1,
			contenttypeid: '12',
			firstimage2: 'http://tong.visitkorea.or.kr/cms/resource/38/1896238_image3_1.jpg',
			mapx: 129.263007,
			mapy: 35.851623,
			location: '경주',
			isChecked: false,
			type: '음식점',
			order: 3,
			placeOrder: 3,

			rating: '',
			comment: '',
			img_origin_name: '',
			img_path: '',
			reviewImg: [],
		},
		{
			contentid: '1954014',
			title: '벨루스로즈펜션',
			areacode: 31,
			sigungucode: 19,
			contenttypeid: '32',
			firstimage2: 'http://tong.visitkorea.or.kr/cms/resource/94/1893194_image3_1.jpg',
			mapx: 129.260246,
			mapy: 35.859056,
			location: '경주',
			isChecked: false,
			type: '숙박',
			order: 4,
			placeOrder: null,

			rating: '',
			comment: '',
			img_origin_name: '',
			img_path: '',
			reviewImg: [],
		},
	],
	2: [
		{
			contentid: '2762860',
			title: '별채반교동쌈밥',
			areacode: 31,
			sigungucode: 19,
			contenttypeid: '32',
			firstimage2: 'http://tong.visitkorea.or.kr/cms/resource/46/2762946_image2_1.jpg',
			mapx: 129.210587,
			mapy: 35.833286,
			location: '경주',
			isChecked: false,
			type: '음식점',
			order: 1,
			placeOrder: null,

			rating: '',
			comment: '',
			img_origin_name: '',
			img_path: '',
			reviewImg: [],
		},
		{
			contentid: '1492402',
			title: '경주 대릉원 일원',
			areacode: 31,
			sigungucode: 19,
			contenttypeid: '39',
			firstimage2: 'http://tong.visitkorea.or.kr/cms/resource/00/2653400_image3_1.jpg',
			mapx: 129.210721,
			mapy: 35.839185,
			location: '경주',
			isChecked: false,
			type: '관광지',
			placeOrder: 1,
			order: 2,

			rating: '',
			comment: '',
			img_origin_name: '',
			img_path: '',
			reviewImg: [],
		},
		{
			contentid: '2770730',
			title: '노벰버 스파리조트(노벰버리조트)',
			areacode: 31,
			sigungucode: 19,
			contenttypeid: '39',
			firstimage2: 'http://tong.visitkorea.or.kr/cms/resource/23/2771823_image2_1.jpg',
			mapx: 129.509068,
			mapy: 35.808091,
			location: '경주',
			isChecked: false,
			type: '숙박',
			placeOrder: null,
			order: 3,

			rating: '',
			comment: '',
			img_origin_name: '',
			img_path: '',
			reviewImg: [],
		},
	],
	3: [
		{
			contentid: '126218',
			title: '경주 문무대왕릉',
			areacode: 31,
			sigungucode: 19,
			contenttypeid: '32',
			firstimage2: 'http://tong.visitkorea.or.kr/cms/resource/46/1613346_image3_1.jpg',
			mapx: 129.482766,
			mapy: 35.740729,
			location: '경주',
			isChecked: false,
			type: '관광지',
			order: 1,
			placeOrder: 1,

			rating: '',
			comment: '',
			img_origin_name: '',
			img_path: '',
			reviewImg: [],
		},
		{
			contentid: '2766530',
			title: '두부마을',
			areacode: 31,
			sigungucode: 19,
			contenttypeid: '39',
			firstimage2: 'http://tong.visitkorea.or.kr/cms/resource/34/2766934_image2_1.jpg',
			mapx: 129.312091,
			mapy: 35.783949,
			location: '경주',
			isChecked: false,
			type: '음식점',
			placeOrder: 2,
			order: 2,

			rating: '',
			comment: '',
			img_origin_name: '',
			img_path: '',
			reviewImg: [],
		},
	],
}


/*일자별 데이터 (Ex. 각 Day에 대한 코멘트)*/
let dailyComment_sample = {
    1: "",
    2: "",
    3: "",
};
/*이 페이지 마운트 시 백에서 가져오는 정보 End*/


export default function TripStoryCreate() {
	const [dailyComment, setDailyComment] = useState({})
	const [storySummary, setStorySummary] = useState({})
	const [storyList, setStoryList] = useState({})
	const [starCnt, setStarCnt] = useState(0)
    const [mapDataList, setMapDataList ] = useState([])

	// 유저 아이디 가져오기
	const [userId, setUserId] = useState('yhg9801')

	// 파라미터에서 storyId, scheduleId 가져오기
	const { storyId, scheduleId } = useParams()

	// 현재 출력되는 day
	const [day, setDay] = useState(0)

	//임시데이터 세팅
	// useEffect(() => {
	// 	setStorySummary(storySummary_sample)
	// 	setStoryList(storyList_sample)
	// 	setDailyComment(dailyComment_sample)
	// }, [])

	// back-end에서 화면에 띄울 데이터를 fetch해옴
	const getSchedule = async () => {
		// TripSchedule 데이터 가져와서 StorySummary, StoryList 상태값을 set함.
		await axios
			.get('/tripschedule/info', {
				params: { scheduleId: scheduleId }, // scheduleId: 2,
			})
			.then(function (res) {
				setStorySummary({
					cityName: res.data.areaTitle,
					title: '',
					rating: '',
					dateList: res.data.dateList,
					comment: '',
					img: [],
				})

				let planList = res.data.planList

				// 각 객체에 추가할 프로퍼티
				const additionalProperties = {
					rating: '',
					comment: '',
					img_origin_name: '',
					img_path: '',
					reviewImg: [],
				}

				// 각 객체에 프로퍼티 추가
				Object.keys(planList).forEach((key) => {
					planList[key].forEach((obj) => {
						Object.assign(obj, additionalProperties)
					})
                })
                
                console.log("==================storyList==============")
                console.log(planList)

				setStoryList(planList)

				/* =========================================================================== */
			})
			.catch(function (error) {
				console.log('get_info 실패', error)
			})

		// 좋아요 데이터 가져오기  -> 스토리 생성이 아닌 수정 페이지에서 필요함.
		// const likeResponse = await axios.post('/tripstory/detail/getStoryLike', { storyId, userId })
		// setIsLiked(likeResponse.data.isLiked)
		// setLikeCnt(likeResponse.data.likeCnt)
	}

	// back-end에서 화면에 띄울 데이터를 fetch해옴
	useEffect(() => {
        getSchedule()
        
    }, [])


    useEffect(() => {
        if (Object.keys(storyList).length > 0) { // storyList가 존재할 때
            console.log('+++++++++++++++++')
            console.log(storyList)
            console.log(day)

            // 지도에 표기할 좌표 데이터 추출
            let placeData = []
            storyList[day + 1].forEach((item, index) => {
                placeData.push({
                    order: item.order, //index + 1,
                    // placeOrder: item.type === "숙박" ? null : placeOrder,
                    placeOrder: item.placeOrder,
                    type: item.type === '숙박' ? 'dorm' : 'place',
                    center: { lat: item.mapy, lng: item.mapx },
                })
            })

            setMapDataList(placeData)
            // mapDataList.push(placeData)
				}
        console.log('------------------')
					
    }, [storyList, day])
    


	//저장 버튼 눌렀을 때 백으로 Story 데이터 보내기.
	const saveTripStory = () => {
		const requestBody = {
			dateList: storySummary.dateList,
			storyList: storyList,
			dailyComment: dailyComment,
			storyData: storySummary,
		}

		console.log(requestBody)
		axios
			.post('/tripstory/info', requestBody)
			.then(function (response) {
				console.log('saveTripStory  성공')
				alert('저장이 완료되었습니다.')
				Navigate('/tripstory')
			})
			.catch(function (error) {
				console.log('saveTripStory  실패', error)
			})
	}

	// 이미지 엑박 처리
	const [imageError, setImageError] = useState(false)

	const handleImageError = () => {
		setImageError(true)
	}

	//Write Form Title Change
	const handleWriteTitle = (e) => {
		setStorySummary((storyData) => ({
			...storyData,
			title: e.target.value,
		}))
	}

	//Write Form Comment Change
	const handleWriteComment = (e) => {
		setStorySummary((storyData) => ({
			...storyData,
			comment: e.target.value,
		}))
	}

	//Write Form Rating Change
	const handlWriteImg = (imgList) => {
		setStorySummary((storyData) => ({
			...storyData,
			img: imgList,
		}))
	}


	return (
		<>
			{!dailyComment || !(Object.keys(storyList).length > 0) || !(mapDataList.length >0) ? (
				<Loading />
			) : (
				<div className={styles['width-wrapper']}>
					<button className={styles['save-button']} onClick={saveTripStory}>
						저장하기
					</button>

					{/* 각 day별 경로 표시된 구글맵 */}
					{
						<div className={styles['map-wrap']}>
							<GoogleMapPolyline mapDataList={mapDataList} width={'90rem'} height={'30rem'} />
						</div>
					}
					<div className={styles['tripstory-title']}>
						<input className={styles['story-title']} type='text' value={storySummary.title} onChange={handleWriteTitle} />
					</div>
					<div className={styles['star-score-area']}>
						<div className={styles['trip-score-ment']}>이번 여행 이야기의 총 평점을 입력해주세요.</div>
						<StarClick starCnt={starCnt} setStarCnt={setStarCnt} height={'3rem'} />
					</div>
					<div className={styles['tripstory-content']}>
						<textarea placeholder='이번 여행은 어떤 여행이었나요?' value={storySummary.comment} onChange={handleWriteComment} />
					</div>
					<div className={styles['upload-photo-area']}>
						<UploadPhoto photos={storySummary.img} setPhotos={handlWriteImg} />
					</div>

					{/* day 선택 버튼 */}
					{storySummary.dateList && (
						<div className={styles['day-btn-wrap']}>
							{storySummary.dateList.map((_, index) => (
								<button key={index} className={`${styles['day-btn']} ${(day === index) & styles['day-btn-filled']}`} onClick={() => setDay(index)}>
									Day {index + 1}
								</button>
							))}
						</div>
					)}

					{/* 선택한 day별 데이터 적용됨 */}

					<TripStoryCreateDay
						day={day}
						date={storySummary.dateList[day]}
						dailyComment={dailyComment}
						setDailyComment={setDailyComment}
						storyList={storyList}
						setStoryList={setStoryList}
					/>
				</div>
			)}
		</>
	)
};
