package cn.wangz.sql.analysis;

import cn.wangz.sql.analysis.exception.AnalysisException;

/**
 * @author wang_zh
 * @date 2020/4/16
 */
public interface SqlAnalysis {

    void analysis(String sql) throws AnalysisException;

    SqlContext getSqlContext();

    SqlType getSqlType();

    enum SqlType {
        HIVE, SPARK;
    }

}
