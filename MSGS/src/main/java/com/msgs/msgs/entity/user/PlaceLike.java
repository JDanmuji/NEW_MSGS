package com.msgs.msgs.entity.user;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name="user_like")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlaceLike {

    @Id
    @Column(name = "like_id", length = 20)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    // FK 설정
    // @JoinColumn이 선언된 Entity에서 값 변환을 해야 함
    private UserEntity userLike;

//    //join with place review
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "review_id", nullable = false)
//    private PlaceReview placeReview;

    @Column(name = "like_date")
    private Date date;

    @Column(name="content_id", nullable=false, length = 20)
    private String content_id;


}
