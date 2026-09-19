package com.nikhil.mediaSequencerJava.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record SyncRequest(

        @NotBlank(message = "Media ID is required")
        String mediaId,

        @Positive(message = "Sync duration must be greater than zero")
        long durationMs

) {
}