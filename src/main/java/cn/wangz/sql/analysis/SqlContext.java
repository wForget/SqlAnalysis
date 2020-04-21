package cn.wangz.sql.analysis;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wang_zh
 * @date 2020/4/16
 */
public class SqlContext {


    private Map<String, Object> labels = new HashMap<>();

    public Object addLabel(String label, Object value) {
        return labels.put(label, value);
    }

    public Object getLabel(String label) {
        return labels.get(label);
    }

}
