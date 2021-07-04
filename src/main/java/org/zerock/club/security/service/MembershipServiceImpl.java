package org.zerock.club.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.zerock.club.entity.Membership;
import org.zerock.club.entity.MembershipPK;
import org.zerock.club.repository.MembershipRepository;
import org.zerock.club.security.dto.MembershipDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class MembershipServiceImpl implements  MembershipService{

    private final MembershipRepository membershipRepository;

    @Override
    public MembershipDTO get(String userId, String membershipId) {

        Optional<Membership> result = membershipRepository.findById(new MembershipPK("user1", "spc"));

        return entityToDTO(result.get());
    }

    @Override
    public List<MembershipDTO> getAll(String userId) {

        List<Object[]> result = membershipRepository.getMembership(userId);

        System.out.println((Membership)result.get(0)[0]);

        List<MembershipDTO> membershipDTOList = new ArrayList<>();
        for (int i=0 ; i<result.size() ;i++){
            membershipDTOList.add ( entityToDTO((Membership)result.get(i)[0] ));
        }

        return membershipDTOList;
    }

    @Override
    public void register(MembershipDTO membershipDTO) {

        Membership membership = dtoToEntity(membershipDTO);

        membershipRepository.save(membership);

    }

    @Override
    public void remove(String userId, String membershipId) {
        membershipRepository.deleteById(new MembershipPK(userId, membershipId));
    }

    @Override
    public void addPoint(String userId, String membershipId, int money) {
        Optional<Membership> result = membershipRepository.findById(new MembershipPK(userId, membershipId));
        Membership membership = result.get();
        membership.setPoint( membership.getPoint() + ((int)(money * 0.01)));
        membershipRepository.save(membership);
    }
}
