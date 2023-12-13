package com.msgs.tripstory.repository;


import com.msgs.msgs.entity.tripschedule.TripDailySchedule;
import com.msgs.msgs.entity.tripschedule.TripDetailSchedule;
import com.msgs.msgs.entity.tripschedule.TripSchedule;
import com.msgs.msgs.entity.tripstory.TripStory;
import com.msgs.msgs.entity.tripstory.schedule.StoryDailySchedule;
import com.msgs.msgs.entity.tripstory.schedule.StoryPlace;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import com.msgs.msgs.entity.tripstory.StoryComment;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TripStoryRepository {

    private final EntityManager em;

    public List<StoryComment> findAllStoryCommentsList() {
        return em.createQuery("select storyComment from StoryComment storyComment", StoryComment.class)
                .getResultList();
    }

    public void saveStory(TripStory tripStory){
        em.persist(tripStory); //영속성 컨텍스트에 멤버 객체(엔티티)를 넣음. 나중에 트랜잭션 커밋 되는 시점에 DB에 넣음(=insert 쿼리 실행함)
        em.persist(tripStory); //영속성 컨텍스트에 멤버 객체(엔티티)를 넣음. 나중에 트랜잭션 커밋 되는 시점에 DB에 넣음(=insert 쿼리 실행함)
    }


    public void saveDailyStory(StoryDailySchedule storyDaily){
        em.persist(storyDaily);
    }

    public void saveDetailStory(StoryPlace storyPlace){
        em.persist(storyPlace);
    }



}
