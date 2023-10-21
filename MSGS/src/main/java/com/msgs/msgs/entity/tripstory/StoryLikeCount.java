package com.msgs.msgs.entity.tripstory;


import static jakarta.persistence.FetchType.LAZY;

import com.msgs.msgs.entity.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="story_like_count", indexes = @Index(name = "story_like_count_index", columnList = "seq"))
@Getter @Setter
@NoArgsConstructor
public class StoryLikeCount {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int seq;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "story_id", nullable = false)
    private TripStory tripLikeCnt;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="user_id", nullable = false)
    private UserEntity userStoryLike;

}
