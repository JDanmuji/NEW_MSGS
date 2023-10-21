package com.msgs.msgs.entity.tripschedule;

import static jakarta.persistence.FetchType.LAZY;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Entity
//@IdClass(DailyScheduleID.class)
@Table(name="trip_daily_schedule", indexes = @Index(name = "trip_daily_schedule_index", columnList = "daily_id"))
@Getter @Setter
@NoArgsConstructor
public class TripDailySchedule {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "daily_id")
    private int dailyId;

    //@Id
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private TripSchedule tripSchedule;

    /* mapping */
    @OneToMany(mappedBy = "tripDailySchedule", fetch = LAZY)
    private List<TripDetailSchedule> tripDetailSchedules = new ArrayList<>();

}
