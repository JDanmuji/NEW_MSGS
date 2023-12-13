package com.msgs.tripschedule.repository;

import com.msgs.msgs.entity.tripschedule.TripDailySchedule;
import com.msgs.msgs.entity.tripschedule.TripDetailSchedule;
import com.msgs.msgs.entity.tripschedule.TripSchedule;
import jakarta.persistence.EntityManager;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TripscheduleRepository {

    private final EntityManager em;

    /* TripSchedule 저장 */
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
    public TripSchedule findById(int id){
        return em.find(TripSchedule.class, id);
    }


    /* 리스트 조회 */
    public List<TripDailySchedule> findAllDailyBySchedule(TripSchedule tripSchedule){
        return em.createQuery("select d from TripDailySchedule d "
                + "where d.tripSchedule = :tripSchedule order by  d.dailyId", TripDailySchedule.class)
            .setParameter("tripSchedule", tripSchedule) //파라미터 바인딩
            .getResultList();
    }

    /* TRIP_DETAIL_SCHEDULE의 레코드들을 삭제 */
    public int deleteDetailByDaily(TripDailySchedule dailySchedule){
        return em.createQuery("delete from TripDetailSchedule d where d.tripDailySchedule=:dailySchedule")
            .setParameter("dailySchedule", dailySchedule)
            .executeUpdate();
    }

    /* TRIP_DAILY_SCHEDULE의 레코드들을 삭제 -> detailSchedule도 cascade로 삭제됨 */
//    public int deleteDailyByScheduleId(TripSchedule tripSchedule){
//        return em.createQuery("delete from TripDailySchedule d where d.tripSchedule=:tripSchedule")
//            .setParameter("tripSchedule", tripSchedule)
//            .executeUpdate();
//    }


    public List<TripDetailSchedule> findAllDetailByDaily(TripDailySchedule dailySchedule){
        return em.createQuery("select d from TripDetailSchedule d where d.tripDailySchedule = :dailySchedule", TripDetailSchedule.class)
            .setParameter("dailySchedule", dailySchedule) //파라미터 바인딩
            .getResultList();
    }

}
