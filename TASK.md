# TASK.md — Flow → GoTube rebrand & repackage

> **Yeh file ek self-contained brief hai.** Ise naye chat me do aur bolo: *"TASK.md execute karo."*
> Iske andar saari zaroori discovery already ho chuki hai — counts, exact line numbers, identifier
> lists, aur jo cheezein **touch nahi karni** hain. Dobara discover karne ki zaroorat nahi.
>
> Repo: `GothwadTech/tubeapp` @ `/home/user/tubeapp`, branch `arena/7bab4b2e-tubeapp`
> Upstream source: `Flow-main.zip` (A-EDev/Flow), already extracted into repo root, zip deleted.

---

## PART 0 — Introduction: kya karna hai (user ki requirements)

Is repo me **Flow** naam ka ek Android app (privacy-first YouTube + YouTube Music client) hai.
Ise **Gothwad Tech** ke apne product me convert karna hai. User ne jo bataya:

1. **Package name** `io.github.aedev.flow` se badal kar **`com.gothwad.tube`** karna hai.
2. **Folder structure** bhi usi package ke hisaab se — matlab source tree
   `…/java/io/github/aedev/flow/` se `…/java/com/gothwad/tube/` par shift hoga.
3. **App name** poori tarah change karna hai:
   - **Short / main name = `GoTube`** (yahi launcher par dikhega, yahi primary naam hai)
   - **Formal name = `Gothwad Tube`** — jahan formal mention sahi lage (legal/about/docs) wahan ye use ho
   - **"powered by YouTube"** bhi mention karna hai
4. **Branding replace** — jahan jahan `Flow` (brand ke taur par) likha hai, wahan **`GoTube`** karo.
5. Jahan formal naam rakhna sahi ho, wahan UI me **`Gothwad Tube`** rakhna.
6. App **develop & manage "Gothwad Tech"** karega — ye attribution add karna.
7. Ye **Flow ka fork hai** — isliye **README ke Special Thanks / Acknowledgments me Flow (A-EDev)
   ka credit zaroor** rakhna.
8. **App ke About section me bhi** wahi saare credits add karo jo README me hain. Agar About UI me
   koi credit missing hai toh wahan bhi daalo.

### Locked decisions (user se confirm ho chuke — dobara mat poochna)

| # | Sawal | Decision |
|---|---|---|
| 1 | Code ke andar 91 `Flow*` Kotlin files (FlowBottomSheet, FlowApp, FlowLogo…) | **Sab rename karo** → `GoTube*` (classes + files + saare references) |
| 2 | Feature naam jisme "Flow" hai (Deep Flow Mode, Flow Engine, Flow Brain/Personality, `FlowNeuroEngine`) | **Sab rename karo** → Deep GoTube Mode, GoTube Engine, GoTube Brain, `GoTubeNeuroEngine` |
| 3 | Links (website `flow.aedev.me`, GitHub `A-EDev/flow`, Reddit `r/Flow_Official`, Weblate) | **GitHub → `github.com/GothwadTech/tubeapp`**; website aur Reddit rows **hata do** (unke abhi koi URL nahi hain) |
| 4 | `app_name` 26 locales me "Flow" hai | **Sab 26 locales me GoTube** karo (brand name translate nahi hota) |

### Naming spec (authoritative — har phase me yahi follow karna)

| Context | Value |
|---|---|
| Application ID (release) | `com.gothwad.tube` |
| Kotlin namespace / package root | `com.gothwad.tube` |
| Debug application ID | `com.gothwad.tube.debug` |
| Nightly application ID | `com.gothwad.tube.nightly` |
| Launcher / short name | `GoTube` |
| Uppercase variant (`app_name_uppercase`) | `GOTUBE` |
| Formal / legal name | `Gothwad Tube` |
| Tagline / attribution | `Powered by YouTube` |
| Developer / maintainer | `Gothwad Tech` |
| GitHub URL | `https://github.com/GothwadTech/tubeapp` |
| Upstream project (fork origin) | `Flow` by `A-EDev` — `https://github.com/A-EDev/Flow` |
| License | GNU GPL v3 (inherit ho rahi hai — change nahi karni) |

---

## PART 1 — Verified repo facts (baseline; inhe dobara verify karne ki zaroorat nahi)

### Scale of the rename

| Fact | Value |
|---|---|
| Files containing the string `io.github.aedev.flow` | **2434** (ab **2435** — is `TASK.md` me bhi ye string likhi hai) |
| Total occurrences of `io.github.aedev.flow` | **11835** |
| By extension | 2416 `.kt`, 5 `.md`, 4 `.xml`, 2 `.kts`, 1 `.yml`, 1 `.txt`, 1 `.pro`, 1 `.old`, 1 `.java`, 1 `.bak`, 1 `.backup` |
| Distinct Kotlin/Java identifiers containing `Flow` | **301** |
| → to RENAME (brand) | **233** (Appendix A) |
| → to KEEP (coroutines / Material / English) | **68** (Appendix B) |
| Code files whose **basename** contains `Flow` | **91** (Appendix C) — 89 rename honge, 2 nahi |
| `kotlinx.coroutines.flow` import lines | **1005** — untouched |
| `StateFlow`/`SharedFlow`/`flowOf`/`flowOn`/`flow {}` style usages | **1131** — untouched |

### Source roots that contain `io/github/aedev/flow/` (9 — sab move karne hain)

```
./app/src/main/java/
./app/src/github/java/
./app/src/foss/java/
./app/src/androidTest/java/
./app/src/androidTestGithub/java/
./app/src/test/java/
./app/src/testGithub/java/
./app/src/testFoss/java/
./benchmark/src/main/java/
```

Note: `app/src/githubRelease/` aur `app/src/nightly/` me sirf resources/baseline-profiles hain,
koi java source nahi.

### Slash-form `io/github/aedev/flow` bhi badalna hai (5 files)

| File | Note |
|---|---|
| `app/src/githubRelease/generated/baselineProfiles/baseline-prof.txt` | **7338** occurrences |
| `app/src/githubRelease/generated/baselineProfiles/startup-prof.txt` | **6616** occurrences |
| `.github/labeler.yml` | path globs |
| `privacy.md` | doc |
| `app/src/test/java/io/github/aedev/flow/data/recommendation/music/MusicBrainLeakGuardTest.kt` | test assertion |

### `app/build.gradle.kts` — exact anchors

| Line | Content |
|---|---|
| 35 | `namespace = "io.github.aedev.flow"` |
| 36 | `compileSdk = 37` |
| 39 | `applicationId = "io.github.aedev.flow"` |
| 40–43 | `minSdk = 26`, `targetSdk = 36`, `versionCode = 18`, `versionName = "2.2.1"` |
| 47–48 | `LASTFM_API_KEY` / `LASTFM_API_SECRET` buildConfigFields |
| 78–89 | `productFlavors`: `github` (UPDATER_ENABLED=true, `DISCORD_APPLICATION_ID = "1526515771021328514"`), `foss` (UPDATER_ENABLED=false) |
| 96–119 | `signingConfigs` — `release` (`release.keystore`) + `nightly` (`nightly.keystore`) |
| 121–165 | `buildTypes` — `debug` (`applicationIdSuffix = ".debug"` @123), `release`, `nightly` (`.nightly` @158) |

### Room schemas — **koi package FQN nahi hai** ✅

`grep -rl "io.github.aedev.flow" app/schemas/` → **0 matches**. Schema JSONs (`24.json` … `33.json`)
ko touch karne ki zaroorat nahi. Ye ek bada risk automatic eliminate ho gaya.

### UI strings

| Item | Value |
|---|---|
| `app/src/main/res/values/strings.xml` — total lines containing `Flow` | **117** |
| `app_name` (line 3) | `Flow` |
| `app_launcher_name` (line 4) | `Flow` |
| `app_name_uppercase` (line 404) | `FLOW` |
| `settings_item_about_flow` (line 742) | `About Flow` |
| `settings_item_about_flow_subtitle` (743) | `Version, License, Contributors` |
| `about_creator_name` (1744) | `A-EDev` |
| `about_website_address` (1746) | `flow.aedev.me` |
| `about_reddit_subtitle` (1748) | `r/Flow_Official` |
| Locales jisme `app_name` = `Flow` | **26** |
| Locales jisme `app_name` **empty/absent** | 4 — `values-b+yue+Hant`, `values-fa`, `values-kab`, `values-lo` |

### About screen (jahan Gothwad Tech + Special Thanks jaana hai)

| File | Detail |
|---|---|
| `app/src/main/java/io/github/aedev/flow/ui/screens/settings/about/AboutScreen.kt` | 180 lines |
| `app/src/main/java/io/github/aedev/flow/ui/screens/settings/index/AboutIndex.kt` | entry registry |
| `app/src/main/java/io/github/aedev/flow/ui/screens/settings/about/ChangelogSheet.kt` | changelog |
| `app/src/main/java/io/github/aedev/flow/ui/tv/screens/settings/TvAboutSettingsPane.kt` | **TV variant — alag se handle karna, isme apne URL constants hain (e.g. `FLOW_REDDIT_URL` line 47)** |
| `app/src/test/java/io/github/aedev/flow/ui/screens/settings/DiagnosticsAndAboutTest.kt` | test — update karna padega |

