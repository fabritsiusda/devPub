package main.mappers;

import com.fasterxml.jackson.annotation.JsonProperty;
import main.models.Setting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.List;

public class SettingMapper {

    private SettingMapper(){}

    private static final Logger LOG = LoggerFactory.getLogger(SettingMapper.class);

    public static <T> T mapSettingsToResponse(List<Setting> settings, Class<T> clazz){
        try {
            T t = clazz.getDeclaredConstructor().newInstance();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields){
                JsonProperty jsonProperty = field.getAnnotation(JsonProperty.class);
                if (jsonProperty != null){
                    String value = jsonProperty.value();
                    settings.stream()
                            .filter(s -> s.getCode().equals(value))
                            .forEach(s -> setValueInField(field, t, s.getValue().equalsIgnoreCase("yes")));
                }
            }
            return t;
        } catch (Exception e) {
            LOG.error("cannot create response", e);
        }
        return null;
    }

    private static void setValueInField(Field field, Object obj, Object value){
        field.setAccessible(true);
        try {
            field.set(obj, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            LOG.warn("cannot set value in field", e);
        }
    }

}
