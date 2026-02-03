BEGIN;

-- Optional: enable UUID generator. If you don't have permissions, comment this line.
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- ============================================================
-- 1) USERS (3 records)
-- ============================================================
-- NOTE: passwords are bcrypt hashes (example hash for "password")
-- email is unique (uk_users_email)

INSERT INTO users (
    id, created_at, updated_at,
    name, surname, email, phone_number,
    password, is_active, balance, role
) VALUES
      -- Password1A --
      (
          '11111111-1111-1111-1111-111111111111',
          now(), NULL,
          'Ivan', 'Petrenko', 'ivan@gmail.com', '+380501111111',
          '$2b$12$8rU1Pq6lRk3K7mYt7l4C1eE8Yz0hE8nYyH6qv0mH0yFJ1M4Yb5B8S',
          true, 5000.00, 'USER'
      ),
      -- Password2B --
      (
          '22222222-2222-2222-2222-222222222222',
          now(), NULL,
          'Maria', 'Koval', 'maria.manager@gmail.com', '+380502222222',
          '$2b$12$FzZ7KpJ7zP6Q6M2V9z0b6e5ZP6R3Y9ZQn1r7ZxM7cYQYqF2q2pF3S',
          true, 12000.00, 'MANAGER'
      ),
      -- Password3C --
      (
          '33333333-3333-3333-3333-333333333333',
          now(), NULL,
          'Oleh', 'Admin', 'oleh.admin@gmail.com', '+380503333333',
          '$2b$12$wEJ0d2S0BvP0M8GkFZpZ9O0X7M9x1E1MZkJxN0KJ5z0z8qZ0Hn6pK',
          true, 99999.00, 'ADMIN'
      )
    ON CONFLICT (email) DO NOTHING;

-- ============================================================
-- 2) TOURS (25 records)
-- ============================================================
-- Enums:
-- tour_type: REST | EXCURSION | SHOPPING
-- transfer_type: CAR | PLANE | SHIP
-- hotel_type: THREE | FOUR | FIVE

