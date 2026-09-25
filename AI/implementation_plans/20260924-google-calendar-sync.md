# Implementation plan — Google Calendar sync (hall + catering + pujari)

**Date:** 2026-09-24  
**Project:** Shree Jalaram Mandir Website  
**Status:** Owner Accept is explicit — implement this Rev  
**Rev:** 2026-09-24j — BT_PC live verify folded in (placeholder `createCalendarEvent`, controller-after-submit, `pujari_seva`, missing CREATE TABLE column, CateringController-only approve). Builds on 2026-09-24i must-fixes.  
**Pending item:** #4 (Mandir leftovers) — tracking only; not a technical gap  
**Repo of truth for PR:** GitHub `BhumitThakkar/MandirWebsite` (this tree). BT_PC `E:\Website\ShreeJalaramMandir` has **no `.git`**; use it as a **read-only live-code cross-check**, not as the PR source. Keep this file at `AI/implementation_plans/20260924-google-calendar-sync.md`.  
**Scope:** Google Calendar sync for Mandir **hall / catering / pujari only**. No voice/Cruiser content. QA UI lane follows after Lab jar (Carina briefs QA). Automated tests in §9 must pass before Lab.

---

## 0. Purpose

Keep Google Calendar correct when Mandir bookings change, without manual double-entry.

| # | Behavior |
|---|----------|
| 1 | Hall reservation **create** → create Google Calendar event |
| 2 | Hall reservation **cancel / reject** → soft-cancel that calendar event |
| 3 | Hall reservation **edit** → update that calendar event |
| 4 | **Catering** add/change (approve path) → update catering details on the **same** hall event |
| 5 | Hall-linked **pujari seva** details → on the **same** hall event |
| 6 | **Standalone** pujari seva (no hall reservation) → **own** Google Calendar event |

**Execution:** Cursor own model credits. **Do not enable Cursor on-demand.**

This document is the contract for that work — not a license to rewrite booking/invoice/ledger systems beyond the hooks listed here.

---

## 1. Hard constraints (do not violate)

1. **Reuse the hall / catering / pujari models and services.** No parallel booking system. No second calendar pipeline.
2. **REPLACE** any `GoogleCalendarService` placeholder (fake `CAL_<timestamp>_<id>` ids that are never stored). Public hall submit must not keep calling a mock **and** a new sync service. **One writer only.**
3. **Persist event IDs** on the records that own the event (`sjm.hall_reservations.google_calendar_event_id`, `sjm.pujari_seva.google_calendar_event_id`) so create/edit/cancel stay idempotent.
4. **Calendar writes on booking lifecycle only** — not on payment ledger / invoice assembler / money paths (`TransactionService` add/refund/void, `HallInvoiceAssembler`).
5. **No payment secrets, no money totals, no devotee PII** in event description (no confirmation numbers, card data, amounts, devotee email/phone/street address, org tax ids).
6. **Dedicated Mandir calendar** owned by **`sjm.itteam@gmail.com`**. Wrong forms `sjm.itt@` and `sjm.ittteam@` must not appear.
7. **Credentials env-only** — never `app_property`, never git. Calendar **id** may be env. Reuse existing `app.timezone` and `mandir.address.*`. Do **not** add a second timezone key.
8. **JPA + DDL:** Hibernate `ddl-auto=validate` (`JPA_DDL_AUTO=validate`). New columns belong in entity **and** in `scripts/setup-sjm-website-db.sql` **CREATE TABLE** bodies. ALTER is optional one-shot for already-existing DBs only.
9. **Best-effort:** Calendar is external. Catch all errors. Booking TX must succeed if Google is down. Default is **not** hard-fail.
10. **Frontend:** no new public UX required for v1. Do **not** show calendar-failed flash when the feature flag is off (not attempted).
11. **No extra reject/cancel tracking** beyond existing **status**. Soft-cancel is the same for `REJECTED` and `CANCELLED`.
12. **Soft cancel only** on Google (`event.status = cancelled`). Title unchanged. Event id kept. No hard-delete of the Google event as the primary strategy.
13. **Do not inject `CateringService` into the calendar / assembler path** (cycle T8). Use `CateringRepository` or pass the already-saved `Catering`.
14. **Do not enable Cursor on-demand.**

---

## 2. Live verification (this clone) + locked target contracts

### 2.0 What this GitHub tree actually contains (verified)