**`AboutScreen.kt` ke current URL constants (lines 48–55):**
```kotlin
private const val WEBSITE_URL = "https://flow.aedev.me"
private const val GITHUB_URL  = "https://github.com/A-EDev/flow"
private const val REDDIT_URL  = "https://www.reddit.com/r/Flow_Official/"
private const val CREATOR_URL = "https://github.com/A-EDev"
private const val NEWPIPE_URL = "https://github.com/TeamNewPipe/NewPipeExtractor"
private const val LICENSE_URL = "https://github.com/A-EDev/Flow/blob/main/License"
private const val CREATOR_AVATAR_URL = "https://github.com/A-EDev.png?size=144"
```

**Current About layout structure:**
```
item("about.header")     → AboutHeader()  [logo + app_name + version]
group("about.creator")   → CreatorRow()
group("about.app")       header=R.string.section_app     → changelog
group("about.contact")   header=R.string.section_contact → website, github, reddit
group("about.legal")     header=R.string.section_legal   → license, newPipe
group("about.device")    header=R.string.section_device  → deviceInfo
```
`AboutIndex.all = listOf(changelog, website, github, reddit, creator, license, newPipe, deviceInfo)`

### README acknowledgments (yahi list About UI me bhi jaani chahiye)

`README.md` lines 203–215, section `## 🙏 Acknowledgments`:

1. **NewPipeExtractor** — https://github.com/TeamNewPipe/NewPipeExtractor
2. **NewPipe** — https://github.com/TeamNewPipe/NewPipe
3. **PipePipe** — https://codeberg.org/NullPointerException/PipePipe
4. **PipePipe Developer Docs** — https://priveetee.github.io/Docs-PipePipe/
5. **MetroList** — https://github.com/MetrolistGroup/Metrolist
6. **LibreTube** — https://github.com/LibreTube/LibreTube
7. **ExoPlayer** — https://github.com/google/ExoPlayer
8. **Jetpack Compose** — https://developer.android.com/jetpack/compose
9. **Material Design 3** — https://m3.material.io/

Abhi About UI me inme se sirf **1** hai (`newPipe`). Baaki 8 add karne hain + **Flow (A-EDev)** ka
fork-origin credit = total **10** entries in Special Thanks.

`README.md` me `Copyright © 2025-2026 A-EDev` bhi hai (License & Copyright section) — GoTube ke liye
Gothwad Tech copyright add karna, upstream ka credit preserve karte hue (GPLv3 requirement).

### Other branding touch-points

| Location | Detail |
|---|---|
| `DeArrowRepository.kt:57` | `.header("User-Agent", "FlowYouTube/1.0")` |
| `SponsorBlockRepository.kt:100` | `.addQueryParameter("userAgent", "FlowYouTube/1.0")` |
| `utils/cipher/CipherWebView.kt:342` | `TAG = "Flow_CipherWebView"` (logcat tag) |
| `utils/cipher/FunctionNameExtractor.kt:13` | `TAG = "Flow_CipherFnExtract"` |
| `utils/cipher/PlayerJsFetcher.kt:23` | `TAG = "Flow_CipherFetcher"` |
| `utils/cipher/CipherDeobfuscator.kt:20` | `TAG = "Flow_CipherDeobfusc"` |
| `app/src/main/AndroidManifest.xml` | activity-aliases `.IconFlowRed`, `.IconFlowLight`, `.IconFlowPlay`; `.FlowApplication` @66; `android:label="@string/app_launcher_name"` @72 |
| Splash themes | **39** styles named `Theme_Flow_Starting_*` in `res/values*/themes*.xml` |
| Drawables | `ic_flow_logo.xml`, `ic_flow_badge_glyph.xml`, `ic_flow_badge_shape.xml`, `ic_fg_flow_play.xml` |
| Mipmaps | `ic_launcher_flow_light(.round).xml`, `ic_launcher_flow_play(.round).xml` |
| Repo meta files w/ `A-EDev` | `.github/CODEOWNERS`, `.github/FUNDING.yml`, `.github/ISSUE_TEMPLATE/config.yml`, `CONTRIBUTING.md`, `README.md`, `SECURITY.md` |
| `fastlane/` | 18 files — `title.txt`, `short_description.txt`, `full_description.txt`, 7 changelogs, icon + 6 screenshots |

### CI/CD (`/.github/workflows/build.yml`)

> **⚠️ Ye workflow restructure ho chuka hai.** Neeche current state hai, upstream ka nahi.

| Item | Detail |
|---|---|
| Jobs | `build`, `release` (tag only). Upstream ka `nightly` job **hata diya gaya**. |
| Artifact | **Sirf 1** — universal release APK, naam **`GoTube-v1.0.<run>-Release.apk`** |
| Versioning | `versionCode = GITHUB_RUN_NUMBER`, `versionName = "1.0.<run>"`. Dono `app/build.gradle.kts` ke `androidComponents { onVariants(selector().withBuildType("release")) }` block se aate hain, isliye file name aur APK ke andar ki version kabhi diverge nahi kar sakti. Local builds me `GITHUB_RUN_NUMBER` unset hota hai → `defaultConfig` ki values rehti hain. |
| ABI splits | **Disabled** in `app/build.gradle.kts`. Har variant = 1 universal APK. Pehle 4 ABI + universal = 5 per variant, 15 APKs per run, jisme se x86/x86_64 kabhi publish hi nahi hote the. |
| Hata diya gaya | AAB (`bundleFossRelease`), debug APK, nightly keystore/build/publish, per-ABI uploads, lint steps (`lint { abortOnError = false }` tha, kabhi fail nahi karta tha, sirf ~15 min leta tha) |
| `--max-workers` | `1` → **`2`** (GitHub runner par 4 cores hain) |
| **`EXPECTED_SIGNER_SHA256`** | **`731c73b1908f9fd4685f3330952d9afe71577493a0ea1de6b8a4f45e8c2ff303`** — ye is repo ke apne keystore ka digest hai, CI ne 2026-10-09 ko signed APK se measure kiya. Upstream Flow ka `4322294e…` ab use nahi hota. ⚠️ Ye key rotate mat karna — Android dusri key se signed update install nahi karta. |
| Required repo secrets | `RELEASE_KEYSTORE_BASE64`, `STORE_PASSWORD`, `KEY_ALIAS`, `KEY_PASSWORD`, `LASTFM_API_KEY`, `LASTFM_API_SECRET` (+ `GITHUB_TOKEN` auto). Nightly ke 4 secrets ab zaroori nahi. |
| Release job | Tag (`v*`) par `GoTube-v1.0.<run>-Release.apk` + `checksums.txt` publish karta hai |

**Measured CI timings** (GitHub-hosted `ubuntu-latest`, run 37918549634):

| Step | Time |
|---|---|
| Check Kotlin formatting (`spotlessCheck`) | 1m17s |
| Run unit tests (2 flavors) | 14m35s |
| Compile instrumentation tests | 5s (build cache) |
| **Build universal release APK (1 APK)** | **14m56s** |
| **Total** | **~31 min** |

Baseline comparison — upstream config, run 37904688418:

| Step | Time |
|---|---|
| Lint GitHub nightly | 13m01s *(ab hata diya, `abortOnError = false` tha)* |
| **Build CI APKs (15 APKs)** | **59m44s aur tab bhi khatam nahi hua** *(cancel hua)* |

**`--max-workers` par ek measured finding:** `2` par unit tests **fail** hote hain
(`Run unit tests 09:52:58 -> 10:00:40, 7m42s, failure`, annotation `Process completed with exit
code 1`), `1` par pass (`13m40s, success`). 16 GB runner par do Kotlin/Gradle workers +
Robolectric ki memory pressure lagti hai. **`--max-workers=1` hi rakhna.**

### Misc gotchas

- `legacy/` folder (16 files) **gitignored** hai (`.gitignore:11 → /legacy`). Build me participate nahi
  karta, `settings.gradle.kts` sirf `:app` aur `:benchmark` include karta hai. Rename me include karna
  optional — recommend **skip** karo, ya `.gitignore` se hatao phir karo.
- `settings.gradle.kts` me `rootProject.name = "Flow"` → `"GoTube"` karna.
- `.gitignore` me `release.keystore`, `*.keystore`, `*.jks`, `local.properties` already ignored hain.

---

## PART 2 — ⛔ HARD RULES: jo bilkul touch nahi karna

> **Sabse bada risk ye hai.** Blanket `sed 's/Flow/GoTube/g'` project ko tod dega.

1. **Kotlin coroutines `Flow` kabhi rename nahi karna.** `StateFlow`, `SharedFlow`,
   `MutableStateFlow`, `MutableSharedFlow`, `asStateFlow`, `asSharedFlow`, `snapshotFlow`,
   `callbackFlow`, `channelFlow`, `emptyFlow`, `flowOf`, `flowOn`, `FlowPreview`,
   `kotlinx.coroutines.flow.*` imports — **sab as-is**. Poore list ke liye Appendix B dekho.
