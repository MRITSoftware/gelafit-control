# Testing Guide

This project uses Jest for unit testing TypeScript code.

## Setup

Jest is configured with the following:
- **Test Runner**: Jest 29.x
- **TypeScript Support**: ts-jest
- **Test Location**: Tests are located in `__tests__` directories next to the source files
- **Test Pattern**: `*.test.ts` or `*.spec.ts`
- **Coverage**: Enabled with detailed reporting

## Running Tests

```bash
# Run all tests
npm test

# Run tests in watch mode
npm run test:watch

# Run tests with coverage report
npm run test:coverage
```

## Configuration

Jest configuration is in `jest.config.js`. Key settings:
- Uses `ts-jest` preset for TypeScript support
- Test environment: Node.js
- Mocks VS Code API via `scripts/vscode-stub.js`
- Transforms ignore patterns for ES modules from `@octokit`

## Writing Tests

### Test Structure

Tests should be placed in a `__tests__` directory at the same level as the source file:

```
src/
  utils/
    cve.ts
    __tests__/
      cve.test.ts
```

### Example Test

```typescript
import { myFunction } from '../myModule';

describe('myFunction', () => {
    test('should do something', () => {
        const result = myFunction('input');
        expect(result).toBe('expected output');
    });
});
```

### Mocking External Dependencies

When testing modules that import VS Code API or other external dependencies, use Jest mocks:

```typescript
// Mock vscode module
jest.mock('vscode', () => ({
    window: {
        showInformationMessage: jest.fn()
    }
}));

// Mock other modules
jest.mock('../otherModule', () => ({
    someFunction: jest.fn()
}));
```

## Current Test Coverage

Run `npm run test:coverage` to see the current test coverage report.

## Example: CVE Tests

See `src/utils/__tests__/cve.test.ts` for a comprehensive example of testing utility functions with:
- Multiple test suites using `describe` blocks
- Mocked external dependencies (@octokit/rest, platform configuration)
- Edge case testing (null, undefined, empty strings)
- Real-world examples from GitHub Security Advisories
