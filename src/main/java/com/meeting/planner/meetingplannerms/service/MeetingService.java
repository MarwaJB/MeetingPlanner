package com.meeting.planner.meetingplannerms.service;

import com.meeting.planner.meetingplannerms.converter.MeetingConverter;
import com.meeting.planner.meetingplannerms.converter.MeetingDtoConverter;
import com.meeting.planner.meetingplannerms.dto.MeetingDto;
import com.meeting.planner.meetingplannerms.entity.Meeting;
import com.meeting.planner.meetingplannerms.entity.Room;
import com.meeting.planner.meetingplannerms.repository.MeetingRepository;
import com.meeting.planner.meetingplannerms.repository.RoomRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@AllArgsConstructor
public class MeetingService {

    private final RoomRepository roomRepository;
    private final MeetingRepository meetingRepository;

    /**
     * réserver une salle pour une reunion
     **/
    public MeetingDto createMeeting(MeetingDto meetingDto) {

        //première  filtre pour déterminer la liste des salles disponible selon (jour, heure, capacité)

        List<Room> availableRooms = roomRepository.getAvailableRooms(meetingDto.getDay(), meetingDto.getStartTime().minusHours(1), meetingDto.getStartTime(), meetingDto.getStartTime().plusHours(1), meetingDto.getNumberPersons());
        if (availableRooms.isEmpty()) return null;

        List<String> vcEquipements = List.of("WEBCAM", "ECRAN", "PIEUVRE");
        List<String> rcEquipements = List.of("TABLEAU", "ECRAN", "PIEUVRE");
        List<String> specEquipements = List.of("TABLEAU");

        //deuxième filtre pour choisir la bonne salle selon type de reunion(équipements)

        Room room = null;
        switch (meetingDto.getTypeMeeting()) {

            case VC:
                room = getBestRoom(vcEquipements, availableRooms, meetingDto);
                break;
            case RS:
                room = getBestRoom(new ArrayList<>(), availableRooms, meetingDto);
                break;
            case RC:
                room = getBestRoom(rcEquipements, availableRooms, meetingDto);
                break;
            case SPEC:
                room = getBestRoom(specEquipements, availableRooms, meetingDto);
                break;
        }

        if (Objects.isNull(room)) {

            return null;
        } else {
            Meeting meeting = MeetingDtoConverter.newInstance().convert(meetingDto);
            meeting.setRoom(room);
            Meeting savedMeeting = meetingRepository.save(meeting);
            return MeetingConverter.newInstance().convert(savedMeeting);
        }

    }

    /**
     * choisir la bonne salle selon les équipements disponible dans les salles et les équipements amovibles
     **/
    public Room getBestRoom(List<String> equipments, List<Room> availableRooms, MeetingDto meetingDto) {

        //affecter un score à chaque salle en comparant les équipements disponible dans cette salle et les équipements nécessaire pour la reunion
        HashMap<Room, Integer> roomScore = new HashMap<>();
        availableRooms.forEach(room -> {
            roomScore.put(room, 0);
            room.getEquipmentsAsList().forEach(equipement -> {
                if (equipments.contains(equipement)) {
                    roomScore.put(room, roomScore.get(room) + 1);
                } else {
                    roomScore.put(room, roomScore.get(room) - 1);
                }
            });
        });

        //choisir la salle qui a le meilleur score
        Map.Entry<Room, Integer> roomWithBestScore = null;
        for (Map.Entry<Room, Integer> entry : roomScore.entrySet()) {
            if (roomWithBestScore == null ||
                    (entry.getValue().compareTo(roomWithBestScore.getValue()) == 0 && entry.getKey().getCapacity() < roomWithBestScore.getKey().getCapacity())
                    || entry.getValue().compareTo(roomWithBestScore.getValue()) > 0) {
                roomWithBestScore = entry;
            }
        }

        //La salle sélectionné contient tous les équipements necessaires
        if (roomWithBestScore.getValue() >= equipments.size()) {
            return roomWithBestScore.getKey();
        }
        //La salle sélectionné manque des équipements
        else {
            HashMap<String, Integer> equipmentsMobile = new HashMap<>();
            equipmentsMobile.put("TABLEAU", 2);
            equipmentsMobile.put("ECRAN", 5);
            equipmentsMobile.put("WEBCAM", 4);
            equipmentsMobile.put("PIEUVRE", 4);

            //récupérer la liste des reunions créer à la même heure pour pouvoir déterminer le nombre des équipements amovibles disponible
            List<Meeting> meetings = meetingRepository.findMeetingByTime(meetingDto.getDay(), meetingDto.getStartTime());

            meetings.forEach(meeting -> {
                List<String> meetingEquipments = Arrays.asList();
                List<String> roomEquipments = meeting.getRoom().getEquipmentsAsList();
                switch (meeting.getTypeMeeting()) {

                    case VC:
                        meetingEquipments = List.of("WEBCAM", "ECRAN", "PIEUVRE");
                        break;
                    case RC:
                        meetingEquipments = List.of("TABLEAU", "ECRAN", "PIEUVRE");
                        break;
                    case SPEC:
                        meetingEquipments = List.of("TABLEAU");
                        break;

                }

                //déterminer le nombre des équipements amovibles restant
                List<String> reserveEquipment = new ArrayList<>(meetingEquipments);
                reserveEquipment.removeAll(roomEquipments);

                reserveEquipment.forEach(eq -> {
                    equipmentsMobile.put(eq, equipmentsMobile.get(eq) - 1);

                });
            });

            //le nombre des équipements amovibles dont on a besoin ne doit pas étre inferieur à 0
            List<String> meetingEquipments = Arrays.asList();
            for (Map.Entry<String, Integer> equipmentMap : equipmentsMobile.entrySet()) {
                switch (meetingDto.getTypeMeeting()) {

                    case VC:
                        meetingEquipments = List.of("WEBCAM", "ECRAN", "PIEUVRE");
                        break;
                    case RC:
                        meetingEquipments = List.of("TABLEAU", "ECRAN", "PIEUVRE");
                        break;
                    case SPEC:
                        meetingEquipments = List.of("TABLEAU");
                        break;

                }
                if (equipmentMap.getValue() <= 0 && meetingEquipments.contains(equipmentMap.getKey())) {
                    return null;
                }
            }

            return roomWithBestScore.getKey();
        }

    }


}