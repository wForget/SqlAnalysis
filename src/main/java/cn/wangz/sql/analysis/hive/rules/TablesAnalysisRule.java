package cn.wangz.sql.analysis.hive.rules;

import cn.wangz.sql.analysis.hive.HiveSqlContext;
import cn.wangz.sql.analysis.AnalysisRule;
import org.apache.hadoop.hive.ql.exec.TableScanOperator;
import org.apache.hadoop.hive.ql.parse.ParseContext;
import org.apache.hadoop.hive.ql.parse.PrunedPartitionList;
import org.apache.hadoop.hive.ql.parse.SemanticAnalyzer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wang_zh
 * @date 2020/4/20
 */
public class TablesAnalysisRule implements AnalysisRule<SemanticAnalyzer, HiveSqlContext> {

    @Override
    public void analysis(SemanticAnalyzer sem, HiveSqlContext context) {
        ParseContext parseContext  = sem.getParseContext();
        HashMap<TableScanOperator, PrunedPartitionList> opToPartList = parseContext.getOpToPartList();
        Map<String, PrunedPartitionList> prunedPartitions = parseContext.getPrunedPartitions();

        System.out.println(parseContext);
        // TODO generate context
    }

}
