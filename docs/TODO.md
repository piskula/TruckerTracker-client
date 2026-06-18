# TODO

## Vehicles
- [ ] Differentiate between **trucks** and **trailers** — trailers should have a distinct icon from trucks.
  Requires backend support for a vehicle type field (truck vs. trailer) if not already present.

## Features & Improvements

### Issue Attachments
- [ ] Photos/attachments should be uploadable at any point, not only when creating a ticket.
  Upload button should be available in the issue detail screen for open/in-progress issues too.

### Issues List — Driver (Open tab)
- [ ] Split the single list into two separate lists (or two tabs):
  1. **Open** issues
  2. **In Progress** issues
- [ ] Both lists should support **infinite scrolling** (pagination).

### Issues List — Mechanic
- [ ] Split the single list into two separate lists (or two tabs):
  1. **In Progress** issues
  2. **Closed** issues
- [ ] Both lists should support **infinite scrolling** (pagination).
- [ ] Decide on UX: separate scrollable sections on one screen vs. tab bar on top.

### Issues List — Dual role (Driver + Mechanic)
- [ ] When a user has **both** Driver and Mechanic roles, show a single 3-tab layout:
  1. **Open**
  2. **In Progress**
  3. **All** (default tab on open)


- [ ] **Driver** view: show assigned mechanic instead of the reporter.
- [ ] **Mechanic** view: show the reporting driver instead of the reporter.

### Issue Detail — Resolve confirmation
- [ ] Tapping "Resolve issue" should require confirmation before executing — either a
  confirmation dialog or a swipe-to-confirm mechanism.

### Issue Detail — Reassign
- [ ] Add a **"Reassign to me"** button in issue detail when the issue is **In Progress**.
  Any mechanic should be able to reassign the issue to themselves via this button.




