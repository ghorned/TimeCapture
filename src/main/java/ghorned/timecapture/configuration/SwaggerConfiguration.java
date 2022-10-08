package ghorned.timecapture.configuration;

import io.swagger.v3.oas.models.media.DateTimeSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenApiCustomiser openApiCustomiser() {
        return openApi -> {
            openApi.getComponents().getSchemas().forEach((s, schema) -> {
                Map<String, Schema> properties = schema.getProperties();
                if (properties == null) {
                    properties = Map.of();
                }
                for (String propertyName : properties.keySet()) {
                    Schema propertySchema = properties.get(propertyName);
                    if (propertySchema instanceof DateTimeSchema) {
                        properties.replace(propertyName, new StringSchema()
                                .example("2022-10-07 08:27:00")
                                .pattern("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$")
                                .description(propertySchema.getDescription()));
                    }
                }
            });
        };
    }
}
