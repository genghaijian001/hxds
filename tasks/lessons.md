# Lessons

- Before editing business code in this repo, fully read the complete related chain first:
  page entry, routed page, shared helpers, imported files, upstream callers, downstream services, Feign links, DAO/XML, runtime build output if that is what the user is actually running.
- Prefer adjusting existing files and existing logic over adding new files.
- When source files and compiled runtime output diverge, base debugging conclusions on the code path the user is truly executing.
- When source and runtime differ, do not assume the source is newer. Judge "new vs old" separately per module from actual behavior, available routes, config richness, and the user's intended baseline.
- When the user provides a live database and asks about current behavior or schema, inspect the real MySQL instance first and do not rely on `db/*.sql` as the primary truth source.
- For this customer mini-program, registration must not require avatar upload or profile completion; default avatar / empty avatar is acceptable unless the user explicitly asks to make it mandatory.
- If a shell utility that I normally prefer, such as `rg`, is blocked by the local environment, record the exact failure and immediately switch to an equivalent PowerShell-native fallback instead of stalling the investigation.
- When installing or migrating an MCP server for this workspace, verify three layers separately: config file written, server process can start, and the current Codex session has actually hot-loaded the MCP tools.
- In this Codex desktop environment, adding a new MCP server to `C:\Users\GENG HAIJIAN\.codex\config.toml` is not enough for the current live thread; if the thread still reports `unknown MCP server`, treat that as a session hot-load limitation and do not pretend the MCP is callable until the app/thread is restarted.
- When manually syncing compiled mini-program WXML files, never flatten or rewrite complex page markup as a single compressed line; keep the structure multiline and re-open the exact runtime file in UTF-8 immediately after editing to verify every closing tag.
- In this repo's uni-app pages, after changing template bindings, always verify that every bound handler in the template has a matching method implementation in both source and runtime files before treating the page as synchronized.
- When documenting the "current version" of this repo, verify the active backend module list and live configuration first; do not describe TX-LCN or other legacy transaction stacks as active unless the current modules and runtime config prove they are actually in use.
- When a GitHub push is blocked by secret scanning, do not break the user's local runnable setup just to sanitize the repo; prefer a separate publish-only copy or an untracked local override strategy so the local machine can keep using real secrets while GitHub receives placeholders.
- When the user asks for a version refresh summary, cover the whole stack from the current codebase: Java version, Spring family versions, frontend framework versions, active microservice list, and current middleware choices, rather than only describing one migration item.
