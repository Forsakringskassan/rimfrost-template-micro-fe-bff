package se.fk.github.templatebff.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PatchTaskBody(@JsonProperty("ersattning_id")String ersattningId,String yrkandestatus){}
