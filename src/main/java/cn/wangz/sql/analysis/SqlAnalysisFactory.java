package cn.wangz.sql.analysis;

import cn.wangz.sql.analysis.exception.AnalysisException;
import cn.wangz.sql.analysis.hive.HiveAnalysis;

import java.util.Properties;

/**
 * @author wang_zh
 * @date 2020/4/20
 */
public class SqlAnalysisFactory {

    public static SqlAnalysis getSqlAnalysis(String type) throws AnalysisException {
        return getSqlAnalysis(type, new Properties());
    }

    public static SqlAnalysis getSqlAnalysis(String type, Properties properties) throws AnalysisException {
        SqlAnalysis analysis = null;
        if (type == null) {
            type = "hive";
        }
        switch (type.toLowerCase()) {
            case "hive":
                analysis = new HiveAnalysis(properties);
                break;
            case "spark":
                break;
            default:
                throw new AnalysisException("Unsupported type: [ " + type + " ]");
        }
        return analysis;
    }

}
