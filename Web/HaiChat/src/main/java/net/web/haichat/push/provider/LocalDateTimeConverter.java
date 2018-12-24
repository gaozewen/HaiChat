package net.web.haichat.push.provider;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * LocalDateTime 是一个 Java8 的新时间类型，
 * 使用起来比常规的 Date 更加 Nice；
 * 但是Gson目前并没有默认支持对LocalDateTime的转换
 * <p>
 * 该工具类主要是为了解决LocalDateTime与Json字符串相互转换的问题
 */
public class LocalDateTimeConverter
        implements JsonSerializer<LocalDateTime>,
        JsonDeserializer<LocalDateTime> {

    /**
     * 时间转换的格式为：yyyy-MM-dd'T'HH:mm:ss.SSS
     */
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter
            .ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.ENGLISH);

    /**
     * 把一个LocalDateTime格式的时间转换为Gson支持的JsonElement
     */
    @Override
    public JsonElement serialize(LocalDateTime src,
                                 Type typeOfSrc,
                                 JsonSerializationContext context) {
        return new JsonPrimitive(FORMATTER.format(src));
    }

    /**
     * 把一个Gson的JsonElement转换为LocalDateTime时间格式
     */
    @Override
    public LocalDateTime deserialize(JsonElement json,
                                     Type typeOfT,
                                     JsonDeserializationContext context)
            throws JsonParseException {
        return FORMATTER.parse(json.getAsString(), LocalDateTime::from);
    }
}