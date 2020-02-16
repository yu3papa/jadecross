SELECT
    t1.table_name,
    t1.index_name,
    t2.uniqueness,
    t2.blevel,
    t2.leaf_blocks,
    t2.distinct_keys,
    t2.clustering_factor,
    t2.num_rows
FROM
    v$object_usage t1,
    user_indexes t2
WHERE
    t1.index_name = t2.index_name
    AND   t1.used = 'NO;