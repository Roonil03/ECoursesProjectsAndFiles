# LLM Contribution Reflection

## 1. Algorithmic Scaffolding and Balance Enforcement
Microsoft Copilot helped translate complex mathematical balancing mechanics into working C# rotation code. It assisted in mapping the left/right rotation rules for the AVL tree, ensuring height balance values update properly after adjustments.

## 2. Priority Scheduling and Sorting Optimizations
During the scheduling rewrite, Copilot helped design the array indexing offsets required to map a binary heap array safely (`2 * index + 1`). For the sorting upgrade, Copilot helped refactor the slow quadratic logic into a stable Merge Sort pattern, generating helper copy functions (`Array.Copy`) that avoid out-of-bounds errors.

## 3. Resiliency Framework and Exception Interception
Copilot assisted in making the final task runner engine more robust by suggesting explicit error-handling checks. It generated precise try-catch blocks and defensive argument validation guards that handle edge cases—like running actions against empty queues—without crashing the system.
