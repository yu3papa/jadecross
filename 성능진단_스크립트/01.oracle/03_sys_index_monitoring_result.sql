-- V$OBJECT_USAGE 뷰는 자신 소유의 인덱스만 조회되므로  SYS게정으로 조회시 각 스키마의 인덱스 모니터링 데이터는 조회 안됨
-- SYS계정으로 인덱스 모니터링 데이트를 조회하려면 실제 데이터가 있는 OBJECT_USAGE 테이블을 조회하면 됨
-- 각 스키마는 자신 소유의 인덱스 모니터링 데이터를 V$OBJECT_USAGE 뷰를 통해 조회 가능함

SELECT
    username AS owner,
    io.name AS index_name,
    t.name AS table_name,
    DECODE(bitand(i.flags,65536),0,'NO','YES') AS monitoring,
    DECODE(bitand(ou.flags,1),0,'NO','YES') AS used,
    ou.start_monitoring,
    ou.end_monitoring
FROM
    sys.obj$ io,
    sys.obj$ t,
    sys.ind$ i,
    sys.object_usage ou,
    dba_users u
WHERE
    i.obj# = ou.obj#
    AND   io.obj# = ou.obj#
    AND   t.obj# = i.bo#
    AND   user_id = t.owner#;