2. **`createFlow`** = Room ka `InvalidationTracker.createFlow` (used at `data/local/ViewHistory.kt:265`).
   **Touch nahi karna.**
3. **`Flower`** = Material Shapes enum value (`MaterialShapes.Flower`, used in `OnboardingHero.kt`).
   **Touch nahi karna.**
4. **`PlayerStateFlows.kt` / `PlayerStateFlowsTest.kt`** — naam me "Flows" (plural) hai, ye Kotlin
   Flows ke baare me hai, brand nahi. **File rename nahi karna.**
5. **`inFlow`** (`TvSyncScreen.kt:68`) — English word "in the wizard flow", local boolean. **Skip.**
6. **Rule of thumb:** identifier me `Flow` agar **bilkul end par** hai → wo coroutines hai → **skip**.
   Exception sirf `DeepFlow` / `deepFlow` (brand feature, explicitly include).
7. **Blanket case-insensitive replace mat karna.** `flow` lowercase mostly coroutines hai.
8. **License file (`License`, GPLv3) aur upstream copyright notice mitana nahi** — GPLv3 me derivative
   work ke liye original attribution rakhna zaroori hai.

---

## PART 3 — The 10 Phases

Har phase ke end me uska **verify command** diya hai. Phase khatam hone par verify zaroor chalana.

---

### Phase 1 — Baseline, safety net aur inventory

**Goal:** rollback point banana aur machine-readable rename map fix karna.

- [ ] Current state commit karo (`git add -A && git commit`) — taaki har phase ka diff clean dikhe
- [ ] Ek backup tag lagao: `git tag pre-rebrand-baseline`
- [ ] `/tmp/final_map.json` jaisa rename-map regenerate karo (script neeche) aur use **freeze** karo —
      saare phases usi map se chalein, taaki inconsistency na aaye
- [ ] Baseline counts record karo (Part 1 ki table)

**Map generator script:**
```python
import re, json, subprocess
out = subprocess.run(
  ["bash","-c","grep -rhoE '\\b[A-Za-z_][A-Za-z0-9_]*\\b' --include='*.kt' --include='*.java' app/src benchmark/src | grep Flow | sort | uniq -c | sort -rn"],
  capture_output=True, text=True).stdout
ids = {}
for line in out.splitlines():
    if line.strip():
        n, name = line.split(None,1); ids[name]=int(n)

EXCLUDE = {"Flow","Flower","FlowPreview","FlowCollector","FlowOperator",
           "PlayerStateFlows","PlayerStateFlowsTest","inFlow","createFlow"}
FORCE_INCLUDE = {"DeepFlow","deepFlow"}   # brand despite 'Flow' at end

brand, keep = [], []
for name in ids:
    if name in EXCLUDE: keep.append(name); continue
    m = re.search(r'Flow', name)
    if not m: continue
    if m.start()+4 == len(name) and name not in FORCE_INCLUDE:
        keep.append(name); continue
    brand.append(name)
mapping = {b: b.replace("Flow","GoTube") for b in sorted(brand)}
json.dump({"rename":mapping,"keep":sorted(keep)}, open("rename_map.json","w"), indent=1)
```

**Verify:**
```bash
python3 -c "import json;d=json.load(open('rename_map.json'));print(len(d['rename']),len(d['keep']))"
# expected: 233 68
```

---

### Phase 2 — Package rename: `io.github.aedev.flow` → `com.gothwad.tube`

**Goal:** directory tree + har dotted reference.

- [ ] 9 source roots me `io/github/aedev/flow/` → `com/gothwad/tube/` move karo (git mv), aur
      khali bache `io/github/aedev` dirs hatao
- [ ] Har text file me dotted string `io.github.aedev.flow` → `com.gothwad.tube`
- [ ] Slash form `io/github/aedev/flow` → `com/gothwad/tube` (5 files — baseline profiles me ~13,954 hits)
- [ ] `app/build.gradle.kts:35` namespace aur `:39` applicationId
- [ ] `app/proguard-rules.pro` (1 hit)
- [ ] `.github/labeler.yml`, `privacy.md`

```bash
# directories
for root in app/src/main/java app/src/github/java app/src/foss/java \
            app/src/androidTest/java app/src/androidTestGithub/java \
            app/src/test/java app/src/testGithub/java app/src/testFoss/java \
            benchmark/src/main/java; do
  mkdir -p "$root/com/gothwad"
  git mv "$root/io/github/aedev/flow" "$root/com/gothwad/tube"
  rmdir -p --ignore-fail-on-non-empty "$root/io/github/aedev" 2>/dev/null || true
done
# contents (dotted) — TASK.md ko exclude karna, warna ye doc khud rewrite ho jayegi
grep -rl --binary-files=without-match 'io\.github\.aedev\.flow' . --exclude-dir=.git --exclude=TASK.md \
  | xargs sed -i 's/io\.github\.aedev\.flow/com.gothwad.tube/g'
# contents (slash)
grep -rl --binary-files=without-match 'io/github/aedev/flow' . --exclude-dir=.git --exclude=TASK.md \
  | xargs sed -i 's|io/github/aedev/flow|com/gothwad/tube|g'
```

**Verify (dono zero aana chahiye):**
```bash
# NOTE: TASK.md khud me ye strings rakhti hai, isliye use exclude karna zaroori hai
grep -rc 'io\.github\.aedev\.flow' . --exclude-dir=.git --exclude=TASK.md | grep -v ':0$' | wc -l   # 0
grep -rc 'io/github/aedev/flow' . --exclude-dir=.git --exclude=TASK.md | grep -v ':0$' | wc -l      # 0
find . -type d -name flow -path '*aedev*' -not -path './.git/*' | wc -l                            # 0
```

---

### Phase 3 — Code identifier rename: 233 brand identifiers

**Goal:** `Flow*` → `GoTube*`, `DeepFlow*` → `DeepGoTube*`, etc. — **word-boundary safe**.

- [ ] `rename_map.json` ke 233 entries apply karo, **longest-first** order me (taaki
      `rememberFlowBottomSheetState` `rememberFlow…` se pehle replace ho)
- [ ] Sirf `.kt` / `.java` files par
- [ ] Har replace `\b`-anchored ho

```python
import json, re, pathlib
mapping = json.load(open("rename_map.json"))["rename"]
# longest first — prefix collisions se bachne ke liye
keys = sorted(mapping, key=len, reverse=True)
pat = re.compile(r'\b(' + '|'.join(map(re.escape, keys)) + r')\b')
for p in list(pathlib.Path("app/src").rglob("*.kt")) + \
         list(pathlib.Path("benchmark/src").rglob("*.kt")) + \
         list(pathlib.Path("app/src").rglob("*.java")):
    s = p.read_text(encoding="utf-8")
    n = pat.sub(lambda m: mapping[m.group(1)], s)
    if n != s: p.write_text(n, encoding="utf-8")
```

**Verify:**
```bash
# coroutines APIs intact rehni chahiye — ye counts Phase-1 baseline se match karein
grep -rc 'StateFlow' --include='*.kt' app/src | grep -v ':0$' | wc -l     # non-zero
grep -rn 'GoTubeStateFlow\|GoTubePreview\|asGoTube\|snapshotGoTube' --include='*.kt' app/src   # MUST be empty
grep -rn '\bGoTube\b *<' --include='*.kt' app/src | head   # bare 'GoTube<' type usage = leak, MUST be empty
```

> **⚠️ Spotless formatting — is phase ke baad `spotlessApply` chalana zaroori hai.**
> `build.gradle.kts` me `spotless { ratchetFrom(...) }` hai, matlab sirf *changed* files check hoti
> hain. Is phase me ~2400 files change hongi, isliye spotless un sabko check karega aur
> `spotlessCheck` fail ho sakta hai. Fix:
> ```bash
> ./gradlew spotlessApply      # sab changed files ko format kar dega
> ./gradlew spotlessCheck      # ab pass hona chahiye
> ```
> Ye JDK chahiye — sandbox me nahi chalega, isliye local ya CI par karna.
>
> **Background:** ratchet base commit originally upstream `52c4928e…` (A-EDev/Flow) tha. Wo object
> is repo me exist nahi karta, isliye import ke baad `spotlessCheck` turant fail ho raha tha
> (`Check Kotlin formatting` step, exit 1). Use hamare import commit
> `4908f99aab7c6282374dd4359e52ac0ee57f1866` par re-anchor kar diya gaya hai.

---

### Phase 4 — File, resource aur manifest rename

**Goal:** filenames, drawables, mipmaps, XML ids, activity-aliases.

- [ ] 89 Kotlin files rename (Appendix C se — `PlayerStateFlows.kt` aur
      `PlayerStateFlowsTest.kt` **skip**)
- [ ] Drawables: `ic_flow_logo` → `ic_gotube_logo`, `ic_flow_badge_glyph` → `ic_gotube_badge_glyph`,
      `ic_flow_badge_shape` → `ic_gotube_badge_shape`, `ic_fg_flow_play` → `ic_fg_gotube_play`
