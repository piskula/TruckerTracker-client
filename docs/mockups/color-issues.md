# Color Semantic Issues

Non-container / foreground colors used as backgrounds.

## Definite fixes

- [ ] **`Button.kt:45`** — `warning` used as button `containerColor`; swap to `warningContainer` + use `warning` as `contentColor` (instead of `onPrimary`)
- [ ] **`Button.kt:46`** — `positive` used as button `containerColor`; swap to `positiveContainer` + use `positive` as `contentColor` (instead of `onPrimary`)
- [ ] **`IssueDetailScreen.kt:509`** — `onSurfaceVariant` used as background for the comment dot circle; swap to `surfaceVariant`

## Questionable

- [ ] **`IssueDetailScreen.kt:646`** — `primary` used as the send-button circle background; debatable — action button circles using `primary` is common in M3, but `primaryContainer` + `onPrimaryContainer` would be the strict semantic choice

## Intentional — do not change

- `DashboardTopBar.kt` — `primaryContainer` as top bar background, `onPrimaryContainer` for all content
- `Toolbar.kt` — `primaryContainer` as toolbar background, `onPrimaryContainer` for title
- `FilterChipRow.kt` — `primaryContainer` as filter chip row background; active chip = `primary`/`onPrimary`
- `FloatingActionButton.kt:26` — `primary` as FAB `containerColor` (correct per M3)
- `IssueDetailScreen.kt` — people strip uses `surfaceContainerHighest` / `onSurface` / `onSurfaceVariant`
