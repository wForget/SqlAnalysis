package cn.wangz.sql.analysis.hive;

import cn.wangz.sql.analysis.exception.AnalysisException;
import org.junit.Before;
import org.junit.Test;

/**
 * @author wang_zh
 * @date 2020/4/17
 */
public class HiveAnalysisTests {

    HiveAnalysis analysis;

    @Before
    public void init() throws AnalysisException {
        String user = "app";
        analysis = new HiveAnalysis(user);
    }


    @Test
    public void analysisTest() throws AnalysisException {
//        String sql = "insert into test.wangz_test001 select * from test.wang_zh_test where name = 'wangz'";
        String sql = "insert into test.apppkg_active_runtime_dis_monthly_tmp PARTITION (par=20200400) (apppkg,zone) select apppkg,zone from test.apppkg_active_runtime_dis_monthly_tmp where par=20190400 limit 10";

        analysis.analysis(sql);
    }

}
