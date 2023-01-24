package generation.rules;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.databind.JsonNode;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JFieldVar;
import org.jsonschema2pojo.GenerationConfig;
import org.jsonschema2pojo.Jackson2Annotator;

public class CustomJacksonAnnotator extends Jackson2Annotator {
    public CustomJacksonAnnotator(GenerationConfig generationConfig) {
        super(generationConfig);
    }

    @Override
    public void propertyField(JFieldVar field,
                              JDefinedClass clazz,
                              String propertyName,
                              JsonNode propertyNode) {
        if (!propertyName.equals(field.name()))
            field.annotate(JsonProperty.class)
                    .param("value", propertyName);

        if (propertyNode.has("description"))
            field.annotate(JsonPropertyDescription.class)
                    .param("value", propertyNode.get("description").asText());
    }
}