- [ ] Mipmaps: `ic_launcher_flow_light(_round)`, `ic_launcher_flow_play(_round)` → `…_gotube_…`
- [ ] `AndroidManifest.xml`: `.FlowApplication` → `.GoTubeApplication`, activity-aliases
      `.IconFlowRed/.IconFlowLight/.IconFlowPlay` → `.IconGoTube*`
- [ ] Splash themes: **39** `Theme_Flow_Starting_*` → `Theme_GoTube_Starting_*`
      (`res/values*/themes*.xml` + Kotlin/manifest references)
- [ ] String resource **keys** jo brand le jaate hain: `settings_item_about_flow`,
      `icon_name_flow_red/light/play`, `deep_flow_*`, `settings_flow_engine_header`,
      `settings_reset_brain_*`, `whats_new_in_flow`, etc. — keys rename karke **saare
      `R.string.*` references bhi update** karna
- [ ] Logcat TAGs: `Flow_CipherWebView` etc. → `GoTube_CipherWebView`
- [ ] User-Agent: `FlowYouTube/1.0` → `GoTubeYouTube/1.0` (2 files)
- [ ] `Theme`/`style` names in `res/values/themes.xml` aur `styles.xml`

**Verify:**
```bash
find app/src benchmark/src -type f \( -name '*.kt' -o -name '*.java' \) -exec basename {} \; \
  | grep Flow | grep -v PlayerStateFlows        # MUST be empty
grep -rn 'R\.drawable\.ic_flow\|R\.mipmap\.ic_launcher_flow' --include='*.kt' app/src   # MUST be empty
grep -rn 'Theme_Flow_' app/src | wc -l                                                   # 0
grep -rn 'android:name="\.Flow' app/src/main/AndroidManifest.xml                          # empty
```

---

### Phase 5 — Build config, flavors aur packaging

**Goal:** Gradle level par GoTube identity.

- [ ] `app/build.gradle.kts`: namespace, applicationId already Phase 2 me hua — **confirm** karo;
      `applicationIdSuffix` `.debug` / `.nightly` as-is rahega (final = `com.gothwad.tube.debug`)
- [ ] `settings.gradle.kts`: `rootProject.name = "Flow"` → `"GoTube"`
- [ ] `gradle.properties` — koi `flow` reference ho toh update
- [ ] `benchmark/build.gradle.kts` — target package / applicationId references
- [ ] `app/proguard-rules.pro` — package rules
- [ ] Baseline profile files regenerate/rewrite (Phase 2 me slash-form already hua)
- [ ] `DISCORD_APPLICATION_ID` (`app/build.gradle.kts:83`, value `1526515771021328514`) —
      ye **upstream A-EDev ka Discord app ID hai**. GoTube ke liye **apna Discord application
      banao ya feature disable karo**. ⚠️ **User se poochna** — iske bina Discord rich presence
      upstream ke app ke naam se dikhega.
- [ ] **Versioning already CI-driven hai** — `androidComponents { withBuildType("release") }` me
      `versionCode = GITHUB_RUN_NUMBER` aur `versionName = "1.0.<run>"` set hota hai. Isliye
      `defaultConfig` ke `versionCode = 18` / `versionName = "2.2.1"` sirf **local builds** ke liye
      fallback hain. Rebrand ke waqt inhe `1` / `"1.0.0"` par reset kar sakte ho, CI par koi asar nahi.

**Verify:**
```bash
grep -n 'namespace\|applicationId\|rootProject.name' app/build.gradle.kts settings.gradle.kts benchmark/build.gradle.kts
grep -rin 'flow' build.gradle.kts settings.gradle.kts gradle.properties app/build.gradle.kts benchmark/build.gradle.kts
```

---

### Phase 6 — App name aur UI strings (26 locales)

**Goal:** user-visible naam `GoTube`, formal jagah `Gothwad Tube`, plus `Powered by YouTube`.

- [ ] `res/values/strings.xml`:
      - `app_name` (3) → `GoTube`
      - `app_launcher_name` (4) → `GoTube`
      - `app_name_uppercase` (404) → `GOTUBE`
      - `settings_item_about_flow` (742) → `About GoTube`
- [ ] Naye strings add karo:
      ```xml
      <string name="app_formal_name" translatable="false">Gothwad Tube</string>
      <string name="app_powered_by">Powered by YouTube</string>
      <string name="app_developer">Gothwad Tech</string>
      <string name="about_developed_by">Developed &amp; managed by Gothwad Tech</string>
      <string name="about_fork_of">GoTube is a fork of Flow</string>
      <string name="section_special_thanks">Special Thanks</string>
      ```
- [ ] Baaki **117** `Flow`-containing strings me se brand references badlo — **dhyan se**, har line
      padh ke. Feature strings: `settings_flow_engine_header` → `GoTube Engine`,
      `deep_flow_mode_title` → `Deep GoTube Mode`, `settings_reset_brain_title` →
      `Reset GoTube Personality?`, `theme_name_classic_dark` → `GoTube Default`, etc.
- [ ] **Sab 26 locales** me `app_name` → `GoTube`
- [ ] Sentences ke andar wali `Flow` wording: English me poori badlo; baaki locales me kam se kam
      `app_name` + brand-proper-noun badlo (baaki translated wording ko blindly sed mat karna —
      grammar toot sakti hai)

```bash
for f in app/src/main/res/values*/strings.xml; do
  sed -i 's|<string name="app_name">Flow</string>|<string name="app_name">GoTube</string>|' "$f"
  sed -i 's|<string name="app_launcher_name" translatable="false">Flow</string>|<string name="app_launcher_name" translatable="false">GoTube</string>|' "$f"
done
```

**Verify:**
```bash
for f in app/src/main/res/values*/strings.xml; do
  printf "%-42s %s\n" "$f" "$(grep -o '<string name="app_name">[^<]*' "$f" | sed 's/.*>//')"
done
# sab 26 me 'GoTube' aana chahiye (4 empty wale locales pehle se empty the)
grep -c 'Flow' app/src/main/res/values/strings.xml   # target: 0
```

---

### Phase 7 — About screen: Gothwad Tech + Special Thanks section

**Goal:** About UI me developer attribution aur poora credits list.

- [ ] `AboutScreen.kt` ke URL constants update:
      ```kotlin
      private const val GITHUB_URL      = "https://github.com/GothwadTech/tubeapp"
      private const val LICENSE_URL     = "https://github.com/GothwadTech/tubeapp/blob/main/License"
      private const val DEVELOPER_URL   = "https://github.com/GothwadTech"
      private const val UPSTREAM_URL    = "https://github.com/A-EDev/Flow"
      // WEBSITE_URL, REDDIT_URL, CREATOR_URL, CREATOR_AVATAR_URL → REMOVE
      ```
- [ ] `WEBSITE_URL` aur `REDDIT_URL` **rows hatao** (`AboutIndex.website`, `AboutIndex.reddit`)
- [ ] `CreatorRow` → **Developer row**: `Gothwad Tech`, role = "Developer & Maintainer",
      avatar `https://github.com/GothwadTech.png?size=144`, link `DEVELOPER_URL`
- [ ] **Naya group `about.thanks`** add karo, header `R.string.section_special_thanks`, jisme
      **10 entries** ho — README ke 9 acknowledgments + fork origin:
      | Entry | URL |
      |---|---|
      | Flow (A-EDev) — *GoTube is a fork of Flow* | https://github.com/A-EDev/Flow |
      | NewPipeExtractor | https://github.com/TeamNewPipe/NewPipeExtractor |
      | NewPipe | https://github.com/TeamNewPipe/NewPipe |
      | PipePipe | https://codeberg.org/NullPointerException/PipePipe |
      | PipePipe Developer Docs | https://priveetee.github.io/Docs-PipePipe/ |
      | MetroList | https://github.com/MetrolistGroup/Metrolist |
      | LibreTube | https://github.com/LibreTube/LibreTube |
      | ExoPlayer | https://github.com/google/ExoPlayer |
      | Jetpack Compose | https://developer.android.com/jetpack/compose |
      | Material Design 3 | https://m3.material.io/ |
- [ ] `AboutIndex.kt` me in sabke `entry(...)` add karo aur `AboutIndex.all` update karo
      (ye settings-search me index hota hai)
- [ ] `AboutHeader()` me `Powered by YouTube` line add karo (version ke neeche) aur formal name
      `Gothwad Tube` — short name `GoTube` headline me rahega
- [ ] Strings add karo: `about_thanks_*` title/subtitle pairs (10 entries × 2), `section_special_thanks`

**Verify:**
```bash
grep -n 'WEBSITE_URL\|REDDIT_URL\|CREATOR_URL\|flow.aedev.me\|A-EDev/flow' \
  app/src/main/java/com/gothwad/tube/ui/screens/settings/about/AboutScreen.kt   # MUST be empty
grep -c 'about_thanks_' app/src/main/res/values/strings.xml                      # >= 20
grep -n 'AboutIndex.all' app/src/main/java/com/gothwad/tube/ui/screens/settings/index/AboutIndex.kt
```

---

### Phase 8 — TV About pane aur baaki UI surfaces

**Goal:** jo About ke alawa brand dikhaate hain.