INSERT INTO tours (
    id, created_at, updated_at,
    title, long_description, short_description,
    price, country, city,
    is_hot, is_active, capacity,
    tour_type, transfer_type, hotel_type,
    check_in_datetime, check_out_datetime
) VALUES
      ('10000000-0000-0000-0000-000000000001', now(), NULL, 'Bukovel Relax', '3 days in the mountains with light activities and breakfast.', '3 days / mountains', 7200.00, 'Ukraine', 'Bukovel', true,  true,  20, 'REST',      'CAR',   'FOUR',  '2026-02-10 12:00:00', '2026-02-13 10:00:00'),
      ('10000000-0000-0000-0000-000000000002', now(), NULL, 'Lviv City Tour', 'Two-day cultural program in Lviv with guided walks.',        '2 days / culture',    4300.00, 'Ukraine', 'Lviv',    false, true,  30, 'EXCURSION', 'CAR',   'THREE', '2026-03-05 09:00:00', '2026-03-07 18:00:00'),
      ('10000000-0000-0000-0000-000000000003', now(), NULL, 'Istanbul Shopping', 'Flight + hotel, free time for shopping and прогулки.',       '4 days / flight',     12000.00,'Turkey',   'Istanbul',true,  true,  25, 'SHOPPING',  'PLANE', 'FIVE',  '2026-04-12 08:00:00', '2026-04-16 22:00:00'),
      ('10000000-0000-0000-0000-000000000004', now(), NULL, 'Odesa Sea Weekend', 'Weekend near the sea with flexible schedule.',               '2 days / sea',        3900.00, 'Ukraine', 'Odesa',   false, true,  40, 'REST',      'CAR',   'THREE', '2026-06-06 10:00:00', '2026-06-08 12:00:00'),
      ('10000000-0000-0000-0000-000000000005', now(), NULL, 'Kyiv Highlights', 'City highlights tour with museums and landmarks.',            '1 day / city',        1500.00, 'Ukraine', 'Kyiv',    false, true,  50, 'EXCURSION', 'CAR',   'THREE', '2026-02-20 09:00:00', '2026-02-20 20:00:00'),

      ('10000000-0000-0000-0000-000000000006', now(), NULL, 'Prague Old Town', 'Historic city exploration with local guide.',                 '3 days / Europe',     9800.00, 'Czechia', 'Prague',  true,  true,  22, 'EXCURSION', 'PLANE', 'FOUR',  '2026-05-10 08:00:00', '2026-05-13 21:00:00'),
      ('10000000-0000-0000-0000-000000000007', now(), NULL, 'Budapest Thermal', 'Relax + thermal baths and evening cruise.',                   '3 days / spa',        8700.00, 'Hungary', 'Budapest',false, true,  24, 'REST',      'PLANE', 'FOUR',  '2026-05-18 09:00:00', '2026-05-21 20:00:00'),
      ('10000000-0000-0000-0000-000000000008', now(), NULL, 'Warsaw Weekend', 'Short weekend trip with central hotel.',                       '2 days / weekend',    6100.00, 'Poland',  'Warsaw',  false, true,  28, 'EXCURSION', 'PLANE', 'THREE', '2026-04-03 08:00:00', '2026-04-05 21:00:00'),
      ('10000000-0000-0000-0000-000000000009', now(), NULL, 'Krakow + Salt Mine', 'City + famous salt mine visit.',                            '3 days / classic',    7400.00, 'Poland',  'Krakow',  true,  true,  26, 'EXCURSION', 'CAR',   'THREE', '2026-04-20 07:00:00', '2026-04-23 22:00:00'),
      ('10000000-0000-0000-0000-00000000000a', now(), NULL, 'Gdansk Sea Walks', 'Northern coast vibe, old town, and sea walks.',               '3 days / coast',      7900.00, 'Poland',  'Gdansk',  false, true,  18, 'REST',      'PLANE', 'FOUR',  '2026-07-02 09:00:00', '2026-07-05 20:00:00'),

      ('10000000-0000-0000-0000-00000000000b', now(), NULL, 'Barcelona City Break', 'Architecture + beach time.',                               '4 days / mix',        16500.00,'Spain',   'Barcelona',true,  true,  20, 'REST',      'PLANE', 'FOUR',  '2026-08-10 08:00:00', '2026-08-14 22:00:00'),
      ('10000000-0000-0000-0000-00000000000c', now(), NULL, 'Rome Classic', 'Ancient city exploration with optional Vatican day.',           '4 days / history',    15800.00,'Italy',   'Rome',    false, true,  25, 'EXCURSION', 'PLANE', 'FOUR',  '2026-09-05 08:00:00', '2026-09-09 21:00:00'),
      ('10000000-0000-0000-0000-00000000000d', now(), NULL, 'Venice Escape', 'Canals, islands, and evening walks.',                          '3 days / romance',    14900.00,'Italy',   'Venice',  true,  true,  16, 'REST',      'PLANE', 'FIVE',  '2026-09-12 08:00:00', '2026-09-15 21:00:00'),
      ('10000000-0000-0000-0000-00000000000e', now(), NULL, 'Paris Museum Route', 'Museums, river cruise, and central hotel.',                '4 days / art',        18900.00,'France',  'Paris',   true,  true,  20, 'EXCURSION', 'PLANE', 'FIVE',  '2026-10-03 09:00:00', '2026-10-07 22:00:00'),
      ('10000000-0000-0000-0000-00000000000f', now(), NULL, 'Berlin Tech Weekend', 'Modern Berlin + optional museums.',                         '3 days / modern',     10500.00,'Germany', 'Berlin',  false, true,  30, 'EXCURSION', 'PLANE', 'FOUR',  '2026-04-10 08:00:00', '2026-04-13 20:00:00'),

      ('10000000-0000-0000-0000-000000000010', now(), NULL, 'Alps Ski Starter', 'Entry-level ski trip with equipment guidance.',              '5 days / ski',        22000.00,'Austria', 'Innsbruck',true, true,  14, 'REST',      'PLANE', 'FOUR',  '2026-12-15 09:00:00', '2026-12-20 18:00:00'),
      ('10000000-0000-0000-0000-000000000011', now(), NULL, 'Zakarpattya Thermal', 'Local thermal waters and спокойний відпочинок.',           '2 days / thermal',    3100.00, 'Ukraine', 'Berehove',false, true,  35, 'REST',      'CAR',   'THREE', '2026-02-28 10:00:00', '2026-03-02 12:00:00'),
      ('10000000-0000-0000-0000-000000000012', now(), NULL, 'Dnipro Weekend', 'Short city trip with flexible activities.',                    '2 days / city',       2600.00, 'Ukraine', 'Dnipro',  false, true,  40, 'EXCURSION', 'CAR',   'THREE', '2026-03-14 09:00:00', '2026-03-16 12:00:00'),
      ('10000000-0000-0000-0000-000000000013', now(), NULL, 'Kharkiv Discovery', 'City discovery + local cuisine.',                            '2 days / discovery',  2800.00, 'Ukraine', 'Kharkiv', false, true,  40, 'EXCURSION', 'CAR',   'THREE', '2026-03-21 09:00:00', '2026-03-23 12:00:00'),
      ('10000000-0000-0000-0000-000000000014', now(), NULL, 'Zakynthos Beach', 'Beach rest with hotel near the sea.',                         '6 days / beach',      24500.00,'Greece',  'Zakynthos',true, true,  18, 'REST',      'PLANE', 'FIVE',  '2026-06-20 08:00:00', '2026-06-26 22:00:00'),

      ('10000000-0000-0000-0000-000000000015', now(), NULL, 'Dubai Shopping Rush', 'Shopping malls + desert tour option.',                     '5 days / luxury',     29000.00,'UAE',     'Dubai',   true,  true,  20, 'SHOPPING',  'PLANE', 'FIVE',  '2026-11-05 09:00:00', '2026-11-10 22:00:00'),
      ('10000000-0000-0000-0000-000000000016', now(), NULL, 'Antalya All Inclusive', 'All-inclusive rest with hotel services.',                 '7 days / all inc',    21000.00,'Turkey',  'Antalya', true,  true,  30, 'REST',      'PLANE', 'FIVE',  '2026-07-15 08:00:00', '2026-07-22 22:00:00'),
      ('10000000-0000-0000-0000-000000000017', now(), NULL, 'Izmir Coastal', 'Coastal rest and city walks.',                                  '5 days / coast',      17500.00,'Turkey',  'Izmir',   false, true,  22, 'REST',      'PLANE', 'FOUR',  '2026-08-01 08:00:00', '2026-08-06 22:00:00'),
      ('10000000-0000-0000-0000-000000000018', now(), NULL, 'Thessaloniki Week', 'Greek food, sea, and museums.',                              '6 days / mix',        19800.00,'Greece',  'Thessaloniki',false,true,24,'EXCURSION','PLANE','FOUR', '2026-09-20 08:00:00', '2026-09-26 22:00:00'),
      ('10000000-0000-0000-0000-000000000019', now(), NULL, 'Split Sea & Old Town', 'Croatian coast with historic center walks.',              '6 days / coast',      20500.00,'Croatia', 'Split',   true,  true,  18, 'REST',      'PLANE', 'FOUR',  '2026-08-18 08:00:00', '2026-08-24 22:00:00'),

      ('10000000-0000-0000-0000-00000000001a', now(), NULL, 'Stockholm Weekend', 'Nordic city break with central hotel.',                      '3 days / nordic',     21500.00,'Sweden',  'Stockholm',false,true, 16, 'EXCURSION', 'PLANE', 'FOUR',  '2026-10-15 09:00:00', '2026-10-18 22:00:00'),
      ('10000000-0000-0000-0000-00000000001b', now(), NULL, 'Oslo Fjord Walks', 'Fjords vibe and city exploration.',                           '3 days / fjords',     22500.00,'Norway',  'Oslo',    false, true,  16, 'EXCURSION', 'PLANE', 'FOUR',  '2026-10-22 09:00:00', '2026-10-25 22:00:00'),
      ('10000000-0000-0000-0000-00000000001c', now(), NULL, 'Lisbon Sunny Days', 'Sun, ocean, and old town.',                                  '5 days / sunny',      17200.00,'Portugal','Lisbon',  true,  true,  22, 'REST',      'PLANE', 'FOUR',  '2026-09-01 08:00:00', '2026-09-06 22:00:00'),
      ('10000000-0000-0000-0000-00000000001d', now(), NULL, 'London City + Shopping', 'City sightseeing + shopping time.',                      '4 days / city',       24000.00,'UK',      'London',  true,  true,  20, 'SHOPPING',  'PLANE', 'FOUR',  '2026-11-18 09:00:00', '2026-11-22 22:00:00'),
      ('10000000-0000-0000-0000-00000000001e', now(), NULL, 'Cruise: Black Sea', 'Short cruise experience with onboard program.',              '5 days / cruise',     19500.00,'International', 'Black Sea',false,true,120,'REST',   'SHIP',  'FOUR',  '2026-07-05 16:00:00', '2026-07-10 10:00:00')
    ON CONFLICT (id) DO NOTHING;

