package com.msgs.tripschedule.repository;

import com.msgs.msgs.entity.tripschedule.DetailScheduleID;
import com.msgs.msgs.entity.tripschedule.TripDailySchedule;
import com.msgs.msgs.entity.tripschedule.TripDetailSchedule;
import com.msgs.msgs.entity.tripschedule.TripSchedule;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class TripscheduleRepository {

    private final EntityManager em;

    public void saveSchedule(TripSchedule tripSchedule){
        em.persist(tripSchedule); //영속성 컨텍스트에 멤버 객체(엔티티)를 넣음. 나중에 트랜잭션 커밋 되는 시점에 DB에 넣음(=insert 쿼리 실행함)
    }

    public void saveDailySchedule(TripDailySchedule tripDaily){
        em.persist(tripDaily);
    }

    public void saveDetailSchedule(TripDetailSchedule tripDetail){
        em.persist(tripDetail);
    }


    /* 단건 조회 */
    public TripSchedule findScheduleById(int id){
        return em.find(TripSchedule.class, id);
    }
    //updateSchedule에서 쓰임.
    public TripDetailSchedule findDetailScheduleById(DetailScheduleID detailScheId){
        return em.find(TripDetailSchedule.class, detailScheId);
    }


    /* 리스트 조회 */
    public List<TripDailySchedule> findAllDailyScheduleBySchedule(TripSchedule tripSchedule){
        return em.createQuery("select d from TripDailySchedule d where d.tripSchedule = :tripSchedule", TripDailySchedule.class)
            .setParameter("tripSchedule", tripSchedule) //파라미터 바인딩
            .getResultList();
    }

    public List<TripDetailSchedule> findAllDetailScheduleByDailySchedule(TripDailySchedule dailySchedule){
        return em.createQuery("select d from TripDetailSchedule d where d.tripDailySchedule = :dailySchedule", TripDetailSchedule.class)
            .setParameter("dailySchedule", dailySchedule) //파라미터 바인딩
            .getResultList();
    }

}
