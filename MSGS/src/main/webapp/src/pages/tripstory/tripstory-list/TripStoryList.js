import React, { useState, useEffect } from "react";
import axios from "axios";

import items from "../tripstory-data/TripStoryData";
import styles from "./TripStoryList.module.css";

import TripStoryItem from "./TripStoryItem";

const TripStoryList = () => {
  // DB 데이터 담을 state
  const [data, setData] = useState([]);

  const getData = async () => {
    try {
			//모든 여행 이야기(TripStory)를 불러옴
			const response = await axios.get("/tripstory")
			const data = {}

			// 데이터 형태 재구성
			response.data.forEach((item) => {
				const storyId = item.storyId

				if (!data[storyId]) {
					data[storyId] = {
						storyId: storyId,
						scheduleId: item.scheduleId,
						title: item.title,
						comment: item.comment,
						dateList: item.dateList,
						userId: item.userId,
						userName: item.userName,
						userImgPath: item.userImgPath,
						storyImgs: [],
					}
				}

				if (item.storyImgPath && item.storyImgPath.length > 0) {
					data[storyId].storyImgs.push(item.storyImgPath)
				}
			})

			setData(Object.values(data))
			console.log("=====getStoryList=====", Object.values(data))
			// console.log("======data======", data['001'].storyId) // tripId 출력 방법
		} catch (error) {
      console.log(error);
    }
  };

  // getStoryList 실행
  useEffect(() => {
    getData();
  }, []);
  
  return (
    <div className={styles["main-wrapper"]}>
      <p className={styles["trip-story-list-title"]}>실시간 여행기🐤</p>
      <p className={styles["trip-story-list-sub-title"]}>
        직접 다녀온 추천 일정과 여행 꿀팁 확인하기
      </p>

      <div className={styles["items-wrapper"]}>
        {Object.values(data).map((data, index) => (
          <TripStoryItem
            key={index}
            storyId={data.storyId}
            scheduleId={data.scheduleId}
            tripTitle={data.title}
            tripComment={data.comment}
            tripDateList={data.dateList}
            userNickname={data.userName}
            userImgPath={data.userImgPath}
            tripImg={data.storyImgs}
          />
        ))}
      </div>
    </div>
  );
};

export default TripStoryList;
