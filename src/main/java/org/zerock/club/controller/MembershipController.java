package org.zerock.club.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zerock.club.security.dto.MembershipDTO;
import org.zerock.club.security.dto.NoteDTO;
import org.zerock.club.security.service.MembershipService;

import java.util.List;

@RestController
@Log4j2
@RequestMapping("/membership/")
@RequiredArgsConstructor
public class MembershipController {

    private final MembershipService membershipService;

    @PostMapping(value = "")
    public ResponseEntity<MembershipDTO> register(@RequestBody MembershipDTO membershipDTO) {

        log.info("-----------register--------------");
        log.info(membershipDTO);

        membershipService.register(membershipDTO);

        return new ResponseEntity<>(membershipDTO, HttpStatus.OK);

    }

    @GetMapping(value="/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MembershipDTO>> getList(String userId){
        log.info("-----------getList--------------");
        log.info(userId);

        return new ResponseEntity<>(membershipService.getAll(userId), HttpStatus.OK);
    }

}