-- ============================================================
-- 3) ORDERS (16 total: 1 + 5 + 10)
-- ============================================================
-- orders.order_number is unique
-- status: REGISTERED | PAID | CANCELED

INSERT INTO orders (
    id, created_at, updated_at,
    order_number, total_amount, status,
    user_id, tour_id
) VALUES
-- USER (1 order)
('20000000-0000-0000-0000-000000000001', now(), NULL, 'ORD-000001', 7200.00,  'REGISTERED', '11111111-1111-1111-1111-111111111111', '10000000-0000-0000-0000-000000000001'),

-- MANAGER (5 orders)
('20000000-0000-0000-0000-000000000002', now(), NULL, 'ORD-000002', 4300.00,  'PAID',       '22222222-2222-2222-2222-222222222222', '10000000-0000-0000-0000-000000000002'),
('20000000-0000-0000-0000-000000000003', now(), NULL, 'ORD-000003', 12000.00, 'PAID',       '22222222-2222-2222-2222-222222222222', '10000000-0000-0000-0000-000000000003'),
('20000000-0000-0000-0000-000000000004', now(), NULL, 'ORD-000004', 6100.00,  'REGISTERED', '22222222-2222-2222-2222-222222222222', '10000000-0000-0000-0000-000000000008'),
('20000000-0000-0000-0000-000000000005', now(), NULL, 'ORD-000005', 8700.00,  'CANCELED',   '22222222-2222-2222-2222-222222222222', '10000000-0000-0000-0000-000000000007'),
('20000000-0000-0000-0000-000000000006', now(), NULL, 'ORD-000006', 9800.00,  'PAID',       '22222222-2222-2222-2222-222222222222', '10000000-0000-0000-0000-000000000006'),

