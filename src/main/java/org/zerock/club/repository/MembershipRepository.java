package org.zerock.club.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.club.entity.ClubMember;
import org.zerock.club.entity.Membership;
import org.zerock.club.entity.MembershipPK;

import java.util.List;

public interface MembershipRepository extends JpaRepository<Membership, MembershipPK> {

//    Membership findByUserIdAndMembershipId(MembershipPK membershipPK);

//    List<Membership> findByUserId(String userId);

    @Query("SELECT b FROM Membership b WHERE b.membershipPK.userId = :userId")
    List<Object[]> getMembership(@Param("userId") String userId);

}
