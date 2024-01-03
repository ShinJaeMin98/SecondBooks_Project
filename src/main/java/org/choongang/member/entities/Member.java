package org.choongang.member.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.choongang.commons.entities.Base;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Member extends Base {

    @Id @GeneratedValue
    private Long seq;

    @Column(length = 80, nullable = false, unique = true)
    private String email;

    @Column(length = 40, nullable = false, unique = true)
    private String userId;

    @Column(length = 65, nullable = false)
    private String password;

    @Column(length = 40, nullable = false)
    private String name;

    @ToString.Exclude
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Authorities> authorities = new ArrayList<>();
}