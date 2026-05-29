package se.fk.github.templatebff;

import se.fk.github.templatebff.model.RawTaskData;
import se.fk.github.templatebff.model.TaskData;

// Example mapper — replace RawTaskData/TaskData with your actual backend response types
public class TaskMapper
{
   public static TaskData transform(RawTaskData raw)
   {
      TaskData result = new TaskData();
      result.taskField = raw.taskField;
      return result;
   }
}
