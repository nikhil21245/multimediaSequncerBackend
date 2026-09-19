package com.nikhil.mediaSequencerJava.controller;
import com.nikhil.mediaSequencerJava.dto.SyncRequest;
import com.nikhil.mediaSequencerJava.service.SyncService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sync")
@CrossOrigin(origins = "*")
public class SyncController {

    private final SyncService syncService;

    public SyncController(SyncService syncService) {
        this.syncService = syncService;
    }

    @PostMapping
    public SyncService.SyncEvent sync(
            @Valid @RequestBody SyncRequest request
    ) {
        return syncService.createSyncEvent(
                request.mediaId(),
                request.durationMs()
        );
    }
}