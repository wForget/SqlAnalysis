package cn.wangz.sql.analysis.hive;

import cn.wangz.sql.analysis.BaseSqlAnalysis;
import cn.wangz.sql.analysis.SqlContext;
import cn.wangz.sql.analysis.exception.AnalysisException;
import org.apache.commons.lang3.CharEncoding;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.ql.Context;
import org.apache.hadoop.hive.ql.exec.TableScanOperator;
import org.apache.hadoop.hive.ql.metadata.Table;
import org.apache.hadoop.hive.ql.parse.*;
import org.apache.hadoop.hive.ql.session.SessionState;
import org.apache.hadoop.security.UserGroupInformation;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wang_zh
 * @date 2020/4/16
 */
public class HiveAnalysis extends BaseSqlAnalysis {

    HiveSqlContext context;

    HiveConf conf;
    ParseDriver pd;
    Context ctx;

    public HiveAnalysis() throws AnalysisException {
        this(null);
    }

    public HiveAnalysis(String user) throws AnalysisException {
        super(new HiveSqlContext());
        this.user = user;
        try {
            init();
        } catch (Throwable t) {
            throw new AnalysisException("init hive analysis error.", t);
        }
    }

    private String user;
    private void init() throws Throwable {
        if (StringUtils.isNotBlank(this.user)) {
            // set real login user
            UserGroupInformation.setLoginUser(UserGroupInformation.createRemoteUser(this.user));
        }
        this.conf = new HiveConf();
        SessionState sessionState = SessionState.start(conf);
        sessionState.out =
                new PrintStream(new FileOutputStream(sessionState.getTmpOutputFile()), true, CharEncoding.UTF_8);
        SessionState.get().initTxnMgr(conf);
        this.ctx = new Context(conf);
        this.ctx.setTryCount(Integer.MAX_VALUE);
        this.ctx.setHDFSCleanup(true);
        this.pd = new ParseDriver();
    }

    @Override
    public void analysis(String sql) throws AnalysisException {
        try {
            ASTNode tree = pd.parse(sql, ctx);
            System.out.println(tree.dump());
            tree = ParseUtils.findRootNonNullToken(tree);
            BaseSemanticAnalyzer baseSemanticAnalyzer = SemanticAnalyzerFactory.get(conf, tree);
            if (baseSemanticAnalyzer instanceof SemanticAnalyzer) {
                SemanticAnalyzer sem = (SemanticAnalyzer) baseSemanticAnalyzer;
                sem.initCtx(ctx);
                sem.init(true);
                sem.analyze(tree, ctx);
                processSemanticAnalyzer(sem);
            }
        } catch (Throwable e) {
            throw new AnalysisException(e);
        }
    }

    private void processSemanticAnalyzer(SemanticAnalyzer sem) {
        ParseContext parseContext  = sem.getParseContext();
        HashMap<TableScanOperator, PrunedPartitionList> opToPartList = parseContext.getOpToPartList();
        Map<String, PrunedPartitionList> prunedPartitions = parseContext.getPrunedPartitions();

        System.out.println(parseContext);
        // TODO generate context
    }

    public static void main(String[] args) throws Exception {
//        String sql = "insert into test.wangz_test001 select * from test.wang_zh_test where name = 'wangz'";
        String sql = "insert into test.apppkg_active_runtime_dis_monthly_tmp PARTITION (par=20200400) (apppkg,zone) select apppkg,zone from test.apppkg_active_runtime_dis_monthly_tmp where par=20190400 limit 10";

        HiveAnalysis analysis = new HiveAnalysis("app");
        analysis.analysis(sql);


//        HiveConf conf = new HiveConf();
//        String user = "app";
//        UserGroupInformation.setLoginUser(UserGroupInformation.createRemoteUser(user));
//        SessionState sessionState = SessionState.start(conf);
//        sessionState.out =
//                new PrintStream(new FileOutputStream(sessionState.getTmpOutputFile()), true, CharEncoding.UTF_8);
//        Context ctx = new Context(conf);
//        ctx.setTryCount(Integer.MAX_VALUE);
//        ctx.setHDFSCleanup(true);
//        ParseDriver pd = new ParseDriver();
//        ASTNode tree = pd.parse(sql, ctx);
//        System.out.println(tree.dump());
//        tree = ParseUtils.findRootNonNullToken(tree);
//        SessionState.get().initTxnMgr(conf);
//        CalcitePlanner sem = new CalcitePlanner(conf);
//        sem.initCtx(ctx);
//        sem.init(true);
//        sem.analyze(tree,ctx );
//        QB qb = sem.getQB();
//        QBMetaData qbMetaData = qb.getMetaData();
//        HashMap<String, Table> aliasToTable = qbMetaData.getAliasToTable();
//        Map<String, Table> nameToDestTable = qbMetaData.getNameToDestTable();
//        System.out.println(qb.getAliases());
    }

}
