package com.meeting.planner.meetingplannerms.converter;

import com.meeting.planner.meetingplannerms.dto.MeetingDto;
import com.meeting.planner.meetingplannerms.dto.RoomDto;
import com.meeting.planner.meetingplannerms.entity.Meeting;
import lombok.Data;
import org.springframework.core.convert.converter.Converter;

import java.util.Objects;

@Data(staticConstructor = "newInstance")
public class MeetingConverter  implements Converter<Meeting,MeetingDto> {

    public MeetingDto convert(Meeting meeting) {
        if(Objects.isNull(meeting)){
            return null;
        }

        return MeetingDto.builder()
                .id(meeting.getId())
                .name(meeting.getName())
                .day(meeting.getDay())
                .numberPersons(meeting.getNumberPersons())
                .typeMeeting(meeting.getTypeMeeting())
                .startTime(meeting.getStartTime())
                .roomDto(RoomDto.builder()
                        .name(meeting.getRoom().getName())
                        .capacity(meeting.getRoom().getCapacity())
                        .equipments(meeting.getRoom().getEquipments())
                        .build())

                .build();


    }

}
