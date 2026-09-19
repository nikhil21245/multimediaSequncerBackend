package com.nikhil.mediaSequencerJava.dto;

import java.util.List;

public record WindowResponse(
        Long id,
        String name,
        List<PlaylistItemResponse> playlist
) {

    public record PlaylistItemResponse(
            String id,
            String name,
            String type,
            String url,
            Long durationMs,
            Integer sequenceOrder
    ) {
    }
}