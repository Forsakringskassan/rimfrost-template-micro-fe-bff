export function transformBackendResponse(backendData: any): any {
    const rawTask = backendData?.data || backendData;

    const task = rawTask ? { taskField: rawTask.task_field } : null;

    return task;
}