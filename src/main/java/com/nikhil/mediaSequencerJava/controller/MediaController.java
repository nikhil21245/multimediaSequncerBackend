package com.nikhil.mediaSequencerJava.controller;

import com.nikhil.mediaSequencerJava.entities.Media;
import com.nikhil.mediaSequencerJava.repository.MediaRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/media")
@CrossOrigin(origins = "*")
public class MediaController {

    private final MediaRepository mediaRepository;

    public MediaController(MediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository;
    }

    @GetMapping
    public List<Media> getAllMedia() {
        return mediaRepository.findAll();
    }
}