Read on 2026-09-25 against `BhumitThakkar/MandirWebsite` `master`:

| Looked for | Result |
|------------|--------|
| `GoogleCalendarService` | **Absent.** No calendar package, no Google Calendar dependency in root `pom.xml` (Hibernate 5.4 + MySQL + JUnit 3.8.1 only). |
| `HallReservationController` / `HallReservationService` | **Absent.** |
| `PujariSeva*` entity/service/controller | **Absent.** Old model is `org.shreejalarammandir.model.pooja.Pooja` (CMS), not standalone pujari booking. |
| `CateringService` / `CateringRepository` | **Absent.** |
| MapStruct hall/pujari mappers | **Absent.** |
| `scripts/setup-sjm-website-db.sql` | **Absent.** |
| Flash keys `Flash.hallReservation.success.*WithCalendar` | **Absent.** |
| Spring Boot / `JPA_DDL_AUTO=validate` | **Absent.** Root app is a Hibernate WAR (`hbm2ddl.auto=update`). |

**Do not invent a second set of method names** to match this skeleton's `Event` / `Pooja` CMS. Those are not hall booking.

**Locked home for this feature:** Maven module `sjm-website/` — Spring Boot, schema `sjm`, the call sites in §4. The 2020 WAR is left untouched (no calendar hooks there).

### 2.0b BT_PC live verify (2026-09-25, read-only) — source of truth for the real Mandir app

BT_PC tree `E:\Website\ShreeJalaramMandir` still has plan **Rev 2026-09-24h** (16,476 bytes) at the same path. That disk Rev is **stale**. This GitHub file is the contract.

| Check | BT_PC confirmed | Lock for implement |
|-------|-----------------|--------------------|
| `GoogleCalendarService` | **Placeholder exists.** Returns a mock `String` id **or null**. No real Google API. | **REPLACE** it. Do not add `MandirGoogleCalendarSyncService` beside it. Never persist `CAL_…` ids. |
| Public hall submit | `HallReservationController` calls `createCalendarEvent(saved)` **AFTER** `submitReservation` returns | **Delete that call.** Calendar moves **inside** `submitReservation` (beside email). Controller flash uses `CalendarSyncOutcome` only. |
| `PujariSeva` table | `@Table` = **`pujari_seva`** (singular) | Never `pujari_sevas`. |
| `google_calendar_event_id` | **Not** in `setup-sjm-website-db.sql` CREATE TABLE yet | Add it to **both** CREATE TABLE bodies. ALTER is optional one-shot only. |
| Catering | `saveCateringAndApproveReservation` exists and is used by **`CateringController`**. **Zero** callers of `cateringService.save(` | Hook **only** the approve method. Keep `save(` unhooked. |

**BT_PC merge checklist (when copying this PR onto the local Mandir tree):**

1. Remove `HallReservationController`’s after-submit `createCalendarEvent(saved)` block.
2. Delete or hollow `GoogleCalendarService.createCalendarEvent` so it is **not** a second writer.
3. Put `syncHallCreated` inside `submitReservation`; keep email there.
4. Add the two CREATE TABLE columns (BT_PC does not have them yet).
5. Do **not** hook `CateringService.save`.

### 2.1 Locked domain (from live Mandir review + BT_PC — do not rename)

**Hall**
- Entity: `HallReservation` — `reservationDate`, `startTime`, `endTime`, `eventTitle`, guest/contact fields, `publicId`, `status` (`ReservationStatus`: `PAYMENT_VALIDATION_PENDING` → `SJM_CATERING_PENDING` → `APPROVED` → `REJECTED` / `CANCELLED`), flags `sjmPujariSeva`, `sjmCateringSeva`, `serviceLines`, `paymentValidated`, basement `FULL` / `HALF`.
- Table: **`sjm.hall_reservations`**.
- Lifecycle methods (real names — do not invent alternatives):
  - `HallReservationService.submitReservation` — **public create only**. Email (`sendReservationSummary`) already lives **inside** this method. **Calendar lives here too** (beside email). Catch all calendar errors; never fail the booking TX.
  - `HallReservationService.adminUpdate` — admin **edit** only (status, times, title, flags, lines). **No admin create.**
  - `HallReservationService.delete` — physical delete, gated by ledger **R4** `assertDeletable`. Soft-cancel Google **then** delete the row.
  - `HallReservationService.save` — used by catering-approve. **Do not** hook calendar on `save` itself.

