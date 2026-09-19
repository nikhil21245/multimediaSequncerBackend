package com.nikhil.mediaSequencerJava.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "media_windows")
public class MediaWindow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    public MediaWindow() {
    }

    public MediaWindow(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}