- [ ] `ui/tv/screens/settings/TvAboutSettingsPane.kt` — **same treatment** (isme apne
      `FLOW_*_URL` constants hain, e.g. `FLOW_REDDIT_URL` @47). Developer + special thanks yahan bhi.
- [ ] `ui/screens/settings/about/ChangelogSheet.kt` — "What's new in Flow" → GoTube
- [ ] Onboarding screens (`ui/screens/onboarding/`) — brand text
- [ ] Splash / starting window (`Theme_GoTube_Starting_*` + `res/drawable` splash art)
- [ ] Notification channels aur notification text (`notification/` package + `channel_error_log_clip_label`)
- [ ] **Widgets** (9 receivers) — labels: `widget_now_playing_label` etc. me Flow mention ho toh
- [ ] Crash handler (`utils/FlowCrashHandler` → `GoTubeCrashHandler`, crash report text,
      `CrashSummary.kt`)
- [ ] `data/update/` — in-app updater: GitHub repo path `A-EDev/Flow` → `GothwadTech/tubeapp`
      (⚠️ warna updater upstream ke releases fetch karega)
- [ ] `res/values/strings.xml` ke remaining brand strings
- [ ] Permission dialogs (`local_media_permission_body`, `link_not_supported`, etc.)
- [ ] Tests update: `DiagnosticsAndAboutTest.kt`, `SettingsIndexTest.kt`, `SettingsSearchTest.kt`
      (ye AboutIndex entries assert karte hain)

**Verify:**
```bash
grep -rin 'flow' app/src/main/res/values/strings.xml                       # 0
grep -rn 'A-EDev/Flow\|A-EDev/flow' app/src --include='*.kt'                # empty
grep -rln 'Flow' app/src/main/java/com/gothwad/tube/ui/tv/                  # empty
```

---

### Phase 9 — Docs aur repo metadata

**Goal:** README + saare docs GoTube/Gothwad Tech ke, upstream credit preserve.

- [ ] **`README.md`** — poora rewrite:
      - Title/banner → GoTube (Gothwad Tube), "Powered by YouTube"
      - Intro: privacy-first YouTube + YouTube Music client
      - **Naya section near top: `## 🙏 Credits & Acknowledgments`** — clearly state
        *"GoTube is a fork of [Flow](https://github.com/A-EDev/Flow) by A-EDev"* +
        README ke existing 9 acknowledgments
      - Developed & maintained by **Gothwad Tech**
      - GitHub/issue links → `GothwadTech/tubeapp`
      - Weblate/translate section — **hatao ya placeholder** (apna project nahi hai abhi)
      - Donate section (Patreon + crypto addresses, lines ~180–199) — **hatao**, ye upstream ke hain
      - Star History chart → `GothwadTech/tubeapp`
      - License section: GoTube © Gothwad Tech, **plus** original Flow © A-EDev notice (GPLv3)
- [ ] `CONTRIBUTING.md` — "Release and Signing Invariants" section (lines ~560–590) update:
      cert SHA-256, keystore names, repo refs. `A-EDev` mentions → `Gothwad Tech`
- [ ] `SECURITY.md` — contact/repo refs
- [ ] `CODE_OF_CONDUCT.md` — contact refs
- [ ] `privacy.md` — package name + URLs
- [ ] `AGENTS.md` (37 KB) — package map examples me `io.github.aedev.flow` → `com.gothwad.tube`
- [ ] `release-notes.md` — decide: keep as upstream history ya reset. ⚠️ **User se poochna**
- [ ] `License` — GPLv3 text as-is rakho, copyright line me Gothwad Tech add + A-EDev preserve
- [ ] `.github/CODEOWNERS`, `.github/FUNDING.yml`, `.github/ISSUE_TEMPLATE/config.yml`
- [ ] `fastlane/` — `title.txt` → `GoTube`, `short_description.txt`, `full_description.txt`,
      changelogs. Screenshots/icon abhi upstream ke hain — **replace karne padenge** (asset work)
- [ ] `Assets/` — 11 screenshots upstream ke hain, replace karne padenge (asset work)

**Verify:**
```bash
grep -rn 'io\.github\.aedev\.flow\|A-EDev/Flow\|flow\.aedev\.me\|flow-tube\.org' *.md .github fastlane
# sirf intentional fork-credit mentions bachne chahiye
```

---

### Phase 10 — CI/CD, signing aur final verification

**Goal:** workflow GoTube ke liye sahi ho, aur poora rebrand verify ho.

- [ ] `.github/workflows/build.yml` **line 25** `EXPECTED_SIGNER_SHA256` — **apne keystore ke
      digest se badlo**. Naya keystore bana ke:
      ```bash
      keytool -genkeypair -v -keystore release.keystore -alias gotube \
        -keyalg RSA -keysize 2048 -validity 10000
      keytool -list -v -keystore release.keystore | grep SHA256
      ```
      Warna `Verify release signing certificate` step har APK pe fail karega.
- [ ] APK output names: workflow `flow.apk` / `flow-foss.apk` publish karta hai —
      `gotube.apk` / `gotube-foss.apk` karo (⚠️ IzzyOnDroid file-name contract; fork ke liye
      apna feed hoga toh free choice)
- [ ] `.github/workflows/codeql.yml`, `labeler.yml` — path aur package refs
- [ ] Repo secrets setup guide (10 secrets — Part 1 ki CI table)
- [ ] `.github/FUNDING.yml` — upstream ke sponsor links hatao
- [ ] **Final global verification** (neeche)

**Final verification suite:**

> Ye do scripts **already tested hain** — current (un-renamed) repo par dono clean baseline dete hain:
> `package/dir mismatches: 0` (2402 files) aur `UNRESOLVED: 0` (8993 imports, 254 packages).
> Rebrand ke baad inhe `com.gothwad.tube` ke saath chalao — dono **0** hi aana chahiye.

**`tools_verify_pkgdir.py`** — har `.kt` ka `package` declaration uske directory se match karta hai:
```python
import pathlib, re, sys
PKG_ROOT = sys.argv[1] if len(sys.argv) > 1 else "io.github.aedev.flow"
# Known pre-existing quirk in upstream: file declares '...models.body' but sits in '.../models'.
# Kotlin allows package != directory (unlike Java). Do NOT "fix" this during the rename.
KNOWN_OK = {"innertube/models/PlaylistDeleteBody.kt"}
bad, scanned = [], 0
for p in pathlib.Path('.').rglob('*.kt'):
    parts = p.parts
    if '.git' in parts or 'legacy' in parts: continue          # legacy/ is gitignored, not compiled
    m = re.search(r'^package\s+([\w.]+)', p.read_text(errors='ignore'), re.M)
    if not m or not m.group(1).startswith(PKG_ROOT): continue
    scanned += 1
    rel = str(p).lstrip('./')
    if any(rel.endswith(k) for k in KNOWN_OK): continue
    if not str(p.parent).endswith('/'.join(m.group(1).split('.'))):
        bad.append((rel, m.group(1)))
print(f"scanned {scanned} kt files (legacy/ excluded)")
print(f"package/dir mismatches: {len(bad)}")
for f, pk in bad[:20]: print(f"   {f}\n     declares {pk}")
sys.exit(1 if bad else 0)
```

**`tools_verify_imports.py`** — har internal import ko declared top-level symbols se resolve karta hai
(extension functions, nested members, file-facades, wildcards sab handle karta hai):
```python
import pathlib, re, sys, collections

PKG_ROOT = sys.argv[1] if len(sys.argv) > 1 else "io.github.aedev.flow"
roots = [pathlib.Path('app/src'), pathlib.Path('benchmark/src')]
kt = [p for r in roots for p in r.rglob('*.kt')]

TOP = re.compile(
    r'^[ \t]*(?:@[\w.]+(?:\([^)]*\))?[ \t]*)*'
    r'(?:(?:public|internal|private|protected|abstract|final|open|sealed|data|value|enum|annotation|'
    r'expect|actual|inline|noinline|crossinline|const|lateinit|external|operator|infix|suspend|'
    r'tailrec|vararg|fun|companion|inner|reified)[ \t]+)*'
    r'(?:class|interface|object|typealias)[ \t]+([A-Za-z_]\w*)'
    r'|^[ \t]*(?:@[\w.]+(?:\([^)]*\))?[ \t]*)*'
    r'(?:(?:public|internal|private|protected|inline|suspend|operator|infix|tailrec|external|'
    r'const|lateinit|vararg|actual|expect)[ \t]+)*'
    r'(?:fun|val|var)[ \t]+(?:<[^>]*>[ \t]*)?(?:[^\s(=;]+\.)?'
    r'([A-Za-z_]\w*)', re.M)

pkg_syms = collections.defaultdict(set)
pkg_files = collections.defaultdict(set)
for p in kt:
    s = p.read_text(errors='ignore')
    m = re.search(r'^package\s+([\w.]+)', s, re.M)
    if not m: continue
    pkg = m.group(1)
    pkg_files[pkg].add(p.stem)
    for d in TOP.finditer(s):
        pkg_syms[pkg].add(d.group(1) or d.group(2))
    pkg_syms[pkg].add(p.stem + "Kt")

def resolve(imp, wildcard):
    if imp.endswith('.R') or '.R.' in imp or imp.endswith('.BuildConfig') or imp.endswith('.BR'):
        return True
    if wildcard:
        return imp in pkg_syms                      # 'import pkg.*' -> pkg must exist
    parts = imp.split('.')
    for cut in range(len(parts), 1, -1):
        pkg, sym = '.'.join(parts[:cut-1]), parts[cut-1]
        if pkg.startswith(PKG_ROOT) and pkg in pkg_syms:
            return sym in pkg_syms[pkg] or sym in pkg_files[pkg]
    return not imp.startswith(PKG_ROOT)

missing, total = [], 0
for p in kt:
    s = p.read_text(errors='ignore')
    for raw in re.findall(r'^import\s+([\w.*]+)', s, re.M):
        wildcard = raw.endswith('.*')
        imp = raw[:-2] if wildcard else raw
        if not imp.startswith(PKG_ROOT): continue
        total += 1
        if not resolve(imp, wildcard): missing.append((str(p), raw))
print(f"scanned {len(kt)} kt files | {total} internal imports | {len(pkg_syms)} packages indexed")
print(f"UNRESOLVED: {len(missing)}")
for x in missing[:20]: print("  ", x[1], "  <-", x[0].split('/')[-1])
sys.exit(1 if missing else 0)
```

