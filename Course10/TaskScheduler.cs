using System;
using System.Collections.Generic;

namespace AlgorithmSuite
{
    public class ScheduledTask
    {
        public string TaskId { get; set; } = string.Empty;
        public int Priority { get; set; } // Lower value indicates higher priority
    }

    public class TaskPriorityQueue
    {
        private readonly List<ScheduledTask> _heap = new();

        public int Count => _heap.Count;

        public void Enqueue(ScheduledTask task)
        {
            _heap.Add(task);
            HeapifyUp(_heap.Count - 1);
        }

        public ScheduledTask Dequeue()
        {
            if (_heap.Count == 0) throw new InvalidOperationException("Queue is empty.");
            
            var root = _heap[0];
            int lastIndex = _heap.Count - 1;
            _heap[0] = _heap[lastIndex];
            _heap.RemoveAt(lastIndex);

            if (_heap.Count > 0) HeapifyDown(0);
            return root;
        }

        private void HeapifyUp(int index)
        {
            while (index > 0)
            {
                int parentIndex = (index - 1) / 2;
                if (_heap[index].Priority >= _heap[parentIndex].Priority) break;

                Swap(index, parentIndex);
                index = parentIndex;
            }
        }

        private void HeapifyDown(int index)
        {
            int lastIndex = _heap.Count - 1;
            while (true)
            {
                int leftChild = 2 * index + 1;
                int rightChild = 2 * index + 2;
                int smallest = index;

                if (leftChild <= lastIndex && _heap[leftChild].Priority < _heap[smallest].Priority)
                    smallest = leftChild;

                if (rightChild <= lastIndex && _heap[rightChild].Priority < _heap[smallest].Priority)
                    smallest = rightChild;

                if (smallest == index) break;

                Swap(index, smallest);
                index = smallest;
            }
        }

        private void Swap(int i, int j)
        {
            var temp = _heap[i];
            _heap[i] = _heap[j];
            _heap[j] = temp;
        }
    }
}
