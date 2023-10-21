package com.msgs.msgs.entity.tripstory;

import static jakarta.persistence.FetchType.LAZY;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="story_img", indexes = @Index(name = "story_img_index", columnList = "seq"))
@Getter @Setter
@NoArgsConstructor
public class StoryImg {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int seq;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "story_id", nullable = false)
    private TripStory tripStoryImg;

    @Column(name = "img_origin_name", length = 50)
    private String imgOriginName;

    @Column(name = "img_path", length = 2000)
    private String imgPath;

    @Column(name = "reg_date", nullable = false)
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
