package generation.rules;

import com.sun.codemodel.JFieldVar;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jsonschema2pojo.Annotator;
import org.jsonschema2pojo.rules.Rule;
import org.jsonschema2pojo.rules.RuleFactory;
import org.jsonschema2pojo.rules.SchemaRule;

@EqualsAndHashCode(callSuper = true)
@Data
public class CustomRuleFactory extends RuleFactory {
    private SchemaRule schemaRule = new SchemaRuleImpl(this);
    private Annotator annotator;
    private Rule<JFieldVar, JFieldVar> defaultRule = (nodeName, node, parent, gType, currentSchema) -> null;

    @Override
    public Annotator getAnnotator() {
        if (!(annotator instanceof CustomJacksonAnnotator))
            annotator = new CustomJacksonAnnotator(getGenerationConfig());
        return annotator;
    }
}