-- ADMIN (10 orders)
('20000000-0000-0000-0000-000000000007', now(), NULL, 'ORD-000007', 3900.00,  'REGISTERED', '33333333-3333-3333-3333-333333333333', '10000000-0000-0000-0000-000000000004'),
('20000000-0000-0000-0000-000000000008', now(), NULL, 'ORD-000008', 14900.00, 'PAID',       '33333333-3333-3333-3333-333333333333', '10000000-0000-0000-0000-00000000000d'),
('20000000-0000-0000-0000-000000000009', now(), NULL, 'ORD-000009', 15800.00, 'PAID',       '33333333-3333-3333-3333-333333333333', '10000000-0000-0000-0000-00000000000c'),
('20000000-0000-0000-0000-00000000000a', now(), NULL, 'ORD-000010', 18900.00, 'PAID',       '33333333-3333-3333-3333-333333333333', '10000000-0000-0000-0000-00000000000e'),
('20000000-0000-0000-0000-00000000000b', now(), NULL, 'ORD-000011', 3100.00,  'REGISTERED', '33333333-3333-3333-3333-333333333333', '10000000-0000-0000-0000-000000000011'),
('20000000-0000-0000-0000-00000000000c', now(), NULL, 'ORD-000012', 17500.00, 'PAID',       '33333333-3333-3333-3333-333333333333', '10000000-0000-0000-0000-000000000017'),
('20000000-0000-0000-0000-00000000000d', now(), NULL, 'ORD-000013', 21000.00, 'PAID',       '33333333-3333-3333-3333-333333333333', '10000000-0000-0000-0000-000000000016'),
('20000000-0000-0000-0000-00000000000e', now(), NULL, 'ORD-000014', 21500.00, 'CANCELED',   '33333333-3333-3333-3333-333333333333', '10000000-0000-0000-0000-00000000001a'),
('20000000-0000-0000-0000-00000000000f', now(), NULL, 'ORD-000015', 24000.00, 'REGISTERED', '33333333-3333-3333-3333-333333333333', '10000000-0000-0000-0000-00000000001d'),
('20000000-0000-0000-0000-000000000010', now(), NULL, 'ORD-000016', 19500.00, 'PAID',       '33333333-3333-3333-3333-333333333333', '10000000-0000-0000-0000-00000000001e')
    ON CONFLICT (order_number) DO NOTHING;

