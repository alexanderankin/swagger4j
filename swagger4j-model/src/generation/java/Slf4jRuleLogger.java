import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.Delegate;
import org.jsonschema2pojo.AbstractRuleLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Data
public class Slf4jRuleLogger extends AbstractRuleLogger {
    @Delegate
    private Logger log;

    public Slf4jRuleLogger() {
        this("Slf4jRuleLogger");
    }

    public Slf4jRuleLogger(String name) {
        log = LoggerFactory.getLogger(name);
    }

    @Override
    protected void doDebug(String msg) {
        log.debug(msg);
    }

    @Override
    protected void doError(String msg, Throwable e) {
        log.error(msg, e);
    }

    @Override
    protected void doInfo(String msg) {
        log.info(msg);
    }

    @Override
    protected void doTrace(String msg) {
        log.trace(msg);
    }

    @Override
    protected void doWarn(String msg, Throwable e) {
        log.warn(msg, e);
    }
}
