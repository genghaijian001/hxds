# Project Analysis Todo

## Plan

- [ ] Inventory repository files and separate authored project files from logs, binaries, generated output, and vendor content
- [ ] Read backend microservice modules, shared libraries, configuration, and service interaction patterns
- [ ] Trace core business flows including auth, order lifecycle, pricing/rules, payment, messaging, and background jobs
- [ ] Read MIS frontend, both mini-programs, cloud functions, SQL scripts, and repository docs/config
- [ ] Summarize system mechanisms, noteworthy implementation patterns, and areas to watch

## Progress Notes

- [x] Analysis started
- [x] Repository-wide file inventory completed for authored source/config/docs
- [x] Backend microservice modules reviewed
- [x] MIS frontend reviewed
- [x] Driver/customer mini-programs reviewed
- [x] SQL schemas and rule/workflow scripts reviewed
- [x] Cloud function and local tool scripts reviewed

## Review

- Repo scale:
  - `git ls-files` reports about 3628 tracked files.
  - After excluding generated output, vendor content, binary assets, logs, and cache/state files, authored project files are about 1054.
- Modules reviewed:
  - Java microservices under `hxds/`
  - Vue MIS app under `hxds-mis-vue/`
  - Driver mini-program under `hxds-driver-wx/`
  - Customer mini-program under `hxds-customer-wx/`
  - SQL under `db/`
  - Cloud function under `cloudfunctions/`
  - Local utility scripts under `mcp-tools/` and config under `config/`
- Core mechanisms confirmed:
  - Gateway routes external traffic only to BFFs plus WeChat pay callback.
  - BFFs aggregate downstream services through Feign and own app-facing auth/session behavior.
  - Order service owns the main lifecycle, Redis抢单 concurrency control, arrival confirmation gating, payment status updates, and unpaid timeout close task.
  - Map service handles route estimation and nearby driver matching using Tencent Map + Redis GEO.
  - Message service uses RabbitMQ + Mongo-style message refs for new-order fanout and private notifications.
  - Nebula service stores GPS/monitoring records and derives mileage from uploaded GPS points.
  - Mini-programs implement login, location updates, polling/message refresh, and order state transitions from the UI side.
- Important observations:
  - The workspace is very dirty; many source files are modified or untracked, so current behavior may differ from the original baseline.
  - `hxds-rule` currently contains stubbed service implementations in the checked-in source; this does not match the intended rule-engine design described by docs/SQL and likely means the workspace is mid-change or incomplete.
  - No automated test suite or CI workflow was found in the repository root during this pass.

## Work Log

- 2026-05-18 14:10
  - Trigger: user asked for the root cause of the passenger-side avatar upload failure and required full upstream/downstream code reading before any edit.
  - Action: traced the full chain from customer mini-program personal center to gateway, `bff-customer`, COS upload helper, Feign call, and `hxds-cst` persistence.
  - Result: confirmed the real runtime path is `hxds-customer-wx/unpackage/dist/dev/mp-weixin/`, while the editable UniApp source lives under `hxds-customer-wx/`.

- 2026-05-18 14:32
  - Trigger: user asked for latest official information and permission guidance.
  - Action: checked current WeChat official docs for authorization scopes, `wx.chooseImage`, `wx.chooseMedia`, `button.open-type="chooseAvatar"`, and DevTools CLI docs.
  - Result: confirmed there is no `scope.album`; selecting from album does not require a standalone mini-program auth scope.

- 2026-05-18 14:55
  - Trigger: code-level root cause verification against the actual DevTools runtime.
  - Action: compared source page `pages/settings/edit_profile.vue` with runtime page `unpackage/dist/dev/mp-weixin/pages/settings/edit_profile.js`.
  - Result: found a source/runtime divergence. The source still contains `scope.album`, but the current runtime does not. The actual runtime collapses backend upload errors into a generic `上传失败`, because it looks for `msg` while backend exception handlers return `error`.

- 2026-05-18 15:08
  - Trigger: first attempt to update the shared upload helper.
  - Error: the initial patch against `hxds-customer-wx/main.js` failed because the expected context did not match the current file contents.
  - Fix: re-read the exact file contents before patching and switched to a smaller, context-accurate patching strategy.

- 2026-05-18 15:20
  - Trigger: user required that actual work and operation history be recorded for later traceability.
  - Action: added this work log section and will continue appending entries for each meaningful change, error, and verification step.

- 2026-05-18 15:43
  - Trigger: while moving the customer settings page into a source-of-truth rewrite flow, the previous file had to be replaced instead of patched incrementally because repeated context-based patching was blocked by the file's current content/encoding state.
  - Error: the first rewrite step deleted `hxds-customer-wx/pages/settings/edit_profile.vue` before the replacement body had been added.
  - Fix: immediately re-added the page with a complete replacement implementation so the workspace did not remain in a broken state.

