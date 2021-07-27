package com.meeting.planner.meetingplannerms.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Room {

    @Id
    private String name;
    private int capacity;
    private String equipments;

    @OneToMany(mappedBy = "room")
    private List<Meeting> meetings;

    public List<String> getEquipmentsAsList() {
        return StringUtils.isBlank(equipments) ? Lists.newArrayList() : Splitter.on(",").trimResults().omitEmptyStrings().splitToList(equipments);
    }

}
