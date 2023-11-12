package com.msgs.msgs.entity.tripschedule;

import static jakarta.persistence.FetchType.LAZY;

import jakarta.persistence.*;
import lombok.*;

@Entity
@IdClass(DetailScheduleID.class)
@Table(name = "trip_detail_schedule")
@Getter @Setter
@NoArgsConstructor
public class TripDetailSchedule {

    @Id
    @Column(name="order_id")
    private int order;

    @Id
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "daily_id", nullable = false)
    private TripDailySchedule tripDailySchedule;


    @Column(name = "order_day", nullable = false)
    private int orderDay;

    @Column(name = "place_order", nullable = false)
    private int placeOrder;

    @Column(name = "content_id", length = 10)
    private String contentid;

    @Column(length = 50)
    private String title;

    @Column(length = 15)
    private String type;

    @Column(length = 200)
    private String location;

    @Column(name = "map_x", columnDefinition = "decimal(10, 6)")
    private Double mapx;

    @Column(name = "map_y", columnDefinition = "decimal(10, 6)")
    private Double mapy;

    @Column(name = "place_img_url", length = 100)
    private String firstimage2;




}
