---
id: jax-rpc-to-jax-ws
title: Migrate JAX-RPC to JAX-WS
description: Provides specifications for migrating from JAX-RPC to JAX-WS
hierarchy: JAX Tasks
showTask: false
---

Migrate JAX-RPC to JAX-WS for current project. Requirements:
 - When modify files, double check whether it's necessary, don't do unnecessary change. For example
   - Don't add new business logic.
   - Don't delete existing business logic.
   - Don't upgrade dependency's version if original version can still work.
 - When adding new dependencies, double check the version is compatible with existing java version and tomcat version.
 - When updating web.xml, double check it's compatible with existing tomcat version.
 - Delete unused files. Example: JAX-RPC configuration files.
