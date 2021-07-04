package org.zerock.club.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.club.entity.Membership;
import org.zerock.club.security.dto.MembershipDTO;
import org.zerock.club.security.service.MembershipService;

import java.util.List;

@SpringBootTest
public class MembershipServiceTests {

    @Autowired
    MembershipService membershipService;

    @Test
    public void testGet(){

        MembershipDTO membership = membershipService.get("test1","spc");

        System.out.println("-----------------------------------------");
        System.out.println(membership);

    }

    @Test
    public void testGetAll(){
        List<MembershipDTO> membershipDTOList = membershipService.getAll("test1");

        System.out.println("-----------------------------------------");
        for ( MembershipDTO membershipDTO : membershipDTOList){
            System.out.println(membershipDTO);
        }
    }

    @Test
    public void testRegister(){
        MembershipDTO membershipDTO = MembershipDTO.builder()
                .membershipId("spc")
                .membershipName("happypoint")
                .userId("test11")
                .build();

        membershipService.register(membershipDTO);
    }

    @Test
    public void testAddPoint(){
        membershipService.addPoint("test1","spc", 100000);
    }

    @Test
    public void testRemove(){
        membershipService.remove("test11","spc");
    }

}