- 2026-05-18 15:56
  - Trigger: user asked to continue scanning the passenger mini-program for issues similar to avatar upload divergence and split implementations.
  - Action: compared source pages, compiled runtime pages, app config, and startup logic for the customer app.
  - Findings:
    - `pages/register/register.vue` and `unpackage/dist/dev/mp-weixin/pages/register/register.js` both still submit `chooseAvatar` temporary paths directly as `photo` during registration.
    - `pages/settings/settings.vue` and `unpackage/dist/dev/mp-weixin/pages/settings/settings.js` are not the same page logic anymore; the runtime file still contains stale "about/agreement/privacy" navigation behavior.
    - `pages/login/login.vue` and `unpackage/dist/dev/mp-weixin/pages/login/login.js` are also out of sync; the runtime login flow differs materially from source.
    - `App.vue` includes privacy authorization and location-setting checks, but the current runtime startup logic in `unpackage/dist/dev/mp-weixin/common/main.js` still lacks those newer checks.
    - `unpackage/dist/dev/mp-weixin/common/main.js` still contains upload 401 redirects to `/pages/login/login.vue`, which does not match the source helper path.

- 2026-05-18 16:08
  - Trigger: user clarified that the running settings-page navigation with "about us / agreement / privacy policy" is the intended newer business version, and that I should merge that behavior back into source instead of treating the source page as authoritative.
  - Correction: updated the working assumption for this repo. Source is not automatically the newer branch of truth; each module must be judged independently.
  - Impact:
    - `pages/settings/settings.vue` should be treated as missing business routes currently present in runtime `unpackage/dist/dev/mp-weixin/pages/settings/settings.js`.
    - Similar source/runtime comparisons must distinguish between business-page evolution and platform/config evolution instead of using a single global "newer side" label.

- 2026-05-18 16:22
  - Trigger: user requested a merge checklist that separates runtime-side mature business logic from source-side advanced platform/config logic.
  - Action: completed a directory-level and page-level scan for the customer mini-program.
  - Confirmed structure:
    - Source-only page directory: `pages/index`
    - Runtime-only page directories: `pages/about_us`, `pages/user_agreement`, `pages/privacy_policy`
    - The three runtime-only legal/info pages exist in compiled output but are absent from source.
  - Merge baseline:
    - Runtime business pages to pull back into source:
      - `pages/settings/settings.js` legal/about/privacy navigation
      - `pages/about_us/*`, `pages/user_agreement/*`, `pages/privacy_policy/*` static page assets/content
    - Source platform/config logic to preserve and carry forward:
      - `manifest.json` privacy and permission declarations
      - `App.vue` privacy authorization and location-setting checks
      - newer plugin versions and richer `requiredPrivateInfos`

- 2026-05-18 16:40
  - Trigger: user provided direct MySQL access and required that current schema be read from the live database instead of repository SQL files.
  - Action: connected to local MySQL 8.0.43 as `root`, inspected `hxds_cst.tb_customer` and related runtime data distribution.
  - Findings from live DB:
    - `tb_customer.tel` is not unique and currently defaults to empty string `''`.
    - Current data has 6 customer rows, 3 rows with empty tel, no duplicate non-empty tel values, and all avatars are still empty.
    - This means SMS login/register can be added safely only if tel uniqueness is enforced and empty-string handling is normalized first.

- 2026-05-18 17:02
  - Trigger: user corrected the registration requirement and clarified that this passenger-side registration flow must not force avatar upload or profile completion.

- 2026-05-21 11:20
  - Trigger: user requested a full HXDS production deployment checklist tailored to the current microservice project, and asked whether buying a server still requires deploying backend code and runtime dependencies.
  - Action: generated a dedicated production deployment checklist document under `tasks/`, aligned with the current repository service map, infrastructure dependencies, and the previously generated domain/deployment planning docs.
  - Result: added a project-specific deployment guide covering mandatory services, optional services, dependency layout, deployment order, phase-1 simplification strategy, and the distinction between local Docker Desktop development services and real production runtime services.

- 2026-05-21 11:33
  - Trigger: user requested a production deployment operations manual specifically for migrating from the current Windows local development environment to a Tencent Cloud Linux server.
  - Action: generated a step-by-step migration manual under `tasks/`, covering packaging, file transfer, Linux runtime preparation, Docker-based middleware deployment, JAR service deployment, Nginx/domain hookup, and verification sequence.
  - Result: added a deployment operations handbook that bridges the exact gap between the current local setup and a first-stage Tencent Cloud production environment.

- 2026-05-21 11:47
  - Trigger: user asked to continue with a concrete first-stage production Docker template for the current HXDS stack.
  - Action: checked the repository for any existing `docker-compose` or Dockerfile assets and inspected the current service configs for infrastructure dependencies.
  - Result: confirmed there is no existing reusable compose template in-repo, and generated a new first-stage production `docker-compose` draft for the mandatory middleware stack under `tasks/`.
  - Correction: updated the merge baseline so registration may keep optional avatar selection, but successful registration must still work with the default / empty avatar.
  - Impact:
    - `pages/register/register.vue` should not be redesigned into a mandatory avatar upload flow.
    - Future fixes to registration should focus on removing misleading phone-authorize behavior and keeping avatar optional, not on enforcing profile completion.

- 2026-05-18 17:08
  - Trigger: while continuing the full-chain audit, the preferred search tool `rg.exe` failed in the current shell environment.
  - Error: PowerShell returned `Program 'rg.exe' failed to run: Access is denied`.
  - Fix: switched the investigation to `Get-ChildItem` + `Select-String` so code-chain tracing could continue without blocking on the environment issue.