**Catering**
- Entity: `Catering` linked to hall reservation. **No** own Google event id.
- `CateringService.saveCateringAndApproveReservation` — **the only catering hook**. Sets hall `APPROVED` via `hallReservationService.save`, then refresh the **same** hall event (meals in description).
- `CateringService.save` — **no callers / do not hook.**
- Catering delete-by-hall → `syncHallContentRefresh` (strip catering text).
- Lookup for calendar/assembler: **`CateringRepository`** (`findByHallReservation` / `findByHallReservationId`). Never inject `CateringService` into calendar.

**Pujari**
- **Standalone entity:** `PujariSeva` — `@Table(name = "pujari_seva")` (singular). Own date/time/`PujariSevaStatus` (`CANCELLED`, `REJECTED`, active statuses). **No hall FK.**
- Table: **`sjm.pujari_seva`** — never `pujari_sevas`.
- `PujariSevaService.submit` — public POST `/pujari-seva` only. **No calendar call today; add it here.**
- `PujariSevaService.adminUpdate` — cancel vs patch by status.
- `PujariSevaService.deleteById` — R4 `assertDeletable`, soft-cancel Google, then delete.
- **Hall-linked pujari:** hall flag `sjmPujariSeva` + `HallReservationServiceLine` + `HallReservationPujariTitleFormatter` — details go on the **hall** event. Not a second event.

**Placeholder to replace (BT_PC confirmed)**
- Live method: `GoogleCalendarService.createCalendarEvent(HallReservation saved)` → mock `String` id or `null`. **Does not** persist the id. **Does not** call Google.
- Live caller: `HallReservationController` **after** `submitReservation`.
- **Replace both.** `sjm-website` must not contain `createCalendarEvent`. Controller must not inject `GoogleCalendarService`. Flash logic moves to the outcome of `submitReservation`.
- **CateringController** (live name) is the admin caller of `saveCateringAndApproveReservation`. Do not invent a second catering-approve entry point.

---

## 3. Target design

### 3.1 Calendar ownership — LOCKED account + auth default

- Google account that owns the Mandir calendar: **`sjm.itteam@gmail.com`**.
- One dedicated Mandir calendar under that account. Config: env `GOOGLE_CALENDAR_MANDIR_ID` → `mandir.google-calendar.calendar-id`.
- **Auth default (v1):** **service account** shared **writer** on that dedicated calendar. Store JSON via env (`GOOGLE_CALENDAR_CREDENTIALS_JSON` or `GOOGLE_CALENDAR_CREDENTIALS_PATH`). No OAuth consent screen for v1. OAuth refresh tokens are a later option only.
- **`sendUpdates=none`**. No guest attendees. Google must not email devotees.
- **Pin** official Google Java client versions in `sjm-website/pom.xml` if added. Set connect/read timeouts on the HTTP transport (same idea as `GenericRestServiceCaller`).
- Feature flag: `mandir.google-calendar.enabled` **default `false`**.

### 3.2 Event identity storage

| Record | Column | When set |
|--------|--------|----------|
| `sjm.hall_reservations` | `google_calendar_event_id` nullable `VARCHAR(1024)` | After successful create; **kept** after soft cancel |
| `sjm.pujari_seva` | `google_calendar_event_id` nullable `VARCHAR(1024)` | Standalone seva only; **kept** after soft cancel |

Do **not** store an event id on `catering`.

**Id persistence:** after Google returns an id, set the column on the entity and save again (short extra write). If Google fails, leave null; next update **heals** (create + store id). Do not generate or store fake `CAL_` ids.

Optional harden (not required for v1): deterministic Google event id `hall-{publicId}` / `pujari-{publicId}`.

### 3.3 One event vs two

- **Hall booking** = one Calendar event. Catering + hall-linked pujari = description on that event.
- **Standalone `PujariSeva`** = separate event, own stored id.
- `PujariSeva` has **no hall FK**. Do not create a second event for hall-linked pujari lines.

### 3.4 Cancel / reject / re-open / delete — LOCKED

