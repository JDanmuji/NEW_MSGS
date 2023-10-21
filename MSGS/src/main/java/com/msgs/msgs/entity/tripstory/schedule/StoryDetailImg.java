package com.msgs.msgs.entity.tripstory.schedule;


import static jakarta.persistence.FetchType.LAZY;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;


@Entity
//@IdClass(StoryDetailImgID.class)
@Table(name="story_detail_img", indexes = @Index(name = "story_detail_img_index", columnList = "seq"))
@Getter @Setter
@NoArgsConstructor
public class StoryDetailImg {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int seq;

    @ManyToOne(fetch = LAZY)
    @JoinColumns({
        @JoinColumn(name = "order_id", nullable = false),
        @JoinColumn(name = "daily_id", nullable = false)
    })
    private StoryPlace storyPlace;

    @Column(name = "img_origin_name", length = 50)
    private String imgOriginName;

    @Column(name = "img_path", length = 100)
    private String imgPath;

    @Column(name = "reg_date")
    private LocalDateTime regDate;
    @Column(name = "mod_date")
    private LocalDateTime modDate;


    /* 등록일, 수정일 등록 메서드 */
    @PrePersist
    public void setRegDate() {
        this.regDate = LocalDateTime.now();
    }

    @PreUpdate
    public void setModDate() {
        this.modDate = LocalDateTime.now();
    }

}
