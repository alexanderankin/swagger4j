package org.jsonschema2pojo.rules;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CustomRuleFactory extends RuleFactory {
    private SchemaRule schemaRule = new SchemaRuleImpl(this);
}
