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


export default function TripStoryCreate() {
	const [dailyComment, setDailyComment] = useState({})
	const [storySummary, setStorySummary] = useState({})
	const [storyList, setStoryList] = useState({})
	const [starCnt, setStarCnt] = useState(0)
    const [mapDataList, setMapDataList ] = useState([])


	// 파라미터에서 storyId, scheduleId 가져오기
	const { storyId, scheduleId } = useParams()

	// 현재 출력되는 day
	const [day, setDay] = useState(0)


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
                
              
				setStoryList(planList)

			
			})
			.catch(function (error) {
				console.log('get_info 실패', error)
			})

		// 좋아요 데이터 가져오기  -> 스토리 생성이 아닌 수정 페이지에서 필요함.
		// const likeResponse = await axios.get('/tripstory/like', { storyId, userId })
		// setIsLiked(likeResponse.data.isLiked)
		// setLikeCnt(likeResponse.data.likeCnt)
	}

	// back-end에서 화면에 띄울 데이터를 fetch해옴
	useEffect(() => {
        getSchedule();
    }, [])


    useEffect(() => {

        if (Object.keys(storyList).length > 0) { // storyList가 존재할 때
			

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
   
					
    }, [storyList, day])
    


	//저장 버튼 눌렀을 때 백으로 Story 데이터 보내기.
	const saveTripStory = () => {

		//rating 추가
		
		storySummary.rating = starCnt;

		const requestBody = {
			dateList: storySummary.dateList,
			storyList: storyList,
			dailyComment: dailyComment,
			storyData: storySummary,
			scheduleId : scheduleId,
			userToken : localStorage.getItem("token")
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
			{!dailyComment || !(Object.keys(storyList).length > 0) || !(mapDataList.length > 0) ? (
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
						<input
							className={styles['story-title']}
							type='text'
							placeholder='여행 이야기 제목(필수)'
							value={storySummary.title}
							onChange={handleWriteTitle}
						/>
					</div>
					<div className={styles['star-score-area']}>
						<div className={styles['trip-score-ment']}>이번 여행 이야기의 총 평점을 입력해주세요.</div>
						<StarClick starCnt={starCnt} setStarCnt={setStarCnt} height={'3rem'} />
					</div>
					<div className={styles['tripstory-content']}>
						<textarea placeholder='이번 여행은 어떤 여행이었나요?' value={storySummary.comment} onChange={handleWriteComment} />
					</div>
					<div className={styles['upload-photo-area']}>
						{/* <UploadPhoto photos={storySummary.img} setPhotos={handlWriteImg} /> */}
						<UploadPhoto photos={storySummary.img} handlWriteImg={handlWriteImg} /> 
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
