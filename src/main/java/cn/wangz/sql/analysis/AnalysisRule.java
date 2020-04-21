package cn.wangz.sql.analysis;

import cn.wangz.sql.analysis.SqlContext;

/**
 * @author wang_zh
 * @date 2020/4/20
 */
public interface AnalysisRule<T, U extends SqlContext> {

    void analysis(T source, U context);

}
