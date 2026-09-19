package com.nikhil.mediaSequencerJava.entities;
import com.nikhil.mediaSequencerJava.enums.MediaType;
import jakarta.persistence.*;

@Entity
@Table(name = "media")
public class Media {

    @Id
    @Column(length = 50)
    private String id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MediaType type;

    @Column(columnDefinition = "TEXT")
    private String url;

    @Column(nullable = false)
    private Long durationMs;

    public Media() {
    }

    public Media(String id, String name, MediaType type, String url, Long durationMs) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.url = url;
        this.durationMs = durationMs;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public MediaType getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public Long getDurationMs() {
        return durationMs;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(MediaType type) {
        this.type = type;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDurationMs(Long durationMs) {
        this.durationMs = durationMs;
    }
}