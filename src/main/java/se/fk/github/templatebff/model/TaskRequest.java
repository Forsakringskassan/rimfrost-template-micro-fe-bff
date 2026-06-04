package se.fk.github.templatebff.model;

import jakarta.validation.constraints.NotBlank;

public record TaskRequest(@NotBlank String handlaggningId){}
