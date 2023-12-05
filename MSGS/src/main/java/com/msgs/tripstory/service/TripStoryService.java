package com.msgs.tripstory.service;


import com.msgs.msgs.dto.*;

import com.msgs.msgs.entity.tripschedule.TripSchedule;
import com.msgs.msgs.entity.tripstory.TripStory;
import com.msgs.msgs.entity.tripstory.schedule.StoryDailySchedule;
import com.msgs.msgs.entity.tripstory.schedule.StoryPlace;
import com.msgs.msgs.imageupload.ImageUploadController;
import com.msgs.msgs.jwt.JwtTokenProvider;
import com.msgs.tripschedule.dao.TripScheduleDAO;
import com.msgs.tripschedule.repository.TripscheduleRepository;
import com.msgs.tripstory.dao.StoryDailyDAO;
import com.msgs.tripstory.dao.StoryDetailImgDAO;
import com.msgs.tripstory.dao.StoryPlaceDAO;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


import com.msgs.tripstory.repository.TripStoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.msgs.msgs.entity.tripstory.StoryComment;

import com.msgs.msgs.entity.tripstory.StoryImg;

import com.msgs.tripstory.dao.TripStoryDAO;
import com.msgs.tripstory.dao.TripStoryImgDAO;


import com.msgs.msgs.entity.user.UserEntity;
import com.msgs.msgs.entity.user.UserImg;
import com.msgs.tripstory.dao.StoryCommentDAO;
import com.msgs.user.dao.UserDAO;

import java.util.ArrayList;

import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TripStoryService {

    private final UserDAO userDAO;

	@Value("${jwt.secret}")
	private String secretKey;

//	@Value("${imgPath.story}")
//	private String storyImgPath;

	//private final TripScheduleDAO scheduleDAO;

	private final TripStoryDAO storyDAO;

	private final TripStoryImgDAO storyImgDAO;

	private final StoryDailyDAO storyDailyDAO;

	private final StoryPlaceDAO storyPlaceDAO;

	private final StoryDetailImgDAO storyDetailImgDAO;

    private final StoryCommentDAO storyCommentDAO;

	private final TripStoryRepository storyRepo;

	private final TripscheduleRepository scheduleRepo;

	private final ImageUploadController imageUpload;


	/* 이야기 상세페이지 내용 가져오기 */
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




	/* 이야기 상세 댓글 */
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


	/* 특정 TripStory에 대한 1.좋아요 수와 현재 로그인한 아이디가 이 TripStory에 2.좋아요를 눌렀는지 여부 데이터 보냄 */
	public Map<String, Object> getStoryLike(int storyId) {

		//예시코드
		//storyRepo.findStoryLike(stodyId)
		int likeCnt = 17;
		Boolean isLiked = true;

		Map<String, Object> responseMap = new HashMap<>();
		responseMap.put("likeCnt", likeCnt);
		responseMap.put("isLiked", isLiked);

		return responseMap;

		//tripStoryDAO.save(storyLikeCountDTO);
	}


	@Transactional
	public void commentInsert(StoryCommentDTO storyCommentDTO) {
		StoryComment storyComment = new StoryComment();

		// seq 값은 자동 생성되므로 set 사용 X
		storyComment.setContent(storyCommentDTO.getContent());
//		storyComment.setRegDate(storyCommentDTO.getRegDate());
//		storyComment.setModDate(storyCommentDTO.getModDate());

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



	/* 이야기 목록 가져오기 */
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


	/* 이야기 생성 */
	@Transactional
	public Boolean saveStory(List<String> dateList, Map<String, Object> storyData, Map<Integer, String> dailyComment,
							 Map<Integer, List<StoryBlockDTO>> storyList, String userToken, String scheduleId) {

		try {
			JwtTokenProvider token = new JwtTokenProvider(secretKey);
			System.out.println(token.getAuthentication(userToken));

			//Optional<UserEntity> userEntity = userDAO.findById(token.getAuthentication(userToken).toString());

			Optional<UserEntity> userEntity = userDAO.findById("yhg9801");
			UserEntity resultUserEntity = userEntity.orElseThrow();

			Optional<TripSchedule> tripSchedule = Optional.ofNullable(scheduleRepo.findById(Integer.parseInt(scheduleId)));
			TripSchedule resultTripSchedule = tripSchedule.orElseThrow();

			TripStory tripStory = createTripStory(resultUserEntity, resultTripSchedule, storyData, dateList);

			try {
				storyRepo.saveStory(tripStory);
			} catch (Exception e) {
				System.out.println("Error saving TripStory: " + e.getMessage());
				return false;
			}

			for (Map.Entry<Integer, List<StoryBlockDTO>> entry : storyList.entrySet()) {
				StoryDailySchedule storyDaily = createAndSaveStoryDaily(tripStory, entry.getKey());

				for (StoryBlockDTO storyBlockDTO : entry.getValue()) {
					createAndSaveStoryPlace(storyDaily, entry.getKey(), storyBlockDTO);
				}
			}

		} catch (Exception e) {
			System.out.println("Exception during story creation: " + e.getMessage());
			return false;
		}

		return true;
	}

	private TripStory createTripStory(UserEntity userEntity, TripSchedule tripSchedule,
									  Map<String, Object> storyData, List<String> dateList) throws Exception {
		TripStory tripStory = new TripStory();
		tripStory.setUserTripStory(userEntity);
		tripStory.setTripSchedule(tripSchedule);
		tripStory.setTitle(storyData.get("title").toString());
		tripStory.setComment(storyData.get("comment").toString());
		tripStory.setRating(Integer.parseInt(storyData.get("rating").toString()));
		tripStory.setCityName(storyData.get("cityName").toString());



		List<StoryImg> storyImgList = imageUpload.uploadFilesSample2((List<Object>) storyData.get("img"), "/story");

		for (StoryImg storyImgInfo : storyImgList) {
			tripStory.addStoryImgs(storyImgInfo);
		}


//		tripStory.setStoryImgs();

//			//tripStory.setStoryComments();
//			//tripStory.setStoryLikes();
		tripStory.setDateList(String.join(",", dateList));
		return tripStory;
	}

	private StoryDailySchedule createAndSaveStoryDaily(TripStory tripStory, int day) {
		StoryDailySchedule storyDaily = new StoryDailySchedule();
		storyDaily.setTripStory(tripStory);
		storyRepo.saveDailyStory(storyDaily);
		return storyDaily;
	}

	private void createAndSaveStoryPlace(StoryDailySchedule storyDaily, int day, StoryBlockDTO storyBlockDTO) {

		StoryPlace storyPlace = new StoryPlace();

		storyPlace.setOrderId(storyBlockDTO.getOrder());
		storyPlace.setStoryDailySchedule(storyDaily);
		storyPlace.setOrderDay(day);
		storyPlace.setPlaceOrder(storyBlockDTO.getPlaceOrder());
		storyPlace.setTitle(storyBlockDTO.getTitle());
		storyPlace.setType(storyBlockDTO.getType());
		storyPlace.setLocation(storyBlockDTO.getLocation());
		storyPlace.setMapx(storyBlockDTO.getMapx());
		storyPlace.setMapy(storyBlockDTO.getMapy());
		storyPlace.setContentid(storyBlockDTO.getContentid());

		try {
			storyRepo.saveDetailStory(storyPlace);
		} catch (Exception e) {
			System.out.println("Error saving StoryPlace: " + e.getMessage());
		}
	}
}
