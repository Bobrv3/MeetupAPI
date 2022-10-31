package com.bobrov.meetup.dao.util;

import com.bobrov.meetup.dao.util.impl.LDTimeFilter;
import com.bobrov.meetup.dao.util.impl.StringFilter;
import com.bobrov.meetup.model.Meetup;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.Map;

@Component
public final class FilterProvider {
    private static final FilterProvider INSTANCE = new FilterProvider();
    private final Map<FilterType, Filter> repository = new EnumMap<>(FilterType.class);

    private FilterProvider() {
        repository.put(FilterType.LOCAL_DATE_TIME, new LDTimeFilter());
        repository.put(FilterType.STRING, new StringFilter());
    }

    @SneakyThrows
    public Filter getFilter(String field) {
        Class<?> type = Meetup.class.getDeclaredField(field).getType();

        if (type == LocalDateTime.class) {
            return repository.get(FilterType.LOCAL_DATE_TIME);
        } else if (type == String.class) {
            return repository.get(FilterType.STRING);
        } else {
            throw new RuntimeException(String.format("No suitable filter with type %s", type));
        }
    }

    public static FilterProvider getInstance() {
        return INSTANCE;
    }
}
