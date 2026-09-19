package com.nikhil.mediaSequencerJava.service;
import com.nikhil.mediaSequencerJava.entities.Media;
import com.nikhil.mediaSequencerJava.repository.MediaRepository;
import com.nikhil.mediaSequencerJava.websocket.SyncWebSocketHandler;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
public class SyncService {

    private final MediaRepository mediaRepository;
    private final SyncWebSocketHandler syncWebSocketHandler;
    private final ObjectMapper objectMapper;

    public SyncService(
            MediaRepository mediaRepository,
            SyncWebSocketHandler syncWebSocketHandler,
            ObjectMapper objectMapper
    ) {
        this.mediaRepository = mediaRepository;
        this.syncWebSocketHandler = syncWebSocketHandler;
        this.objectMapper = objectMapper;
    }

    public SyncEvent createSyncEvent(
            String mediaId,
            long durationMs
    ) {

        Media media = mediaRepository.findById(mediaId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Media not found: " + mediaId
                        )
                );

        long startTime = System.currentTimeMillis();

        SyncEvent event = new SyncEvent(
                media.getId(),
                media.getType().name(),
                media.getUrl(),
                durationMs,
                startTime
        );

        try {

            String jsonMessage =
                    objectMapper.writeValueAsString(event);

            syncWebSocketHandler.broadcast(jsonMessage);

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to broadcast sync event",
                    e
            );
        }

        return event;
    }

    public record SyncEvent(
            String mediaId,
            String mediaType,
            String mediaUrl,
            long durationMs,
            long startTime
    ) {
    }
}