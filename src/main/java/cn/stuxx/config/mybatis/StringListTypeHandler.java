package cn.stuxx.config.mybatis;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.ibatis.type.MappedTypes;

import java.util.List;

@MappedTypes({List.class})
public class StringListTypeHandler extends ListTypeHandler{
    @Override
    protected TypeReference<List<String>> specificType() {
        return new TypeReference<List<String>>() {
        };
    }
}