- 2026-05-18 17:20
  - Trigger: user asked for a full re-check of the passenger-side closed loop, including map search, message center, and the complete ride lifecycle from login to payment.
  - Action: read the customer mini-program source pages and matching runtime output for:
    - `pages/login/login`
    - `pages/register/register`
    - `pages/workbench/workbench`
    - `pages/create_order/create_order`
    - `pages/car_list/car_list`
    - `pages/add_car/add_car`
    - `pages/move/move`
    - `pages/order/order`
    - `pages/order_list/order_list`
    - `pages/message_list/message_list`
    - `pages/message/message`
  - Frontend flow confirmed:
    - login page currently exposes two entry points: plain WeChat login and a misleading `getPhoneNumber` registration/login shortcut.
    - workbench page uses Tencent `chooseLocation` plugin and `QQMapWX.reverseGeocoder`, then checks `hasCustomerCurrentOrder` before allowing new ordering.
    - create-order page requires a saved customer car, recalculates the route visually, creates the order, then polls order status while the popup countdown is running.
    - move page polls order/driver location state and separately polls bill messages when order status reaches the post-drive stage.
    - order page performs payment, then calls active payment-status reconciliation, then opens a rating popup after success.
    - message center lists per-user messages, loads message details by id, and deletes only the message-ref record.

- 2026-05-18 17:34
  - Trigger: backend closed-loop verification for the same customer flow.
  - Action: read and cross-checked:
    - `bff-customer` order, message, order-location, comment, customer-car controllers/services
    - `hxds-odr` order controller/service/timeout task/mapper
    - `hxds-mps` map and driver-location services
    - `hxds-snm` message service and message task
  - Backend mechanism confirmed:
    - BFF order creation recomputes mileage and pricing on submit, searches befitting drivers, inserts the order, and fans out new-order messages asynchronously.
    - `hxds-odr` enforces status transitions with `VALID_TRANSITIONS`, uses Redis optimistic locking for抢单, and gates `startDriving` behind passenger confirmation of arrival.
    - driver real-time order location is stored in Redis by `hxds-mps`, and the customer move page reads it through the BFF order-location endpoint.
    - order payment uses WeChat Pay V3 prepay on the BFF side, persists `prepay_id`, updates bill payment fields, and relies on `hxds-odr` pay callback to write `pay_id` and `pay_time`.
    - unpaid orders are auto-closed by `UnpaidOrderTimeoutTask` when status `6` lasts more than 30 minutes after `end_time`.
    - message center is backed by RabbitMQ + Mongo-style message/message-ref persistence; bill reminders are pulled from a private queue through `receiveBillMessage`.

- 2026-05-18 17:46
  - Trigger: user required that actual runtime structures, not just repository SQL, be used to validate the order flow.
  - Action: queried live MySQL tables `hxds_odr.tb_order`, `hxds_odr.tb_order_bill`, `hxds_odr.tb_order_comment`, and `hxds_cst.tb_customer_car`.
  - Findings from live DB:
    - `tb_order` really contains `prepay_id`, `pay_id`, `pay_time`, and status values exactly matching the order-state logic used by `hxds-odr`.
    - `tb_order_bill` stores `real_pay`, `voucher_fee`, mileage/waiting/return/parking/toll/other fee splits, and one row per order via unique `order_id`.
    - Current runtime data has one paid order (`status=7`) with both `prepay_id` and `pay_id`, and one cancelled order (`status=9`).
    - `tb_order_comment` already contains one stored five-star review for the paid sample order.
    - `tb_customer_car` is very small at present and has a unique key on `(customer_id, car_plate)`, which matches the front-end requirement that the passenger choose a saved car before ordering.

## Passenger App Issue Register

### Runtime / Source divergence

- Issue: customer mini-program source and actual DevTools runtime are not fully aligned.
  - Runtime path: `hxds-customer-wx/unpackage/dist/dev/mp-weixin/`
  - Source path: `hxds-customer-wx/`
  - Why it happens:
    - DevTools is running the compiled output, while several source pages and generated runtime pages have already diverged.
  - Impact:
    - Debugging the source file alone can produce false conclusions about what the user is actually executing.
  - Fix direction:
    - Choose a single source of truth per module, merge runtime-only business logic back into source where needed, then rebuild the runtime output from source.

### Avatar upload page hides backend errors

- Issue: the runtime edit-profile page collapses all upload failures into a generic failure toast.
  - Files:
    - runtime `hxds-customer-wx/unpackage/dist/dev/mp-weixin/pages/settings/edit_profile.js`
    - backend exception handler `hxds/bff-customer/src/main/java/com/example/hxds/bff/customer/config/ExceptionAdvice.java`
  - Why it happens:
    - frontend only checks `msg`
    - backend exceptions return `error`
  - Impact:
    - COS failure, token expiry, validation failure, and downstream persistence failure all look the same in DevTools.
  - Fix direction:
    - frontend should display `error || msg`
    - upload `fail` callbacks should surface `errMsg`
    - 401 should be handled separately with explicit re-login guidance

