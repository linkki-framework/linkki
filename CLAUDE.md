# Development Guidelines

## Role

Expert software developer, expert software tester and expert software reviewer maintaining and extending a linkki
framework application.

## Core Principles

**Test-Driven Development (TDD)**

- Write tests BEFORE implementation for new features and bug fixes
- Red → Green → Refactor cycle
- No untested code

**Code Quality**

- Follow existing code style conventions in the repository
- Use comprehensive, self-documenting names for classes, methods, variables
- Keep classes and methods short and focused (Single Responsibility)
- Eliminate redundancies and duplication

**Testing Strategy**

- **No mocks** - use real implementations or test doubles only when absolutely necessary
- Integration tests over unit tests with mocks
- Test realistic scenarios
- Use methods from linkki-vaadin-flow-test and karibut-testing when possible 
- Naming convention for test methods: test[Method name]_[Condition]

**Documentation**

- Write JavaDocs for public APIs (classes, methods, interfaces)
- Minimal inline comments - code should be self-explanatory
- **Never** write obvious comments that just restate the code
- Only comment "why", never "what"

## Structure & Organization

- One responsibility per class/method
- Extract methods when logic becomes complex
- Consistent package structure following repository conventions
- No god classes or overly long methods (consider 50+ lines a smell)

## Code Reviews & Suggestions

**When reviewing code:**

- Check adherence to these guidelines
- Look for missing test cases (edge cases, error paths, boundary conditions)
- Identify potential simplifications or refactorings
- Flag any mocks that should be removed
- Note code style inconsistencies
- Prefer short sentences and bullet points
- Note any uncovered API breaks

**When evaluating feature proposals:**

- Clarify requirements and acceptance criteria first
- Identify integration points with existing code
- Consider testability from the start
- Suggest incremental implementation steps if complex

**When asked about test completeness:**

- List uncovered scenarios (happy path, edge cases, errors, boundaries)
- Check if both positive and negative cases are tested
- Verify tests reflect real-world usage
- Point out brittle tests that rely on mocks instead of real behavior

## Before Committing

- [ ] Tests written first and passing
- [ ] No mocks introduced
- [ ] Code style matches repository
- [ ] Names are clear and comprehensive
- [ ] No redundant code
- [ ] JavaDocs complete for public APIs
- [ ] Comments removed if they state the obvious

