-- 인덱스 모니터링 시작
ALTER INDEX pk_sample_id MONITORING USAGE;
-- 인덱스 모니터링 해제
ALTER INDEX pk_sample_id NOMONITORING USAGE;

