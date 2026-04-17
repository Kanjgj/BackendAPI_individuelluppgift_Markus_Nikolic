package org.example.dto;

import jakarta.validation.constraints.NotBlank;

public class AuthorDTO {

    @NotBlank
    private String name;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}