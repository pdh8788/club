package org.zerock.club.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.club.entity.Membership;
import org.zerock.club.entity.MembershipPK;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class MembershipRepositoryTests {

    @Autowired
    MembershipRepository membershipRepository;

    @Test
    public void testClass(){
        System.out.println(membershipRepository.getClass().getName());
    }

    @Test
    public void insertDummies(){

        //1 - 10 까지

       /* IntStream.rangeClosed(1, 10).forEach( i -> {
            Membership member =Membership.builder()
                    .membershipId("spc")
                    .userId("test"+i)
                    .membershipName("happypoint")
                    .membershipStatus(true)
                    .point(100)
                    .build();

            membershipRepository.save(member);
        });*/

        IntStream.rangeClosed(1, 5).forEach( i -> {
            Membership member =Membership.builder()
                    .membershipPK(new MembershipPK("test" + i, "shinesaegae"))
                    .membershipName("shinsegaepoint")
                    .membershipStatus(true)
                    .point(200)
                    .build();

            membershipRepository.save(member);
        });


    }

    @Test
    public void testRead1(){

        Optional<Membership> result = membershipRepository.findById(new MembershipPK("test", "spc"));

        Membership membership = result.get();

        System.out.println(membership);

    }

}