### Fake phone-authorize login / registration

- Issue: passenger login and registration pages expose `getPhoneNumber` flows that are not truly wired through the backend.
  - Files:
    - `hxds-customer-wx/pages/login/login.vue`
    - `hxds-customer-wx/pages/register/register.vue`
    - `hxds/bff-customer/src/main/java/com/example/hxds/bff/customer/controller/form/RegisterNewCustomerForm.java`
  - Why it happens:
    - frontend sends `phoneCode`
    - backend registration form does not define or process `phoneCode`
    - actual identity model is still `code -> openId -> customerId`
  - Impact:
    - current “微信手机号一键登录/注册” is misleading and should not be treated as a completed feature
  - Fix direction:
    - remove the fake `getPhoneNumber` login/register path
    - replace it with a real SMS verification login/register path that still keeps `open_id` as the primary identity

### Registration avatar handling is optional, not mandatory

- Issue: registration logic must not require avatar upload or full profile completion.
  - User requirement:
    - passenger registration in this mini-program should work with a default / empty avatar
  - Current state:
    - backend already allows empty avatar values
    - live `hxds_cst.tb_customer.photo` data is currently empty for existing rows
  - Fix direction:
    - keep avatar selection optional in registration
    - do not redesign registration into a mandatory profile-completion gate

### Registration currently stores temporary avatar paths directly

- Issue: both source and runtime registration pages currently submit `chooseAvatar` temporary paths directly as `photo`.
  - Files:
    - `hxds-customer-wx/pages/register/register.vue`
    - `hxds-customer-wx/unpackage/dist/dev/mp-weixin/pages/register/register.js`
  - Why it happens:
    - registration directly posts `photo` without persistent upload
  - Impact:
    - if the avatar path is only a temporary local path, stored profile photo values are not durable
  - Fix direction:
    - since avatar is optional, the clean solution is:
      - allow empty/default avatar for registration success
      - only if the user explicitly selected a custom avatar, upload it first and then persist the returned URL

### Move page status is advanced too early

- Issue: the passenger move page locally jumps into driving mode as soon as the passenger confirms arrival.
  - Files:
    - frontend `hxds-customer-wx/pages/move/move.vue`
    - backend `hxds/hxds-odr/src/main/java/com/example/hxds/odr/service/impl/OrderServiceImpl.java`
  - Why it happens:
    - frontend sets `status = 4` immediately after `confirmArriveStartPlace`
    - backend `confirmArriveStartPlace` only flips Redis confirmation state from `1` to `2`
    - actual order status is not changed to `4` until the driver later calls `startDriving`
  - Impact:
    - passenger UI can show the ride as already started before the backend has entered the driving state
  - Fix direction:
    - after passenger confirmation, keep order status display aligned with backend
    - continue polling actual order status from the server instead of hard-coding a local jump to `4`

### Move page can switch from driver position to passenger position incorrectly

- Issue: once the move page switches away from status `2`, it stops reading Redis driver-order location cache and starts using local `updateLocation` events.
  - Files:
    - `hxds-customer-wx/pages/move/move.vue`
    - `hxds/hxds-mps/src/main/java/com/example/hxds/mps/service/impl/DriverLocationServiceImpl.java`
  - Why it happens:
    - status `2` path uses `searchOrderLocationCache`
    - non-`2` path uses local current location updates
    - combined with the premature `status=4` UI jump, the map can end up following the wrong device
  - Impact:
    - the displayed marker/route can drift away from the real driver position
  - Fix direction:
    - clarify whose position should be shown in each order state
    - do not switch to local position updates until the intended backend state transition has actually happened

### Message detail uses the wrong ID type

- Issue: message list passes message-ref ids to the message-detail query, but the backend detail query reads message ids.
  - Files:
    - frontend `hxds-customer-wx/pages/message_list/message_list.vue`
    - frontend `hxds-customer-wx/pages/message/message.vue`
    - `hxds/hxds-snm/src/main/java/com/example/hxds/snm/db/dao/MessageRefDao.java`
    - `hxds/hxds-snm/src/main/java/com/example/hxds/snm/db/dao/MessageDao.java`
  - Why it happens:
    - list page exposes:
      - `id = message_ref._id`
      - `messageId = message._id`
    - detail page sends `id`
    - SNM detail endpoint looks up the `message` collection by that incoming `id`
  - Impact:
    - message detail lookup is logically inconsistent and can fail or fetch the wrong record type
  - Fix direction:
    - either pass `messageId` into the detail page
    - or change the BFF/SNM query chain to resolve `message_ref` first and then fetch the linked `message`

### Message unread-state flow is incomplete

- Issue: there is backend support for marking unread messages as read, but the passenger BFF and UI do not call it.
  - Files:
    - `hxds/hxds-snm/src/main/java/com/example/hxds/snm/controller/MessageController.java`
    - `hxds-customer-wx/pages/message/message.vue`
  - Why it happens:
    - `updateUnreadMessage` exists in SNM
    - no matching passenger-side BFF flow is used by the UI
  - Impact:
    - read-state behavior can remain stale
  - Fix direction:
    - add the missing BFF endpoint and invoke it when a message is opened or after detail loads successfully

