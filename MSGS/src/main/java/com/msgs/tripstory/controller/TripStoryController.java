package com.msgs.tripstory.controller;

import com.msgs.msgs.dto.*;
import com.msgs.msgs.entity.tripstory.StoryComment;
import com.msgs.tripstory.dto.StoryLikeCountDTO;
import com.msgs.tripstory.service.TripStoryService;
import java.util.Map;
import lombok.RequiredArgsConstructor;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.msgs.msgs.imageupload.ImageUploadController;

import jakarta.servlet.http.HttpSession;


import java.util.List;

@RestController // JSON 또는 XML 형식의 데이터를 반환
@RequestMapping("/tripstory")
@RequiredArgsConstructor // final 필드에 대한 생성자 생성
public class TripStoryController {

	private final TripStoryService tripStoryService;



	/* 모든 여행 이야기를 List에 담아서 보냄 */
	@GetMapping("/")
	public List<TripStoryMainDTO> getStoryList(){
		System.out.println("=====Controller=====");
		return tripStoryService.getStoryList();
	}

	@PostMapping("/info")
	public ResponseEntity<Void> saveStory(@RequestBody StoryRequestDTO storyRequestDTO){


		List<String> dateList = storyRequestDTO.getDateList();
		Map<String, Object> storyData = storyRequestDTO.getStoryData();
		Map<Integer, String> dailyComment = storyRequestDTO.getDailyComment();
		Map<Integer, List<StoryBlockDTO>> storyList = storyRequestDTO.getStoryList();
		//String cityName = storyRequestDTO.getCityName();
		String scheduleId = storyRequestDTO.getScheduleId();
		String userToken = storyRequestDTO.getUserToken();

		System.out.println("dateList, planList, cityName 받았다!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		System.out.println(dateList);
		System.out.println(storyData);
		System.out.println(dailyComment);
		System.out.println(storyList);
		System.out.println(scheduleId);
		//System.out.println(userToken);
		Boolean isSuccess = tripStoryService.saveStory(dateList, storyData, dailyComment, storyList, userToken, scheduleId);


		if(isSuccess){
			return ResponseEntity.status(HttpStatus.OK).build();
		}else{
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}







//
//
//
//
//	/* 이야기 상세 정보를 response함 */
//	@GetMapping("/info")
////	public StoryResponseDTO getStoryDetail(@RequestParam int storyId) {
//	public ResponseEntity<String> getStoryDetail(@RequestParam int storyId) {
//
//		//나중에 DB에서 가져온 데이터로 대체해야 함.
//		JSONObject responseObj = getDummyData();
//		String responseStr = responseObj.toString();
//		return ResponseEntity.status(HttpStatus.OK).body(responseStr);
//
//		// storyId 데이터 받아오기
////		System.out.println("storyId ============================================"+ storyId);
//
//		// DB에서 데이터 가져옴
////		return tripStoryService.getStoryDetail(storyId);
//
//
//	}
//
//	/* 프론트(TripStoryCreate.js)에서 받은 tripstory 데이터를 저장함 */
//	@PostMapping("/info")
//	public ResponseEntity<Void> saveStory(@RequestBody StoryRequestDTO storyRequest, HttpSession httpSess) throws Exception{
//
//
//		List<Object> imgList = (List<Object>) storyRequest.getStoryData().get("img");
//	//	List<Object> reviewImgList = (List<Object>) storyRequest.getStoryData();
//
//		System.out.println(imgList);
//
//		ImageUploadController imageUpload = new ImageUploadController();
//
//
//		storyRequest.getStoryData().put("img", imageUpload.uploadFilesSample2(imgList, "/user-tripstory", httpSess));
//
//		Map<Integer, List<StoryBlockDTO>> storyList1 = storyRequest.getStoryList();
////
////
////		for (List<StoryBlockDTO> storyBlockDTOList : storyList1.values()) {
//////		  // StoryBlockDTO 반복문 처리
////		  for (StoryBlockDTO storyBlockDTO : storyBlockDTOList) {
//////		    // reviewImg 가져오기
////
////			  System.out.println(storyBlockDTO.getReviewImg());
////			List<String> data =  imageUpload.uploadFilesSample(storyBlockDTO.getReviewImg(), "/user-tripstory", httpSess);
////
////			List<Object> listObject = new ArrayList<>();
////			for (String str : data) {
////			  listObject.add((Object) str);
////			}
////			storyBlockDTO.setReviewImg(listObject);
//////		    // reviewImg에 데이터 추가
//////		    reviewImageList.add("추가할 데이터");
////		  }
////		}
////		storyRequest.getStoryData().get("reviewImg")put("reviewImgList", imageUpload.uploadFilesSample(reviewImgList, "/user-tripstory", httpSess));
////
////		System.out.println(storyRequest.getStoryData().get("img"));
//
//
//		Map<String, Object> storyData = storyRequest.getStoryData();
//		List<String> dateList = storyRequest.getDateList();
//
//
//		Map<Integer, List<StoryBlockDTO>> storyList = storyRequest.getStoryList();
//		Map<Integer, String> dailyComment = storyRequest.getDailyComment();
////
////		System.out.println("storyData, dateList, storyList, dailyComment 받았다!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
////		System.out.println(storyData);
////		System.out.println(dateList);
////		System.out.println(storyList);
////		System.out.println(dailyComment);
////
//		Boolean isSuccess = tripStoryService.saveStory(storyData, dateList, dailyComment, storyList);
//
//
//
//		if(isSuccess){
//			return ResponseEntity.status(HttpStatus.OK).build();
//		} else {
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//		}
//
//
//
//	}
//










	/* 특정 Story에 대한 1.좋아요 수와 해당 userId가 이 TripStory에 2.좋아요를 눌렀는지 여부 데이터 보냄 */
	@GetMapping("/like")
	public Map<String, Object> storyLike(@RequestParam int storyId) { // 기존 코드에서는 userId도 받았다.
		return tripStoryService.getStoryLike(storyId);
	}


	/* 특정 Story에 대한 좋아요 수가 update 됨*/
	@PostMapping("/updateLike")  //storyId와 userId가 requestBody로 옴.
	public void storyLikeUpdate(@RequestBody String data) {
		System.out.println("storyLikeUpdate 메소드");

		// userId, storyId 데이터 받아오기
		JSONObject requestData = new JSONObject(data);
		String userId = requestData.getString("userId");
		int storyId = requestData.getInt("storyId");
		System.out.println(userId);
		System.out.println(storyId);

		// db 업데이트 하기
	}






	/* 특정 Story에 대한 모든 댓글 보냄*/
	@GetMapping("/comments")
	public List<StoryCommentDTO> getCommentList(@RequestParam int storyId) {

		return tripStoryService.getCommentList(storyId);
	}

	/* 특정 Story에 대한 댓글을 저장함 */
	@PostMapping("/comment")
	public void commentInsert(@RequestBody StoryCommentDTO storyCommentDTO) {
		//requestbody로 storyId, userId, content(댓글내용) 보낸다.
		System.out.println("코멘트 인서트" + storyCommentDTO.getStoryId());
		System.out.println("코멘트 인서트" + storyCommentDTO.getContent());

		// 데이터 DB로 보내기
		tripStoryService.commentInsert(storyCommentDTO);
	}


}
