# Color Semantic Issues

Non-container / foreground colors used as backgrounds.

## Definite fixes

- [ ] **`Button.kt:45`** — `warning` used as button `containerColor`; swap to `warningContainer` + use `warning` as `contentColor` (instead of `onPrimary`)
- [ ] **`Button.kt:46`** — `positive` used as button `containerColor`; swap to `positiveContainer` + use `positive` as `contentColor` (instead of `onPrimary`)
- [ ] **`IssueDetailScreen.kt:509`** — `onSurfaceVariant` used as background for the comment dot circle; swap to `surfaceVariant`
- [ ] **`CreateIssueScreen.kt:476`** — `onSurface.copy(alpha = 0.55f)` used as background for the remove-photo ✕ circle; swap to a surface/container color

## Questionable — `primary` as layout background

- [ ] **`IssueDetailScreen.kt:307`** — `primary` used as people-strip background (set here when `tertiary` was removed); in M3 this ideally should be `primaryContainer` to create a lighter band distinct from the toolbar above it
- [ ] **`IssueDetailScreen.kt:646`** — `primary` used as the send-button circle background; debatable — action button circles using `primary` is common in M3, but `primaryContainer` + `onPrimaryContainer` would be the strict semantic choice

## Intentional — do not change

- `DashboardTopBar.kt:34` — `primary` as top bar background (standard M3)
- `Toolbar.kt:29` — `primary` as toolbar background (standard M3)
- `FilterChipRow.kt:33` — `primary` as filter chip row background (extension of the top bar strip)
- `FloatingActionButton.kt:26` — `primary` as FAB `containerColor` (correct per M3)
- `IssueDetailScreen.kt:339` — `onPrimary.copy(alpha = 0.2f)` as avatar circle overlay on a `primary` background (intentional tint pattern)
