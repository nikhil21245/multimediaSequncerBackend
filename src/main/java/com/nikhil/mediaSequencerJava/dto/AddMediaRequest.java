package com.nikhil.mediaSequencerJava.dto;
import jakarta.validation.constraints.NotBlank;

public record AddMediaRequest(

        @NotBlank(message = "Media ID is required")
        String mediaId

) {
}