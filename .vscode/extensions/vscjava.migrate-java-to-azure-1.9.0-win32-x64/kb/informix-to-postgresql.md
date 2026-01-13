---
id: informix-to-postgresql
title: Migrate from Informix to PostgresSQL
description: This KB provides guidelines for migrating from Informix to PostgresSQL
hierarchy: Database Tasks
---

# Migration Considerations: Informix to PostgresSQL

Your task is to update all SQL statements in current project, migrate SQL statements from Informix syntax to PostgreSQL compatibility.

Follow these steps:
1. Find Informix related files by searching with this regex pattern: `(?i)SELECT|INSERT|SERIAL8|LVARCHAR|DATETIME\s+(YEAR\s+TO\s+SECOND|HOUR\s+TO\s+MINUTE)|INTERVAL\s+DAY\s+TO\s+DAY|MDY\(|NVL\(|DBINFO\(|\s+MATCHES\s+|TO_CHAR\([^,]+,\s*'%|TO_TIMESTAMP\([^,]+,\s*'%|FIRST\s+\d+|SKIP\s+\d+|BEGIN\s+WORK|COMMIT\s+WORK|ROLLBACK\s+WORK|FOR\s+UPDATE\s+EXCLUSIVE|\(\+\)|OUTER\s+\(|:\w+\s+REFERENCES|\.NEXTVAL|\.CURRVAL|RETURNING\s+\w+;|DEFINE\s+\w+|LET\s+\w+\s*=|CREATE\s+PROCEDURE|END\s+PROCEDURE|\w+:\w+\s+FROM|\[\d+,\d+\]|CURRENT(?!\s*_)|TODAY(?!\s*\()`
2. Review the checklist items below in sequence
3. Apply each relevant modification to the Informix related lines. **IMPORTANT REQUIREMENTS**:
   - DON'T use script files (like shell scripts or Python scripts) to perform batch updates across multiple files. Script-based replacements cannot handle the complexity and context-specific variations found in real code migrations.
   - When editing a file, ensure ALL relevant occurrences are updated. Carefully review the entire file to avoid missing any instances that need migration.
   - Apply modifications consistently across all files. The same Informix pattern should be migrated to the same PostgreSQL equivalent everywhere in the codebase.
4. For each change you make, add a comment in the migrated code that includes the check item information. Example:
```sql
-- Migrated from Informix to PostgreSQL according to SQL check item 1: Use lowercase for identifiers (like table and column names) and data type (like varchar), use uppercase for SQL keywords (like SELECT, FROM, WHERE).
```

# Check list

SQL check item 0: Don't modify the content if it's obviously not related to Informix. For example: File named mysql-schema.sql, file with path mysql/schema.sql. However, also scan the file content for Informix-specific keywords (e.g., DATETIME YEAR TO SECOND, SERIAL8, LVARCHAR, etc.) to ensure it does not contain Informix-related syntax before excluding it.

SQL check item 1: Use lowercase for identifiers (like table and column names) and data type (like varchar), use uppercase for SQL keywords (like SELECT, FROM, WHERE).
```diff
- CREATE TABLE EMPLOYEES (EMPLOYEE_ID SERIAL PRIMARY KEY, FIRST_NAME VARCHAR(20));
+ CREATE TABLE employees (employee_id serial PRIMARY KEY, first_name varchar(20));
```

SQL check item 2: Replace Informix-specific data types with PostgreSQL equivalents.
```diff
- PRODUCT_ID SERIAL8, DESCRIPTION LVARCHAR(2000), IMAGE BLOB, MODIFIED_TIMESTAMP DATETIME YEAR TO SECOND
+ product_id bigserial, description text, image bytea, modified_timestamp timestamp
```

SQL check item 3: Convert Informix CURRENT to PostgreSQL CURRENT_DATE or CURRENT_TIMESTAMP.
```diff
- CURRENT
+ CURRENT_DATE (or CURRENT_TIMESTAMP)
```

SQL check item 4: Convert Informix date/time functions and literals to PostgreSQL format.
```diff
- MDY(6, 17, 2003)
+ '2003-06-17'
```

SQL check item 5: Replace Informix FIRST/SKIP pagination with LIMIT/OFFSET.
```diff
- SELECT FIRST 10 * FROM EMPLOYEES ORDER BY SALARY DESC;
+ SELECT * FROM employees ORDER BY salary DESC LIMIT 10;

- SELECT SKIP 20 FIRST 10 * FROM EMPLOYEES ORDER BY SALARY DESC;
+ SELECT * FROM employees ORDER BY salary DESC LIMIT 10 OFFSET 20;
```
With placeholders:
```diff
- SELECT FIRST #first column_name FROM table_name WHERE conditions;
+ SELECT column_name FROM table_name WHERE conditions LIMIT #first;
```

SQL check item 6: Convert Informix OUTER clause to standard LEFT/RIGHT JOIN syntax.
```diff
- WHERE e.dept_id = d.dept_id(+)
+ LEFT JOIN departments d ON e.dept_id = d.dept_id
```

SQL check item 7: Replace Informix sequences with PostgreSQL sequences and update usage syntax.
```diff
- emp_seq.NEXTVAL, emp_seq.CURRVAL
+ nextval('emp_seq'), currval('emp_seq')
```

