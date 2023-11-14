import React from "react";
import { Link } from "react-router-dom";
import axios from "axios"

import styles from "./MySchedule.module.css";

import citiesData from "../../tripschedule/tripschedule-details/tipschedule1/CitiesData";

const MySchedule = (props) => {
  const { data } = props;
  const dateArray = data.dateList.split(",");
  const today = new Date();

  const startDay = dateArray[0];
  const endDay = dateArray[dateArray.length - 1];

  const startDate = new Date(startDay);
  const endDate = new Date(endDay);
  const timeDiff = startDate.getTime() - today.getTime();
  const dDay = Math.ceil(timeDiff / (1000 * 60 * 60 * 24)) * -1;

  console.log("dDay: ", dDay);

  // regDate, modDate 형식변환(yyyy-mm-dd)
  const formatDate = (date) => {
    const dateString = new Date(date);
    const year = dateString.getFullYear();
    const month = ("0" + (dateString.getMonth() + 1)).slice(-2);
    const day = ("0" + dateString.getDate()).slice(-2);
    return `${year}-${month}-${day}`;
  };

  // engTitle, imgURL 불러오기
  const selectedCityName = data.cityName;
  const filteredData = citiesData.filter(
    (cityData) => cityData.areaTitle === selectedCityName
  );
  const engTitle = filteredData[0].engTitle;
  const imageUrl = filteredData[ 0 ].imageUrl;
  

  /* 일정 삭제 버튼 눌렀을 때 */
  function deleteSchedule() {
    axios
			.delete(`/tripschedule/${data.scheduleId}`)
			.then((response) => {
				console.log("tripSchedule 삭제 성공:", response)
				window.location.reload()
			})
			.catch((error) => {
				console.error("tripSchedule 삭제 실패:", error)
        alert("일정 삭제 도중 오류가 발생했습니다.")
        // window.location.reload()
			})
    
  }

  return (
    <div className={styles["myschedule-mypage-item"]}>
      {/* 도시 사진 */}
      <div className={styles["myschedule-mypage-photo"]}>
        <div className={styles["myschedule-d-day-text"]}>
          <span>D{dDay < 0 ? dDay : dDay === 0 ? "-day" : `+${dDay}`}</span>
        </div>
        <div className={styles["myschedule-list-image"]}>
          <img src={imageUrl} alt="selectedCityImg" />
        </div>
      </div>

      {/* 도시 이름 */}
      <div className={styles["city-wrap"]}>
        <p className={styles["list-content-fullcityname"]}>
          {data.cityName} 여행
        </p>
        <p className={styles["list-content-cityname"]}>{engTitle}</p>
      </div>

      {/* 기타 정보 */}
      <ul className={styles["schedule-info-list"]}>
        <li className={styles["schedule-info-item"]}>
          <span className={styles["schedule-info-title"]}>선택장소</span>
          <span className={styles["schedule-info-value"]}>{data.placeCnt}</span>
        </li>

        <li className={styles["schedule-info-item"]}>
          <span className={styles["schedule-info-title"]}>여행일자</span>
          <span className={styles["schedule-info-value"]}>
            {startDay} - {endDay}
          </span>
        </li>

        <li className={styles["schedule-info-item"]}>
          <span className={styles["schedule-info-title"]}>최종수정</span>
          <span className={styles["schedule-info-value"]}>
            {data.modDate ? formatDate(data.modDate) : formatDate(data.regDate)}
          </span>
        </li>
      </ul>

      <div className={styles["schedule-info-button"]}>
        <Link to={`/tripSchedule/edit/${data.scheduleId}`}>
          <button>일정 수정</button>
        </Link>
        <button onClick={deleteSchedule}>일정 삭제</button>
        <Link to={`/tripstory/create/${data.scheduleId}`}>
          <button>이야기 생성</button>
        </Link>
      </div>
    </div>
  );
};

export default MySchedule;
