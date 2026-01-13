# File-Based App (FBA) Detection

## Problem Summary
We need to determine when a C# file should be treated as a File-Based App vs a Project-Based App vs a Misc File, and make this status visible to users and debuggable for the team.

## Context
With the introduction of File-Based Apps (top-level statements without explicit project files), we need clear heuristics to determine how to treat each file. This affects:
- IntelliSense behavior
- Debugging functionality
- User feedback and bug reports (need to know what mode they're in)
- Integration with the file-not-in-solution notification feature ([PR 687403](https://devdiv.visualstudio.com/DevDiv/_git/vs-green/pullrequest/687403))

The classification needs to work across different workspace scenarios:
- Small workspaces with few files
- Large repos like Picasso (hundreds of projects, uses Aspire, exclusively uses C# Dev Kit)
- Aspire app hosts (planning to use FBAs)
- Workspace-Based Development (WBD) mode

## Problem Details

### Proposed Classification Logic

This is the proposed decision tree for determining how to classify a C# file:

1. **Is `enableFileBasedApps` enabled?** (default: `true` in release)
   - **No** → Use legacy project-based classification only
   - **Yes** → Continue to next check

2. **Does the file have directives?**
   - **Yes** → Classify as **File-Based App**
   - **No** → Continue to next check

3. **Is `enableFileBasedAppsWithoutDirectives` enabled?** (default: `false` in release)
   - **No** → Use legacy project-based classification
   - **Yes** → Continue to heuristic detection

**Heuristic Detection (when `enableFileBasedAppsWithoutDirectives: true`):**

4. **Is the solution ready?**
   - **No** → Classify as **Misc File** (wait until ready)
   - **Yes** → Continue to next check

5. **Is the project system state up-to-date for this file?**
   - **No** → Classify as **Misc File** (wait until updated)
   - **Yes** → Continue to next check

6. **Is the file in a project that's in the solution?**
   - **Yes** → Classify as **Project-Based App**
   - **No** → Continue to next check

7. **Is the file in a project that's NOT in the solution?**
   - **Yes** → Classify as **Project-Based App** + Show notification to user
   - **No** → Continue to default

8. **Are top-level statements present?**
   - **Yes** → Classify as **File-Based App**
   - **No** → Classify as **Misc File**
### Feature Flags

**`enableFileBasedApps`** (default: `true` in release)
- Controls overall File-Based App support
- When enabled, files with top-level statements are always treated as FBAs

**`enableFileBasedAppsWithoutDirectives`** (default: `false` in release)
- Controls heuristic-based FBA classification (steps 4-8 above)
- When disabled, files without top-level statements won't show "phantom diagnostics" before project loading completes
- Users can opt-in to enable heuristic detection while we refine the classification logic


### User Experience

1. **Status Bar Visibility**: Show the current file's mode in the status bar
   - "File Based App: something.cs"
   - "Project: Something.csproj"
   - "Misc File: Something.cs"

2. **Mode Switching**: Allow users to manually override classification (with persistence in workspace settings)

3. **Integration with File-Not-In-Solution Feature**: Work harmoniously with PR 687403 to ensure correct project configuration

## Solutions Considered and Rejected

- Relying on file based apps having directives
- Custom file extension
- Recommending naming conventions *.app.cs

## Open Questions

### Technical Implementation

1. **"Is the file in a project that is not in the solution?"**
   - This is difficult to answer correctly
   - Could guess by walking up the directory tree looking for .csproj
   - But projects can reference files from anywhere (not just in their directory tree)
   - Loading projects implicitly to check has perf impact
   - Less likely in Workspace-Based Development (unless project is outside open folder)
   - Who is the authority for answering this question?
   - Need a single implementation that can be queried

2. **"Is the project system up-to-date for file?"**
   - Project system should be the authority on this
   - Need a way to query this state

### User Conventions and Configuration

3. **Naming conventions**: Is there a recommended pattern we should encourage?
   - Any existing trends in how folks name their File-Based Apps?
   - **Damian's recommendation w/o actually recommending**: Use `*.app.cs` pattern (e.g., `Program.app.cs`, `MyScript.app.cs`)
   - Could we start a convention to make detection more reliable?
   - Could this support explicit marking (editor command, workspace setting)?

4. **Directory conventions**: Where should FBAs be allowed?
   - Rikki's proposal: Assume SDK-style default behavior (EnableDefaultCompileItems, no explicit Compile items)
   - **Recommendation**: Don't put FBAs in the same directory as a .csproj file
   - If you want to exclude a file using `Compile Exclude`, experience will be clunky
   - "Embrace the platform conventions and the tooling will work better"

5. **Workspace configuration**: Should large repos be able to opt-out or configure FBA behavior?
   - Globbing patterns to declare where to watch for FBAs?
   - Setting to disable FBA detection in very large repos?


## Meeting Notes


## Follow-up

