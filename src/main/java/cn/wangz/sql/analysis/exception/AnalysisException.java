package cn.wangz.sql.analysis.exception;

/**
 * @author wang_zh
 * @date 2020/4/17
 */
public class AnalysisException extends Exception {

    public AnalysisException() {
    }

    public AnalysisException(String message) {
        super(message);
    }

    public AnalysisException(Throwable cause) {
        super(cause);
    }

    public AnalysisException(String message, Throwable cause) {
        super(message, cause);
    }
}
