package cn.wangz.sql.analysis;

/**
 * @author wang_zh
 * @date 2020/4/16
 */
public abstract class BaseSqlAnalysis implements SqlAnalysis {

    private SqlContext context;

    public BaseSqlAnalysis(SqlContext context) {
        this.context = context;
    }

    @Override
    public SqlContext getSqlContext() {
        return this.context;
    }
}