```bash
python3 tools_verify_pkgdir.py  com.gothwad.tube ; echo "exit=$?"   # mismatches: 0, exit=0
python3 tools_verify_imports.py com.gothwad.tube ; echo "exit=$?"   # UNRESOLVED: 0, exit=0
```

**Baaki quick greps:**
```bash
# 1. Package fully migrated (TASK.md exclude — usme reference strings hain)
grep -rc 'io\.github\.aedev\.flow\|io/github/aedev/flow' . --exclude-dir=.git --exclude=TASK.md \
  | grep -v ':0$' | wc -l                                                    # 0

# 2. No brand 'Flow' left in code (coroutines allowed)
grep -rn '\bFlow[A-Z]' --include='*.kt' app/src benchmark/src | wc -l        # 0

# 3. Coroutines intact — baseline se match kare, koi leak na ho
grep -rc 'StateFlow' --include='*.kt' app/src | grep -v ':0$' | wc -l         # non-zero, unchanged
grep -rn 'GoTubeStateFlow\|GoTubePreview\|snapshotGoTube\|asGoTube' --include='*.kt' app/src   # empty
grep -rn 'MaterialShapes.GoTube\|GoTubePreview::class' --include='*.kt' app/src                 # empty

# 4. Resource references resolve
grep -rn 'R\.string\.[a-z_]*flow\|R\.drawable\.ic_flow\|R\.style\.Theme_Flow' \
  --include='*.kt' app/src                                                   # empty
grep -rn 'Theme_Flow_' app/src | wc -l                                       # 0
```

**⚠️ Note — 1 pre-existing quirk jo "fix" nahi karna:**
`app/src/main/java/io/github/aedev/flow/innertube/models/PlaylistDeleteBody.kt` package
`…innertube.models.body` declare karta hai par file `…innertube/models/` me hai. Kotlin me package
aur directory ka match karna zaroori nahi (Java me hota hai). Ye upstream me pehle se aisa hai —
**rename ke waqt ise "theek" mat karna**, warna imports tootenge. `tools_verify_pkgdir.py` ise
`KNOWN_OK` me whitelist karta hai.

---

## PART 4 — ⚠️ Known verification blocker (is sandbox me)

**Gradle build yahan nahi chal sakta.** Verified facts:
- `java: command not found`
- `ANDROID_HOME` / `ANDROID_SDK_ROOT` dono unset
- Network sirf inhi hosts tak limited hai: `github.com`, `codeload.github.com`, `api.github.com`,
  `registry.npmjs.org`, `pypi.org`, `files.pythonhosted.org` — JDK/Android SDK/Gradle distribution
  (`services.gradle.org`, `dl.google.com`) reachable nahi

**Iska matlab:** rebrand ke baad compile-verify nahi ho payega. Phase 10 ka static verification
suite (package/dir consistency + import resolution + resource reference check) hi best available
check hai. **User ko ye clearly batana** — aur recommend karna ki merge se pehle local machine ya
CI par `./gradlew :app:assembleGithubDebug` ek baar chalein.

---

## PART 5 — Open questions jo execution ke dauran poochne hain

1. **`DISCORD_APPLICATION_ID`** (`app/build.gradle.kts:83` = `1526515771021328514`) — upstream ka
   Discord app ID. Apna banao ya feature disable karo?
2. **Version reset** — `versionCode 18 / versionName 2.2.1` rakhein ya `1 / 1.0.0` par reset karein?
3. **`release-notes.md`** — upstream history rakhein ya fresh start?
4. **App icon aur screenshots** (`Assets/`, `fastlane/metadata/android/en-US/images/`,
   `res/mipmap-*`) — abhi upstream Flow ke hain. Naye assets chahiye ya temporarily rehne dein?
5. **`legacy/` folder** — gitignored hai. Rename me include karna hai ya skip?

---

## Appendix A — 233 identifiers to RENAME (`Flow` → `GoTube`)

*(machine-readable: `rename_map.json` Phase 1 me generate hoga — ye list sirf human reference ke liye)*

