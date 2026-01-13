---
id: semantic-kernel-to-agent-framework
title: Migrate Semantic Kernel to Agent Framework
description: This knowledge base provides specifications of migrating from using Semantic Kernel to use the new Microsoft Agent Framework.
hierarchy: Agent Framework Migration
author: microsoft
language: python
---

# Semantic Kernel to Agent Framework Migration Guide (Python)

## Executive Summary

Agent Framework for Python replaces Semantic Kernel with a simplified, unified interface across AI providers. Key improvements include direct client usage (no kernel required), built-in thread management, simplified tool registration, and enhanced memory capabilities.

## Functional Requirements

- FR001: All Semantic Kernel usage are converted to corresponding Microsoft Agent Framework usages
- FR002: Semantic consistency is maintained during the migration, no extra coding logic is generated
- FR003: dependencies are properly setup in the project's package manager
- FR004: Syntax and lint checks are passed
- FR005: Don't update(add/remove/update) the Copyright information at the beginning of the file
- FR006: Don't import unnecessary and unused modules
- FR007: Ensure the migrated code use the valid APIs and classes from Agent Framework.
- FR008: Skip migration on Jupyter Notebooks, which are .ipynb files

## Quick Migration Reference

### **MUST**: Install the package(s) you actually need 

- Package details
    - release state: `prerelease`, so ENSURE prerelease package is allowed, e.g., `--pre` for pip, `--prerelease=allow` for uv
    - **IMPORTANT**: `--pre` and `--prerelease=allow` are **installation flags only** for package managers (pip, uv, etc.). They should **NEVER** be added to dependency files like `requirements.txt`, `pyproject.toml`, or any other dependency management files. 

- Installation strategies
    - **MUST** install `agent-framework-core`

        1. Install only the core package to start:

            pip install agent-framework-core --pre

        3. Use AI to do the static analysis and then add subpackages only when your code or imports require them. Example (add Azure integration when needed):

            pip install agent-framework-azure-ai --pre

    - Typical subpackages you may install individually (install only those that your code imports):
        | Group                 | Packages                                                                                   |
        |-----------------------|---------------------------------------------------------------------------------------------|
        | **Core**              | agent-framework-core                                                                        |
        | **Model Providers**   | agent-framework-anthropic<br>agent-framework-azure-ai<br>agent-framework-chatkit<br>agent-framework-copilotstudio |
        | **Memory / Storage**  | agent-framework-redis<br>agent-framework-mem0                                               |
        | **Cloud Integrations**| agent-framework-a2a<br>agent-framework-purview                                              |
        | **UI / Developer Tools** | agent-framework-ag-ui<br>agent-framework-devui<br>agent-framework-lab                     |


- Notes and best practices
    - Installation is not automatic on import: to avoid installing unneeded packages you must choose which packages to declare in your project's dependency manifest (requirements.txt, pyproject.toml, etc.).
    - If your project only imports a small subset of agent-framework modules, prefer installing only those subpackages to reduce install time, security surface, and transitive dependency conflicts.

- DO: use your project's package manager to install only the packages you need

