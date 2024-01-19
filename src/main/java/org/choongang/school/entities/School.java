package org.choongang.school.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.choongang.commons.constants.Location;
import org.choongang.commons.entities.BaseMember;
import org.choongang.member.entities.Member;

import java.util.ArrayList;
import java.util.List;

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
    @Lob
    private String content;

    @OneToMany(mappedBy = "school", fetch = FetchType.LAZY)
    List<Member> members = new ArrayList<>();





}
