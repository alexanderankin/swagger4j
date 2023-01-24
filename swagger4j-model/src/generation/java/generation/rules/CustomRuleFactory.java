package generation.rules;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jsonschema2pojo.rules.RuleFactory;
import org.jsonschema2pojo.rules.SchemaRule;

@EqualsAndHashCode(callSuper = true)
@Data
public class CustomRuleFactory extends RuleFactory {
    private SchemaRule schemaRule = new SchemaRuleImpl(this);
}
