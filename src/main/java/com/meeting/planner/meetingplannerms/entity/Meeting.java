package com.meeting.planner.meetingplannerms.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private LocalTime startTime;
    @Enumerated(EnumType.STRING)
    private MeetingType typeMeeting;
    private int numberPersons;
    private LocalDate day;
    
    @ManyToOne
    private Room room;

}
