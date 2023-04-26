UPDATE
    mlf2_entries
	JOIN 
    (
        SELECT
            tid,
            MAX(time) last_reply 
        FROM
            mlf2_entries
        GROUP BY
            tid
    ) sub ON mlf2_entries.tid = sub.tid
SET
    mlf2_entries.last_reply = sub.last_reply;