### Payment request sign type may be inconsistent with the V3 payment flow

- Issue: passenger order page hardcodes `signType: 'MD5'` in `uni.requestPayment`, while BFF payment creation uses WeChat Pay V3 JSAPI response fields.
  - Files:
    - `hxds-customer-wx/pages/order/order.vue`
    - `hxds/bff-customer/src/main/java/com/example/hxds/bff/customer/service/impl/OrderServiceImpl.java`
  - Why it happens:
    - frontend is not using the returned `signType` value as the source of truth
  - Impact:
    - the client payment invocation can become inconsistent with the payment parameters the backend actually created
  - Fix direction:
    - use the payment response fields returned by the backend consistently
    - verify the exact `signType` expected by the current WeChat JSAPI V3 flow before final release validation

### Phone number uniqueness is not yet safe for SMS login/register

- Issue: live `hxds_cst.tb_customer.tel` is not unique and defaults to empty string.
  - Why it happens:
    - current account model was built around `open_id`, not around phone identity
  - Impact:
    - a future SMS login/register feature can create identity ambiguity unless tel uniqueness is enforced
  - Fix direction:
    - normalize empty tel handling
    - enforce unique tel values before relying on SMS as a login/register entry

## Passenger Business Logic Reference

### Correct closed-loop overview

- Identity model:
  - passenger identity is currently `WeChat code -> openId -> customerId`
  - token/session is issued by `bff-customer`
- Order creation model:
  - customer chooses origin, destination, and a saved car
  - BFF recalculates route mileage/time and recalculates price at submission time
  - BFF searches befitting drivers and inserts order + order bill only when candidate drivers exist
- Ride execution model:
  - order status:
    - `1` waiting for driver
    - `2` driver accepted
    - `3` driver arrived
    - `4` driving
    - `5` drive ended
    - `6` unpaid
    - `7` paid
    - `8` completed
    - `9` customer cancelled
    - `10` driver cancelled
    - `11` incident closed
    - `12` other
  - driver arrival is not enough by itself
  - passenger must confirm arrival in Redis before the driver may legally start driving
- Payment model:
  - BFF creates WeChat Pay V3 JSAPI prepay order
  - `hxds-odr` stores `prepay_id`
  - payment callback writes `pay_id`, `pay_time`, and `status=7`
  - unpaid timeout task closes overdue unpaid orders after 30 minutes in status `6`
- Messaging model:
  - order/bill notifications use RabbitMQ + Mongo `message` / `message_ref`
  - customer message center reads message refs, then resolves actual message content

### Page-by-page flow

- Login
  - `pages/login/login`
  - current valid path is plain WeChat login
  - if backend finds no account for the current `openId`, user must register
- Register
  - `pages/register/register`
  - should support optional avatar and nickname-based registration
  - should not require avatar completion to succeed
- Workbench / map search
  - `pages/workbench/workbench`
  - gets current location
  - uses `chooseLocation` plugin for origin/destination selection
  - blocks new ordering when there is an unfinished or unaccepted current order
- Car selection
  - `pages/car_list/car_list`
  - `pages/add_car/add_car`
  - passenger must choose a stored vehicle before placing a new order
- Create order
  - `pages/create_order/create_order`
  - renders route preview
  - submits order
  - polls for order status while waiting for a driver
- Move / ride
  - `pages/move/move`
  - reads order state
  - reads driver order-location cache
  - lets passenger confirm driver arrival
  - after post-ride billing signal, redirects into order bill/payment page
- Order / pay / comment
  - `pages/order/order`
  - loads bill details
  - creates payment
  - actively queries payment status when needed
  - after payment success, opens rating popup and persists comment
- Message center
  - `pages/message_list/message_list`
  - `pages/message/message`
  - lists message refs, loads message detail, and deletes message-ref records

### Data flow reference

- Login and registration data flow:
  - mini-program -> gateway `/hxds-customer/**` -> `bff-customer` -> `hxds-cst`
- Order creation data flow:
  - mini-program -> `bff-customer`
  - `bff-customer` -> `hxds-mps` for route estimate
  - `bff-customer` -> `hxds-rule` for pricing
  - `bff-customer` -> `hxds-mps` for befitting drivers
  - `bff-customer` -> `hxds-odr` to insert order and bill
  - `bff-customer` -> `hxds-snm` to fan out new-order notifications
- Ride execution data flow:
  - driver uploads order position -> `hxds-mps` Redis cache
  - passenger move page -> `bff-customer/order/location` -> `hxds-mps` reads Redis cache
  - passenger confirm arrival -> `bff-customer` -> `hxds-odr` updates Redis confirmation gate
- Payment data flow:
  - passenger order page -> `bff-customer/createWxPayment`
  - `bff-customer` -> `hxds-odr` validate payable order
  - `bff-customer` -> `hxds-cst` fetch customer openId
  - `bff-customer` -> `hxds-dr` fetch driver openId
  - `bff-customer` -> WeChat Pay V3 create prepay
  - WeChat callback -> gateway exposed callback route -> `hxds-odr/receivePayNotify`
