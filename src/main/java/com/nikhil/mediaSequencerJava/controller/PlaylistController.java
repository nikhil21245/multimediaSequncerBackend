package com.nikhil.mediaSequencerJava.controller;
import com.nikhil.mediaSequencerJava.dto.AddMediaRequest;
import com.nikhil.mediaSequencerJava.dto.WindowResponse;
import com.nikhil.mediaSequencerJava.service.PlaylistService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/windows")
@CrossOrigin(origins = "*")
public class PlaylistController {

    private final PlaylistService playlistService;

    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @GetMapping("/{windowId}/playlist")
    public WindowResponse getPlaylist(
            @PathVariable Long windowId
    ) {
        return playlistService.getWindowWithPlaylist(windowId);
    }

    @PostMapping("/{windowId}/media")
    public WindowResponse.PlaylistItemResponse addMedia(
            @PathVariable Long windowId,
            @Valid @RequestBody AddMediaRequest request
    ) {
        return playlistService.addMediaToWindow(
                windowId,
                request.mediaId()
        );
    }
}