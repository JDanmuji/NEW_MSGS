package com.msgs.tripschedule.service;

import com.msgs.msgs.dto.PlanBlockDTO;
import com.msgs.msgs.entity.tripschedule.TripDailySchedule;
import com.msgs.msgs.entity.tripschedule.TripDetailSchedule;
import com.msgs.msgs.entity.tripschedule.TripSchedule;
import com.msgs.msgs.entity.user.UserEntity;
import com.msgs.tripschedule.repository.TripscheduleRepository;
import com.msgs.user.dao.UserDAO;
import java.time.LocalDateTime;
import java.util.Arrays;
import com.google.gson.Gson;
import com.msgs.msgs.dto.PlaceInfoDTO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Collections;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TripScheduleService {

    @Value("${tourApi.decodingKey}")
    private String decodingKey;

    List<Integer> contentTypeIds = Arrays.asList(12, 39); //place의 contentId 저장해놓음. 12=관광지, 39=음식점

    Gson gson = new Gson();

    private final UserDAO userDAO;
    private final TripscheduleRepository scheduleRepo;





    public List<PlaceInfoDTO> getDormList(int areaCode, List<Integer> sigunguCodeList){

        WebClient wc = WebClient.builder().baseUrl("http://apis.data.go.kr/B551011/KorService1/areaBasedList1")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE)
            .build();


        List<PlaceInfoDTO> joined = new ArrayList<PlaceInfoDTO>();

        String url_1 =
            "?MobileOS=ETC" +
                "&MobileApp=MSGS" +
                "&pageNo=1" +
                "&numOfRows=50" + //50개 출력됨.
                "&arrange=Q" +
                "&contentTypeId=32" + //숙박
                "&areaCode=" + areaCode;


        //시군구 코드 Ex[1, 5, 7] 반복문
        for(int sigunguCode : sigunguCodeList ){
            System.out.println("시군구코드!!!!!!!!!!! = " + sigunguCode);

            //sigunguCode가 없는 경우면 url에서 뺀다.
            String url_2 = sigunguCode == 0 ? "" : "&sigunguCode=" + sigunguCode;
            String url =  url_1 + url_2 + "&serviceKey={serviceKey}";

            System.out.println("url!!!!!!!!!!! = "+ url);

            String response = wc.get()
                .uri(url, decodingKey)
                .retrieve()
                .bodyToMono(String.class) //MonoFlatMap형 리턴함
                .block();

            JSONObject items = XML.toJSONObject(response.toString()).getJSONObject("response").getJSONObject("body").getJSONObject("items");
            JSONArray item = items.getJSONArray("item"); //item = [{...}, {...}, ...]

            // title값 가공
            JSONArray filteredArray = new JSONArray();

            // 제목 뒤의 부가설명 지우기
            for(int i = 0; i < item.length(); i++){
                JSONObject filteredItem = item.getJSONObject(i);
                String firstimage2 = filteredItem.getString("firstimage2");
                String title = filteredItem.getString("title");

                //이미지나 타이틀 정보 없는 경우 뺀다.
                if(firstimage2.isEmpty() || title.isEmpty()) {
                    continue;
                }

                String modifiedTitle = (title.indexOf("[") != -1 && title.indexOf("]") != -1)
                    ? title.substring(0, title.indexOf("["))
                    : title;

                filteredItem.put("title", modifiedTitle.trim());
                filteredArray.put(filteredItem);

            }

            PlaceInfoDTO[] arr = gson.fromJson(filteredArray.toString(), PlaceInfoDTO[].class);
            List<PlaceInfoDTO> list = Arrays.asList(arr);

            joined.addAll(list); //시군구 코드 여러개인 경우 하나의 리스트로 합친다.


        } ////시군구 코드 Ex [1, 5, 7] 반복문 End

        Collections.shuffle(joined);
        System.out.println(joined);
        return joined;
    }



    public List<PlaceInfoDTO> getPlaceList(int areaCode, List<Integer> sigunguCodeList){

        WebClient wc = WebClient.builder().baseUrl("http://apis.data.go.kr/B551011/KorService1/areaBasedList1")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE)
            .build();

        List<PlaceInfoDTO> joined = new ArrayList<PlaceInfoDTO>();

        String url_1 =
            "?MobileOS=ETC" +
                "&MobileApp=MSGS" +
                "&pageNo=1" +
                "&numOfRows=30" + //30개 출력됨.
                "&arrange=Q" +
                "&areaCode=" + areaCode;

        //시군구 코드 Ex[1, 5, 7] 반복문
        for(int sigunguCode : sigunguCodeList ){
            for(int contentTypeId: contentTypeIds){ //12=관광지, 39=음식점

                String url_2 = sigunguCode == 0
                    ? "&contentTypeId=" + contentTypeId //관광지 or 음식점
                    : "&contentTypeId=" + contentTypeId + "&sigunguCode=" + sigunguCode;

                String url =  url_1 + url_2 + "&serviceKey={serviceKey}";


                String response = wc.get()
                    .uri(url, decodingKey)
                    .retrieve()
                    .bodyToMono(String.class) //MonoFlatMap형 리턴함
                    .block();


                JSONObject items = XML.toJSONObject(response.toString()).getJSONObject("response").getJSONObject("body").getJSONObject("items");
                JSONArray item = items.getJSONArray("item");

                // title값 가공
                JSONArray filteredArray = new JSONArray();

                // 제목 뒤의 부가설명 지우기
                for(int i = 0; i < item.length(); i++){
                    JSONObject filteredItem = item.getJSONObject(i);
                    String title = filteredItem.getString("title");
                    String firstimage2 = filteredItem.getString("firstimage2");

                    //이미지나 타이틀 정보 없는 경우 뺀다.
                    if(firstimage2.isEmpty() || title.isEmpty()) {
                        continue;
                    }

                    String modifiedTitle = (title.indexOf("[") != -1 && title.indexOf("]") != -1)
                        ? title.substring(0, title.indexOf("["))
                        : title;

                    filteredItem.put("title", modifiedTitle.trim());
                    filteredArray.put(filteredItem);

                }

                PlaceInfoDTO[] arr = gson.fromJson(item.toString(), PlaceInfoDTO[].class);
                List<PlaceInfoDTO> list = Arrays.asList(arr);

                joined.addAll(list); //시군구 코드 여러개인 경우 하나의 리스트로 합친다.
            }

        } ////시군구 코드 Ex[1, 5, 7] 반복문 End

        Collections.shuffle(joined);
        return joined;


    };




    @Transactional   /* planList(tripSchedule 페이지에서 입력한 일정)를 저장함 */
    public Boolean saveSchedule(List<String> dateList, Map<Integer, List<PlanBlockDTO>> planList, String cityName){

        try{
            Optional<UserEntity> userEntity = userDAO.findById("yhg9801"); // id 이용해서 User엔티티 가져오기 */
            UserEntity resultUserEntity = userEntity.get();

            /*TRIP_SCHEDULE*/
            TripSchedule tripSchedule = new TripSchedule();
            tripSchedule.setUserEntity(resultUserEntity);
            tripSchedule.setCityName(cityName);
            tripSchedule.setDateList( String.join(",", dateList) );

            try{
                scheduleRepo.saveSchedule(tripSchedule); //TRIP_SCHEDULE을 DB에 저장 -> id 얻어오기 위함
            }catch(Exception e){
                System.out.println("scheduleDAO.saveAndFlush(tripSchedule) 에서 에러남=================================");
            }

            for (Map.Entry<Integer, List<PlanBlockDTO>> entry : planList.entrySet()) {
                /*TRIP_DAILY_SCHEDULE에 저장*/
                TripDailySchedule tripDaily = new TripDailySchedule();
                tripDaily.setTripSchedule(tripSchedule);
                scheduleRepo.saveDailySchedule(tripDaily); // DB에 저장


                int day = entry.getKey(); // DAY1
                List<PlanBlockDTO> planBlocks = entry.getValue(); // PlanBlockDTO 목록


                /*TRIP_DETAIL_SCHEDULE에 저장*/
                for(PlanBlockDTO planBlockDTO : planBlocks) {

                    TripDetailSchedule tripDetail = new TripDetailSchedule();
                    tripDetail.setTripDailySchedule(tripDaily);
                    tripDetail.setOrderDay(day);
                    tripDetail.setOrder(planBlockDTO.getOrder());
                    tripDetail.setPlaceOrder(planBlockDTO.getPlaceOrder());
                    tripDetail.setTitle(planBlockDTO.getTitle());
                    tripDetail.setType(planBlockDTO.getType());
                    tripDetail.setLocation(planBlockDTO.getLocation());
                    tripDetail.setMapx(planBlockDTO.getMapx());
                    tripDetail.setMapy(planBlockDTO.getMapy());
                    tripDetail.setContentid(planBlockDTO.getContentid());
                    tripDetail.setFirstimage2(planBlockDTO.getFirstimage2());

                    scheduleRepo.saveDetailSchedule(tripDetail);

                }

            }

        }catch (Exception e) {
            System.out.println(e);
            return false;
        }

        return true;
    }



    //해당 schedule_id 에 해당하는 여행일정 정보 반환함
    public Map<String, Object> getSchedule(int scheduleId){

        Map<String, Object> responseMap = new HashMap<>();

        /* schedule_id 이용해서 SchduleEntity 엔티티 가져오기 */
        TripSchedule resultSchedule = scheduleRepo.findById(scheduleId);

        System.out.println("Sche111111111111111111111111111111111111111111111111");
        /* [1] areaTitle */
        String areaTitle = resultSchedule.getCityName();
        System.out.println("areaTitle" + areaTitle);
        responseMap.put("areaTitle", areaTitle);

        /* [2] dateList */
        List<String> dateList = new ArrayList<String>(Arrays.asList(
            resultSchedule.getDateList().split(",")));
        responseMap.put("dateList", dateList);

        /* [3] planList */
        Map<Integer, List<PlanBlockDTO>> planList = new HashMap<>();
        List<TripDailySchedule> dailyScheList = scheduleRepo.findAllDailyBySchedule(resultSchedule);

        for (TripDailySchedule dailySchedule: dailyScheList){
            System.out.println("sche222222222222222222222222222222222222222222222222222222222222222222");
            List<TripDetailSchedule> detailScheList = scheduleRepo.findAllDetailByDaily(dailySchedule);


            List<PlanBlockDTO> planBlockList = new ArrayList<>();

            for (TripDetailSchedule detailSchedule : detailScheList){

                PlanBlockDTO planblock = new PlanBlockDTO();
                planblock.setOrder(detailSchedule.getOrder());
                planblock.setPlaceOrder(detailSchedule.getPlaceOrder());
                planblock.setTitle(detailSchedule.getTitle());
                System.out.println("getTitle"+ detailSchedule.getTitle());
                planblock.setType(detailSchedule.getType());
                planblock.setLocation(detailSchedule.getLocation());

                planblock.setMapx(detailSchedule.getMapx());
                planblock.setMapy(detailSchedule.getMapy());
                planblock.setContentid(detailSchedule.getContentid());
                planblock.setFirstimage2(detailSchedule.getFirstimage2());

                System.out.println("sche3333333333333333333333333333333333333333");

                // 리스트에 PlanBlockDTO 객체 추가
                planBlockList.add(planblock);

                // Map에 Key(몇일차)와 함께 리스트(일정블록 객체들이 있는 배열)를 추가
                planList.put(detailSchedule.getOrderDay(), planBlockList);


            }

        }
        responseMap.put("planList", planList);

        return responseMap;

    }






    @Transactional /*planList(tripSchedule 페이지에서 수정한 일정)를 업데이트 함 */
    public Boolean updateSchedule(int scheduleId, Map<Integer, List<PlanBlockDTO>> planList){

        try{
            UserEntity user = userDAO.findById("yhg9801").get();

            /*TRIP_SCHEDULE 업데이트 -> mod_date(수정 시간) 입력하기 위함*/
            TripSchedule tripSchedule = scheduleRepo.findById(scheduleId);
            tripSchedule.setModDate(LocalDateTime.now()); //변경 감지 사용 -> mod_date(수정 시간) 입력

            /*scheduleId를 외래키로 갖고 있는 TRIP_DAILY_SCHEDULE의 레코드들을 삭제 -> detailSchedule도 cascade로 삭제됨 */
            List<TripDailySchedule> dailyScheList = scheduleRepo.findAllDailyBySchedule(tripSchedule);


            //detailSchedule 삭제
            for (TripDailySchedule dailySchedule: dailyScheList) { //Day 1, 2, 3
                int updatedRows = scheduleRepo.deleteDetailByDaily(dailySchedule);
                System.out.println("!!!!!!!!!!!!!!updatedRows:"+ updatedRows);
            }


            for (Map.Entry<Integer, List<PlanBlockDTO>> entry : planList.entrySet()) { //Day 1,2,3 있으면 3번 반복함
                int day = entry.getKey(); // DAY1
                List<PlanBlockDTO> planBlocks = entry.getValue(); // PlanBlockDTO 목록
                System.out.println("12/8 3333333333333333333333333333333333333");

                /*TRIP_DETAIL_SCHEDULE에 저장*/
                for(PlanBlockDTO planBlockDTO : planBlocks) {
                    TripDetailSchedule tripDetail = new TripDetailSchedule();
                    //tripDetail.setTripDailySchedule(tripDaily);
                    tripDetail.setTripDailySchedule(dailyScheList.get(day-1));
                    tripDetail.setOrderDay(day);
                    tripDetail.setOrder(planBlockDTO.getOrder());
                    tripDetail.setPlaceOrder(planBlockDTO.getPlaceOrder());
                    tripDetail.setTitle(planBlockDTO.getTitle());
                    tripDetail.setType(planBlockDTO.getType());
                    tripDetail.setLocation(planBlockDTO.getLocation());
                    tripDetail.setMapx(planBlockDTO.getMapx());
                    tripDetail.setMapy(planBlockDTO.getMapy());
                    tripDetail.setContentid(planBlockDTO.getContentid());
                    tripDetail.setFirstimage2(planBlockDTO.getFirstimage2());

                    scheduleRepo.saveDetailSchedule(tripDetail);

                }

            }

        }catch (Exception e) {
            return false;
        }

        return true;


    }

    @Transactional   /*해당 tripschedule을 삭제 */
    public void deleteSchedule(int id) {
        TripSchedule schedule = scheduleRepo.findById(id);
        schedule.setDeleted(true);
    }
}