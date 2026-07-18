using System;

namespace AlgorithmSuite
{
    public class TaskExecutor
    {
        private readonly TaskPriorityQueue _scheduler = new();

        public void RegisterTask(string taskId, int priority)
        {
            if (string.IsNullOrWhiteSpace(taskId))
            {
                Console.WriteLine("[LOG ERROR]: Task identification name cannot be null or empty.");
                return;
            }

            _scheduler.Enqueue(new ScheduledTask { TaskId = taskId, Priority = priority });
        }

        public void ExecuteNextTask()
        {
            try
            {
                if (_scheduler.Count == 0)
                {
                    Console.WriteLine("[LOG WARNING]: Process runner invoked with zero active elements inside the queue.");
                    return;
                }

                var task = _scheduler.Dequeue();
                Console.WriteLine($"[LOG INFO]: Successfully executed Task: {task.TaskId} with Priority rank: {task.Priority}");
            }
            catch (InvalidOperationException ex)
            {
                Console.WriteLine($"[LOG CRITICAL]: Operational validation breakdown matched: {ex.Message}");
            }
            catch (Exception ex)
            {
                Console.WriteLine($"[LOG CRITICAL]: Unhandled runner exception captured: {ex.Message}");
            }
        }
    }
}
