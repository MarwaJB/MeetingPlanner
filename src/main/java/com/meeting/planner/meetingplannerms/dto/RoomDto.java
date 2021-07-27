package com.meeting.planner.meetingplannerms.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RoomDto {

    private String name;
    private int capacity;
    private String equipments;
}
