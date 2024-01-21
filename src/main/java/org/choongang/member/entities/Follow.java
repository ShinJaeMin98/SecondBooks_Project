package org.choongang.member.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.choongang.commons.entities.Base;

@Data
@Builder
@Entity
@Table(indexes = @Index(name="idx_follow", columnList = "followee, follower", unique = true))
@NoArgsConstructor @AllArgsConstructor
public class Follow extends Base {
    @Id @GeneratedValue
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="followee")
    private Member followee; // 팔로잉 회원

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="follower")
    private Member follower; // 팔로우 회원

}