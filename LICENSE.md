# Obscuria Modding License (OML) v1.3

Obscuria projects are distributed under a custom license designed to protect project identity and official distribution while supporting an open modding ecosystem. All rights not expressly granted remain reserved.

This license applies to projects solely authored by the original Obscuria author. For collaborative projects co-authored with one or more additional authors — typically identified by joint branding such as "Obscuria X [Collaborator]" in official project materials — see Section 7.

<br>

## 1. Permitted Uses

### 1.1 Modpacks
Inclusion in modpacks via CurseForge, Modrinth, or similar platforms is permitted, including monetization through official platform reward programs. Runtime modifications such as mixins, datapacks, configs, and compatibility layers are permitted.

### 1.2 Content Creation
Creation and monetization of videos, streams, tutorials, reviews, and similar content featuring the project is permitted. This includes revenue generated through advertisements, memberships, sponsorships, donations, or comparable platform features.

### 1.3 Servers
Use on public or private servers is permitted, including standard monetization models such as donations, cosmetics, and access tiers, provided the mod itself is not sold separately and access to the mod is not independently paywalled.

### 1.4 Integration and Dependency
Using this project as a library or dependency is explicitly permitted. Developers may include the original, unmodified JAR as a nested dependency (e.g. via Fabric's Nested JARs or similar mechanisms) in their own mods, provided that:
- the included files are unmodified
- the original project is clearly credited with a link to the official source
- the containing mod provides substantial independent functionality beyond re-bundling this project
- the integration does not misrepresent the project or imply official endorsement

### 1.5 Private Modification
Modifying the source code for personal, private, or internal use is permitted. You may maintain private forks and apply patches for local use without restriction.

### 1.6 Community Contributions
Forks and pull requests intended to contribute back to the official project are welcome. Public forks for experimentation are permitted, provided they are not distributed as independent releases or marketed as alternative versions of the project.

<br>

## 2. Restrictions

To preserve project integrity and branding, the following actions are not permitted without explicit written authorization:

- **Public redistribution** — redistributing mod files outside official distribution platforms, including mirrors or alternative download hosts
- **Distribution of modified versions** — publishing altered JARs or forks as standalone releases on any platform
- **Rebranding** — presenting any version of this project under a different name or as an independent project
- **Commercial exploitation of derivatives** — monetizing modified versions through donations, platform programs, memberships, or any other means
- **Asset reuse** — using this project's code, assets, or substantial design elements as the basis of a standalone project

> **Note:** Modifying the code privately is not restricted. The restrictions above apply to *publishing or monetizing* those modifications.

<br>

## 3. Commercial Use

Commercial activity is permitted only within the scope defined in Section 1. Any commercial use involving modified versions, redistribution outside official channels, or derivative products requires explicit written authorization.

<br>

## 4. Source Code and Transparency

Source code may be publicly accessible to encourage transparency, learning, and collaborative development. Public availability does not grant rights to publish modified releases, create independent branded derivatives, or relicense the project. All intellectual property rights remain with the original author.

<br>

## 5. Branding and Identity

The project name, branding, visual identity, and associated materials remain the intellectual property of the original author. Integrations and compatibility mods must clearly identify themselves as independent projects and must not imply official endorsement without permission.

<br>

## 6. Author Inactivity

### 6.1 Definition of Activity
The author is considered active if any of the following has occurred within the relevant period, across any official channel associated with this project:

- a commit, release, or issue response in any official project repository (e.g. GitHub)
- a post, reply, or visible online presence on official project pages (e.g. CurseForge, Modrinth)
- a message or reply in the official project Discord server
- a direct response to a contact attempt as described in Section 6.3

Activity on *any* official channel counts, regardless of which project it relates to. Inactivity is assessed per-author, not per-project.

### 6.2 Inactivity Thresholds

**Tier 1 — 90 days of inactivity:**
The restrictions in Section 2 on distributing and publishing modified versions are suspended. Community members may publish forks under the following conditions:

- the release title must include the label **"Community Fork"** (e.g. *"Obscuria [ProjectName] — Community Fork"*)
- the original author must be clearly credited in the project description and README
- the fork must link to the original official release page
- rebranding remains prohibited — the original project name may be referenced but the fork must not present itself as the official project
- commercial exploitation of the fork (donations, platform programs, memberships, etc.) remains prohibited unless the author has been unreachable for 365 days (see Tier 2)

**Tier 2 — 365 days of inactivity:**
All restrictions in Section 2 are fully suspended. The project is relicensed under the **MIT License** for all purposes. Community forks may be distributed, modified, commercially supported, and maintained freely. The original author's name must still be credited in all derivative works.

### 6.3 Inactivity Verification Process
A person wishing to invoke inactivity protections must complete all of the following steps before publishing anything under this section:

1. **Attempt contact** via at least two of the following official channels: a GitHub issue or discussion in any official repository; a direct message in the official project Discord server; a comment on an official CurseForge or Modrinth project page.
2. **Wait at least 14 days** after the last contact attempt for a response.
3. **Document the attempts** — screenshots, links, or timestamps of the contact attempts must be retained and made publicly available (e.g. in the fork's README or a linked public post).
4. **Verify inactivity** independently — confirm that no activity as defined in Section 6.1 has occurred in the relevant period across all known official channels.

If the author responds at any point during the contact period, the inactivity clock resets and the process must begin again.

### 6.4 Author Return
If the author resumes activity after a period of inactivity:

- Rights granted under **Tier 1** to existing community forks already published are not revoked. Those forks may continue to be maintained and distributed under the conditions of Tier 1.
- The author may resume enforcing this license in full for any *new* forks or distributions initiated after the date of their return.
- The author may not retroactively claim infringement for actions taken in good faith under Section 6.3 during a verified period of inactivity.
- If **Tier 2** (MIT relicensing) has been triggered, it cannot be reversed. The MIT license grant is permanent and irrevocable.

### 6.5 Good Faith Requirement
This section may not be invoked in bad faith. Actions that constitute bad faith include but are not limited to: deliberately avoiding contact attempts while claiming inactivity; initiating the process within days of a known hiatus announcement by the author; coordinating with others to manufacture the appearance of inactivity. Forks published under this section that are later found to have been initiated in bad faith are considered in violation of this license.

<br>

## 7. Collaborative Projects

### 7.1 Scope
This section applies exclusively to projects jointly created by the Obscuria author and one or more named collaborators. Such projects are typically identified by joint branding in official project materials, such as "Obscuria X [Collaborator Name]" in cover art, project titles, or official descriptions. If a project's authorship is ambiguous, it is governed by the solo-author terms of this license unless a separate written agreement states otherwise.

### 7.2 Inactivity in Collaborative Projects
The inactivity provisions of Section 6 apply differently to collaborative projects. If the Obscuria author becomes inactive as defined in Section 6.1:

- **Tier 1 (90 days)** and **Tier 2 (365 days)** community provisions described in Section 6.2 do **not** apply to collaborative projects.
- Instead, full ownership, maintenance rights, and distribution rights over the collaborative project transfer exclusively to the named co-author(s).
- The co-author(s) may continue to develop, publish, and maintain the project under their own terms without restriction, provided the Obscuria author's original contribution is credited.

### 7.3 Transfer of Rights
Upon confirmed inactivity of the Obscuria author (verified using the process in Section 6.3), the co-author(s) of a collaborative project receive:

- the right to publish updates and new versions of the project independently
- the right to determine the license under which the project is distributed going forward
- the right to maintain or retire the original branding at their discretion, including removing the "Obscuria X" joint branding if they choose

The co-author(s) may not, however, misrepresent the Obscuria author's past contributions or claim sole original authorship of work created jointly.

### 7.4 Author Return
If the Obscuria author returns after a period of inactivity in a collaborative project, any rights already exercised by the co-author(s) under Section 7.3 are not revoked. Future development of the project is subject to mutual agreement between all original authors. Neither party may unilaterally revoke the other's authorship credit or contributions.