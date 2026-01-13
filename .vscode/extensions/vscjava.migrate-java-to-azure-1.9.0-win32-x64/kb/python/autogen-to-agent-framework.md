---
id: autogen-to-agent-framework
title: Migrate AutoGen to Agent Framework
description: This knowledge base provides specifications of migrating from using AutoGen to use the new Microsoft Agent Framework.
hierarchy: Agent Framework Migration
author: microsoft
language: python
---

# AutoGen to Agent Framework Migration Guide (Python)
AutoGen is a framework for building AI agents and multi-agent systems using large language models (LLMs).
Microsoft Agent Framework is a new multi-language SDK for building AI agents and workflows using LLMs. 
This Guide provides the key knowledge for migrating a Python app from using AutoGen to the Microsoft Agent Framework.


## Key Similarities and Differences

### What Stays the Same
The foundations are familiar. You still create agents around a model client, provide instructions, and attach tools. Both libraries support function-style tools, token streaming, multimodal content, and async I/O.

```
# Both frameworks follow similar patterns
# AutoGen
agent = AssistantAgent(name="assistant", model_client=client, tools=[my_tool])
result = await agent.run(task="Help me with this task")

# Agent Framework
agent = ChatAgent(name="assistant", chat_client=client, tools=[my_tool])
result = await agent.run("Help me with this task")
```

### Key Differences

1. Orchestration style: AutoGen pairs an event-driven core with a high‑level Team. Agent Framework centers on a typed, graph‑based Workflow that routes data along edges and activates executors when inputs are ready.

2. Tools: AutoGen wraps functions with FunctionTool. Agent Framework uses @ai_function, infers schemas automatically, and adds hosted tools such as a code interpreter and web search.

3. Agent behavior: AssistantAgent is single‑turn unless you increase max_tool_iterations. ChatAgent is multi‑turn by default and keeps invoking tools until it can return a final answer.

4. Runtime: AutoGen offers embedded and experimental distributed runtimes. Agent Framework focuses on single‑process composition today; distributed execution is planned.


## Functional Requirements

- FR001: All AutoGen usage are converted to corresponding Microsoft Agent Framework usages
- FR002: Semantic consistency is maintained during the migration, no extra coding logic is generated
- FR003: dependencies are properly setup in the project's package manager
- FR004: Syntax and lint checks are passed
- FR005: Skip migration on Jupyter Notebooks, which are .ipynb files

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

DO: use your project's package manager to install only the packages you need