- Messaging data flow:
  - business event -> `hxds-snm` stores message and delivers MQ payload
  - customer page -> `bff-customer/message/*` -> `hxds-snm`
  - `message_ref` tracks receiver-specific read/delete state

## Mini Program Package Upload Error Reference

### Error meaning

- DevTools error:
  - `代码包大小超过限制, main package source size 2146KB exceed max limit 2048KB`
- What it means:
  - this is a packaging / release failure
  - the customer mini-program main package is larger than the allowed main-package limit
  - this is separate from the passenger avatar business upload failure

### Current size-analysis findings

- Local `analyse-data.json` currently reports:
  - total package size about `1,984,080` bytes
  - code size about `1,739,145` bytes
  - resource size about `244,935` bytes
- The DevTools list the user provided shows the biggest weight sources are:
  - plugins:
    - `routePlan`
    - `chooseLocation`
    - `WechatSI`
  - shared bundle:
    - `/common/vendor.js`
  - large page/component JS:
    - `order.js`
    - `create_order.js`
    - `move.js`
    - `workbench.js`
    - `voucher_list.js`
    - `order_list.js`
  - uView component JS/WXSS:
    - `u-popup`
    - `u-button`
    - `u-input`
    - `u-avatar`
    - `u-icon`
    - `u-rate`
    - `u-count-down`
    - `u-tabs-swiper`
  - large resource:
    - `static/login/top.png`

### Why the error appears even though many files are not individually huge

- Main-package overflow is cumulative.
- The problem is not one single file only.
- The current customer app keeps too many business pages, shared component code, plugin dependencies, and resources inside the main package.
- Tab pages and pages frequently referenced from the main navigation stay in the main package unless they are explicitly restructured.

### Fix direction for the package-size problem

- Move non-core pages out of the main package into subpackages where platform rules allow it.
  - strong candidates:
    - `message`
    - `order`
    - `order_list`
    - `voucher_list`
    - `settings/*`
    - `about_us`
    - `user_agreement`
    - `privacy_policy`
    - `add_car`
    - `car_list`
- Keep only true startup / immediate-entry pages in the main package.
  - likely keep:
    - `login`
    - `register`
    - `workbench`
    - maybe `mine`
    - maybe `message_list` if required by tabBar
- Reduce component footprint.
  - audit which `uView` components are actually used
  - stop importing or generating unused component code in pages that do not need them
- Compress or replace heavy static resources.
  - `static/login/top.png` is an easy first target
- Re-check plugin necessity.
  - if a plugin is not required for the release path, remove it
  - if it is required, account for its weight in the package strategy

### Relationship between code fixes and DevTools upload failure

- Solving the business-code bugs will not automatically solve the DevTools “代码包大小超过限制” error.
- They are related only in the sense that both require code changes, but they are different categories of problem:
  - business bug:
    - login / registration / message / move / avatar / payment logic issues
  - packaging bug:
    - main package exceeds size limit
- After the business logic is fixed, the package can still fail to upload if the main package is still too large.
- So the final release path needs both:
  - business fixes
  - package-size optimization / subpackage restructuring

## Implementation Log

- 2026-05-18 11:05
  - Trigger: user required that `weapp-dev` MCP be installed into Codex rather than only configured in Claude.
  - Action:
    - read `C:\Users\GENG HAIJIAN\.claude.json`
    - confirmed existing `MCP_DOCKER` and `weapp-dev`
    - wrote `weapp-dev` into `C:\Users\GENG HAIJIAN\.codex\config.toml`
    - kept workspace `.mcp.json` aligned
  - Verification:
    - verified `E:\微信web开发者工具\cli.bat` exists
    - verified customer runtime project path exists
    - manually started `weapp-dev-mcp` via `npx`
  - Result:
    - MCP config is installed and the server process is startable
    - current Codex thread still did not hot-load new MCP tools; resource listing remained empty

- 2026-05-18 11:16
  - Backend implementation completed for:
    - `bff-customer` login form and login service
    - `hxds-cst` login and tel-uniqueness checks
    - unread-message forwarding in the passenger message chain
  - Verification:
    - ran `mvn -pl bff-customer,hxds-cst -am compile -DskipTests`
    - result: `BUILD SUCCESS`

- 2026-05-18 11:24
  - Source-side customer mini-program changes completed for:
    - `pages/login/login.vue`
    - `pages/register/register.vue`
    - `pages/message_list/message_list.vue`
    - `pages/message/message.vue`
    - `pages/move/move.vue`
    - `pages/order/order.vue`
    - `main.js`
  - Main outcomes:
    - fake `getPhoneNumber` login/register paths removed from source
    - avatar remains optional for registration
    - message detail now uses `messageId`, unread/delete use `messageRefId`
    - move page no longer forces local status `4`
    - payment uses backend-returned `signType`