SQL check item 8: Convert Informix stored procedures to PostgreSQL functions with dollar-quoted syntax.
```diff
- CREATE PROCEDURE update_item(p_id INT) ... END PROCEDURE;
+ CREATE OR REPLACE PROCEDURE update_item(p_id integer) AS $$ BEGIN ... END; $$ LANGUAGE plpgsql;
```

SQL check item 9: Convert Informix SPL functions to PostgreSQL PL/pgSQL functions with proper RETURNS clause.
```diff
- CREATE FUNCTION calc(p_val DECIMAL) RETURNING DECIMAL; DEFINE v_total DECIMAL; LET v_total = ...; END FUNCTION;
+ CREATE OR REPLACE FUNCTION calc(p_val decimal) RETURNS decimal AS $$ DECLARE v_total decimal; BEGIN v_total := ...; END; $$ LANGUAGE plpgsql;
```

SQL check item 10: Replace Informix SERIAL and SERIAL8 with PostgreSQL SERIAL and BIGSERIAL.
```diff
- LOG_ID SERIAL, BIG_ID SERIAL8
+ log_id serial, big_id bigserial
```

SQL check item 11: Convert Informix LVARCHAR to PostgreSQL TEXT type.
```diff
- LVARCHAR(10000)
+ text
```

SQL check item 12: Replace Informix DATETIME YEAR TO SECOND with PostgreSQL TIMESTAMP.
```diff
- DATETIME YEAR TO SECOND, DATETIME HOUR TO MINUTE
+ timestamp, time
```

SQL check item 13: Replace Informix TODAY with PostgreSQL CURRENT_DATE.
```diff
- TODAY
+ CURRENT_DATE
```

SQL check item 14: Convert Informix transaction control syntax.
```diff
- BEGIN WORK; ... COMMIT WORK; ... ROLLBACK WORK;
+ BEGIN; ... COMMIT; ... ROLLBACK;
```

SQL check item 15: Replace Informix-specific variable declarations in SPL with PostgreSQL PL/pgSQL declarations.
```diff
- DEFINE v_total DECIMAL(10,2); DEFINE v_status CHAR(1);
+ DECLARE v_total decimal(10,2); v_status char(1);
```

SQL check item 16: Replace Informix collection types with PostgreSQL arrays or custom types.
```diff
- item_list LIST(VARCHAR(50) NOT NULL)
+ item_list varchar(50)[]
```

SQL check item 17: Replace Informix MATCHES wildcard pattern matching with PostgreSQL LIKE or SIMILAR TO.
```diff
- WHERE last_name MATCHES 'S*'
+ WHERE last_name LIKE 'S%'
```

SQL check item 18: Convert Informix DBINFO function to PostgreSQL system functions.
```diff
- DBINFO('version', 'full')
+ version()
```

SQL check item 19: Replace Informix exclusive lock mode with PostgreSQL lock syntax.
```diff
- FOR UPDATE EXCLUSIVE
+ FOR UPDATE
```

SQL check item 20: Convert database:table notation to database.table notation.
```diff
- sales:orders
+ sales.orders
```

SQL check item 21: Convert Informix `string_value[start, end]` to PostgreSQL `SUBSTRING(string_value, start, length)`.
```diff
- string_value[2,4]  -- Informix: from position 2 to 4 (3 chars: positions 2,3,4)
+ SUBSTRING(string_value, 2, 3)  -- PostgreSQL: start at 2, length 3 (4-2+1=3)

- string_value[1]    -- Informix: single character at position 1
+ SUBSTRING(string_value, 1, 1)  -- PostgreSQL: start at 1, length 1
```

SQL check item 22: Replace Informix TO_CHAR function with PostgreSQL TO_CHAR function with appropriate format patterns.
```diff
- TO_CHAR(order_date, '%Y-%m-%d')
+ TO_CHAR(order_date, 'YYYY-MM-DD')
```

SQL check item 23: Replace Informix TO_TIMESTAMP function with PostgreSQL TO_TIMESTAMP function.
```diff
- TO_TIMESTAMP('2023-01-15 14:30:00', '%Y-%m-%d %H:%M:%S')
+ TO_TIMESTAMP('2023-01-15 14:30:00', 'YYYY-MM-DD HH24:MI:SS')
```

SQL check item 24: Replace Informix NVL function with PostgreSQL COALESCE function.
```diff
- NVL(phone_number, 'N/A')
+ COALESCE(phone_number, 'N/A')
```

SQL check item 25: Remove Informix dedicated index syntax (+{column}) as it doesn't exist in PostgreSQL.
```diff
- CREATE INDEX +{customer_id} ON orders(customer_id);
+ CREATE INDEX idx_orders_customer_id ON orders(customer_id);
```

SQL check item 26: Replace Informix INTERVAL syntax with PostgreSQL INTERVAL syntax.
```diff
- INTERVAL (30) DAY TO DAY
+ INTERVAL '30 days'
```

SQL check item 9999: Migrate all other Informix-specific content to PostgreSQL. For each line, carefully verify whether it uses Informix-only features.
