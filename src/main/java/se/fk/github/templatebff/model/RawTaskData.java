package se.fk.github.templatebff.model;

import com.fasterxml.jackson.annotation.JsonProperty;

// Example raw backend response — replace fields with your actual API contract
public class RawTaskData
{
   @JsonProperty("task_field")
   public String taskField;
}
