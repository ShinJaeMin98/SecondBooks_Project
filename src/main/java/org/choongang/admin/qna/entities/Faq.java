package org.choongang.admin.qna.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.choongang.commons.entities.BaseMember;

@Entity
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Faq extends BaseMember {

    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 60)
    private String subject;

    @Lob
    private String content;
}