```
DeepFlow -> DeepGoTube
DeepFlowDefaults -> DeepGoTubeDefaults
DeepFlowDuration -> DeepGoTubeDuration
DeepFlowDurations -> DeepGoTubeDurations
DeepFlowHistory -> DeepGoTubeHistory
DeepFlowManager -> DeepGoTubeManager
DeepFlowScrobble -> DeepGoTubeScrobble
DeepFlowSnapshot -> DeepGoTubeSnapshot
DeepFlowState -> DeepGoTubeState
FlowActionButton -> GoTubeActionButton
FlowActionButtonPair -> GoTubeActionButtonPair
FlowAlertDialog -> GoTubeAlertDialog
FlowApp -> GoTubeApp
FlowAppSideEffects -> GoTubeAppSideEffects
FlowApplication -> GoTubeApplication
FlowBottomInsets -> GoTubeBottomInsets
FlowBottomInsetsTest -> GoTubeBottomInsetsTest
FlowBottomSheet -> GoTubeBottomSheet
FlowBottomSheetState -> GoTubeBottomSheetState
FlowBottomSheetTest -> GoTubeBottomSheetTest
FlowChaptersBottomSheet -> GoTubeChaptersBottomSheet
FlowChoice -> GoTubeChoice
FlowChoiceDialog -> GoTubeChoiceDialog
FlowColorPickerDialog -> GoTubeColorPickerDialog
FlowCommentItem -> GoTubeCommentItem
FlowCommentsBottomSheet -> GoTubeCommentsBottomSheet
FlowCommentsList -> GoTubeCommentsList
FlowConnectedToggleGroup -> GoTubeConnectedToggleGroup
FlowCrashBreadcrumb -> GoTubeCrashBreadcrumb
FlowCrashHandler -> GoTubeCrashHandler
FlowCrashReportFormatter -> GoTubeCrashReportFormatter
FlowCrashReportFormatterTest -> GoTubeCrashReportFormatterTest
FlowCrashReportSnapshot -> GoTubeCrashReportSnapshot
FlowDescriptionBottomSheet -> GoTubeDescriptionBottomSheet
FlowDiagnostics -> GoTubeDiagnostics
FlowDialogDefaults -> GoTubeDialogDefaults
FlowDownloadService -> GoTubeDownloadService
FlowDropdownFilterChip -> GoTubeDropdownFilterChip
FlowEmptyState -> GoTubeEmptyState
FlowErrorState -> GoTubeErrorState
FlowFeedFooter -> GoTubeFeedFooter
FlowFeedProgress -> GoTubeFeedProgress
FlowFilterChip -> GoTubeFilterChip
FlowFontFamily -> GoTubeFontFamily
FlowGlanceTheme -> GoTubeGlanceTheme
FlowGlobalActions -> GoTubeGlobalActions
FlowGlobalActionsMode -> GoTubeGlobalActionsMode
FlowGlobalActionsRow -> GoTubeGlobalActionsRow
FlowHeaderLogoIcon -> GoTubeHeaderLogoIcon
FlowLiveChatBottomSheet -> GoTubeLiveChatBottomSheet
FlowLiveChatBottomSheetTest -> GoTubeLiveChatBottomSheetTest
FlowLoadingIndicator -> GoTubeLoadingIndicator
FlowLoadingIndicatorRobolectricTest -> GoTubeLoadingIndicatorRobolectricTest
FlowLogo -> GoTubeLogo
FlowMaxContentWidth -> GoTubeMaxContentWidth
FlowMediaNavigator -> GoTubeMediaNavigator
FlowModalSheetDefaults -> GoTubeModalSheetDefaults
FlowMorphingPortrait -> GoTubeMorphingPortrait
FlowMusicAlgorithm -> GoTubeMusicAlgorithm
FlowNavRow -> GoTubeNavRow
FlowNavTransitions -> GoTubeNavTransitions
FlowNavigation -> GoTubeNavigation
FlowNavigationBar -> GoTubeNavigationBar
FlowNavigationBarTest -> GoTubeNavigationBarTest
FlowNavigationChrome -> GoTubeNavigationChrome
FlowNavigationChromeTest -> GoTubeNavigationChromeTest
FlowNavigationDefaults -> GoTubeNavigationDefaults
FlowNavigationRail -> GoTubeNavigationRail
FlowNavigationScrollState -> GoTubeNavigationScrollState
FlowNetwork -> GoTubeNetwork
FlowNeuro -> GoTubeNeuro
FlowNeuroBrainSnapshot -> GoTubeNeuroBrainSnapshot
FlowNeuroEngine -> GoTubeNeuroEngine
FlowNoteCard -> GoTubeNoteCard
FlowNoteEditorDialog -> GoTubeNoteEditorDialog
FlowNotificationsAction -> GoTubeNotificationsAction
FlowPairAction -> GoTubePairAction
FlowPalettes -> GoTubePalettes
FlowPaneState -> GoTubePaneState
FlowPersona -> GoTubePersona
FlowPillChip -> GoTubePillChip
FlowPillSection -> GoTubePillSection
FlowPlayerOverlays -> GoTubePlayerOverlays
FlowPlayerSessionEffects -> GoTubePlayerSessionEffects
FlowPlaylistQueueBottomSheet -> GoTubePlaylistQueueBottomSheet
FlowPopIn -> GoTubePopIn
FlowProgressBanner -> GoTubeProgressBanner
FlowPullToRefreshBox -> GoTubePullToRefreshBox
FlowQuickSearchChips -> GoTubeQuickSearchChips
FlowReplyItem -> GoTubeReplyItem
FlowRow -> GoTubeRow
FlowRowGroup -> GoTubeRowGroup
FlowRowScope -> GoTubeRowScope
FlowRowsTest -> GoTubeRowsTest
FlowSearchField -> GoTubeSearchField
FlowSearchFieldEchoTest -> GoTubeSearchFieldEchoTest
FlowSearchTopBar -> GoTubeSearchTopBar
FlowSectionHeader -> GoTubeSectionHeader
FlowSegmentedGap -> GoTubeSegmentedGap
FlowSelectionAction -> GoTubeSelectionAction
FlowSelectionRow -> GoTubeSelectionRow
FlowSelectionToolbar -> GoTubeSelectionToolbar
FlowShapes -> GoTubeShapes
FlowSheetHeader -> GoTubeSheetHeader
FlowSheetHeaderDefaults -> GoTubeSheetHeaderDefaults
FlowSidePanes -> GoTubeSidePanes
FlowSleepTimerBottomSheet -> GoTubeSleepTimerBottomSheet
FlowSortChip -> GoTubeSortChip
FlowStartTabEffects -> GoTubeStartTabEffects
FlowStateIcon -> GoTubeStateIcon
FlowSubscribeButton -> GoTubeSubscribeButton
FlowSubscribeButtonSize -> GoTubeSubscribeButtonSize
FlowSuggestionRow -> GoTubeSuggestionRow
FlowSwitch -> GoTubeSwitch
FlowSwitchRow -> GoTubeSwitchRow
FlowTab -> GoTubeTab
FlowTabTest -> GoTubeTabTest
FlowTagFields -> GoTubeTagFields
FlowTheme -> GoTubeTheme
FlowThemeSlot -> GoTubeThemeSlot
FlowToggleOption -> GoTubeToggleOption
FlowTopBar -> GoTubeTopBar
FlowTopBarDefaults -> GoTubeTopBarDefaults
FlowTopBarMenuItem -> GoTubeTopBarMenuItem
FlowTopBarOverflow -> GoTubeTopBarOverflow
FlowTopBarTest -> GoTubeTopBarTest
FlowTopBarTitle -> GoTubeTopBarTitle
FlowTranscriptBottomSheet -> GoTubeTranscriptBottomSheet
FlowTranscriptBottomSheetTest -> GoTubeTranscriptBottomSheetTest
FlowTvApp -> GoTubeTvApp
FlowTypographyTest -> GoTubeTypographyTest
FlowUriHandler -> GoTubeUriHandler
FlowUriHandlerTest -> GoTubeUriHandlerTest
FlowVideoAutoNext -> GoTubeVideoAutoNext
FlowVideoLifecycle -> GoTubeVideoLifecycle
FlowWidgetEntry -> GoTubeWidgetEntry
FlowWidgets -> GoTubeWidgets
FlowYouTube -> GoTubeYouTube
Flow_CipherDeobfusc -> GoTube_CipherDeobfusc
Flow_CipherFetcher -> GoTube_CipherFetcher
Flow_CipherFnExtract -> GoTube_CipherFnExtract
Flow_CipherWebView -> GoTube_CipherWebView
Flow_Official -> GoTube_Official
IconFlowLight -> IconGoTubeLight
IconFlowPlay -> IconGoTubePlay
IconFlowRed -> IconGoTubeRed
LocalFlowBottomInsets -> LocalGoTubeBottomInsets
LocalFlowGlobalActions -> LocalGoTubeGlobalActions
NeuroDeepFlowBookkeepingTest -> NeuroDeepGoTubeBookkeepingTest
NeuroDeepFlowHousekeeping -> NeuroDeepGoTubeHousekeeping
ProvideFlowGlobalActions -> ProvideGoTubeGlobalActions
Theme_Flow_Starting_Black -> Theme_GoTube_Starting_Black
Theme_Flow_Starting_Black_Amoled -> Theme_GoTube_Starting_Black_Amoled
Theme_Flow_Starting_Black_Dynamic -> Theme_GoTube_Starting_Black_Dynamic
Theme_Flow_Starting_Black_Expressive_Cookie -> Theme_GoTube_Starting_Black_Expressive_Cookie
Theme_Flow_Starting_Black_Expressive_Mint -> Theme_GoTube_Starting_Black_Expressive_Mint
Theme_Flow_Starting_Black_Expressive_Oval -> Theme_GoTube_Starting_Black_Expressive_Oval
Theme_Flow_Starting_Black_Expressive_Pill -> Theme_GoTube_Starting_Black_Expressive_Pill
Theme_Flow_Starting_Black_Expressive_Play -> Theme_GoTube_Starting_Black_Expressive_Play
Theme_Flow_Starting_Black_Expressive_Scallop -> Theme_GoTube_Starting_Black_Expressive_Scallop
Theme_Flow_Starting_Black_Expressive_Segmented -> Theme_GoTube_Starting_Black_Expressive_Segmented
Theme_Flow_Starting_Black_Expressive_Sky -> Theme_GoTube_Starting_Black_Expressive_Sky
Theme_Flow_Starting_Black_Ghost -> Theme_GoTube_Starting_Black_Ghost
Theme_Flow_Starting_Black_Monochrome -> Theme_GoTube_Starting_Black_Monochrome
Theme_Flow_Starting_Black_Play -> Theme_GoTube_Starting_Black_Play
Theme_Flow_Starting_Dark -> Theme_GoTube_Starting_Dark
Theme_Flow_Starting_Dark_Amoled -> Theme_GoTube_Starting_Dark_Amoled
Theme_Flow_Starting_Dark_Dynamic -> Theme_GoTube_Starting_Dark_Dynamic
Theme_Flow_Starting_Dark_Expressive_Cookie -> Theme_GoTube_Starting_Dark_Expressive_Cookie
Theme_Flow_Starting_Dark_Expressive_Mint -> Theme_GoTube_Starting_Dark_Expressive_Mint
Theme_Flow_Starting_Dark_Expressive_Oval -> Theme_GoTube_Starting_Dark_Expressive_Oval
Theme_Flow_Starting_Dark_Expressive_Pill -> Theme_GoTube_Starting_Dark_Expressive_Pill
Theme_Flow_Starting_Dark_Expressive_Play -> Theme_GoTube_Starting_Dark_Expressive_Play
Theme_Flow_Starting_Dark_Expressive_Scallop -> Theme_GoTube_Starting_Dark_Expressive_Scallop
Theme_Flow_Starting_Dark_Expressive_Segmented -> Theme_GoTube_Starting_Dark_Expressive_Segmented
Theme_Flow_Starting_Dark_Expressive_Sky -> Theme_GoTube_Starting_Dark_Expressive_Sky
Theme_Flow_Starting_Dark_Ghost -> Theme_GoTube_Starting_Dark_Ghost
Theme_Flow_Starting_Dark_Monochrome -> Theme_GoTube_Starting_Dark_Monochrome
Theme_Flow_Starting_Dark_Play -> Theme_GoTube_Starting_Dark_Play
Theme_Flow_Starting_Light -> Theme_GoTube_Starting_Light
Theme_Flow_Starting_Light_Amoled -> Theme_GoTube_Starting_Light_Amoled
Theme_Flow_Starting_Light_Dynamic -> Theme_GoTube_Starting_Light_Dynamic
Theme_Flow_Starting_Light_Expressive_Cookie -> Theme_GoTube_Starting_Light_Expressive_Cookie
Theme_Flow_Starting_Light_Expressive_Mint -> Theme_GoTube_Starting_Light_Expressive_Mint
Theme_Flow_Starting_Light_Expressive_Oval -> Theme_GoTube_Starting_Light_Expressive_Oval
Theme_Flow_Starting_Light_Expressive_Pill -> Theme_GoTube_Starting_Light_Expressive_Pill
Theme_Flow_Starting_Light_Expressive_Play -> Theme_GoTube_Starting_Light_Expressive_Play
Theme_Flow_Starting_Light_Expressive_Scallop -> Theme_GoTube_Starting_Light_Expressive_Scallop
Theme_Flow_Starting_Light_Expressive_Segmented -> Theme_GoTube_Starting_Light_Expressive_Segmented
Theme_Flow_Starting_Light_Expressive_Sky -> Theme_GoTube_Starting_Light_Expressive_Sky
Theme_Flow_Starting_Light_Monochrome -> Theme_GoTube_Starting_Light_Monochrome
Theme_Flow_Starting_Light_Play -> Theme_GoTube_Starting_Light_Play
TvFlowEngineSettingsPane -> TvGoTubeEngineSettingsPane
deepFlow -> deepGoTube
deepFlowActivatedAt -> deepGoTubeActivatedAt
deepFlowActive -> deepGoTubeActive
deepFlowBookkeeping -> deepGoTubeBookkeeping
deepFlowDuration -> deepGoTubeDuration
deepFlowDurationLabel -> deepGoTubeDurationLabel
deepFlowExpireHours -> deepGoTubeExpireHours
deepFlowGlyphAlpha -> deepGoTubeGlyphAlpha
deepFlowHistory -> deepGoTubeHistory
deepFlowIncognitoAlpha -> deepGoTubeIncognitoAlpha
deepFlowLabel -> deepGoTubeLabel
deepFlowLongPress -> deepGoTubeLongPress
deepFlowMessage -> deepGoTubeMessage
deepFlowSaveToHistory -> deepGoTubeSaveToHistory
deepFlowScrobble -> deepGoTubeScrobble
deepFlowStatus -> deepGoTubeStatus
importFlowBackup -> importGoTubeBackup
isDeepFlowActive -> isDeepGoTubeActive
isDeepFlowCurrentlyActive -> isDeepGoTubeCurrentlyActive
isDeepFlowSaveToHistoryEnabled -> isDeepGoTubeSaveToHistoryEnabled
isFlowFeed -> isGoTubeFeed
isFlowPackage -> isGoTubePackage
loadFlowFeed -> loadGoTubeFeed
onDeepFlowChange -> onDeepGoTubeChange
provideFlowNeuroEngine -> provideGoTubeNeuroEngine
rememberFlowBottomSheetState -> rememberGoTubeBottomSheetState
rememberFlowNavigationScrollState -> rememberGoTubeNavigationScrollState
rememberFlowPaneScaffoldDirective -> rememberGoTubePaneScaffoldDirective
rememberFlowPaneState -> rememberGoTubePaneState
rememberFlowSheetState -> rememberGoTubeSheetState
rememberSelectedFlowTab -> rememberSelectedGoTubeTab
resolveDefaultFlowTab -> resolveDefaultGoTubeTab
resolveFlowColorScheme -> resolveGoTubeColorScheme
resolveFlowThemeSlot -> resolveGoTubeThemeSlot
setDeepFlowActive -> setDeepGoTubeActive
setDeepFlowEnabled -> setDeepGoTubeEnabled
setDeepFlowExpireHours -> setDeepGoTubeExpireHours
setDeepFlowSaveToHistory -> setDeepGoTubeSaveToHistory
setDeepFlowScrobble -> setDeepGoTubeScrobble
visibleFlowTabs -> visibleGoTubeTabs
```