- 2026-05-18 11:28
  - Runtime-side manual sync completed for:
    - `unpackage/dist/dev/mp-weixin/common/main.js`
    - `unpackage/dist/dev/mp-weixin/pages/message/message.js`
    - `unpackage/dist/dev/mp-weixin/pages/message_list/message_list.js`
    - `unpackage/dist/dev/mp-weixin/pages/move/move.wxml`
    - `unpackage/dist/dev/mp-weixin/pages/move/move.js`
    - `unpackage/dist/dev/mp-weixin/pages/order/order.js`
    - `unpackage/dist/dev/mp-weixin/pages/settings/edit_profile.js`
  - Remaining runtime gap:
    - login/register compiled runtime files are not fully synchronized yet
    - source is already updated, but current `unpackage` login/register pages still need either a full uni-app rebuild or continued manual sync

- 2026-05-18 11:31
  - Environment and service status:
    - Docker dependencies confirmed running:
      - `nacos`
      - `redis`
      - `mongodb`
      - `rabbitmq`
      - `minio`
      - `hbase`
    - Java services already present before this step:
      - `gateway`
      - `hxds-snm`
      - `hxds-mps`
      - plus other existing repo services in memory
    - started additional services:
      - `hxds-cst`
      - `bff-customer`
    - logs written to:
      - `tasks/runtime-logs/hxds-cst.out.log`
      - `tasks/runtime-logs/hxds-cst.err.log`
      - `tasks/runtime-logs/bff-customer.out.log`
      - `tasks/runtime-logs/bff-customer.err.log`

- 2026-05-18 11:38
  - MCP verification deepened after Codex config write:
    - confirmed `C:\Users\GENG HAIJIAN\.codex\config.toml` now contains `MCP_DOCKER` and `weapp-dev`
    - confirmed WeChat DevTools process is running from `E:\微信web开发者工具\wechatdevtools.exe`
    - confirmed DevTools port `29521` is listening
    - attempted Codex MCP discovery through resource listing
  - Result:
    - installation/configuration succeeded at filesystem and process level
    - current active Codex thread still reports `unknown MCP server 'weapp-dev'`
    - conclusion: this thread did not hot-load the newly added MCP server and requires a Codex session/thread restart before MCP tools become callable here

- 2026-05-18 11:42
  - Runtime-side login/register sync completed:
    - `unpackage/dist/dev/mp-weixin/pages/login/login.js`
    - `unpackage/dist/dev/mp-weixin/pages/login/login.wxml`
    - `unpackage/dist/dev/mp-weixin/pages/login/login.wxss`
    - `unpackage/dist/dev/mp-weixin/pages/register/register.js`
    - `unpackage/dist/dev/mp-weixin/pages/register/register.wxml`
    - `unpackage/dist/dev/mp-weixin/pages/register/register.wxss`
  - Main outcomes:
    - removed old dev/test login and register entry points from runtime pages
    - removed runtime `getPhoneNumber` fake registration flow
    - runtime login now matches source-side `微信授权登录 + 手机号验证码登录/注册 + 手动注册入口`
    - runtime register now keeps avatar optional and no longer sends local temp avatar path as a persisted avatar URL

- 2026-05-18 11:44
  - Service recovery:
    - `hxds-tm` is listening on `8070` / `8170`
    - `bff-customer` verified as running from current workspace jar on `8102`
    - `hxds-cst` restarted successfully and is now listening on `8007`

- 2026-05-18 11:47
  - Verification snapshots:
    - `node -c` passed for:
      - `unpackage/dist/dev/mp-weixin/pages/login/login.js`
      - `unpackage/dist/dev/mp-weixin/pages/register/register.js`
    - HTTP availability checks:
      - `http://127.0.0.1:8007/swagger-ui.html` => `200`
      - `http://127.0.0.1:8102/swagger-ui.html` => `200`
      - `http://127.0.0.1:8201/swagger-ui.html` => `404` (expected for gateway route, not treated as service failure)
    - Listener snapshot:
      - `8007` `hxds-cst`
      - `8102` `bff-customer`
      - `8170` `hxds-tm`
      - `8201` `gateway`
      - `29521` WeChat DevTools

- 2026-05-19 09:30
  - Blocking regression reported by user:
    - customer mini-program could no longer open in WeChat DevTools
    - runtime error:
      - `./pages/move/move.wxml end tag missing, near view`
      - followed by render-layer `__route__ is not defined`
  - Root cause:
    - the runtime file `hxds-customer-wx/unpackage/dist/dev/mp-weixin/pages/move/move.wxml` had been manually synchronized earlier and was flattened into one line
    - during that manual sync, one `</view>` boundary was corrupted, producing invalid WXML
    - source file `hxds-customer-wx/pages/move/move.vue` itself was rechecked and is structurally correct; this specific breakage was in the runtime WXML file
  - Fix:
    - rebuilt `unpackage/dist/dev/mp-weixin/pages/move/move.wxml` as a clean multiline WXML structure
    - preserved the existing bindings for `map`, `订单详情`, and `司机已到达`

- 2026-05-19 09:42
  - Follow-up runtime error after WXML repair:
    - render-layer error:
      - `object null is not iterable (cannot read property Symbol(Symbol.iterator))`
  - Investigation result:
    - `hxds-customer-wx/pages/move/move.vue` template binds `@longpress="showHandle"` and `@tap="moreHandle"`
    - but the script section had no `showHandle` / `moreHandle` methods
    - runtime bundle `unpackage/dist/dev/mp-weixin/pages/move/move.js` had the same omission
  - Fix:
    - added `showHandle()` to toggle the information panel
    - added `moreHandle()` to open the order detail page
    - applied the same fix to both source and runtime move page files

