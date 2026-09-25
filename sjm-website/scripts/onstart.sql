-- Reused config keys (not calendar secrets). Credentials stay env-only.
INSERT INTO sjm.app_property (property_key, property_value) VALUES
  ('app.timezone', 'America/Chicago'),
  ('mandir.address.line1', '5N515 IL-59'),
  ('mandir.address.city', 'Bartlett'),
  ('mandir.address.state', 'IL'),
  ('mandir.address.zip', '60103'),
  ('Flash.hallReservation.success.approved', 'Your hall reservation was submitted and is approved.'),
  ('Flash.hallReservation.success.pending', 'Your hall reservation was submitted and is pending review.'),
  ('Flash.hallReservation.success.approvedWithCalendar', 'Your hall reservation was submitted and the calendar date has been booked.'),
  ('Flash.hallReservation.success.pendingWithCalendar', 'Your hall reservation was submitted and the calendar date has been booked.'),
  ('Flash.hallReservation.success.approvedCalendarFailed', 'Your hall reservation was submitted. Calendar booking failed. Please contact us.'),
  ('Flash.hallReservation.success.pendingCalendarFailed', 'Your hall reservation was submitted. Calendar booking failed. Please contact us.')
ON CONFLICT (property_key) DO NOTHING;
