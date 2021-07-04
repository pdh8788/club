package org.zerock.club.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "membership")
@ToString
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Membership extends BaseMemberEntity implements Serializable {

    @EmbeddedId
    private MembershipPK membershipPK;

    private String membershipName;

    private boolean membershipStatus;

    private int point;

}
