package com.nikhil.mediaSequencerJava.controller;
import com.nikhil.mediaSequencerJava.entities.MediaWindow;
import com.nikhil.mediaSequencerJava.service.PlaylistService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/windows")
@CrossOrigin(origins = "*")
public class WindowController {

    private final PlaylistService playlistService;

    public WindowController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @GetMapping
    public List<MediaWindow> getAllWindows() {
        return playlistService.getAllWindows();
    }
}