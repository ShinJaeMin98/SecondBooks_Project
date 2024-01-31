package org.choongang.school.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.choongang.commons.constants.Location;
import org.choongang.commons.entities.BaseMember;
import org.choongang.file.entities.FileInfo;
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


    @Lob
    private String content;

    @Transient
    private FileInfo banner_top; // 상단 배너

    @Transient
    private FileInfo banner_bottom; // 하단 배너

    @Transient
    private FileInfo logoImage; // 로고 이미지

    @ToString.Exclude
    @OneToMany(mappedBy = "school", fetch = FetchType.LAZY)
    List<Member> members = new ArrayList<>();






}
