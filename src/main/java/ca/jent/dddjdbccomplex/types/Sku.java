package ca.jent.dddjdbccomplex.types;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.IOException;

@JsonSerialize(using = Sku.SkuSerializer.class)
@JsonDeserialize(using = Sku.SkuDeserializer.class)
public record Sku(String value) {

    @Override
    public String toString() {
        return value;
    }

    // factory method
    public static Sku of(String value) {
        return new Sku(value);
    }

    // serializer / deserializer
    public static class SkuSerializer extends JsonSerializer<Sku> {
        @Override
        public void serialize(Sku sku, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
            gen.writeString(sku.value);
        }
    }

    public static class SkuDeserializer extends JsonDeserializer<Sku> {
        @Override
        public Sku deserialize(JsonParser parser, DeserializationContext ctx) throws IOException {
            return Sku.of(parser.getText());
        }
    }
}

