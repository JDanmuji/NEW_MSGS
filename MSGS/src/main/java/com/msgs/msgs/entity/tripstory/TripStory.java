package com.msgs.msgs.entity.tripstory;

import static jakarta.persistence.FetchType.LAZY;

import com.msgs.msgs.entity.tripschedule.TripSchedule;
import com.msgs.msgs.entity.user.UserEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "trip_story")
@Getter @Setter
@NoArgsConstructor
public class TripStory {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "story_id")
	private int id;

	@OneToOne(fetch = LAZY)
	@JoinColumn(name = "schedule_id", nullable = false)
	private TripSchedule tripSchedule;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private UserEntity userTripStory;

	@Column(length = 100)
	private String title;

	@Column(columnDefinition = "int(1)")
	private int rating;

	@Column(columnDefinition = "text")
	private String comment;

	@Column(name = "date_list", length = 500, nullable = false)
	private String dateList;

	@Column(name = "city_name", length = 30)
	private String cityName;

	@Column(name = "reg_date", nullable = false)
	private LocalDateTime regDate;
	@Column(name = "mod_date")
	private LocalDateTime modDate;



	/* mapping */
	@OneToMany(mappedBy = "tripStoryImg")
	private List<StoryImg> storyImgs = new ArrayList<>();

	@OneToMany(mappedBy = "tripStoryCmnt")
	private List<StoryComment> storyComments = new ArrayList<>();

	@OneToMany(mappedBy = "tripLikeCnt")
	private List<StoryLike> storyLikes = new ArrayList<>();


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
