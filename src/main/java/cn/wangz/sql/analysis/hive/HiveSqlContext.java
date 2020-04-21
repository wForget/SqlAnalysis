package cn.wangz.sql.analysis.hive;

import cn.wangz.sql.analysis.AnalysisRule;
import cn.wangz.sql.analysis.SqlContext;
import cn.wangz.sql.analysis.hive.rules.NodeAnalysisRule;
import cn.wangz.sql.analysis.hive.rules.TablesAnalysisRule;
import org.apache.hadoop.hive.ql.parse.ASTNode;
import org.apache.hadoop.hive.ql.parse.SemanticAnalyzer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wang_zh
 * @date 2020/4/17
 */
public class HiveSqlContext extends SqlContext {

    public HiveSqlContext() {

    }

    private static List<AnalysisRule<SemanticAnalyzer, HiveSqlContext>> SEM_RULES = new ArrayList<>();
    static {
        SEM_RULES.add(new TablesAnalysisRule());
    }

    private static List<AnalysisRule<ASTNode, HiveSqlContext>> ASTNODE_RULES = new ArrayList<>();
    static {
        ASTNODE_RULES.add(new NodeAnalysisRule());
    }

    // 从 SemanticAnalyzer 分析后的对象中获取 sql 标签
    public List<AnalysisRule<SemanticAnalyzer, HiveSqlContext>> getSemAnalysisRules() {
        return SEM_RULES;
    }

    // 从 ASTNode 对象中获取 sql 标签
    public List<AnalysisRule<ASTNode, HiveSqlContext>> getASTNodeAnalysisRules() {
        return ASTNODE_RULES;
    }

}
