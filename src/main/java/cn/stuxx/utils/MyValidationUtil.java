package cn.stuxx.utils;

import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

public class MyValidationUtil {
    private static final Validator validator = Validation.buildDefaultValidatorFactory()
            .getValidator();
    public static void validate(@Valid Object bean,Class<?>... classes) {
        Set<ConstraintViolation<@Valid Object>> validateSet = validator.validate(bean, classes);
        if (!CollectionUtils.isEmpty(validateSet)) {
            String messages = validateSet.stream()
                    .map(ConstraintViolation::getMessage)
                    .reduce((m1, m2) -> m1 + "；" + m2)
                    .orElse("参数输入有误！");
            throw new IllegalArgumentException(messages);

        }
    }
}
