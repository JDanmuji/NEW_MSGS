package com.msgs.user.repository;

import com.msgs.msgs.entity.tripschedule.TripSchedule;
import com.msgs.msgs.entity.user.UserEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.StoredProcedureQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final EntityManager em;


    public void saveUser(UserEntity userEntity) {

        //생성 후, userId 넣기
        userEntity.setId(createUser(userEntity.getType()));

        em.persist(userEntity);
    }


    //userid 생성 - procedure 호출 (create_user_id)
    private String createUser(String userType) {
        /*
            user ID 생성
            회원가입 userType (MSGS -> M, Kakao -> K, Google -> G, Naver -> N) + 시퀀스(000001)
        */
        StoredProcedureQuery query = this.em.createNamedStoredProcedureQuery("createUserId");
        query.setParameter("_userType", userType);
        query.execute();

        String userId = query.getOutputParameterValue("_userId").toString();

        return userId;
    }


//   findByemail 전에 다른 쪽 파일에 있던 내용 추가
//    @Query("SELECT ue FROM UserEntity ue WHERE ue.email LIKE %:email%")
//        // @Param: 쿼리 메서드의 매개변수와 쿼리에서 사용하는 매개변수 이름을 연결시켜주는 역할
//    Optional<UserEntity> findByEmail(@Param("email") String email);

    public Optional<UserEntity>  findByEmail(String email) {

        Optional<UserEntity> singelResult = Optional.ofNullable(em.createQuery("SELECT ue FROM UserEntity ue WHERE ue.email = :email", UserEntity.class)
                .setParameter("email", email).getSingleResult());

        return singelResult;
    }

    public Optional<UserEntity> findById(String id) {
        return Optional.ofNullable(em.find(UserEntity.class, id));
    }


}
