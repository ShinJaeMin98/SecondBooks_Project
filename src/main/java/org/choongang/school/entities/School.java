package org.choongang.school.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import org.choongang.commons.constants.Location;
import org.choongang.commons.entities.BaseMember;

@Entity
@Data
public class School extends BaseMember {

    @Id @GeneratedValue
    private Long num ;
    @Column(length = 80 , nullable = false)
    private String gid;
    @Column(length = 50 , nullable = false)
    private String schoolName;
    @Column(length = 50 , nullable = false)
    private String domain;
    @Column(length = 10 , nullable = false)
    private Location menuLocation;

}
