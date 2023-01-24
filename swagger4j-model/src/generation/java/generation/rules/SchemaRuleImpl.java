package generation.rules;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sun.codemodel.JClassContainer;
import com.sun.codemodel.JType;
import org.jsonschema2pojo.Jsonschema2Pojo;
import org.jsonschema2pojo.Schema;
import org.jsonschema2pojo.rules.RuleFactory;
import org.jsonschema2pojo.rules.SchemaRule;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import static org.apache.commons.lang3.StringUtils.contains;
import static org.apache.commons.lang3.StringUtils.split;

public class SchemaRuleImpl extends SchemaRule {
    private final RuleFactory ruleFactory;

    protected SchemaRuleImpl(RuleFactory ruleFactory) {
        super(ruleFactory);
        this.ruleFactory = ruleFactory;
    }

    @Override
    public JType apply(String nodeName,
                       final JsonNode schemaNode,
                       JsonNode parent,
                       JClassContainer generatableType,
                       Schema schema) {
        if (schemaNode.has("$ref")) {
            final String nameFromRef = nameFromRef(schemaNode.get("$ref").asText());

            schema = ruleFactory.getSchemaStore().create(schema,
                    schemaNode.get("$ref").asText(),
                    ruleFactory.getGenerationConfig().getRefFragmentPathDelimiters());
            ObjectNode referencedContent = (ObjectNode) schema.getContent();
            ((ObjectNode) schemaNode).setAll(referencedContent);
            ((ObjectNode) schemaNode).remove("$ref");

            if (schema.isGenerated()) {
                return schema.getJavaType();
            }

            String name = nameFromRef != null ? nameFromRef : nodeName;
            return apply(name, schemaNode, parent, generatableType, schema);
        }

        JType javaType;
        if (schemaNode.has("enum")) {
            javaType = ruleFactory.getEnumRule().apply(nodeName, schemaNode, parent, generatableType, schema);
        } else {
            javaType = ruleFactory.getTypeRule().apply(nodeName, schemaNode, parent, generatableType.getPackage(), schema);
        }
        schema.setJavaTypeIfEmpty(javaType);

        return javaType;
    }

    private String nameFromRef(String ref) {

        if ("#".equals(ref)) {
            return null;
        }

        String nameFromRef;
        if (!contains(ref, "#")) {
            nameFromRef = Jsonschema2Pojo.getNodeName(ref, ruleFactory.getGenerationConfig());
        } else {
            String[] nameParts = split(ref, "/\\#");
            nameFromRef = nameParts[nameParts.length - 1];
        }

        return URLDecoder.decode(nameFromRef, StandardCharsets.UTF_8);
    }
}