1. Soft cancel: Google `event.status = "cancelled"`. **Do not** change the title (no `[CANCELLED]` prefix).
2. Do **not** hard-delete the Google event as the primary strategy.
3. `REJECTED` = `CANCELLED` on Calendar (same soft cancel).
4. Keep `google_calendar_event_id` after cancel.
5. **`SJM_CATERING_PENDING` keeps the Google event ACTIVE.** Only `REJECTED` and `CANCELLED` soft-cancel. After payment approval, a catering hall is `SJM_CATERING_PENDING`, not cancelled.
6. **Un-cancel / re-open:** admin **can** move status off `CANCELLED`/`REJECTED` back to an active status. If an event id exists → **PATCH `status=confirmed`** (plus title/times/description). **Same event id. No second insert.** Heal-on-404 is for missing events, not a substitute for re-open.
7. **Physical delete (exception path):** load event id → soft-cancel Google → then delete the DB row if R4 `assertDeletable` allows. Same for hall `delete` and pujari `deleteById`. Cancel/reject remains the normal path.

### 3.5 Event payload (no secrets, no PII, no money)

**Hall event**
- Title: event title (+ basement short label if useful).
- Start/end: `reservationDate` + `startTime`/`endTime` in **`app.timezone`** (`America/Chicago`). Do not add `mandir.google-calendar.timezone`.
- Location: `mandir.address.line1` / `city` / `state` / `zip`.
- Description (plain text, locked allow-list): `publicId`, status, date/time, basement (`FULL`/`HALF`), guest count, nonprofit yes/no (**no** org tax ids), catering meal summary, hall pujari line titles via `HallReservationPujariTitleFormatter`, optional admin URL using `public_id`.
- **Exclude:** devotee email, phone, street address, payment confirmation numbers, ledger amounts, **any** money totals. The old placeholder TODO ("include contact info") is **forbidden**.

**Standalone pujari event**
- Title from selected seva types / formatter.
- Start/end from `sevaDate` + times; location from venue fields / place enum (or Mandir address if on-site).
- Description: publicId, selected sevas, status. Same exclusions.

### 3.6 One sync service (the only writer)

Name: **`GoogleCalendarService`** (replace placeholder). Optional package-private helpers (`GoogleCalendarClient`, description builders). **Do not** add `MandirGoogleCalendarSyncService` as a second public writer.

| Method | Behavior |
|--------|----------|
| `syncHallCreated(HallReservation)` | Create if no id; persist id; `SJM_CATERING_PENDING` stays active |
| `syncHallUpdated(HallReservation)` | If `REJECTED`/`CANCELLED` → soft cancel; else patch (re-open → `status=confirmed`); if missing id, create (heal) |
| `syncHallCancelled(HallReservation)` | Soft cancel (`status=cancelled`) |
| `syncHallContentRefresh(HallReservation)` | Rebuild description from hall + **CateringRepository** (or passed `Catering`) + pujari lines |
| `syncStandalonePujariCreated/Updated/Cancelled(PujariSeva)` | Parallel; Cancelled **and** Rejected → soft cancel; re-open → confirmed |

Idempotent: if id present, update; if Google 404, recreate and rewrite id. **Re-open with existing id = patch confirmed, not insert.**

Flag off: **no Google call**; methods return `CalendarSyncOutcome.NOT_ATTEMPTED` (null id). Never throw into the booking TX.

### 3.7 Feature flag vs public flash — LOCKED

Today a mock that always returns an id makes every devotee see "calendar date has been booked". If flag-off returns null and the controller treats null as failure, every devotee sees **"calendar booking failed"**.

| Flag | Google call | Public flash |
|------|-------------|--------------|
| Off | none | existing success **without** calendar-failed copy (treat as **not attempted**) |
| On + Google OK | insert/patch | `*WithCalendar` |
| On + Google down | none / error logged | `*CalendarFailed` is OK |

`HallReservationService.submitReservation` returns the saved reservation **plus** `CalendarSyncOutcome` (`NOT_ATTEMPTED` / `SUCCESS` / `FAILED`). Controller flash uses that. Controller does **not** call Google.

---

## 4. Hook list (locked — verify names; do not invent extras)

Call **one** sync service. Catch errors. Never throw into the booking TX.

