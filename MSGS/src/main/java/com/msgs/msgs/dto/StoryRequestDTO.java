package com.msgs.msgs.dto;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoryRequestDTO {
//    private Map<String, Object> storyData; //schdeuleId, cityName, title, rating, comment
//
//    private List<String> dateList;
//    private Map<Integer, List<StoryBlockDTO>> storyList;
//    private Map<Integer, String> dailyComment;
//
//
//
//

    private List<String> dateList;

    private Map<Integer, String> dailyComment;

    private Map<String, Object> storyData; //schdeuleId, cityName, title, rating, comment

    private Map<Integer, List<StoryBlockDTO>> storyList;
    private String cityName;

    //updateSchedule()에서 필요
    private String scheduleId;
    private String userToken;


}




