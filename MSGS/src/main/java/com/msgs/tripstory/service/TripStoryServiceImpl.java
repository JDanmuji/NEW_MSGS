package com.msgs.tripstory.service;


import com.msgs.msgs.dto.PlanBlockDTO;
import com.msgs.msgs.dto.StoryBlockDTO;

import com.msgs.msgs.entity.tripschedule.TripDetailSchedule;

import com.msgs.msgs.dto.StoryResponseDTO;

import com.msgs.msgs.entity.tripschedule.TripSchedule;
import com.msgs.msgs.entity.tripstory.TripStory;
import com.msgs.msgs.entity.tripstory.schedule.StoryDailySchedule;
import com.msgs.msgs.entity.tripstory.schedule.StoryDetailImg;
import com.msgs.msgs.entity.tripstory.schedule.StoryPlace;
import com.msgs.msgs.entity.tripstory.schedule.StoryPlaceID;
import com.msgs.tripschedule.dao.TripScheduleDAO;
import com.msgs.tripstory.dao.StoryDailyDAO;
import com.msgs.tripstory.dao.StoryDetailImgDAO;
import com.msgs.tripstory.dao.StoryPlaceDAO;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


import jdk.swing.interop.SwingInterOpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.msgs.msgs.dto.StoryCommentDTO;
import com.msgs.msgs.dto.TripStoryMainDTO;
import com.msgs.msgs.entity.tripstory.StoryComment;

import com.msgs.msgs.entity.tripstory.StoryImg;

import com.msgs.tripstory.dao.TripStoryDAO;
import com.msgs.tripstory.dao.TripStoryImgDAO;
import com.msgs.tripstory.dto.StoryLikeCountDTO;


import com.msgs.msgs.entity.user.UserEntity;
import com.msgs.msgs.entity.user.UserImg;
import com.msgs.tripstory.dao.StoryCommentDAO;
import com.msgs.user.dao.UserDAO;

import java.util.ArrayList;

