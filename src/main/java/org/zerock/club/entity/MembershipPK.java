package org.zerock.club.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class MembershipPK implements Serializable {

    @Column(name = "user_id")
    private String userId;

    private String membershipId;
}
