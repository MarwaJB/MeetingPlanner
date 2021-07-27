package com.meeting.planner.meetingplannerms.repository;

import com.meeting.planner.meetingplannerms.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, String> {

    @Query(value = "SELECT R FROM Room R " +
            "WHERE R.name not in ( SELECT M.room.name FROM Meeting M WHERE M.day=:date " +
            "and (M.startTime=:time1 or M.startTime=:time2 or M.startTime=:time3))" +
            "and (R.capacity * 0.7) >:capacity")
    List<Room> getAvailableRooms(LocalDate date, LocalTime time1, LocalTime time2, LocalTime time3, double capacity);
}
