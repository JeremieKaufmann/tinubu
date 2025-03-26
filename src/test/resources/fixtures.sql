DELETE FROM insurance_policy;
INSERT INTO insurance_policy 
(id, name, status, coverage_start_date, coverage_end_date, creation_date)
VALUES 
(1, 'Life Insurance', 'ACTIVE', '2025-01-01', '2025-12-01', '2025-01-01 12:00:00'),
(2, 'Health Insurance', 'ACTIVE', '2025-01-02', '2025-12-02', '2025-01-01 12:00:00'),
(3, 'Car Insurance', 'INACTIVE', '2025-01-03', '2025-12-03', '2025-01-01 12:00:00'),
(4, 'Professional Insurance', 'ACTIVE', '2025-01-04', '2025-12-04', '2025-01-01 12:00:00'),
(5, 'Travel Insurance', 'ACTIVE', '2025-01-05', '2025-12-05', '2025-01-01 12:00:00');
