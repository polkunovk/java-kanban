package ru.yandex.practicum.catsgram.controller;

import java.time.Instant;

public class Post {
    private Long id;
    private String description;
    private Instant postDate;

    // Геттеры и сеттеры для полей

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getPostDate() {
        return postDate;
    }

    public void setPostDate(Instant postDate) {
        this.postDate = postDate;
    }
}
