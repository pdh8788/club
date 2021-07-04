package org.zerock.club.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MembershipDTO {

    private String userId;

    private String membershipId;

    private String membershipName;

    private boolean membershipStatus;

    private int point;

    private LocalDateTime startDate;
}
