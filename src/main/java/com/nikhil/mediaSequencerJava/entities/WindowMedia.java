package com.nikhil.mediaSequencerJava.entities;

import jakarta.persistence.*;

@Entity
@Table(
        name = "window_media",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"window_id", "sequence_order"}
                )
        }
)
public class WindowMedia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "window_id", nullable = false)
    private MediaWindow window;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "media_id", nullable = false)
    private Media media;

    @Column(name = "sequence_order", nullable = false)
    private Integer sequenceOrder;

    public WindowMedia() {
    }

    public WindowMedia(MediaWindow window, Media media, Integer sequenceOrder) {
        this.window = window;
        this.media = media;
        this.sequenceOrder = sequenceOrder;
    }

    public Long getId() {
        return id;
    }

    public MediaWindow getWindow() {
        return window;
    }

    public Media getMedia() {
        return media;
    }

    public Integer getSequenceOrder() {
        return sequenceOrder;
    }

    public void setWindow(MediaWindow window) {
        this.window = window;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public void setSequenceOrder(Integer sequenceOrder) {
        this.sequenceOrder = sequenceOrder;
    }
}