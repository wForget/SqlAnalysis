package cn.wangz.sql.analysis.hive.rules;

import cn.wangz.sql.analysis.AnalysisRule;
import cn.wangz.sql.analysis.hive.HiveSqlContext;
import org.apache.hadoop.hive.ql.parse.ASTNode;

/**
 * @author wang_zh
 * @date 2020/4/20
 */
public class NodeAnalysisRule implements AnalysisRule<ASTNode, HiveSqlContext> {
    @Override
    public void analysis(ASTNode source, HiveSqlContext context) {
        // TODO
    }
}
