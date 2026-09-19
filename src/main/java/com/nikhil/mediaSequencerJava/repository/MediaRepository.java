package com.nikhil.mediaSequencerJava.repository;
import com.nikhil.mediaSequencerJava.entities.Media;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MediaRepository extends JpaRepository<Media, String> {
}