### API Documentation References
For detailed API information, refer to the official documentation:
- **Semantic Kernel API**: [Python API Reference](https://learn.microsoft.com/en-us/python/api/semantic-kernel/semantic_kernel?view=semantic-kernel-python)
- **Agent Framework API**: [Python API Reference](https://learn.microsoft.com/en-us/python/api/agent-framework-core/agent_framework?view=agent-framework-python-latest)

When working with the Agent Framework migration, ensure you pull all necessary API documentation and examples in a single operation rather than making incremental requests. Only perform additional lookups when specific edge cases or undocumented scenarios are encountered during the migration process.

### Migration Samples and Examples
#### Chat completion parity
- [01_basic_chat_completion.py](https://raw.githubusercontent.com/microsoft/agent-framework/refs/heads/main/python/samples/semantic-kernel-migration/chat_completion/01_basic_chat_completion.py) — Minimal SK `ChatCompletionAgent` and AF `ChatAgent` conversation.
- [02_chat_completion_with_tool.py](https://raw.githubusercontent.com/microsoft/agent-framework/refs/heads/main/python/samples/semantic-kernel-migration/chat_completion/02_chat_completion_with_tool.py) — Adds a simple tool/function call in both SDKs.
- [03_chat_completion_thread_and_stream.py](https://raw.githubusercontent.com/microsoft/agent-framework/refs/heads/main/python/samples/semantic-kernel-migration/chat_completion/03_chat_completion_thread_and_stream.py) — Demonstrates thread reuse and streaming prompts.

#### Azure AI agent parity
- [01_basic_azure_ai_agent.py](https://raw.githubusercontent.com/microsoft/agent-framework/refs/heads/main/python/samples/semantic-kernel-migration/azure_ai_agent/01_basic_azure_ai_agent.py) — Create and run an Azure AI agent end to end.
- [02_azure_ai_agent_with_code_interpreter.py](https://raw.githubusercontent.com/microsoft/agent-framework/refs/heads/main/python/samples/semantic-kernel-migration/azure_ai_agent/02_azure_ai_agent_with_code_interpreter.py) — Enable hosted code interpreter/tool execution.
- [03_azure_ai_agent_threads_and_followups.py](https://raw.githubusercontent.com/microsoft/agent-framework/refs/heads/main/python/samples/semantic-kernel-migration/azure_ai_agent/03_azure_ai_agent_threads_and_followups.py) — Persist threads and follow-ups across invocations.

#### OpenAI Assistants API parity
- [01_basic_openai_assistant.py](https://raw.githubusercontent.com/microsoft/agent-framework/refs/heads/main/python/samples/semantic-kernel-migration/openai_assistant/01_basic_openai_assistant.py) — Baseline assistant comparison.
- [02_openai_assistant_with_code_interpreter.py](https://raw.githubusercontent.com/microsoft/agent-framework/refs/heads/main/python/samples/semantic-kernel-migration/openai_assistant/02_openai_assistant_with_code_interpreter.py) — Code interpreter tool usage.
- [03_openai_assistant_function_tool.py](https://raw.githubusercontent.com/microsoft/agent-framework/refs/heads/main/python/samples/semantic-kernel-migration/openai_assistant/03_openai_assistant_function_tool.py) — Custom function tooling.

#### OpenAI Responses API parity
- [01_basic_responses_agent.py](https://raw.githubusercontent.com/microsoft/agent-framework/refs/heads/main/python/samples/semantic-kernel-migration/openai_responses/01_basic_responses_agent.py) — Basic responses agent migration.
- [02_responses_agent_with_tool.py](https://raw.githubusercontent.com/microsoft/agent-framework/refs/heads/main/python/samples/semantic-kernel-migration/openai_responses/02_responses_agent_with_tool.py) — Tool-augmented responses workflows.
- [03_responses_agent_structured_output.py](https://raw.githubusercontent.com/microsoft/agent-framework/refs/heads/main/python/samples/semantic-kernel-migration/openai_responses/03_responses_agent_structured_output.py) — Structured JSON output alignment.

#### Copilot Studio parity
- [01_basic_copilot_studio_agent.py](https://raw.githubusercontent.com/microsoft/agent-framework/refs/heads/main/python/samples/semantic-kernel-migration/copilot_studio/01_basic_copilot_studio_agent.py) — Minimal Copilot Studio agent invocation.
- [02_copilot_studio_streaming.py](https://raw.githubusercontent.com/microsoft/agent-framework/refs/heads/main/python/samples/semantic-kernel-migration/copilot_studio/02_copilot_studio_streaming.py) — Streaming responses from Copilot Studio agents.

#### Orchestrations
- [sequential.py](https://raw.githubusercontent.com/microsoft/agent-framework/refs/heads/main/python/samples/semantic-kernel-migration/orchestrations/sequential.py) — Step-by-step SK Team → AF `SequentialBuilder` migration.
- [concurrent_basic.py](https://raw.githubusercontent.com/microsoft/agent-framework/refs/heads/main/python/samples/semantic-kernel-migration/orchestrations/concurrent_basic.py) — Concurrent orchestration parity.
- [group_chat.py](https://raw.githubusercontent.com/microsoft/agent-framework/refs/heads/main/python/samples/semantic-kernel-migration/orchestrations/group_chat.py) — Group chat coordination with an LLM-backed manager in both SDKs.
- [handoff.py](https://raw.githubusercontent.com/microsoft/agent-framework/refs/heads/main/python/samples/semantic-kernel-migration/orchestrations/handoff.py) - Handoff coordination between agents.
- [magentic.py](https://raw.githubusercontent.com/microsoft/agent-framework/refs/heads/main/python/samples/semantic-kernel-migration/orchestrations/magentic.py) — Magentic Team orchestration vs. AF builder wiring.

#### Processes
- [fan_out_fan_in_process.py](https://raw.githubusercontent.com/microsoft/agent-framework/refs/heads/main/python/samples/semantic-kernel-migration/processes/fan_out_fan_in_process.py) — Fan-out/fan-in comparison between SK Process Framework and AF workflows.
- [nested_process.py](https://raw.githubusercontent.com/microsoft/agent-framework/refs/heads/main/python/samples/semantic-kernel-migration/processes/nested_process.py) — Nested process orchestration vs. AF sub-workflows.

Each script is fully async and the `main()` routine runs both implementations back to back so you can observe their outputs in a single execution.

These [samples](https://raw.githubusercontent.com/microsoft/agent-framework/refs/heads/main/python/samples/semantic-kernel-migration/README.md) provide real-world examples of common migration patterns and best practices.

View the **Migration Samples and Examples** as the best practice reference for migrating from Semantic Kernel to Agent Framework. Follow the patterns demonstrated in these samples to ensure a smooth transition. Especially pay attention to the differences in imports, agent creation, thread management, tool registration, and API method usage.

### Agent Framework Package Imports

Here are the correct import packages for all classes mentioned in this guide:

#### Core Framework Classes

```python
# Main agent and workflow classes
# requires: pip install agent-framework-core --pre
from agent_framework import (
    ChatAgent,                    # Primary agent class
    AgentThread,                  # Thread management
    AgentProtocol,               # Agent interface protocol
    BaseAgent,                   # Base agent implementation
    BaseChatClient,              # Base client for custom implementations
    ChatClientProtocol,          # Chat client interface

    # Workflow orchestration
    SequentialBuilder,           # Sequential agent workflows
    ConcurrentBuilder,           # Parallel agent execution
    WorkflowBuilder,             # Custom workflow construction
    WorkflowAgent,               # Workflow-based agents
    MagenticBuilder,             # Dynamic orchestration
    Workflow,                    # Workflow execution

    # Workflow events and executors
    WorkflowOutputEvent,         # Output events from workflows
    WorkflowEvent,               # Base workflow event
    AgentExecutor,               # Agent execution in workflows
    RequestInfoExecutor,         # Human-in-the-loop requests
    FunctionExecutor,            # Function execution in workflows
    Executor,                    # Base executor class
    handler,                     # Decorator for executor handlers

    # Context and memory
    ContextProvider,             # Custom context providers
    Context,                     # Context container
    ChatContext,                 # Context for chat middleware
    WorkflowContext,             # Context for workflow executors
    AggregateContextProvider,    # Multiple context providers
    ChatMessageStore,            # Message persistence protocol
    ChatMessageStoreProtocol,    # Message storage interface

    # Types and responses
    ChatMessage,                 # Message objects
    ChatResponse,                # Chat response objects
    ChatResponseUpdate,          # Streaming chat updates
    AgentRunResponse,            # Agent execution responses
    AgentRunResponseUpdate,      # Streaming response updates
    Role,                        # Message roles (user, assistant, etc.)
    ChatOptions,                 # Chat configuration options
    TextContent,                 # Text content
    FunctionCallContent,         # Function call content
    FunctionResultContent,       # Function result content

    # Middleware
    AgentMiddleware,             # Agent middleware base class
    ChatMiddleware,              # Chat middleware base class
    FunctionMiddleware,          # Function middleware base class
    agent_middleware,            # Agent middleware decorator
    chat_middleware,             # Chat middleware decorator
    function_middleware,         # Function middleware decorator
    use_agent_middleware,        # Agent middleware helper
    use_chat_middleware,         # Chat middleware helper
    
    # Tools and functions
    ai_function,                 # Decorator for AI functions
    HostedCodeInterpreterTool,   # Hosted code interpreter tool
    HostedFileSearchTool,        # Hosted file search tool
)
```

#### Azure Integration
```python
# requires: pip install agent-framework-azure-ai --pre
from agent_framework.azure import (
    AzureAIAgentClient,          # Azure AI Foundry/Studio integration
    AzureAISettings,             # Azure AI configuration
    AzureOpenAIChatClient,       # Azure OpenAI Chat Completion
    AzureOpenAIAssistantsClient, # Azure OpenAI Assistants API
    AzureOpenAIResponsesClient,  # Azure OpenAI with memory
    AzureOpenAISettings,         # Azure OpenAI configuration
    get_entra_auth_token,        # Entra ID authentication helper
)
```

#### OpenAI Integration
```python
# requires: pip install agent-framework-core --pre
from agent_framework.openai import (
    OpenAIChatClient,            # OpenAI Chat Completion
    OpenAIAssistantsClient,      # OpenAI Assistants API
    OpenAIResponsesClient,       # OpenAI with conversation memory
    OpenAISettings,              # OpenAI configuration
    OpenAIContentFilterException, # Exception for content filtering
    ContentFilterResultSeverity, # Enum for filter severity
)
```

#### Exception Handling
```python
# Exception classes for error handling
# requires: pip install agent-framework-core --pre
from agent_framework.exceptions import (
    AgentFrameworkException,     # Base exception class
    AgentException,              # Agent-specific exceptions
    AgentExecutionException,     # Agent execution errors
    AgentInitializationError,    # Agent initialization errors
    AgentThreadException,        # Agent thread management errors
    ChatClientException,         # Chat client errors
    ChatClientInitializationError, # Chat client initialization errors
    ServiceException,            # Service-related errors
    ServiceInitializationError,  # Service initialization errors
    ServiceResponseException,    # Service response errors
    ServiceInvalidRequestError,  # Invalid request errors
    ServiceInvalidResponseError, # Invalid response errors
    ServiceContentFilterException, # Exception for content filtering
    ServiceInvalidAuthError,     # Exception for invalid authentication
    ServiceInvalidExecutionSettingsError, # Exception for invalid execution settings
    ToolException,               # Tool execution errors
    ToolExecutionException,      # Tool execution failures
    MiddlewareException,         # Middleware execution errors
    ContentError,                # Content processing errors
    AdditionItemMismatch,        # Exception for addition item mismatches
)
```

#### Authentication (Azure)

- For method / constructor accepting a `credential` argument, such as `AzureOpenAIChatClient`, use the sync version: `azure.identity.DefaultAzureCredential`
- For method / constructor accepting an `async_credential` argument, such as `AzureAIAgentClient`, use the async version: `azure.identity.aio.DefaultAzureCredential`

### Unsupported Semantic Kernel packages
Following packages are **not supported** in Agent Framework and require custom implementation or alternative approaches:
- semantic_kernel.core_plugins
- semantic_kernel.data
- semantic_kernel.memory
- semantic_kernel.reliability
- semantic_kernel.schema
- semantic_kernel.services
- semantic_kernel.template_engine
- semantic_kernel.text

### Core Agent Mapping

| **Semantic Kernel** | **Agent Framework** | **Key Improvement** |
|---------------------|---------------------|-------------------|
| `AzureChatCompletion` + `Kernel` | `AzureOpenAIChatClient` | Direct client, no kernel |
| `OpenAIChatCompletion` + `Kernel` | `OpenAIChatClient` | Simplified API calls |
| `AzureOpenAIAssistantAgent` | `AzureOpenAIAssistantsClient` | Built-in lifecycle management |
| `OpenAIAssistantAgent` | `OpenAIAssistantsClient` | Native file/tool support |
| `AzureAIAgent` | `AzureAIAgentClient` | Azure AI Foundry integration |
| Not available | `AzureOpenAIResponsesClient` / `OpenAIResponsesClient` | Memory-enhanced conversations |

### Available Clients by Category

#### Chat Clients (Basic Conversations)
- `AzureOpenAIChatClient` - Azure OpenAI Chat Completion
- `OpenAIChatClient` - OpenAI Chat Completion

#### Agent Clients (Azure AI Foundry)
- `AzureAIAgentClient` - Azure AI Foundry managed agents

#### Assistant Clients (Advanced Features)
- `AzureOpenAIAssistantsClient` - Azure OpenAI Assistants with file/code capabilities
- `OpenAIAssistantsClient` - OpenAI Assistants with full toolset

#### Memory-Enhanced Clients (Next Generation)
- `AzureOpenAIResponsesClient` - Azure + conversation memory
- `OpenAIResponsesClient` - OpenAI + conversation memory

#### Custom Implementation
- `BaseChatClient` - Foundation for custom AI integrations
- `ChatAgent` - Universal agent wrapper for any client

## Key differences
Here is a summary of the key differences between the Semantic Kernel Agent Framework and Microsoft Agent Framework to help you migrate your code.

### 1. Package and import updates

#### Semantic Kernel
Semantic Kernel packages are installed as semantic-kernel and imported as semantic_kernel. The package also has a number of extras that you can install to install the different dependencies for different AI providers and other features.

```
from semantic_kernel import Kernel
from semantic_kernel.agents import ChatCompletionAgent
```

#### Agent Framework
Agent Framework package is installed as agent-framework and imported as agent_framework. Agent Framework is built up differently, it has a core package agent-framework-core that contains the core functionality, and then there are multiple packages that rely on that core package, such as agent-framework-azure-ai, agent-framework-mem0, agent-framework-copilotstudio, etc. When you run pip install agent-framework it will install the core package and all packages, so that you can get started with all the features quickly. When you are ready to reduce the number of packages because you know what you need, you can install only the packages you need, so for instance if you only plan to use Azure AI Foundry and Mem0 you can install only those two packages: pip install agent-framework-azure-ai agent-framework-mem0, agent-framework-core is a dependency to those two, so will automatically be installed.

Even though the packages are split up, the imports are all from agent_framework, or it's modules. So for instance to import the client for Azure AI Foundry you would do:

```
from agent_framework.azure import AzureAIAgentClient
```

Many of the most commonly used types are imported directly from agent_framework:

```
from agent_framework import ChatMessage, ChatAgent
```


### 2. Agent Type Consolidation

#### Semantic Kernel
Semantic Kernel provides specific agent classes for various services, for example, ChatCompletionAgent, AzureAIAgent, OpenAIAssistantAgent, etc. See [Agent types in Semantic Kernel](https://learn.microsoft.com/en-us/semantic-kernel/Frameworks/agent/agent-types/azure-ai-agent?pivots=programming-language-python).

#### Agent Framework
In Agent Framework, the majority of agents are built using the ChatAgent which can be used with all the ChatClient based services, such as Azure AI Foundry, OpenAI ChatCompletion, and OpenAI Responses. There are two additional agents: CopilotStudioAgent for use with Copilot Studio and A2AAgent for use with A2A.

All the built-in agents are based on the BaseAgent (from agent_framework import BaseAgent). And all agents are consistent with the AgentProtocol (from agent_framework import AgentProtocol) interface.

### 3. Agent Creation Simplification

#### Semantic Kernel

Every agent in Semantic Kernel depends on a Kernel instance and will have an empty Kernel if not provided.

```
from semantic_kernel.agents import ChatCompletionAgent
from semantic_kernel.connectors.ai.open_ai import OpenAIChatCompletion

agent = ChatCompletionAgent(
    service=OpenAIChatCompletion(),
    name="Support",
    instructions="Answer in one sentence.",
)
```

#### Agent Framework

Agent creation in Agent Framework can be done in two ways, directly:

```
from agent_framework.azure import AzureAIAgentClient
from agent_framework import ChatMessage, ChatAgent

agent = ChatAgent(chat_client=AzureAIAgentClient(credential=AzureCliCredential()), instructions="You are a helpful assistant")

```
Or, with the convenience methods provided by chat clients:

```
from agent_framework.azure import AzureOpenAIChatClient
from azure.identity import AzureCliCredential
agent = AzureOpenAIChatClient(credential=AzureCliCredential()).create_agent(instructions="You are a helpful assistant")
```
The direct method exposes all possible parameters you can set for your agent. While the convenience method has a subset, you can still pass in the same set of parameters, because it calls the direct method internally.

### 4. Thread Management Improvements

#### Semantic Kernel
The caller has to know the thread type and create it manually.

```
from semantic_kernel.agents import ChatHistoryAgentThread

thread = ChatHistoryAgentThread()
```

#### Agent Framework
The agent can be asked to create a new thread for you.

```
agent = ...
thread = agent.get_new_thread()
```

A thread is then created in one of three ways:

1. If the agent has a thread_id (or conversation_id or something similar) set, it will create a thread in the underlying service with that ID. Once a thread has a service_thread_id, you can no longer use it to store messages in memory. This only applies to agents that have a service-side thread concept. such as Azure AI Foundry Agents and OpenAI Assistants.
2. If the agent has a chat_message_store_factory set, it will use that factory to create a message store and use that to create an in-memory thread. It can then no longer be used with a agent with the store parameter set to True.
3. If neither of the previous settings is set, it's considered uninitialized and depending on how it is used, it will either become a in-memory thread or a service thread.

Agent Framework doesn't have a thread deletion API in the AgentThread type as not all providers support hosted threads or thread deletion and this will become more common as more providers shift to responses based architectures.

If you require thread deletion and the provider allows this, the caller should keep track of the created threads and delete them later when necessary via the provider's sdk.

OpenAI Assistants Provider:

```
# OpenAI Assistants threads have self-deletion method in Semantic Kernel
await thread.delete_async()
```

### 5. Tool Registration
#### Semantic Kernel
To expose a function as a tool, you must:

1.Decorate the function with a @kernel_function decorator.
2. Have a Plugin class or use the kernel plugin factory to wrap the function.
3. Have a Kernel to add your plugin to.
4. Pass the Kernel to the agent.

```
from semantic_kernel.functions import kernel_function

class SpecialsPlugin:
    @kernel_function(name="specials", description="List daily specials")
    def specials(self) -> str:
        return "Clam chowder, Cobb salad, Chai tea"

agent = ChatCompletionAgent(
    service=OpenAIChatCompletion(),
    name="Host",
    instructions="Answer menu questions accurately.",
    plugins=[SpecialsPlugin()],
)
```

#### Agent Framework
In a single call, you can register tools directly in the agent creation process. Agent Framework doesn't have the concept of a plugin to wrap multiple functions, but you can still do that if desired.

The simplest way to create a tool is just to create a Python function:

```
def get_weather(location: str) -> str:
    """Get the weather for a given location."""
    return f"The weather in {location} is sunny."

agent = chat_client.create_agent(tools=get_weather)
```

The tools parameter is present on both the agent creation, the run and run_stream methods, as well as the get_response and get_streaming_response methods, it allows you to supply tools both as a list or a single function.

The name of the function will then become the name of the tool, and the docstring will become the description of the tool, you can also add a description to the parameters:

```
from typing import Annotated

def get_weather(location: Annotated[str, "The location to get the weather for."]) -> str:
    """Get the weather for a given location."""
    return f"The weather in {location} is sunny."
```
Finally, you can use the decorator to further customize the name and description of the tool:

```
from typing import Annotated
from agent_framework import ai_function

@ai_function(name="weather_tool", description="Retrieves weather information for any location")
def get_weather(location: Annotated[str, "The location to get the weather for."])
    """Get the weather for a given location."""
    return f"The weather in {location} is sunny."
```

This also works when you create a class with multiple tools as methods.

When creating the agent, you can now provide the function tool to the agent by passing it to the tools parameter.

```
class Plugin:

    def __init__(self, initial_state: str):
        self.state: list[str] = [initial_state]

    def get_weather(self, location: Annotated[str, "The location to get the weather for."]) -> str:
        """Get the weather for a given location."""
        self.state.append(f"Requested weather for {location}. ")
        return f"The weather in {location} is sunny."

    def get_weather_details(self, location: Annotated[str, "The location to get the weather details for."]) -> str:
        """Get detailed weather for a given location."""
        self.state.append(f"Requested detailed weather for {location}. ")
        return f"The weather in {location} is sunny with a high of 25°C and a low of 15°C."

plugin = Plugin("Initial state")
agent = chat_client.create_agent(tools=[plugin.get_weather, plugin.get_weather_details])

... # use the agent

print("Plugin state:", plugin.state)
```
This mechanism is also useful for tools that need additional input that cannot be supplied by the LLM, such as connections, secrets, etc.


### 6. Agent Non-Streaming Invocation
Key differences can be seen in the method names from invoke to run, return types (for example, AgentRunResponse) and parameters.

#### Semantic Kernel
The Non-Streaming invoke uses an async iterator pattern for returning multiple agent messages.

```
async for response in agent.invoke(
    messages=user_input,
    thread=thread,
):
    print(f"# {response.role}: {response}")
    thread = response.thread
```
And there was a convenience method to get the final response:
```
response = await agent.get_response(messages="How do I reset my bike tire?", thread=thread)
print(f"# {response.role}: {response}")
```

#### Agent Framework
The Non-Streaming run returns a single AgentRunResponse with the agent response that can contain multiple messages. The text result of the run is available in response.text or str(response). All messages created as part of the response are returned in the response.messages list. This might include tool call messages, function results, reasoning updates and final results.

```
agent = ...

response = await agent.run(user_input, thread)
print("Agent response:", response.text)
```

### 7. Agent Streaming Invocation
Key differences in the method names from invoke to run_stream, return types (AgentRunResponseUpdate) and parameters.

#### Semantic Kernel

```
async for update in agent.invoke_stream(
    messages="Draft a 2 sentence blurb.",
    thread=thread,
):
    if update.message:
        print(update.message.content, end="", flush=True)
```

#### Agent Framework

Similar streaming API pattern with the key difference being that it returns AgentRunResponseUpdate objects including more agent related information per update.

All contents produced by any service underlying the Agent are returned. The final result of the agent is available by combining the update values into a single response.

```
from agent_framework import AgentRunResponse
agent = ...
updates = []
async for update in agent.run_stream(user_input, thread):
    updates.append(update)
    print(update.text)

full_response = AgentRunResponse.from_agent_run_response_updates(updates)
print("Full agent response:", full_response.text)
```
You can even do that directly:

```
from agent_framework import AgentRunResponse
agent = ...
full_response = AgentRunResponse.from_agent_response_generator(agent.run_stream(user_input, thread))
print("Full agent response:", full_response.text)
```

### 8. Options Configuration
Problem: Complex options setup in Semantic Kernel
```
from semantic_kernel.connectors.ai.open_ai import OpenAIPromptExecutionSettings

settings = OpenAIPromptExecutionSettings(max_tokens=1000)
arguments = KernelArguments(settings)

response = await agent.get_response(user_input, thread=thread, arguments=arguments)
```
Solution: Simplified options in Agent Framework

Agent Framework allows the passing of all parameters directly to the relevant methods, so that you don't have to import anything extra, or create any options objects, unless you want to. Internally, it uses a ChatOptions object for ChatClients and ChatAgents, which you can also create and pass in if you want to. This is also created in a ChatAgent to hold the options and can be overridden per call.

```
agent = ...

response = await agent.run(user_input, thread, max_tokens=1000, frequency_penalty=0.5)
```


## Client Selection Guide

| **Use Case** | **Recommended Client** | **Why** |
|-------------|------------------------|---------|
| **Azure AI Foundry Projects** | `AzureAIAgentClient` | Native integration, managed services |
| **Enterprise Azure** | `AzureOpenAIChatClient` | Security, compliance, direct API |
| **File Analysis/Code** | `AzureOpenAIAssistantsClient` / `OpenAIAssistantsClient` | Built-in file processing |
| **Long Conversations** | `AzureOpenAIResponsesClient` / `OpenAIResponsesClient` | Memory capabilities |
| **Cost Optimization** | `OpenAIChatClient` | Direct pricing |
| **Latest Features** | `OpenAIChatClient` / `OpenAIAssistantsClient` | First access to new capabilities |
| **Custom Integration** | `BaseChatClient` | Full customization |

## Common Migration Issues

### Issue 1: Import Errors
**Problem:**
```python
# Error: cannot import name 'ChatClientAgent'
from agent_framework import ChatClientAgent
```

**Solution:**
```python
# Correct import
from agent_framework import ChatAgent
```

### Issue 2: Kernel Dependencies
**Problem:**
```python
# Old code still references kernel
kernel = Kernel()
agent = ChatCompletionAgent(kernel=kernel, ...)
```

**Solution:**
```python
# Remove kernel entirely
agent = OpenAIChatClient().create_agent(
    instructions="You are a helpful assistant"
)
```

### Issue 3: Thread Type Confusion
**Problem:**
```python
# Error: Don't know which thread type to use
from semantic_kernel.agents import OpenAIAssistantAgentThread
thread = OpenAIAssistantAgentThread(...)
```

**Solution:**
```python
# Let agent create the thread
thread = agent.get_new_thread()
```

### Issue 4: Tool Registration
**Problem:**
```python
# Old decorator doesn't work
from semantic_kernel.functions import kernel_function

@kernel_function
def my_tool():
    pass
```

**Solution:**
```python
# No decorator needed, or use @ai_function
def my_tool(param: str) -> str:
    """Tool description."""
    return f"Result: {param}"

# Pass directly to agent
agent = OpenAIChatClient().create_agent(tools=my_tool)
```

### Issue 5: Authentication Errors
**Problem:**
```bash
# Error: 401 Unauthorized with Azure services
```

**Solution:**
```bash
# Ensure Azure CLI login
az login

# Then use AzureCliCredential
from azure.identity import AzureCliCredential
credential = AzureCliCredential()
```

### Issue 6: Async Context Issues
**Problem:**
```python
# Error: coroutine was never awaited
response = agent.run("Hello")
```

**Solution:**
```python
# Always wrap async operations
import asyncio

async def main():
    response = await agent.run("Hello")
    return response

if __name__ == "__main__":
    result = asyncio.run(main())
```

## Best Practices

### 1. Resource Management
```python
# Use try-finally for cleanup
agent = None
try:
    agent = OpenAIChatClient().create_agent(...)
    response = await agent.run("Hello")
finally:
    if agent:
        # Cleanup if needed
        pass

# For async context managers (Azure AI Agent)
async with AzureCliCredential() as credential:
    async with AzureAIAgentClient(
        async_credential=credential
    ).create_agent() as agent:
        response = await agent.run("Hello")
        # Automatic cleanup on exit
```

### 2. Tool Design
```python
# Keep tools simple and fast
def efficient_tool(x: int, y: int) -> int:
    """Fast calculation."""
    return x * y

# For I/O operations, use async
async def async_tool(query: str) -> str:
    """Database lookup."""
    result = await database.fetch(query)
    return str(result)
```

### 3. Error Handling
```python
from agent_framework.exceptions import AuthenticationException, ServiceException

async def robust_interaction():
    try:
        response = await agent.run("Hello")
        return response.text
    except AuthenticationException:
        return "Authentication failed - check login"
    except ServiceException as e:
        return f"Service error: {e}"
    except Exception as e:
        return f"Unexpected error: {e}"
```

### 4. Configuration Management
```python
from dataclasses import dataclass

@dataclass
class AgentConfig:
    instructions: str
    temperature: float = 0.7
    max_tokens: int = 1000

config = AgentConfig(
    instructions="You are a helpful assistant",
    temperature=0.8
)

agent = AzureOpenAIChatClient(credential=credential).create_agent(
    instructions=config.instructions,
    temperature=config.temperature,
    max_tokens=config.max_tokens
)
```

### 5. Thread Management
```python
# Always use threads for multi-turn conversations
async def conversation():
    thread = agent.get_new_thread()
    
    # Multiple related interactions
    response1 = await agent.run("Hello", thread=thread)
    response2 = await agent.run("How are you?", thread=thread)
    response3 = await agent.run("What did I say first?", thread=thread)
    # Agent remembers: "You said Hello"
```

## Complete Migration Example

**Before (Semantic Kernel):**
```python
import os
from azure.identity import AzureCliCredential
from semantic_kernel import Kernel
from semantic_kernel.agents import ChatCompletionAgent
from semantic_kernel.connectors.ai.open_ai import OpenAIChatCompletion
from semantic_kernel.functions import kernel_function

class WeatherPlugin:
    @kernel_function(description="Get weather")
    def get_weather(self, location: str) -> str:
        return f"Weather in {location}: sunny"

async def old_pattern():
    # Complex setup
    kernel = Kernel()
    kernel.add_service(OpenAIChatCompletion(
        api_key=os.environ["OPENAI_API_KEY"],
        ai_model_id="gpt-4o"
    ))
    
    # Add plugin to kernel
    kernel.add_plugin(WeatherPlugin(), "weather")
    
    # Create agent with kernel
    agent = ChatCompletionAgent(
        kernel=kernel,
        name="Assistant",
        instructions="Answer questions helpfully"
    )
    
    # Manual thread management
    from semantic_kernel.agents import ChatHistoryAgentThread
    thread = ChatHistoryAgentThread()
    
    # Complex invocation
    async for response in agent.invoke(
        messages="What's the weather in Seattle?",
        thread=thread
    ):
        print(f"{response.role}: {response.content}")
```

**After (Agent Framework):**
```python
from agent_framework.openai import OpenAIChatClient
from typing import Annotated

# Simple tool definition - no decorator needed
def get_weather(location: Annotated[str, "City name"]) -> str:
    """Get weather for a location."""
    return f"Weather in {location}: sunny"

async def new_pattern():
    # Simple setup - no kernel needed
    agent = OpenAIChatClient().create_agent(
        instructions="Answer questions helpfully",
        tools=get_weather  # Direct tool registration
    )
    
    # Automatic thread management
    thread = agent.get_new_thread()
    
    # Simple invocation
    response = await agent.run(
        "What's the weather in Seattle?",
        thread=thread
    )
    print(response.text)
```

## Key Differences Summary

| **Aspect** | **Semantic Kernel** | **Agent Framework** |
|-----------|-------------------|-------------------|
| **Package** | `semantic-kernel` | `agent-framework` |
| **Import** | `semantic_kernel` | `agent_framework` |
| **Core Dependency** | Kernel required | No kernel needed |
| **Agent Types** | Multiple specific classes | Unified `ChatAgent` |
| **Tool Registration** | `@kernel_function` + Plugin | Direct function passing |
| **Thread Creation** | Manual, type-specific | `agent.get_new_thread()` |
| **Invocation** | `invoke()`, `invoke_stream()` | `run()`, `run_stream()` |
| **Response Type** | Iterator/multiple messages | Single `AgentRunResponse` |
| **Options** | Complex settings objects | Direct keyword arguments |
| **Orchestration** | Team classes | Builder patterns |
| **Middleware** | Limited/not available | Full middleware support |
| **Context Injection** | Manual | ContextProvider system |
| **Processes** | Process Framework | Workflows + Executors |

## External Links
1. [Semantic Kernel to Microsoft Agent Framework Migration Guide](https://learn.microsoft.com/agent-framework/migration-guide/from-semantic-kernel/?pivots=programming-language-python)
2. [Semantic Kernel to Microsoft Agent Framework Migration Samples](https://github.com/microsoft/agent-framework/tree/main/python/samples/semantic-kernel-migration)
3. [Get Started with Microsoft Agent Framework for Python Developers](https://pypi.org/project/agent-framework/)
4. [Microsoft Agent Framework Release History](https://pypi.org/project/agent-framework/#history)
5. [Microsoft Agent Framework GitHub Repository](https://github.com/microsoft/agent-framework/tree/main/python)