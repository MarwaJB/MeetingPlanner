package com.meeting.planner.meetingplannerms.repository;

import com.meeting.planner.meetingplannerms.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long> {

    @Query(value = "SELECT M FROM Meeting M WHERE M.day=:date and M.startTime=:time ")

    List<Meeting> findMeetingByTime(LocalDate date, LocalTime time);

}