import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TripStoryServiceImpl implements TripStoryService {

    @Autowired
    private UserDAO userDAO;

	@Autowired
	private TripScheduleDAO scheduleDAO;

    @Autowired
	private TripStoryDAO storyDAO;

    @Autowired
	private TripStoryImgDAO storyImgDAO;

	@Autowired
	private StoryDailyDAO storyDailyDAO;

	@Autowired
	private StoryPlaceDAO storyPlaceDAO;

	@Autowired
	private StoryDetailImgDAO storyDetailImgDAO;

    @Autowired
    private StoryCommentDAO storyCommentDAO;


	@Override
	public StoryResponseDTO getStoryDetail(int storyId) {

		StoryResponseDTO storyResponseDTO = new StoryResponseDTO();
		Map<String, Object> storyData = new HashMap<>();


		/* story_id 이용해서 StoryEntity 엔티티 가져오기 */
		Optional<TripStory> prevStoryEntity = storyDAO.findById(storyId);
		TripStory storyEntity = prevStoryEntity.get();

		/* [1] Set storyData  */
		storyData.put("cityName", storyEntity.getCityName());
		storyData.put("rating", storyEntity.getRating());
		storyData.put("comment", storyEntity.getComment());
		storyData.put("title", storyEntity.getTitle());
		storyResponseDTO.setStoryData(storyData);


		/* [2] Set dateList  */
		List<String> dateList = new ArrayList<String>(Arrays.asList(storyEntity.getDateList().split(",")));
		storyResponseDTO.setDateList(dateList);

		Map<Integer, String> dailyComment = new HashMap<>();
		List<StoryDailySchedule> storyDailyScheList = storyDailyDAO.findAllByTripStory_Id(storyId);

		Map<Integer, List<StoryBlockDTO>> storyList = new HashMap<>();

		for(StoryDailySchedule storyDailySche: storyDailyScheList){
			int dailyId = storyDailySche.getId();

			List<StoryPlace> storyPlaceList = storyPlaceDAO.findAllByStoryDailySchedule_Id(dailyId);
			List<StoryBlockDTO> storyBlockDTOList = new ArrayList<>();

			for(StoryPlace storyPlace: storyPlaceList){
				/* [3] Set dailyComment  */
				dailyComment.put(storyPlace.getOrderDay(), storyDailySche.getComment());

				StoryBlockDTO storyBlockDTO = new StoryBlockDTO();
				storyBlockDTO.setOrder(storyPlace.getOrderId());
				storyBlockDTO.setPlaceOrder(storyPlace.getPlaceOrder());
				storyBlockDTO.setTitle(storyPlace.getTitle());
				storyBlockDTO.setType(storyPlace.getType());

				storyBlockDTO.setLocation(storyPlace.getLocation());
				storyBlockDTO.setMapx(storyPlace.getMapx());
				storyBlockDTO.setMapy(storyPlace.getMapy());
				storyBlockDTO.setContentid(storyPlace.getContentid());
				storyBlockDTO.setRating(storyPlace.getRating());
				storyBlockDTO.setComment(storyPlace.getComment());


//				List<StoryDetailImg> storyDetailImgList = storyDetailImgDAO.findAllByStoryPlaceOrderIdAndStoryPlaceDailyId(storyPlace.getOrderId(), dailyId);
//
//				storyBlockDTO.setImgOriginName(storyDetailImgList.get(0).getImgOriginName());
//				storyBlockDTO.setImgPath(storyDetailImgList.get(0).getImgPath());

				storyBlockDTOList.add(storyBlockDTO);

			}
			storyList.put(storyPlaceList.get(0).getOrderDay(), storyBlockDTOList );

		}

		/* [4] Set storyList  */
		storyResponseDTO.setStoryList(storyList);

		return storyResponseDTO;
	}





	@Override
	@Transactional
	//storyList(tripStoryCreate 페이지에서 입력한 여행기) 저장
	public Boolean saveStory(
		Map<String, Object> storyData,
		List<String> dateList,
		Map<Integer, String> dailyComment,
		Map<Integer, List<StoryBlockDTO>> storyList) {

		System.out.println("s11111111111111111111111111111111111111111111111111111111111111111111111");

		/*TRIP_STORY 엔티티에 저장*/
		Optional<UserEntity> userEntity = userDAO.findById("0f82a90f9f96402"); // id 이용해서 UserEntity 엔티티 가져오기 */

		//UserEntity resultUserEntity = userEntity.get();

	//	if (!userEntity.isPresent()) {
	//		return false;
	//	}
System.out.println(userEntity);
System.out.println(userEntity.get());
		UserEntity resultUserEntity = userEntity.get();
		System.out.println("S2222222222222222222222222222222222222222222222222222222222222222");


System.out.println(storyData.get("schedule_id").toString());
		Optional<TripSchedule> scheduleEntity = scheduleDAO.findById(
			Integer.parseInt(storyData.get("schedule_id").toString())
		); // schedule_id 이용해서 SchduleEntity 엔티티 가져오기 */

		TripSchedule resultScheduleEntity = scheduleEntity.get();

		System.out.println("S333333333333333333333333333333333333333333333333333333333333333");

		System.out.println(resultUserEntity.getId());
		System.out.println(resultScheduleEntity.getId());

		Optional<TripStory> tripStoryData = storyDAO.findById(resultScheduleEntity.getId());

	System.out.println(resultUserEntity);
	TripStory tripStory = tripStoryData.isEmpty() ? new TripStory():  tripStoryData.get();
		tripStory.setUserTripStory(resultUserEntity);
		tripStory.setTripSchedule(resultScheduleEntity);
		tripStory.setTitle(storyData.get("title").toString());
		tripStory.setRating(Integer.parseInt(storyData.get("rating").toString()));
		tripStory.setComment(storyData.get("comment").toString());
		tripStory.setDateList(String.join(",", dateList));
		tripStory.setCityName(storyData.get("cityName").toString());


		/*TRIP_STORY 테이블에 레코드 저장*/
		TripStory savedTripStory = null;
		savedTripStory = storyDAO.saveAndFlush(tripStory); //DB에 저장 -> id 얻어오기 위함




		if(storyData.get("img").toString().length() > 0) {

			List<String> data = (List<String>) storyData.get("img");



			for (String imagePath : data) {


				StoryImg storyImg = new StoryImg();
				StoryImg savedStoryImg = null;
				storyImg.setTripStoryImg(savedTripStory);
				storyImg.setImgOriginName(imagePath.substring(imagePath.lastIndexOf("/") + 1));
				storyImg.setImgPath(imagePath);

				savedStoryImg = storyImgDAO.saveAndFlush(storyImg);
			}
		}



		System.out.println("S44444444444444444444444444444444444444444444444444444444444");

		for (Map.Entry<Integer, String> commentEntry : dailyComment.entrySet()) {
			/*STORY_DAILY 엔티티에 저장*/
			StoryDailySchedule storyDaily = new StoryDailySchedule();
			storyDaily.setTripStory(savedTripStory);
			storyDaily.setComment(commentEntry.getValue());

			/*STORY_DAILY 테이블에 레코드 저장*/
			StoryDailySchedule savedStoryDaily = null;
			savedStoryDaily = storyDailyDAO.saveAndFlush(storyDaily);

			System.out.println("S5555555555555555555555555555555555555555555555555555555555");

			for (Map.Entry<Integer, List<StoryBlockDTO>> entry : storyList.entrySet()){

				//STORY_PLACE의 id가 복합키이므로 ID 클래스에 우선 저장
//				StoryPlaceID storyPlaceID = new StoryPlaceID();
//				storyPlaceID.setOrderId(entry.getKey());
//				storyPlaceID.setStoryDailySchedule(savedStoryDaily);

				List<StoryBlockDTO> storyBlocks = entry.getValue() ;

				for(StoryBlockDTO storyblock : storyBlocks){

					/*STORY_PLACE 엔티티에 저장*/
					StoryPlace storyPlace = new StoryPlace();
					storyPlace.setOrderId(storyblock.getOrder());
					storyPlace.setStoryDailySchedule(savedStoryDaily);
					storyPlace.setOrderDay(entry.getKey());
					storyPlace.setPlaceOrder(storyblock.getPlaceOrder());
					storyPlace.setTitle(storyblock.getTitle());
					storyPlace.setType(storyblock.getType());

					storyPlace.setLocation(storyblock.getLocation());
					storyPlace.setMapx(storyblock.getMapx());
					storyPlace.setMapy(storyblock.getMapy());

					storyPlace.setContentid(storyblock.getContentid());
					storyPlace.setRating(storyblock.getRating());
					storyPlace.setComment(storyblock.getComment());


					/*STORY_PLACE 테이블에 레코드 저장*/
					StoryPlace savedStoryPlace = null;
					savedStoryPlace = storyPlaceDAO.saveAndFlush(storyPlace);

					System.out.println("S666666666666666666666666666666666666666666666666666");

					/*STORY_DETAIL_IMG 엔티티에 저장*/
//					if(!storyblock.getReviewImg().isEmpty()){
//						//해당 장소에 대해 유저가 업로드한 이미지가 있는 경우
//
//						List<Object> data = storyblock.getReviewImg();
//
//
//
//						for (Object imagePath : data) {
//
//
//
//
//							StoryDetailImg storyDetailImg = new StoryDetailImg();
//							storyDetailImg.setStoryPlace(savedStoryPlace);
//							storyDetailImg.setImgPath(imagePath.toString());
//							storyDetailImg.setImgOriginName(imagePath.toString().substring(imagePath.toString().lastIndexOf("/") + 1));
//							storyDetailImgDAO.saveAndFlush(storyDetailImg);
//						}
//
//
//
//					}else{
//
//						continue;
//					}



				}


			}



		}


		return true;
	}



	@Override
	public List<StoryCommentDTO> getCommentList(int storyId) {
        List<Object[]> queryResult = storyCommentDAO.findAllWithUserAndImg(storyId);

        List<StoryCommentDTO> resultList = new ArrayList<>(); // 반환받을 DTO

        for(Object[] result : queryResult) {
        	StoryComment storyComment = (StoryComment) result[0];
        	UserEntity userEntity = (UserEntity) result[1];
        	UserImg userImg = (UserImg) result[2];
        	System.out.println("=======getCommentList===========" + result);

            StoryCommentDTO storyCommentDTO = new StoryCommentDTO(); // StoryCommentDTO 객체 생성

            storyCommentDTO.setUserId(userEntity.getId());
            storyCommentDTO.setUserName(userEntity.getName());
            storyCommentDTO.setSeq(storyComment.getSeq());
            storyCommentDTO.setContent(storyComment.getContent());
            storyCommentDTO.setStoryId(storyComment.getTripStoryCmnt().getId());

            if(userImg != null) {
        		storyCommentDTO.setUserImgPath(userImg.getImgPath());
        	}
            // 리스트에 댓글 하나 더함
        	resultList.add(storyCommentDTO);
        }
		return resultList;
	}

	@Override
	public void storyLike(StoryLikeCountDTO storyLikeCountDTO) {

		storyLikeCountDTO.setStoryId("");

		storyLikeCountDTO.setUserId("msgs01");
//		tripStoryDAO.save(storyLikeCountDTO);
	}


	@Override
	public void commentInsert(StoryCommentDTO storyCommentDTO) {
		StoryComment storyComment = new StoryComment();

		// seq 값은 자동 생성되므로 set 사용 X
		storyComment.setContent(storyCommentDTO.getContent());
		storyComment.setRegDate(storyCommentDTO.getRegDate());
		storyComment.setModDate(storyCommentDTO.getModDate());

		// storyId 이용한 TripStory 엔티티 반환
		Optional<TripStory> tripStory = storyDAO.findById(storyCommentDTO.getStoryId());
		if(tripStory.isPresent()) {
			TripStory resultTripStory = tripStory.get();
			storyComment.setTripStoryCmnt(resultTripStory);
		}

		// userId 이용한 UserEntity 엔티티 반환
		Optional<UserEntity> userEntity = userDAO.findById(storyCommentDTO.getUserId());
		if(userEntity.isPresent()) {
			UserEntity resultUserEntity = userEntity.get();
			storyComment.setUserStoryCmnt(resultUserEntity);
		}

		storyCommentDAO.save(storyComment);
	}



	@Override
	public List<TripStoryMainDTO> getStoryList() {
        List<Object[]> queryResult = storyDAO.findAllWithStoryImgsAndUserAndImg(); // 반환받은 Entity


        List<TripStoryMainDTO> resultList = new ArrayList<>(); // 반환받을 DTO

        for(Object[] result : queryResult) {
        	TripStory tripStory = (TripStory) result[0];
        	UserEntity userEntity = (UserEntity) result[1];
        	UserImg userImg = (UserImg) result[2];
        	StoryImg storyImg = (StoryImg) result[3];

        	TripStoryMainDTO tripStoryMainDTO = new TripStoryMainDTO(); // TripStoryMainDTO 객체 생성

        	tripStoryMainDTO.setStoryId(tripStory.getId());
        	tripStoryMainDTO.setScheduleId(tripStory.getTripSchedule().getId());
        	tripStoryMainDTO.setTitle(tripStory.getTitle());
        	tripStoryMainDTO.setDateList(tripStory.getDateList());
        	tripStoryMainDTO.setComment(tripStory.getComment());
        	tripStoryMainDTO.setUserId(userEntity.getId());
        	tripStoryMainDTO.setUserName(userEntity.getName());

            if (userImg != null && storyImg != null) {
                tripStoryMainDTO.setUserImgPath(userImg.getImgPath());
                tripStoryMainDTO.setStoryImgOriginName(storyImg.getImgOriginName());
                tripStoryMainDTO.setStoryImgPath(storyImg.getImgPath());
            } else if (userImg != null) {
                tripStoryMainDTO.setUserImgPath(userImg.getImgPath());
            } else if (storyImg != null) {
                tripStoryMainDTO.setStoryImgOriginName(storyImg.getImgOriginName());
                tripStoryMainDTO.setStoryImgPath(storyImg.getImgPath());
            } else {
                System.out.println("하이!");
            }

        	System.out.println("=======getStoryImgs===========" + tripStoryMainDTO.getDateList());
        	resultList.add(tripStoryMainDTO);

        }

		return resultList;

	}

	// 삭제 예정
//	@Override
//	public List<StoryComment> storyCommentsList() {
//		return null;
//	}

}
