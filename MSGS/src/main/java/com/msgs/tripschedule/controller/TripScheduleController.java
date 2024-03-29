package com.msgs.tripschedule.controller;


import com.msgs.msgs.dto.PlaceInfoDTO;
import com.msgs.msgs.dto.PlanBlockDTO;
import com.msgs.msgs.dto.ScheduleRequestDTO;
import com.msgs.tripschedule.service.TripScheduleService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController  // JSON 형식의 데이터 반환
@RequestMapping("/tripschedule")
@CrossOrigin(origins={"localhost:3000"})
@RequiredArgsConstructor
public class TripScheduleController {

    private final TripScheduleService tripScheduleService;


    //해당 areaCode, sigunguCode 에 해당하는 숙박 정보
    @GetMapping("/dormInfo")
    public List<PlaceInfoDTO> getDormList(@RequestParam int areaCode, @RequestParam List<Integer> sigunguCodeList) {
        System.out.println(sigunguCodeList.getClass().getName());
        System.out.println(sigunguCodeList);
        return tripScheduleService.getDormList(areaCode, sigunguCodeList);
    }


    //해당 areaCode, sigunguCode 에 해당하는 관광지, 음식점 정보
    @GetMapping("/placeInfo")
    public List<PlaceInfoDTO> getPlaceList(@RequestParam int areaCode, @RequestParam List<Integer> sigunguCodeList) {
        return tripScheduleService.getPlaceList(areaCode, sigunguCodeList);
    }


    //프론트에서 받은 여행일정 데이터를 DB에 저장함
    @PostMapping("/info")
    public ResponseEntity<Void> saveSchedule(@RequestBody ScheduleRequestDTO scheduleRequest){

        List<String> dateList = scheduleRequest.getDateList();
        Map<Integer, List<PlanBlockDTO>> planList = scheduleRequest.getPlanList();
        String cityName = scheduleRequest.getCityName();
        String userToken = scheduleRequest.getUserToken();


        System.out.println("dateList, planList, cityName 받았다!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println(dateList);
        System.out.println(planList);
        System.out.println(cityName);
        System.out.println(userToken);

        Boolean isSuccess = tripScheduleService.saveSchedule(dateList, planList, cityName, userToken);



        if(isSuccess){
            return ResponseEntity.status(HttpStatus.OK).build();
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }



    // Edit TripSchedule 하기 위해, 해당 schedule_id 에 해당하는 여행일정 정보 반환함
    @GetMapping("/info")
    public Map<String, Object> getSchedule(@RequestParam int scheduleId) {
        return tripScheduleService.getSchedule(scheduleId);
    }



    //프론트에서 받은 여행일정 데이터를 DB에 Update함
    @PutMapping("/info/{scheduleId}")
    public ResponseEntity<Void> updateSchedule(@PathVariable int scheduleId, @RequestBody ScheduleRequestDTO scheduleRequest){
        Map<Integer, List<PlanBlockDTO>> planList = scheduleRequest.getPlanList();
        Boolean isSuccess = tripScheduleService.updateSchedule(scheduleId, planList);

        if(isSuccess){
            return ResponseEntity.status(HttpStatus.OK).build();
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    //해당 여행일정을 DB에서 삭제함
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSchedule(@PathVariable int id) {
        try {
            tripScheduleService.deleteSchedule(id);
            return new ResponseEntity<>("리소스 삭제 성공", HttpStatus.OK);

        } catch(Exception e) {
            return new ResponseEntity<>("리소스 삭제 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

}
