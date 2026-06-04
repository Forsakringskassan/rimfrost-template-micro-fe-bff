package se.fk.github.templatebff.model;

import jakarta.validation.constraints.NotBlank;

public record PatchTaskRequest(@NotBlank String handlaggningId,String ersattningId,String yrkandestatus){}
