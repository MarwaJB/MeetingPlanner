package com.meeting.planner.meetingplannerms.api;

import com.meeting.planner.meetingplannerms.dto.MeetingDto;
import com.meeting.planner.meetingplannerms.entity.Meeting;
import com.meeting.planner.meetingplannerms.entity.Room;
import com.meeting.planner.meetingplannerms.service.MeetingService;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Objects;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/api")
public class MeetingResource {

    public final MeetingService meetingService;

    @PostMapping(path = "/v1/meetings")
    ResponseEntity<MeetingDto> reserveRoom(@RequestBody MeetingDto meetingDto) throws NotFoundException {
        log.info("reserve meeting");

        MeetingDto savedMeetingDto =  meetingService.createMeeting(meetingDto);
        if (Objects.isNull(savedMeetingDto)){
            return ResponseEntity.notFound().build();
        }

        final URI location = ServletUriComponentsBuilder.fromCurrentServletMapping().path("/api/v1/meetings/{id}").build().expand(savedMeetingDto.getId()).toUri();
        return ResponseEntity.created(location).body(savedMeetingDto);

    }


}

