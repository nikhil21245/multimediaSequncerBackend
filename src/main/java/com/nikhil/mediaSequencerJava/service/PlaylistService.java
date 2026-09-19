package com.nikhil.mediaSequencerJava.service;
import com.nikhil.mediaSequencerJava.dto.WindowResponse;
import com.nikhil.mediaSequencerJava.entities.Media;
import com.nikhil.mediaSequencerJava.entities.MediaWindow;
import com.nikhil.mediaSequencerJava.entities.WindowMedia;
import com.nikhil.mediaSequencerJava.repository.MediaRepository;
import com.nikhil.mediaSequencerJava.repository.MediaWindowRepository;
import com.nikhil.mediaSequencerJava.repository.WindowMediaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PlaylistService {

    private final MediaRepository mediaRepository;
    private final MediaWindowRepository mediaWindowRepository;
    private final WindowMediaRepository windowMediaRepository;

    public PlaylistService(
            MediaRepository mediaRepository,
            MediaWindowRepository mediaWindowRepository,
            WindowMediaRepository windowMediaRepository
    ) {
        this.mediaRepository = mediaRepository;
        this.mediaWindowRepository = mediaWindowRepository;
        this.windowMediaRepository = windowMediaRepository;
    }

    public List<MediaWindow> getAllWindows() {
        return mediaWindowRepository.findAll();
    }

    @Transactional(readOnly = true)
    public WindowResponse getWindowWithPlaylist(Long windowId) {

        MediaWindow window = mediaWindowRepository.findById(windowId)
                .orElseThrow(() ->
                        new RuntimeException("Window not found: " + windowId)
                );

        List<WindowMedia> playlist =
                windowMediaRepository
                        .findByWindowOrderBySequenceOrderAsc(window);

        List<WindowResponse.PlaylistItemResponse> playlistItems =
                playlist.stream()
                        .map(windowMedia -> {

                            Media media = windowMedia.getMedia();

                            return new WindowResponse.PlaylistItemResponse(
                                    media.getId(),
                                    media.getName(),
                                    media.getType().name(),
                                    media.getUrl(),
                                    media.getDurationMs(),
                                    windowMedia.getSequenceOrder()
                            );
                        })
                        .toList();

        return new WindowResponse(
                window.getId(),
                window.getName(),
                playlistItems
        );
    }

    @Transactional
    public WindowResponse.PlaylistItemResponse addMediaToWindow(
            Long windowId,
            String mediaId
    ) {

        MediaWindow window = mediaWindowRepository.findById(windowId)
                .orElseThrow(() ->
                        new RuntimeException("Window not found: " + windowId)
                );

        Media media = mediaRepository.findById(mediaId)
                .orElseThrow(() ->
                        new RuntimeException("Media not found: " + mediaId)
                );

        List<WindowMedia> existingPlaylist =
                windowMediaRepository
                        .findByWindowOrderBySequenceOrderAsc(window);

        int nextSequence = existingPlaylist.size() + 1;

        WindowMedia windowMedia =
                new WindowMedia(
                        window,
                        media,
                        nextSequence
                );

        WindowMedia saved =
                windowMediaRepository.save(windowMedia);

        return new WindowResponse.PlaylistItemResponse(
                media.getId(),
                media.getName(),
                media.getType().name(),
                media.getUrl(),
                media.getDurationMs(),
                saved.getSequenceOrder()
        );
    }
}