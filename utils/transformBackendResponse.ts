interface BackendTaskData {
  data?: { task_field?: string };
  task_field?: string;
}

interface TransformedTask {
  taskField: string | undefined;
}

export function transformBackendResponse(backendData: BackendTaskData): TransformedTask | null {
  const rawTask = backendData?.data || backendData;
  return rawTask ? { taskField: rawTask.task_field } : null;
}