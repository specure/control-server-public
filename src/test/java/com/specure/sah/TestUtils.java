package com.specure.sah;

import com.bedatadriven.jackson.datatype.jts.JtsModule;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.DoubleNode;
import lombok.experimental.UtilityClass;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.elasticsearch.core.geo.GeoJsonPoint;
import org.testcontainers.shaded.org.apache.commons.lang3.text.StrSubstitutor;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.stream.Stream;

@UtilityClass
public class TestUtils {

    private ObjectMapper mapper = new ObjectMapper();

    static {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(GeoJsonPoint.class, new GeoJsonPointDeserializer());
        mapper.registerModule(module);
    }

    public byte[] convertObjectToJsonBytes(Object object) throws IOException {
        return mapper.writeValueAsBytes(object);
    }

    public String getJsonFromFilePath(String path) throws IOException {
        return IOUtils.toString(new ClassPathResource(path).getInputStream(), Charset.defaultCharset());
    }

    public static String removeLineEndings(String source) {
        return source.replace("\n", "").replace("\r", "");
    }

    public static String asJsonString(final Object obj) throws JsonProcessingException {
        mapper.registerModule(new JtsModule());
        return mapper.writeValueAsString(obj);
    }

    public static <T> T readObjectFromJson(String json, JavaType objectClass) throws JsonProcessingException {
        return mapper.readValue(json, objectClass);
    }

    public static <T> T readObjectFromJson(String json, Class<T> objectClass) throws JsonProcessingException {
        return mapper.readValue(json, objectClass);
    }

    public static <T> T readObjectFromFilePath(String path, Class<T> objectClass, Map<String, String> data) throws IOException {
        String jsonTemplate = IOUtils.toString(new ClassPathResource(path).getInputStream(), Charset.defaultCharset());
        String populatedJson = StrSubstitutor.replace(jsonTemplate, data);
        return mapper.readValue(populatedJson, objectClass);
    }

    public static <T> T readObjectFromFilePath(String path, Class<T> objectClass) throws IOException {
        String json = IOUtils.toString(new ClassPathResource(path).getInputStream(), Charset.defaultCharset());
        return mapper.readValue(json, objectClass);
    }

    private static class GeoJsonPointDeserializer extends StdDeserializer<GeoJsonPoint> {

        protected GeoJsonPointDeserializer() {
            super((Class<?>) null);
        }

        protected GeoJsonPointDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public GeoJsonPoint deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            JsonNode node = jp.getCodec().readTree(jp);
            Double x = ((DoubleNode) node.get("x")).doubleValue();
            Double y = ((DoubleNode) node.get("y")).doubleValue();

            return GeoJsonPoint.of(x, y);
        }
    }

    public static Stream<Arguments> boolArgumentsProvider() {
        return Stream.of(Arguments.of(true, true),
                Arguments.of(true, true));
    }

    public static Stream<Arguments> booleanArgumentsProvider() {
        return Stream.of(Arguments.of(Boolean.TRUE, Boolean.TRUE),
                Arguments.of(Boolean.FALSE, Boolean.FALSE),
                Arguments.of(null, null));
    }

    public static Stream<Arguments> booleanToBoolArgumentsProvider() {
        return Stream.of(Arguments.of(true, Boolean.TRUE),
                Arguments.of(false, Boolean.FALSE),
                Arguments.of(false, null));
    }
}