### API Documentation References
For detailed API information, refer to the official documentation:
- **Autogen API**: [Python API Reference](https://microsoft.github.io/autogen/stable/reference/index.html)
- **Agent Framework API**: [Python API Reference](https://learn.microsoft.com/en-us/python/api/agent-framework-core/agent_framework?view=agent-framework-python-latest)

When working with the Agent Framework migration, ensure you pull all necessary API documentation and examples in a single operation rather than making incremental requests. Only perform additional lookups when specific edge cases or undocumented scenarios are encountered during the migration process.

### Migration Samples and Examples

#### Single-Agent Parity

- [01_basic_assistant_agent.py](https://github.com/microsoft/agent-framework/blob/main/python/samples/autogen-migration/single_agent/01_basic_assistant_agent.py) — Minimal AutoGen `AssistantAgent` and AF `ChatAgent` comparison.
- [02_assistant_agent_with_tool.py](https://github.com/microsoft/agent-framework/blob/main/python/samples/autogen-migration/single_agent/02_assistant_agent_with_tool.py) — Function tool integration in both SDKs.
- [03_assistant_agent_thread_and_stream.py](https://github.com/microsoft/agent-framework/blob/main/python/samples/autogen-migration/single_agent/03_assistant_agent_thread_and_stream.py) — Thread management and streaming responses.
- [04_agent_as_tool.py](https://github.com/microsoft/agent-framework/blob/main/python/samples/autogen-migration/single_agent/04_agent_as_tool.py) — Using agents as tools (hierarchical agent pattern) and streaming with tools.

#### Multi-Agent Orchestration

- [01_round_robin_group_chat.py](https://github.com/microsoft/agent-framework/blob/main/python/samples/autogen-migration/orchestrations/01_round_robin_group_chat.py) — AutoGen `RoundRobinGroupChat` → AF `GroupChatBuilder`/`SequentialBuilder`.
- [02_selector_group_chat.py](https://github.com/microsoft/agent-framework/blob/main/python/samples/autogen-migration/orchestrations/02_selector_group_chat.py) — AutoGen `SelectorGroupChat` → AF `GroupChatBuilder`.
- [03_swarm.py](https://github.com/microsoft/agent-framework/blob/main/python/samples/autogen-migration/orchestrations/03_swarm.py) — AutoGen Swarm pattern → AF `HandoffBuilder`.
- [04_magentic_one.py](https://github.com/microsoft/agent-framework/blob/main/python/samples/autogen-migration/orchestrations/04_magentic_one.py) — AutoGen `MagenticOneGroupChat` → AF `MagenticBuilder`.

Each script is fully async and the `main()` routine runs both implementations back to back so you can observe their outputs in a single execution.

View the **Migration Samples and Examples** as the best practice reference for migrating from Autogen to Agent Framework. Follow the patterns demonstrated in these samples to ensure a smooth transition. Especially pay attention to the differences in imports, agent creation, thread management, tool registration, and API method usage.

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

### Unsupported AutoGen packages
Only a few modules (OpenAI, Azure, mem0, redis, MCP) in `autogen_ext` are supported.

The following `autogen_ext` modules have supported equivalents in Agent Framework:
- autogen_ext.agents.azure
- autogen_ext.agents.openai
- autogen_ext.auth.azure
- autogen_ext.memory.mem0
- autogen_ext.memory.redis
- autogen_ext.models.azure
- autogen_ext.models.openai
- autogen_ext.models.azure.config
- autogen_ext.models.openai.config

**⚠️ Important:** All other `autogen_ext` packages are **not supported** in Agent Framework and require custom implementation or alternative approaches:

- `autogen_ext.agents.*` (except azure, openai)
- `autogen_ext.runtimes.*`
- `autogen_ext.models.*` (except azure, openai)
- `autogen_ext.tools.*`
- `autogen_ext.ui.*`
- Any other `autogen_ext.*`(except azure, openai) modules not listed above

### Core Agent Mapping

| **AutoGen** | **Agent Framework** | **Key Improvement** |
|-------------|-------------------- |-------------------|
| `OpenAIChatCompletionClient` | `OpenAIChatClient` | |
| `AzureOpenAIChatCompletionClient` | `AzureOpenAIChatClient` | |
| `AzureAIChatCompletionClient` | `AzureAIAgentClient` | |
| `ConversableAgent` | `ChatAgent` | Unified interface, no manual conversation management |
| `AssistantAgent` | `ChatAgent` with instructions | Simplified creation with built-in system prompts |
| `UserProxyAgent` | Human-in-the-loop workflows | Integrated approval mechanisms |
| `GroupChat` + `GroupChatManager` | `SequentialBuilder` / `ConcurrentBuilder` / `MagenticBuilder` | Declarative workflow orchestration |
| `register_function` | Direct function tools | Native function calling without decorators |
| Manual conversation termination | Automatic workflow completion | Built-in completion detection |
| Complex client configuration | `client.create_agent()` factory methods | One-line agent creation with sensible defaults |
| Manual model/endpoint setup | Client-specific implementations | Provider-optimized configurations |

#### Key Differences

1. Orchestration style: AutoGen pairs an event-driven core with a high‑level `Team`. Agent Framework centers on a typed,
   graph‑based `Workflow` that routes data along edges and activates executors when inputs are ready.
2. Tools: AutoGen wraps functions with `FunctionTool`. Agent Framework uses `@ai_function`, infers schemas automatically,
   and adds hosted tools such as a code interpreter and web search.
3. Agent behavior: `AssistantAgent` is single‑turn unless you increase `max_tool_iterations`. `ChatAgent` is multi‑turn
   by default and keeps invoking tools until it can return a final answer.
4. Runtime: AutoGen offers embedded and experimental distributed runtimes. Agent Framework focuses on single‑process
   composition today; distributed execution is planned.

### Available Clients by Provider

#### Azure Integration (Recommended)
- `AzureAIAgentClient` - Azure AI Foundry/Studio integration
- `AzureOpenAIChatClient` - Azure OpenAI Chat Completion
- `AzureOpenAIAssistantsClient` - Azure OpenAI Assistants
- `AzureOpenAIResponsesClient` - Azure OpenAI with conversation memory

#### Other Providers
- `OpenAIChatClient` - OpenAI Chat Completion
- `OpenAIAssistantsClient` - OpenAI Assistants
- `OpenAIResponsesClient` - OpenAI with conversation memory
- `BaseChatClient` - Custom implementations

#### Workflow Orchestration
- `SequentialBuilder` - Sequential agent interactions
- `ConcurrentBuilder` - Parallel agent execution
- `WorkflowAgent` - Complex multi-step workflows
- `MagenticBuilder` - Dynamic orchestration patterns

## Architectural Changes Overview

### 1. Import Simplification

**Before (AutoGen):**
```python
import autogen
from autogen import ConversableAgent, AssistantAgent, UserProxyAgent, GroupChat, GroupChatManager
from autogen.agentchat.contrib.retrieve_assistant_agent import RetrieveAssistantAgent
```

**After (Agent Framework):**
```python
from agent_framework import ChatAgent, AgentThread, SequentialBuilder, ConcurrentBuilder
from agent_framework.azure import AzureAIAgentClient, AzureOpenAIChatClient
from agent_framework.openai import OpenAIChatClient
```

### 2. Basic Agent Creation and Execution

**Before: AutoGen AssistantAgent**
```python
from autogen_agentchat.agents import AssistantAgent

agent = AssistantAgent(
    name="assistant",
    model_client=client,
    system_message="You are a helpful assistant.",
    tools=[my_tool],
    max_tool_iterations=1  # Single-turn by default
)

# Execution
result = await agent.run(task="What's the weather?")
```

**After: Agent Framework ChatAgent**
```python
from agent_framework import ChatAgent, ai_function
from agent_framework.openai import OpenAIChatClient

# Create simple tools for the example
@ai_function
def get_weather(location: str) -> str:
    """Get weather for a location."""
    return f"Weather in {location}: sunny"

@ai_function
def get_time() -> str:
    """Get current time."""
    return "Current time: 2:30 PM"

# Create client
client = OpenAIChatClient(model_id="gpt-5")

async def example():
    # Direct creation
    agent = ChatAgent(
        name="assistant",
        chat_client=client,
        instructions="You are a helpful assistant.",
        tools=[get_weather]  # Multi-turn by default
    )

    # Factory method (more convenient)
    agent = client.create_agent(
        name="assistant",
        instructions="You are a helpful assistant.",
        tools=[get_weather]
    )

    # Execution with runtime tool configuration
    result = await agent.run(
        "What's the weather?",
        tools=[get_time],  # Can add tools at runtime
        tool_choice="auto"
    )
```

### 3. Managing Conversation State with AgentThread

**Before (AutoGen Manual Management):**
```python
# Complex conversation initiation and management
user_proxy.initiate_chat(
    assistant,
    message="Hello, how can you help me?",
    max_turns=5
)

# Manual conversation history tracking
conversation_history = user_proxy.chat_messages[assistant]
```

**After (Agent Framework Automatic Management):**
```python
# Stateless by default, i.e., without a thread
response = await agent.run("Hello, how can you help me?")
print(response.text)

# Explicit thread management for persistence
thread = agent.get_new_thread()
response1 = await agent.run("Hello!", thread=thread)
response2 = await agent.run("What did I say before?", thread=thread)  # Remembers context

# Thread serialization for persistence
serialized = await thread.serialize()
restored_thread = await agent.deserialize_thread(serialized)
```

### 4. Tool Creation and Integration

**Before: AutoGen FunctionTool**
```python
from autogen_core.tools import FunctionTool

async def get_weather(location: str) -> str:
    """Get weather for a location."""
    return f"Weather in {location}: sunny"

# Manual tool creation
tool = FunctionTool(
    func=get_weather,
    description="Get weather information"
)

# Use with agent
agent = AssistantAgent(name="assistant", model_client=client, tools=[tool])
```

**After: Agent Framework @ai_function:**
```python
from agent_framework import ai_function
from typing import Annotated
from pydantic import Field

@ai_function
def get_weather(
    location: Annotated[str, Field(description="The location to get weather for")]
) -> str:
    """Get weather for a location."""
    return f"Weather in {location}: sunny"

# Direct use with agent (automatic conversion)
agent = ChatAgent(name="assistant", chat_client=client, tools=[get_weather])
```

### 5. Message Types

**Before: AutoGen Message Types**
```python
from autogen_agentchat.messages import TextMessage, MultiModalMessage
from autogen_core.models import UserMessage

# Text message
text_msg = TextMessage(content="Hello", source="user")

# Multi-modal message
multi_modal_msg = MultiModalMessage(
    content=["Describe this image", image_data],
    source="user"
)

# Convert to model format for use with model clients
user_message = text_msg.to_model_message()
```

**After: Agent Framework Message Types**
```python
from agent_framework import ChatMessage, TextContent, DataContent, UriContent, Role
import base64

# Text message
text_msg = ChatMessage(role=Role.USER, text="Hello")

# Supply real image bytes, or use a data: URI/URL via UriContent
image_bytes = b"<your_image_bytes>"
image_b64 = base64.b64encode(image_bytes).decode()
image_uri = f"data:image/jpeg;base64,{image_b64}"

# Multi-modal message with mixed content
multi_modal_msg = ChatMessage(
    role=Role.USER,
    contents=[
        TextContent(text="Describe this image"),
        DataContent(uri=image_uri, media_type="image/jpeg")
    ]
)
```

### 6. Multi-Agent Orchestration Evolution

**Before (AutoGen GroupChat):**
```python
# Complex group chat setup with manual management
agents = [assistant1, assistant2, assistant3]

groupchat = GroupChat(
    agents=agents,
    messages=[],
    max_round=10,
    speaker_selection_method="round_robin"
)

manager = GroupChatManager(
    groupchat=groupchat,
    llm_config=llm_config
)

# Manual initiation and termination handling
user_proxy.initiate_chat(
    manager,
    message="Let's discuss the project plan"
)
```

**After (Agent Framework Workflows):**
```python
# Declarative sequential workflow
writer = client.create_agent(
    name="Writer",
    instructions="You are a creative writer"
)

reviewer = client.create_agent(
    name="Reviewer",
    instructions="You provide constructive feedback"
)

# Simple sequential orchestration
workflow = SequentialBuilder().participants([writer, reviewer]).build()

# Run with streaming results
async for event in workflow.run_stream("Write a story about AI"):
    if isinstance(event, WorkflowOutputEvent):
        print(f"Final result: {event.data}")

# Concurrent execution
concurrent_workflow = ConcurrentBuilder().participants([agent1, agent2, agent3]).build()
```

## Client Implementation Patterns

### Azure AI Agent Client (Recommended for Azure)

```python
from agent_framework.azure import AzureAIAgentClient
from azure.identity.aio import DefaultAzureCredential

async def azure_ai_example():
    async with (
        DefaultAzureCredential() as credential,
        AzureAIAgentClient(async_credential=credential).create_agent(
            name="Assistant",
            instructions="You are a helpful assistant",
            tools=[get_weather]
        ) as agent,
    ):
        response = await agent.run("What's the weather like?")
        print(response.text)
```

### OpenAI Client (Direct OpenAI Integration)

```python
from agent_framework.openai import OpenAIChatClient

# Uses OPENAI_API_KEY environment variable
agent = OpenAIChatClient().create_agent(
    name="Assistant",
    instructions="You are a creative assistant",
    model="gpt-4o",
    temperature=0.9,
    tools=[creative_writing_tool]
)

response = await agent.run("Write a poem about technology")
```

### Azure OpenAI Client

```python
from agent_framework.azure import AzureOpenAIChatClient
from azure.identity import DefaultAzureCredential

client = AzureOpenAIChatClient(
    endpoint="https://your-resource.openai.azure.com/",
    credential=DefaultAzureCredential(), # use sync credential
    deployment_name="gpt-4o"
)

agent = client.create_agent(
    name="Analyst",
    instructions="You are a data analyst",
    temperature=0.3
)
```

## Advanced Migration Patterns

### 1. Streaming Responses

**Before: AutoGen Streaming:**
```python
# Model client streaming
async for chunk in client.create_stream(messages):
    if isinstance(chunk, str):
        print(chunk, end="")

# Agent streaming
async for event in agent.run_stream(task="Hello"):
    if isinstance(event, ModelClientStreamingChunkEvent):
        print(event.content, end="")
    elif isinstance(event, TaskResult):
        print("Final result received")
```

**After: Agent Framework Streaming:**
```python
# Assume we have client, agent, and tools from previous examples
async def streaming_example():
    # Chat client streaming
    async for chunk in client.get_streaming_response("Hello", tools=tools):
        if chunk.text:
            print(chunk.text, end="")

    # Agent streaming
    async for chunk in agent.run_stream("Hello"):
        if chunk.text:
            print(chunk.text, end="", flush=True)
```

### 2. Human-in-the-Loop

**Before (AutoGen UserProxyAgent):**
```python
user_proxy = UserProxyAgent(
    name="user_proxy",
    human_input_mode="ALWAYS",  # or "NEVER" or "TERMINATE"
    max_consecutive_auto_reply=0
)
```

**After (Agent Framework Workflow Integration):**
```python
from agent_framework import RequestInfoExecutor, WorkflowBuilder

# Human approval in workflow
workflow = (
    WorkflowBuilder()
    .add_executor("agent", AgentExecutor(agent))
    .add_executor("approval", RequestInfoExecutor("Approve this response? (y/n)"))
    .add_edge("START", "agent")
    .add_edge("agent", "approval")
    .add_edge("approval", "END")
    .build()
)

# Human input is naturally integrated into the workflow
```

### 3. Retrieval-Augmented Generation (RAG)

**Before (AutoGen RetrieveAssistantAgent):**
```python
from autogen.agentchat.contrib.retrieve_assistant_agent import RetrieveAssistantAgent

assistant = RetrieveAssistantAgent(
    name="assistant",
    system_message="You are a helpful assistant with access to documents",
    llm_config=llm_config,
    retrieve_config={
        "task": "qa",
        "docs_path": "docs/",
    }
)
```

**After (Agent Framework with Context Providers):**
```python
from agent_framework import ContextProvider, ChatAgent

# Custom context provider for RAG
class DocumentContextProvider(ContextProvider):
    async def get_context(self, query: str) -> str:
        # Your document retrieval logic
        relevant_docs = await search_documents(query)
        return "\n".join(relevant_docs)

agent = ChatAgent(
    chat_client=client,
    instructions="You are a helpful assistant. Use the provided context to answer questions.",
    context_providers=[DocumentContextProvider()]
)
```

### 4. Code Execution

**Before (AutoGen Code Execution):**
```python
user_proxy = UserProxyAgent(
    name="user_proxy",
    code_execution_config={
        "work_dir": "coding",
        "use_docker": False
    }
)
```

**After (Agent Framework Assistants):**
```python
from agent_framework.azure import AzureOpenAIAssistantsClient

# Use Assistants API with built-in code interpreter
async with AzureOpenAIAssistantsClient(
    endpoint="https://your-resource.openai.azure.com/",
    credential=DefaultAzureCredential() # sync credential
).create_agent(
    name="CodeAssistant",
    instructions="You are a coding assistant",
    tools=[{"type": "code_interpreter"}]
) as agent:
    response = await agent.run("Write Python code to calculate fibonacci numbers")
```

## Workflow Orchestration Patterns

### 1. Sequential Agent Conversation

**Migration from AutoGen GroupChat to Sequential Workflow:**

```python
# Agent Framework sequential pattern
from agent_framework import SequentialBuilder, WorkflowOutputEvent

writer = client.create_agent(
    name="Writer",
    instructions="You are a creative writer. Write one paragraph."
)

editor = client.create_agent(
    name="Editor",
    instructions="You are an editor. Improve the writing quality."
)

reviewer = client.create_agent(
    name="Reviewer",
    instructions="You are a reviewer. Provide final feedback."
)

# Build sequential workflow
workflow = SequentialBuilder().participants([writer, editor, reviewer]).build()

# Execute with streaming
async for event in workflow.run_stream("Write about artificial intelligence"):
    if isinstance(event, WorkflowOutputEvent):
        # Final conversation history
        conversation = event.data
        for msg in conversation:
            print(f"{msg.author_name}: {msg.text}")
```

### 2. Concurrent Agent Execution

```python
from agent_framework import ConcurrentBuilder

# Multiple agents working in parallel
research_agent = client.create_agent(
    name="Researcher",
    instructions="Research the topic thoroughly"
)

analysis_agent = client.create_agent(
    name="Analyst",
    instructions="Analyze data and provide insights"
)

summary_agent = client.create_agent(
    name="Summarizer",
    instructions="Create a comprehensive summary"
)

# Concurrent execution
workflow = ConcurrentBuilder().participants([
    research_agent,
    analysis_agent,
    summary_agent
]).build()

results = []
async for event in workflow.run_stream("Analyze the impact of AI on healthcare"):
    if isinstance(event, WorkflowOutputEvent):
        results.extend(event.data)
```

### 3. Dynamic Orchestration with Magentic

```python
from agent_framework import MagenticBuilder

# Dynamic multi-agent orchestration
planner = client.create_agent(
    name="Planner",
    instructions="You create detailed plans for complex tasks"
)

executor_agents = [
    client.create_agent(name=f"Executor{i}", instructions=f"You execute task {i}")
    for i in range(3)
]

# Magentic manages dynamic task assignment
workflow = (
    MagenticBuilder()
    .planner(planner)
    .executors(executor_agents)
    .max_iterations(5)
    .build()
)

async for event in workflow.run_stream("Plan and execute a marketing campaign"):
    if hasattr(event, 'text') and event.text:
        print(event.text)
```

## Error Handling and Robustness

### Exception Management

```python
from agent_framework.exceptions import (
    AgentExecutionException,
    AuthenticationException,
    ServiceException
)

async def robust_agent_interaction():
    try:
        response = await agent.run("Complex query")
        return response.text
    except AuthenticationException:
        return "Authentication failed - please check credentials"
    except ServiceException as e:
        return f"Service unavailable: {e}"
    except AgentExecutionException as e:
        return f"Agent execution failed: {e}"
    except Exception as e:
        logger.error(f"Unexpected error: {e}")
        return "An unexpected error occurred"
```

### Resource Management

```python
# Automatic cleanup with async context managers
async def managed_resources():
    async with (
        DefaultAzureCredential() as credential,
        AzureAIAgentClient(async_credential=credential).create_agent(
            name="Assistant",
            instructions="You are helpful"
        ) as agent,
    ):
        # Agent and credentials automatically cleaned up
        return await agent.run("Hello")
```

## Configuration and Best Practices

### 1. Environment Configuration

**AutoGen Environment Setup:**
```python
# AutoGen required complex config dictionaries
config_list = [
    {
        "model": "gpt-4",
        "api_key": os.getenv("OPENAI_API_KEY"),
        "api_base": os.getenv("OPENAI_API_BASE"),
        "api_type": "azure",
        "api_version": "2023-05-15"
    }
]
```

**Agent Framework Environment:**
```python
# Agent Framework uses Azure CLI credentials by default
# Just run: az login

# Or use environment variables naturally:
# For Azure AI Agent Service:
# - No additional environment variables needed with Azure CLI auth

# For Azure OpenAI:
# AZURE_OPENAI_ENDPOINT (e.g., "https://your-resource.openai.azure.com/")
# AZURE_OPENAI_DEPLOYMENT_NAME (e.g., "gpt-4o")

# For OpenAI:
# OPENAI_API_KEY
```

### 2. Performance Optimization

```python
# Efficient tool design
def fast_calculation(x: int, y: int) -> int:
    """Keep tools simple and fast."""
    return x * y

# Use async for I/O operations
async def async_database_lookup(query: str) -> str:
    """Use async for I/O bound operations."""
    result = await database.query(query)
    return str(result)

# Batch operations where possible
async def batch_processing(items: list[str]) -> list[str]:
    """Process multiple items efficiently."""
    tasks = [process_item(item) for item in items]
    return await asyncio.gather(*tasks)
```

### 3. Memory Management

```python
# Custom message store for conversation persistence
from agent_framework import AgentThread, ChatMessageStore

class CustomMessageStore(ChatMessageStore):
    async def add_message(self, message: ChatMessage) -> None:
        # Custom storage logic (database, file, etc.)
        await self.save_to_database(message)

    async def get_messages(self) -> list[ChatMessage]:
        # Retrieve from custom storage
        return await self.load_from_database()

# Use with agent
thread = AgentThread(message_store=CustomMessageStore())
agent = ChatAgent(
    chat_client=client,
    instructions="You are helpful",
    chat_message_store_factory=lambda: CustomMessageStore()
)
```

## Complete Migration Example

**Before (AutoGen Multi-Agent System):**
```python
import autogen
import os

# Complex configuration
config_list = [{
    "model": "gpt-4",
    "api_key": os.getenv("OPENAI_API_KEY")
}]

llm_config = {
    "config_list": config_list,
    "temperature": 0.7
}

# Multiple agent setup
writer = autogen.AssistantAgent(
    name="Writer",
    system_message="You are a creative writer",
    llm_config=llm_config
)

critic = autogen.AssistantAgent(
    name="Critic",
    system_message="You provide constructive criticism",
    llm_config=llm_config
)

user_proxy = autogen.UserProxyAgent(
    name="UserProxy",
    human_input_mode="NEVER",
    max_consecutive_auto_reply=2,
    is_termination_msg=lambda x: x.get("content", "").find("TERMINATE") >= 0
)

# Group chat setup
groupchat = autogen.GroupChat(
    agents=[writer, critic, user_proxy],
    messages=[],
    max_round=6
)

manager = autogen.GroupChatManager(groupchat=groupchat, llm_config=llm_config)

# Execute
user_proxy.initiate_chat(manager, message="Write a short story about robots")
```

**After (Agent Framework):**
```python
import asyncio
from agent_framework import SequentialBuilder, WorkflowOutputEvent
from agent_framework.azure import AzureAIAgentClient
from azure.identity.aio import DefaultAzureCredential

async def modern_multi_agent():
    async with DefaultAzureCredential() as credential:
        client = AzureAIAgentClient(async_credential=credential)

        # Simple agent creation
        writer = client.create_agent(
            name="Writer",
            instructions="You are a creative writer. Write one story segment.",
            temperature=0.7
        )

        critic = client.create_agent(
            name="Critic",
            instructions="You provide constructive feedback on writing.",
            temperature=0.3
        )

        # Declarative workflow
        workflow = SequentialBuilder().participants([writer, critic]).build()

        # Execute with streaming
        print("=== AI Story Collaboration ===")
        async for event in workflow.run_stream("Write a short story about robots"):
            if isinstance(event, WorkflowOutputEvent):
                conversation = event.data
                for msg in conversation[-2:]:  # Show last 2 messages
                    author = msg.author_name or "System"
                    print(f"\n[{author}]:\n{msg.text}")

# Run the modern version
asyncio.run(modern_multi_agent())
```

## Key Benefits of Migration

### 1. **Simplified Architecture**
- No complex configuration dictionaries
- Direct client usage without intermediary layers
- Unified interface across all AI providers

### 2. **Enhanced Developer Experience**
- Native async/await support throughout
- Automatic resource management
- Built-in streaming and error handling

### 3. **Better Azure Integration**
- Native Azure identity integration
- Seamless Azure AI Foundry support
- Simplified authentication with `az login`

### 4. **Powerful Orchestration**
- Declarative workflow definition
- Built-in human-in-the-loop support
- Visual workflow debugging and monitoring

### 5. **Production Ready**
- Comprehensive error handling
- Resource cleanup automation
- Observability and telemetry integration

## Migration Checklist

1. **✅ Replace imports** - Update to Agent Framework packages
2. **✅ Simplify authentication** - Use Azure CLI or service principals
3. **✅ Convert agents** - Replace AutoGen agents with ChatAgent
4. **✅ Update function registration** - Use direct function tools
5. **✅ Migrate conversations** - Replace initiate_chat with agent.run()
6. **✅ Convert group chats** - Use SequentialBuilder or ConcurrentBuilder
7. **✅ Add error handling** - Implement proper exception handling
8. **✅ Test workflows** - Validate multi-agent orchestrations
9. **✅ Optimize performance** - Review tool design and async patterns
10. **✅ Deploy and monitor** - Use built-in observability features

The Agent Framework provides a modern, production-ready foundation for building AI agent systems that scale from simple chatbots to complex multi-agent workflows.


## External links

1. [AutoGen to Microsoft Agent Framework Migration Guide](https://learn.microsoft.com/agent-framework/migration-guide/from-autogen/)
2. [Get Started with Microsoft Agent Framework for Python Developers](https://pypi.org/project/agent-framework/)
3. [Microsoft Agent Framework Release History](https://pypi.org/project/agent-framework/#history)
4. [Microsoft Agent Framework GitHub Repository](https://github.com/microsoft/agent-framework/tree/main/python)
