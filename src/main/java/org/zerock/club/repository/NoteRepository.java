package org.zerock.club.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.club.entity.Note;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Long> {

    @EntityGraph(attributePaths = "writer", type = EntityGraph.EntityGraphType.LOAD)
    @Query("select n from Note n Where n.num = :num")
    Optional<Note> getWithWriter(Long num);

    @EntityGraph(attributePaths = {"writer"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("select n from Note n where n.writer.email = :email")
    List<Note> getList (String email);

    /**
     * ClubMember를 @EntityGraph를 사용해서 처리할 때 ClubMember의 roleSet이 toString()에 사용되지 않도록
     * 주의합시다. 만일 roleSet까지 같이 로딩하고 싶다면 attributePaths = {"writer, "writer.roleSet"}과 같이 구성할 수 있습니다.
     */

}