## Appendix B — 68 identifiers to KEEP (⛔ never rename)

```
Flow
FlowPreview
Flower
MutableSharedFlow
MutableStateFlow
PlayerStateFlowsTest
SharedFlow
StateFlow
asSharedFlow
asStateFlow
callbackFlow
channelFlow
colorsFlow
createFlow
currentBackStackEntryFlow
discordPlaybackSnapshotFlow
emptyFlow
getAllPlaylistsFlow
getDownloadWithItemsFlow
getHistoryRetentionDaysFlow
getLikedMusicFlow
getLikedVideosFlow
getLocalHistoryFlow
getMaxHistorySizeFlow
getMusicHistoryFlow
getMusicOnlyWatchLaterFlow
getMusicPlaylistsFlow
getPlaylistVideosFlow
getPlaylistVideosWithAddedAtFlow
getSavedMusicPlaylistsFlow
getSavedShortsFlow
getSavedVideoPlaylistsFlow
getSearchHistoryFlow
getStateFlow
getUserCreatedMusicPlaylistsFlow
getUserCreatedVideoPlaylistsFlow
getVideoHistoryFlow
getVideoOnlySavedShortsFlow
getVideoOnlyWatchLaterFlow
getWatchLaterIdsFlow
getWatchLaterVideosFlow
getWorkInfosForUniqueWorkFlow
inFlow
isAutoDeleteHistoryEnabledFlow
isPlayingFlow
isSearchHistoryEnabledFlow
isSearchSuggestionsEnabledFlow
isVideoSavedToAnyPlaylistFlow
itemsFlow
musicPlaylistsFlow
nowPlayingSnapshotFlow
playingIdFlow
preferencesFlow
progressFlow
receiveAsFlow
scrobbleDuringDeepFlow
settingsFlow
shortsFlow
snapshotFlow
stateFlow
styleFlow
surfaceReadyFlow
thumbnailQualitiesFlow
toggleDeepFlow
videoPlaylistsFlow
videosFlow
widgetColorsFlow
widgetThemeSignatureFlow
```

## Appendix C — 91 code files whose basename contains `Flow`

89 rename honge; `PlayerStateFlows.kt` aur `PlayerStateFlowsTest.kt` **skip**.

```
DeepFlowLabels.kt
DeepFlowManager.kt
FlowActionButton.kt
FlowActionButtonPair.kt
FlowAlertDialog.kt
FlowApp.kt
FlowAppSideEffects.kt
FlowApplication.kt
FlowBottomInsets.kt
FlowBottomInsetsTest.kt
FlowBottomSheet.kt
FlowBottomSheetTest.kt
FlowCastOptionsProvider.kt
FlowChaptersBottomSheet.kt
FlowChoiceDialog.kt
FlowColorPickerDialog.kt
FlowCommentItem.kt
FlowCommentsBottomSheet.kt
FlowCommentsList.kt
FlowConnectedToggleGroup.kt
FlowCrashHandler.kt
FlowCrashReportFormatter.kt
FlowCrashReportFormatterTest.kt
FlowDescriptionBottomSheet.kt
FlowDiagnostics.kt
FlowFilterChip.kt
FlowGlanceTheme.kt
FlowGlobalActions.kt
FlowHeaderLogoIcon.kt
FlowInteractionModifiers.kt
FlowListRows.kt
FlowLiveChatBottomSheet.kt
FlowLiveChatBottomSheetTest.kt
FlowLoadingIndicator.kt
FlowLoadingIndicatorRobolectricTest.kt
FlowLogo.kt
FlowModalSheetDefaults.kt
FlowNavTransitions.kt
FlowNavigation.kt
FlowNavigationBar.kt
FlowNavigationBarTest.kt
FlowNavigationChrome.kt
FlowNavigationChromeTest.kt
FlowNavigationRail.kt
FlowNavigationScrollState.kt
FlowNeuroEngine.kt
FlowNote.kt
FlowPalettes.kt
FlowPaneScaffold.kt
FlowPillChip.kt
FlowPlayerOverlays.kt
FlowPlayerView.kt
FlowPlaylistQueueBottomSheet.kt
FlowProgressBanner.kt
FlowPullToRefresh.kt
FlowQuickSearchChips.kt
FlowReplyItem.kt
FlowRowsTest.kt
FlowSearchField.kt
FlowSearchFieldEchoTest.kt
FlowSearchTopBar.kt
FlowSegmentedProgress.kt
FlowSelectionToolbar.kt
FlowShapeMotion.kt
FlowShapes.kt
FlowSheetHeader.kt
FlowSidePanes.kt
FlowSortChip.kt
FlowStates.kt
FlowStatusBarStyle.kt
FlowSubscribeButton.kt
FlowSuggestionRow.kt
FlowSwitch.kt
FlowTab.kt
FlowTabTest.kt
FlowTagFields.kt
FlowTopBar.kt
FlowTopBarActions.kt
FlowTopBarDefaults.kt
FlowTopBarTest.kt
FlowTranscriptBottomSheet.kt
FlowTranscriptBottomSheetTest.kt
FlowTvApp.kt
FlowTypographyTest.kt
FlowUriHandler.kt
FlowUriHandlerTest.kt
FlowWidgets.kt
NeuroDeepFlowBookkeepingTest.kt
NeuroDeepFlowHousekeeping.kt
PlayerStateFlows.kt
PlayerStateFlowsTest.kt
```
