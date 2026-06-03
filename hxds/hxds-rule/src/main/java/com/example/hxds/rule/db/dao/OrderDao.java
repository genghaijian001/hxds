
package com.example.hxds.rule.db.dao;

import java.util.Map;

public interface OrderDao {
    public long searchFinishCountInDay(long var1);

    public long searchFinishCountInRange(Map var1);

    public long searchCancelCountInDay(long var1);
}

