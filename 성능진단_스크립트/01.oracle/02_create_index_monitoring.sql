-- create_index_monitoring.sql------------------------------------------------------------
set feedback off
set heading off
set lines 120
set pages 9999

spool index_monitoring.sql

SELECT 'ALTER INDEX ' || owner || '.' || index_name || ' MONITORING USAGE;'
FROM dba_indexes
WHERE
    owner IN ('SCOTT', 'JPETSTORE')
    AND index_type NOT IN ('DOMAIN','LOB');
	
spool off
set heading on
set feedback on