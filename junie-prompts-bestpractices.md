# Best Practices for Junie Task Definitions and Prompts

This document outlines the most effective strategies for defining tasks and prompts when working with Junie (JetBrains' autonomous AI coding agent).

### 1. Use a Spec-Driven Approach
Instead of providing a single large request, follow a structured workflow using dedicated markdown files. This keeps the agent focused and allows for better human oversight.

*   **`requirements.md`**: Define high-level goals and functional requirements.
*   **`plan.md`**: Ask Junie to propose a technical approach before writing any code. Review the architecture first.
*   **`tasks.md`**: Break the plan into small, actionable items. Junie can track progress here.

### 2. Leverage Global Guidelines
Utilize central guideline files like `AGENTS.md` or `.junie/guidelines.md` to define project-wide standards, so you don't have to repeat them in every prompt:

*   **Technology Stack**: Specify versions (e.g., Spring Boot 4.0.3, Java 17).
*   **Coding Conventions**: Tab vs. space, naming patterns, or architectural preferences (e.g., "no service layer, call repositories directly from controllers").
*   **Testing Rules**: Define testing frameworks and placement for new tests.
*   **Security Practices**: Include standard guards like `setDisallowedFields("id")` for data binding.

### 3. Bounded Units of Work (Controlled Iteration)
AI agents perform best when focused on a specific, small scope.

*   **Specific Prompts**: Instead of "Improve the app," use "Add integration tests for all methods in `OwnerController`."
*   **Iterative Execution**: Ask Junie to "Complete task #1 from `tasks.md` and then stop." This prevents "scope drift."
*   **Separate Exploration from Execution**: Use chat mode to explore ideas and refine the plan, then switch to code execution once the scope is clear.

### 4. Context Awareness and Search
Guide Junie to use its built-in search tools to ensure consistency:

*   **Semantic Search**: "Find how validation is implemented in other controllers and apply the same pattern here."
*   **VCS Status**: Use prompts like "Review my current changes and add missing unit tests for the new logic."

### 5. Safety and Control
*   **Brave Mode**: Be aware of "Brave Mode" settings. If disabled, Junie will ask for permission before executing terminal commands.
*   **Explicit Boundaries**: Specify files or directories the agent should ignore in your guidelines to avoid unintended modifications.

---

### Summary Checklist for a Quality Prompt:
1.  **Objective**: Clear, actionable goal (e.g., "Implement a new search endpoint").
2.  **Context**: Reference specific files or existing patterns.
3.  **Constraints**: Mention any specific rules or things to avoid.
4.  **Verification**: Define how the task should be verified (e.g., "Run `./mvnw test` to ensure no regressions").
