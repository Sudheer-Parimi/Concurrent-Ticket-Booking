# Interview Demo Runbook

## Environment
Set `DB_PASSWORD`, `RAZORPAY_KEY_ID`, `RAZORPAY_KEY_SECRET`, and a strong `JWT_SECRET` in the environment before starting the backend.

The backend uses MySQL database `transport`.

## Auth
- `POST /api/auth/register` creates a normal `ROLE_USER` account.
- `POST /api/auth/login` returns a signed JWT.
- Send `Authorization: Bearer <token>` to protected endpoints.
- The seeded admin account is created by `DataInitializer`; change its default password before any real deployment.

## Booking concurrency
Booking and hold operations lock the `Trip` row with `PESSIMISTIC_WRITE`. This serializes seat allocation for a trip inside the database transaction.

## Seat holds
- `POST /api/bookings/hold` creates a `PENDING` booking and `HELD` tickets for 5 minutes.
- `POST /api/bookings/{id}/confirm` converts held tickets to `ACTIVE`.
- A scheduled job runs every 30 seconds and releases expired holds.

## Observability
Actuator exposes health, info and metrics endpoints. Application booking/hold/confirmation/cancellation/expiry events are logged with identifiers useful for tracing a production issue.