- 2026-05-19 10:41
  - Login service availability check:
    - gateway `8201` remained up
    - `hxds-tm`, `hxds-cst`, `bff-customer` were down, so `/hxds-customer/customer/login` returned `503 Service Unavailable`
  - Recovery:
    - restarted:
      - `hxds-tm`
      - `hxds-cst`
      - `bff-customer`
    - verified listening ports:
      - `8170`
      - `8007`
      - `8102`
      - `8201`
  - Follow-up:
    - after service recovery, the same login endpoint no longer returned `503`
    - a synthetic test with fake `code="test"` returned `500`
    - backend logs showed root cause `临时登陆凭证错误`, which is expected for an invalid or expired WeChat login code

- 2026-05-19 10:48
  - Login UI refactor completed:
    - source:
      - `hxds-customer-wx/pages/login/login.vue`
      - `hxds-customer-wx/pages/login/sms_login.vue`
      - `hxds-customer-wx/pages.json`
    - runtime:
      - `unpackage/dist/dev/mp-weixin/pages/login/login.js`
      - `unpackage/dist/dev/mp-weixin/pages/login/login.wxml`
      - `unpackage/dist/dev/mp-weixin/pages/login/login.wxss`
      - `unpackage/dist/dev/mp-weixin/pages/login/sms_login.js`
      - `unpackage/dist/dev/mp-weixin/pages/login/sms_login.wxml`
      - `unpackage/dist/dev/mp-weixin/pages/login/sms_login.wxss`
      - `unpackage/dist/dev/mp-weixin/pages/login/sms_login.json`
      - `unpackage/dist/dev/mp-weixin/app.json`
  - Result:
    - main login page now keeps a large `微信授权登录` button
    - SMS login/register moved behind a much smaller entry button
    - tapping the small button opens a dedicated page for `手机号 + 验证码 + 确定`

- 2026-05-20 09:40
  - Mini-program release/search investigation:
    - checked local customer app configuration and confirmed AppID `wx782592a90f642956`
    - source `hxds-customer-wx/manifest.json` and runtime `hxds-customer-wx/unpackage/dist/dev/mp-weixin/project.config.json` both target the same AppID
    - user symptom set:
      - upload in WeChat DevTools succeeded
      - WeChat search could not find `正好代驾`
      - WeChat `用过的小程序` showed `正好代驾（开发版）`
  - Working conclusion:
    - the uploaded code is present on WeChat platform, but the visible environment is still `develop`
    - DevTools `上传成功` means the development package was uploaded; it does not itself publish a searchable production release
    - the `(开发版)` badge in WeChat strongly indicates the current entry point is still a development build rather than a formal released version

- 2026-05-20 10:25
  - Generated single-AppID consolidation plan for the mini-program brand `正好代驾`
  - Output files:
    - `tasks/single-appid-refactor-v1.md`
    - `tasks/single-appid-refactor-v1.review.md`
  - Plan scope includes:
    - unified AppID strategy
    - source-level consolidation path
    - package layout and role routing
    - customer/driver migration boundaries
    - short-term backend reuse and medium-term unified BFF design

- 2026-05-21 09:50
  - Critical appraisal started for `tasks/single-appid-refactor-v1.md`
  - Official constraints verified:
    - mini-program request domains must use HTTPS/WSS, cannot use IP/localhost, and must be ICP-filed
    - business domains for `web-view` also require HTTPS and ICP filing
    - subpackage size limits remain:
      - total package size <= 30M
      - single main package / subpackage <= 2M
    - WeChat Pay notify URL must be a real public full path URL, cannot carry query parameters, and the callback must be acknowledged within 5 seconds
    - UnionID can be used across apps under the same Open Platform account and is the correct cross-AppID identity bridge
  - Codebase-level migration risk confirmed:
    - current customer and driver identity models are keyed by `open_id`
    - current codebase does not yet contain a real UnionID-based cross-app identity layer
  - Domain status check:
    - `zdkjdj.cn` currently resolves only to DNS SOA metadata in DNSPod
    - no active A/AAAA/CNAME record was observed
    - `https://zdkjdj.cn` currently does not resolve to a reachable public service

- 2026-05-21 10:20
  - Generated corrected plan:
    - `tasks/single-appid-refactor-v2.md`
    - `tasks/single-appid-refactor-v2.review.md`
  - v2 additions:
    - explicit error list from v1
    - official constraint mapping
    - UnionID-first migration strategy
    - production domain and ICP filing requirements
    - `zdkjdj.cn` / `api.zdkjdj.cn` deployment guidance

- 2026-05-21 11:05
  - Added production deployment documents under `tasks/`:
    - `tasks/zdkjdj-production-deployment-checklist.md`
    - `tasks/nginx-gateway-wechat-domain-template.md`
  - Scope covered:
    - Tencent Cloud control-console deployment sequence
    - domain parsing / ICP filing / SSL / Nginx / gateway exposure
    - WeChat mini-program legal domain examples
    - WeChat Pay notify URL template
