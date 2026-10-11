# TASK.md — Rebrand record: Flow → PlusTube (Gothwad Tech)

Status: **completed** on branch `arena/a694f8d9-tubeapp`.

## What was done

The upstream app **Flow** (by A-EDev, GPL-3.0) was rebranded for Gothwad Tech:

1. **Main app name:** `PlusTube` (launcher, UI, notifications, widgets).
2. **Formal name:** `Gothwad Tube` — a product of **Gothwad Tech**, shown in the About
   screen together with the product family (Gothwad Mail, Gothwad Drive, Gothwad Tube).
3. **Package:** `com.gothwad.tube` (was `io.github.aedev.flow`); source tree moved to
   `app/src/*/java/com/gothwad/tube/`, Gradle namespace/applicationId/test runner updated.
4. **Links:** repo links now point to `GothwadTech/tubeapp`, website
   `tubeapp.gothwadtech.com`, org site `www.gothwadtech.com`, social handle `@GothwadTech`
   (X/Twitter). Patreon/crypto donation entries of the original author were removed;
   support routes through the Gothwad Tech website.
5. **Feature branding:** user-facing feature names renamed (e.g. PlusTube Neuro
   recommendation engine, Deep PlusTube mode). Internal code identifiers (class names,
   log tags, R.style keys) were intentionally left unchanged to keep the diff minimal.
6. **Legal attribution (GPL-3.0):**
   - `License` (GPL-3.0 text) kept verbatim.
   - Source-file headers keep the original Flow/A-EDev copyright and add a Gothwad Tech
     modification notice.
   - `ATTRIBUTION.md` credits A-EDev and all Flow contributors; the in-app About screen
     has a "Special thanks" section with the full contributor list.
   - README carries the upstream attribution section.

## Not touched (deliberate)

- GPL-3.0 license text (`License`) — must stay verbatim.
- Internal Kotlin class names (`FlowNeuroEngine`, `FlowEmptyState`, ...), log tags and
  resource style keys — not user-visible.
- Launcher icon artwork.
