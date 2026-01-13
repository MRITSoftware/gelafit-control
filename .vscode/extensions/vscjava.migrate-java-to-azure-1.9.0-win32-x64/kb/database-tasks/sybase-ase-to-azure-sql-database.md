---
id: sybase-ase-to-azure-sql-database
title: Migrate Sybase ASE to Azure SQL Database
description: Migrate Sybase ASE to Azure SQL Database
hierarchy: Database Tasks
---

Your task is to migrate a Java project's database from Sybase ASE to Azure SQL Database.

## Requirements

1. **Authentication**: Use passwordless authentication with user-assigned managed identity (add comments for service principal alternative)
2. **Cleanup**: Remove Sybase-specific files (e.g., `jconn3.jar`, `jconn4.jar`) and dependencies from `pom.xml`/`build.gradle`