| After this succeeds | Call |
|---------------------|------|
| `HallReservationService.submitReservation` | `syncHallCreated` (create or heal); persist id. Beside `sendReservationSummary`. |
| `HallReservationService.adminUpdate` | if `REJECTED`/`CANCELLED` → soft cancel; else patch (re-open → confirmed) |
| `CateringService.saveCateringAndApproveReservation` | `syncHallContentRefresh` on the **same** hall event (status may become `APPROVED`; description includes meals) |
| catering delete-by-hall | `syncHallContentRefresh` (strip catering text) |
| `HallReservationService.delete` | soft-cancel Google, **then** delete row (R4 `assertDeletable` first) |
| `PujariSevaService.submit` | `syncStandalonePujariCreated` |
| `PujariSevaService.adminUpdate` | cancel vs patch by status (re-open → confirmed) |
| `PujariSevaService.deleteById` | soft-cancel Google, **then** delete row (R4) |

**Do not hook:** `TransactionService` add/refund/void, `HallInvoiceAssembler`, email builders, `HallReservationService.save` by itself, `CateringService.save`.

**No admin create** for hall or pujari. Public POST creates (`/hall`, `/pujari-seva`). Admin only edits and deletes.

---

## 5. Mapper / mass assignment

`google_calendar_event_id` is server-owned. Add `@Mapping(target = "googleCalendarEventId", ignore = true)` on:

- `HallReservationMapper.toEntity` / `updateEntity`
- `PujariSevaMapper.toEntity` / `updateEntity`

Same pattern as `status`, `adminNotes`, pricing fields.

---

## 6. Schema (CREATE TABLE is source of truth)

Hibernate boots with **`JPA_DDL_AUTO=validate`**. A column that exists only as ALTER fails a fresh wipe/setup.

**Do both:**

1. Add `google_calendar_event_id VARCHAR(1024) NULL` into the **CREATE TABLE** bodies in `sjm-website/scripts/setup-sjm-website-db.sql` for `sjm.hall_reservations` and `sjm.pujari_seva`.
2. Keep a one-shot ALTER only for an already-existing DB that cannot re-run full setup:

```sql
ALTER TABLE sjm.hall_reservations
  ADD COLUMN IF NOT EXISTS google_calendar_event_id VARCHAR(1024) NULL;

ALTER TABLE sjm.pujari_seva
  ADD COLUMN IF NOT EXISTS google_calendar_event_id VARCHAR(1024) NULL;
```

Match other nullable varchar style (`NULL`, no extra CHECK). Entity fields + getters/setters.

---

## 7. Config (reuse, don't duplicate)

Already-used keys (onstart / `AppPropertyService` / `application.properties`):

- `app.timezone` = `America/Chicago`
- `mandir.address.line1` / `city` / `state` / `zip`

| Key | Purpose |
|-----|---------|
| `mandir.google-calendar.enabled` | Master switch. **Default false.** |
| `mandir.google-calendar.calendar-id` | Dedicated Mandir calendar ID under **`sjm.itteam@gmail.com`** (`${GOOGLE_CALENDAR_MANDIR_ID:}`) |
| `mandir.google-calendar.account-email` | Documented owner — **`sjm.itteam@gmail.com`** (reference / validation hint; not a second calendar) |
| Credentials | **Env only:** `GOOGLE_CALENDAR_CREDENTIALS_JSON` or `GOOGLE_CALENDAR_CREDENTIALS_PATH`. Never `app_property`. Never git. |

No `.env.example` in this repo historically — do not add secrets files. Document env names in this plan only.

Application binds HTTP to `0.0.0.0:$PORT` (Render). Filesystem is ephemeral — event ids live in the database, not disk.

---

## 8. Phased delivery (same PR)

### Phase 0 — Config + client
- Pinned Google Calendar Java client (or thin REST with timeouts).
- `GoogleCalendarClient` wrapper + feature flag default false.
- Unit test client with mocks. No live Google required.

### Phase 1 — Hall create / update / cancel + flash
- DDL + entity field on `HallReservation`.
- Hooks on `submitReservation` + `adminUpdate`.
- Controller flash uses `CalendarSyncOutcome` (flag-off ≠ failed).

### Phase 2 — Catering + hall-linked pujari
- Description builder uses `CateringRepository` + pujari title formatter.
- Hook **only** `saveCateringAndApproveReservation` + catering delete-by-hall.

### Phase 3 — Standalone pujari
- DDL + field on `PujariSeva`.
- Hooks on `submit` / `adminUpdate` / `deleteById`.

### Phase 4 — Hardening in the same PR
- Heal path (missing id / 404).
- Re-open → PATCH confirmed.
- Delete → soft-cancel then row delete.
- README/plan ops: create Mandir calendar, share writer with service account, set env.

