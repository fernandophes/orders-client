package br.edu.ufersa.cc.sd.utils;

import java.time.format.DateTimeFormatter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public interface JsonUtils {

    final Logger LOG = LogManager.getLogger(JsonUtils.class.getSimpleName());
    final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    final ObjectMapper MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());
    final ObjectReader READER = MAPPER.reader();
    final ObjectWriter WRITER = MAPPER.writer().withDefaultPrettyPrinter();

    public static String toJson(final Object object) {
        try {
            return WRITER.writeValueAsString(object);
        } catch (final JsonProcessingException e) {
            LOG.error("Erro de JSON", e);
            return "";
        }
    }

    public static <T> T fromJson(final String json) {
        try {
            return READER.readValue(json);
        } catch (final JsonProcessingException e) {
            LOG.error("Erro de JSON", e);
            return null;
        }
    }

}
