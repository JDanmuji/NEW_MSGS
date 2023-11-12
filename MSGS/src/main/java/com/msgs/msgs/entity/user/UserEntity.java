package com.msgs.msgs.entity.user;

import com.msgs.msgs.entity.placereview.PlaceReview;
import com.msgs.msgs.entity.tripschedule.TripSchedule;
import com.msgs.msgs.entity.tripstory.StoryComment;
import com.msgs.msgs.entity.tripstory.StoryLike;
import com.msgs.msgs.entity.tripstory.TripStory;

import jakarta.persistence.*;
import lombok.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Entity
@Table(name="user")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity implements UserDetails {

   @Id
   @Column(name = "user_id", length = 20)
   private String id;

    @PrePersist
    public void prePersist() {
        String temp_id = "will_be_triggered";
        this.id = temp_id;
    }

   @Column(name = "user_type", length = 5)
   private String type;

   @Column(name = "user_phone", columnDefinition="char(11)")
   private String phone;

   @Column(name = "user_email", length = 50)
   private String email;

   @Column(length = 50)
   private String password;

//   @ElementCollection(fetch = FetchType.EAGER)
//   private List<String> roles;

//   private static List<String> getDefaultRoles() {
//      List<String> defaultRoles = new ArrayList<>();
//      defaultRoles.add("USER");
//      defaultRoles.add("ADMIN");
//      return defaultRoles;
//   }

   @Override
   public Collection<? extends GrantedAuthority> getAuthorities() {
//      return this.roles.stream()
//            .map(SimpleGrantedAuthority::new)
//            .collect(Collectors.toList());
      return null;
   }

   @Column(name = "user_name", length = 30)
   private String name;
   
   @Column(name = "user_exist" , length = 5)
   private String exist;

   @Column(name = "reg_date", nullable = false)
   private LocalDate regDate;
   @Column(name = "mod_date")
   private LocalDate modDate;

   @Column(name="location_consent", columnDefinition="char(1)")
   private String locationConsent;
   @Column(name="reg_user", columnDefinition="char(1)")
   private String regUser;

   // 양방향 의존관계 설정
   // 기본적으로 단방향 의존관계 설정 후, 조회 등 필요 시에만 양방향 의존관계 설정
   
   @OneToMany(mappedBy = "userLike")
   // mappedBy 대상은 연관관계 대상의 필드명
   private List<UserLike> userLikes = new ArrayList<>();
   // ArrayList로 List 객체 초기화하여 NullPointerException 방지

   @OneToOne(mappedBy = "userImg")
   private UserImg userImg;

   // trip schedule
   @OneToMany(mappedBy = "userEntity")
   private List<TripSchedule> tripSchedule = new ArrayList<>();

   // place review
   @OneToMany(mappedBy = "userPlaceReview")
   private List<PlaceReview> placeReviews = new ArrayList<>();

   // trip story
   @OneToMany(mappedBy = "userTripStory")
   private List<TripStory> tripStories = new ArrayList<>();

   @OneToMany(mappedBy = "userStoryCmnt")
   private List<StoryComment> storyComment = new ArrayList<>();

   @OneToMany(mappedBy = "userStoryLike")
   private List<StoryLike> storyLike = new ArrayList<>();


   //jwt
   @Override
   public String getUsername() {
      return id+","+email;
   }

   @Override
   public boolean isAccountNonExpired() {
      return true;
   }

   @Override
   public boolean isAccountNonLocked() {
      return true;
   }

   @Override
   public boolean isCredentialsNonExpired() {
      return true;
   }

   @Override
   public boolean isEnabled() {
      return true;
   }


}