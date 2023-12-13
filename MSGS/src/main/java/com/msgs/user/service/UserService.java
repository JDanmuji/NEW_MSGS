package com.msgs.user.service;

import com.msgs.msgs.dto.UserEntityDTO;
import com.msgs.msgs.entity.user.UserEntity;
import com.msgs.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService  {


    private final UserRepository userRepo;


    // 회원가입

    @Transactional(readOnly = false)
    public void signUp(UserEntity userEntity) {

    //    StoredProcedureQuery spq = em.createNamedStoredProcedureQuery("multiply");

        userRepo.saveUser(userEntity);
        //userDAO.save(userEntity);
    }

    // 회원 정보 검색(이메일)

    public UserEntityDTO getUserInfo(String email) {

        System.out.println("!!!!!!!!!!!!!!!!!"+email);
        //Optional<UserEntity> userEntity = userDAO.findByEmail(email);
        Optional<UserEntity> userEntity = userRepo.findByEmail(email);
        // id 제외 findBy 메서드 생성

        if (userEntity.isPresent()) {
            UserEntity resultUserEntity = userEntity.get();
            UserEntityDTO userEntityDTO = new UserEntityDTO(resultUserEntity);

            return userEntityDTO;
        }

        return null;
    }


    public UserEntityDTO getUser(String id) {
        Optional<UserEntity> userEntity = userRepo.findById(id);
        // id 제외 findBy 메서드 생성

        if (userEntity.isPresent()) {
            UserEntity resultUserEntity = userEntity.get();
            UserEntityDTO userEntityDTO = new UserEntityDTO(resultUserEntity);

            return userEntityDTO;
        }

        return null;
    }
}