-- ============================================================
-- 4) PAYMENTS (some orders have exactly 1 payment, some have no payment)
-- ============================================================
-- payments has unique(order_id)
-- payment_status: NEW | PENDING | SUCCESS | FAILED | CANCELED | REFUNDED
-- paid_at can be NULL for non-success statuses

INSERT INTO payments (
    id, created_at, updated_at,
    order_id, payment_method, status,
    paid_at, amount, failure_reason
) VALUES
-- Manager paid orders
('30000000-0000-0000-0000-000000000001', now(), NULL, '20000000-0000-0000-0000-000000000002', 'CARD',     'SUCCESS',  now(),  4300.00,  NULL),
('30000000-0000-0000-0000-000000000002', now(), NULL, '20000000-0000-0000-0000-000000000003', 'TRANSFER', 'SUCCESS',  now(),  12000.00, NULL),
-- leave ORD-000004 without payment (REGISTERED)
-- leave ORD-000005 without payment (CANCELED)
('30000000-0000-0000-0000-000000000003', now(), NULL, '20000000-0000-0000-0000-000000000006', 'CASH',     'PENDING',  NULL,   9800.00,  NULL),

-- Admin paid orders
('30000000-0000-0000-0000-000000000004', now(), NULL, '20000000-0000-0000-0000-000000000008', 'CARD',     'SUCCESS',  now(),  14900.00, NULL),
('30000000-0000-0000-0000-000000000005', now(), NULL, '20000000-0000-0000-0000-000000000009', 'CARD',     'FAILED',   NULL,   15800.00, 'Insufficient funds'),
('30000000-0000-0000-0000-000000000006', now(), NULL, '20000000-0000-0000-0000-00000000000a', 'TRANSFER', 'SUCCESS',  now(),  18900.00, NULL),
-- leave ORD-000011 without payment (REGISTERED)
('30000000-0000-0000-0000-000000000007', now(), NULL, '20000000-0000-0000-0000-00000000000c', 'CARD',     'SUCCESS',  now(),  17500.00, NULL),
('30000000-0000-0000-0000-000000000008', now(), NULL, '20000000-0000-0000-0000-00000000000d', 'TRANSFER', 'SUCCESS',  now(),  21000.00, NULL),
-- leave ORD-000014 without payment (CANCELED)
-- leave ORD-000015 without payment (REGISTERED)
('30000000-0000-0000-0000-000000000009', now(), NULL, '20000000-0000-0000-0000-000000000010', 'CASH',     'SUCCESS',  now(),  19500.00, NULL)
    ON CONFLICT (order_id) DO NOTHING;

-- ============================================================
-- 5) REVIEWS (some orders have exactly 1 review, some have no review)
-- ============================================================
-- reviews has unique(order_id)

INSERT INTO reviews (
    id, created_at, updated_at,
    comment, rating, order_id
) VALUES
      (gen_random_uuid(), now(), NULL, 'Great organization, everything was on time.', 5, '20000000-0000-0000-0000-000000000002'),
      (gen_random_uuid(), now(), NULL, 'Nice tour, would recommend.',                4, '20000000-0000-0000-0000-000000000003'),
-- no review for ORD-000004
-- no review for ORD-000005
      (gen_random_uuid(), now(), NULL, 'Good experience overall.',                   4, '20000000-0000-0000-0000-000000000006'),

-- Admin reviews
      (gen_random_uuid(), now(), NULL, 'Amazing place and service.',                 5, '20000000-0000-0000-0000-000000000008'),
      (gen_random_uuid(), now(), NULL, 'Payment issues but support helped.',         3, '20000000-0000-0000-0000-000000000009'),
      (gen_random_uuid(), now(), NULL, 'Perfect itinerary, thanks!',                 5, '20000000-0000-0000-0000-00000000000a'),
-- no review for ORD-000011
      (gen_random_uuid(), now(), NULL, 'Everything was ok.',                         4, '20000000-0000-0000-0000-00000000000c'),
-- no review for ORD-000013
-- no review for ORD-000014
-- no review for ORD-000015
      (gen_random_uuid(), now(), NULL, 'Cruise was fun and well organized.',         5, '20000000-0000-0000-0000-000000000010')
    ON CONFLICT (order_id) DO NOTHING;

COMMIT;
