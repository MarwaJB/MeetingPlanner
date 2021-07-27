package com.meeting.planner.meetingplannerms.dto;

import com.meeting.planner.meetingplannerms.entity.MeetingType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
@Data
public class MeetingDto {

    private Long id;
    private String name;
    private LocalTime startTime;
    private MeetingType typeMeeting;
    private int numberPersons;
    private LocalDate day;
    private RoomDto roomDto;

}