---

## 9. Test plan (Done bars)

| Case | Proof |
|------|--------|
| Hall create | DB has `google_calendar_event_id`; mock client insert with correct start/end/title |
| Hall edit time/title | Same id; patch called |
| Hall cancel / reject | Google `event.status=cancelled`; title unchanged; DB row kept; event id kept |
| Catering approve | **Same** event id; description contains meal summary; hall status `APPROVED` on the event |
| Hall pujari lines/flag | Same hall event id; description contains seva titles |
| Standalone pujari create | `sjm.pujari_seva.google_calendar_event_id` set; separate from hall |
| Standalone cancel / reject | Google cancelled; title unchanged; row + id kept |
| Feature flag off | Client **never** called; public hall flash is **not** `*CalendarFailed` |
| Calendar API error | Booking save still succeeds; flash `*CalendarFailed` only when flag **on** |
| Replace placeholder | Only one calendar writer; mock `CAL_` ids never stored |
| `SJM_CATERING_PENDING` | Event created and **not** cancelled |
| Re-open after cancel | Same id; Google status `confirmed`; **no second insert** |
| Hall/pujari physical delete | Google cancelled when R4 allows delete |
| Mapper ignore | Client DTO cannot set `googleCalendarEventId` |
| No money / PII strings | Description builder asserts (grep/assert) |
| Bean cycle | App boots; calendar path does **not** inject `CateringService` |
| Replace `createCalendarEvent` | `HallReservationController` source has **no** `createCalendarEvent`; `GoogleCalendarService` has **no** placeholder method; only one writer bean |
| CateringController | Admin approve path calls `saveCateringAndApproveReservation`; **zero** production callers of `cateringService.save(` |
| `pujari_seva` + DDL | Entity `@Table(name = "pujari_seva")`; both CREATE TABLE bodies include `google_calendar_event_id` |
| Flag-off context | App boots with NoOp client and **no** Google mock |

Owner lock: these automated tests **must pass**. QA UI lane is after Lab jar (Carina briefs QA). Mocks for Google. Flag default false. No on-demand. No secrets in git.

---

## 10. Out of scope (this plan)

- Anand consume API, voice, Cruiser.
- Writing Mandir events into a personal primary calendar.
- Guest email invites / attendee RSVP (`sendUpdates=none`).
- Historical backfill.
- Invoice / deposit / rate-snapshot / ledger money workstreams (R4 assert only).
- Optional admin "Calendar synced" chip (if added later: pair `html` + same-stem `js`/`css`).
- Enabling Cursor on-demand billing.
- Volunteer share list under `sjm.itteam@gmail.com` (ops step; service account must have writer).

---

## 11. Open / locked decisions

**Locked**
- Soft cancel; title unchanged; DB rows not raw-deleted on cancel; event id kept.
- `REJECTED` = `CANCELLED` on Google. Status field is enough.
- `SJM_CATERING_PENDING` = active on Google.
- Re-open = PATCH confirmed, same id.
- Owning account: **`sjm.itteam@gmail.com`**.
- Auth: service account writer on dedicated Mandir calendar; `sendUpdates=none`; pin client versions.
- No money / PII in description.
- Feature flag default false; flag-off flash is not calendar-failed.
- One writer: replace placeholder `GoogleCalendarService`.
- Table `sjm.pujari_seva` (singular).
- DDL in CREATE TABLE; ALTER optional.
- Calendar beside email inside `submitReservation`.
- Catering hook: `saveCateringAndApproveReservation` only.
- `CateringRepository` in calendar/assembler path.
- Mapper ignore `googleCalendarEventId`.
- Config reuse: `app.timezone`, `mandir.address.*`. Credentials env-only.

**Still open (do not block)**
- Who else can see the Mandir calendar (volunteer share) — ops.
- Tracker line "Pending item: #4 (Mandir leftovers)" — not a technical gap.

---

## 12. Accept / delivery gate

| Step | Owner |
|------|--------|
| Accept this plan (Rev 2026-09-24j) | Explicit — Bhumit away; proceed |
| Implement (own credits, no on-demand) | Cursor |
| Automated tests in §9 | Cursor — **must pass** |
| Lab jar | Lab |
| QA UI lane | After Lab; Carina briefs QA |
| monday.com Temple item → Done | Tracking |

**Implement this Rev.**
