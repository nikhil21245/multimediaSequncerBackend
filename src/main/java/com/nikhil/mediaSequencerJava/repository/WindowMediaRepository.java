package com.nikhil.mediaSequencerJava.repository;
import com.nikhil.mediaSequencerJava.entities.MediaWindow;
import com.nikhil.mediaSequencerJava.entities.WindowMedia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WindowMediaRepository extends JpaRepository<WindowMedia, Long> {

    List<WindowMedia> findByWindowOrderBySequenceOrderAsc(MediaWindow window);

    List<WindowMedia> findByWindowIdOrderBySequenceOrderAsc(Long windowId);
}