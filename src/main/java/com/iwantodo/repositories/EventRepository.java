package com.iwantodo.repositories;

import com.iwantodo.entities.event.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("SELECT e from Event e WHERE e.owner.username = :username")
    List<Event> findByOwner(@Param("username") String username);

    @Query("SELECT e from Event e WHERE e.owner.username = :username AND e.title = :title")
    List<Event> findEventByTitle(String username, String title);
}
