package cn.wangz.sql.analysis.hive;

import cn.wangz.sql.analysis.BaseSqlAnalysis;
import cn.wangz.sql.analysis.exception.AnalysisException;
import org.apache.commons.lang3.CharEncoding;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.ql.Context;
import org.apache.hadoop.hive.ql.parse.*;
import org.apache.hadoop.hive.ql.session.SessionState;
import org.apache.hadoop.security.UserGroupInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Properties;

/**
 * @author wang_zh
 * @date 2020/4/16
 */
public class HiveAnalysis extends BaseSqlAnalysis {

    private static final Logger logger = LoggerFactory.getLogger(HiveAnalysis.class);

    HiveSqlContext context;

    HiveConf conf;
    ParseDriver pd;
    Context ctx;

    private Properties properties;

    public HiveAnalysis() throws AnalysisException {
        this(null);
    }

    public HiveAnalysis(Properties properties) throws AnalysisException {
        super(new HiveSqlContext());
        if (properties != null) {
            this.properties = properties;
        } else {
            this.properties = new Properties();
        }

        try {
            init();
        } catch (Throwable t) {
            throw new AnalysisException("HiveAnalysis init error.", t);
        }
    }
    private void init() throws Throwable {
        String user = this.properties.getProperty("user");
        if (StringUtils.isNotBlank(user)) {
            // set real login user
            UserGroupInformation.setLoginUser(UserGroupInformation.createRemoteUser(user));
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
            ASTNode rootTree = pd.parse(sql, ctx);
            ASTNode tree = ParseUtils.findRootNonNullToken(rootTree);
            logger.debug("ast node tree dump, ", tree.dump());
            this.context.getASTNodeAnalysisRules().forEach(rule -> rule.analysis(tree, HiveAnalysis.this.context));
            BaseSemanticAnalyzer baseSemanticAnalyzer = SemanticAnalyzerFactory.get(conf, tree);
            if (baseSemanticAnalyzer instanceof SemanticAnalyzer) {
                SemanticAnalyzer sem = (SemanticAnalyzer) baseSemanticAnalyzer;
                sem.initCtx(ctx);
                sem.init(true);
                sem.analyze(tree, ctx);
                this.context.getSemAnalysisRules().forEach(rule -> rule.analysis(sem, HiveAnalysis.this.context));
            }
        } catch (Throwable t) {
            throw new AnalysisException("HiveAnalysis analysis error.", t);
        }
    }

    @Override
    public SqlType getSqlType() {
        return SqlType.HIVE;
    }

}
