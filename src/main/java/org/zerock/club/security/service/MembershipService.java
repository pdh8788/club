package org.zerock.club.security.service;

import org.zerock.club.entity.Membership;
import org.zerock.club.entity.MembershipPK;
import org.zerock.club.security.dto.MembershipDTO;

import java.util.List;

public interface MembershipService {

    // 맴버십 상세조회하기
    MembershipDTO get(String userId, String membershipId);

    // 맴버십 전체 조회하기
    List<MembershipDTO> getAll(String userId);

    // 맴버십 등록하기
    void register(MembershipDTO membershipDTO);

    // 맴버십 삭제하기
    void remove(String userId, String membershipId);

    public void addPoint(String userId, String membershipId, int money);

    default Membership dtoToEntity(MembershipDTO membershipDTO){

        Membership membership = Membership.builder()
                .membershipPK(new MembershipPK(membershipDTO.getUserId(), membershipDTO.getMembershipId()))
                .membershipName(membershipDTO.getMembershipName())
                .membershipStatus(membershipDTO.isMembershipStatus())
                .point(membershipDTO.getPoint())
                .build();

        return membership;
    }

    default MembershipDTO entityToDTO(Membership membership){

        MembershipDTO membershipDTO = MembershipDTO.builder()
                .membershipId(membership.getMembershipPK().getMembershipId())
                .userId(membership.getMembershipPK().getUserId())
                .membershipName(membership.getMembershipName())
                .membershipStatus(membership.isMembershipStatus())
                .point(membership.getPoint())
                .build();
        return  membershipDTO;
    }

}
