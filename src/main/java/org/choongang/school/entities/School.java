package org.choongang.school.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.choongang.commons.constants.Location;
import org.choongang.file.entities.FileInfo;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class School {

    @Id
    @GeneratedValue
    private Long seq;

    @Column(length = 30 , nullable = false)
    private String domain;
    @Column(length = 30, nullable = false)
    private String schoolName;
    @Column(length = 50, nullable = false)
    private String gid;
    @Column(length = 10, nullable = false)
    private Location menuLocation;  //commons.constants

}
