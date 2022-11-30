package cn.stuxx.exception;

import cn.stuxx.model.base.http.HttpBaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.beanvalidation.MethodValidationInterceptor;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;

@RestControllerAdvice
@Slf4j
public class ExceptionControllerAdvice {
    /**
     * 对方法参数校验异常处理方法(仅对于表单提交有效，对于以json格式提交将会失效) 如果是表单类型的提交，则spring会采用表单数据的处理类进行处理（进行参数校验错误时会抛出BindException异常）
     * 这个的触发条件写法是这样，
     * 1.public void postUser(@Valid @RequestBody User user, BindingResult bindingResult)，跟我们这种类似，会返回bindException,这个时候客户端调用我们的是定义好的BaseRequest结构的json数据
     * 2.public void postUser(@Valid User user, BindingResult bindingResult) ，这个时候客户端是form表单，也就是name=123&password=456&age=234这种格式
     * 3.我们例子里没有用BindException，即绑定Exception,所以抛出异常是下面这个handlerArgumentNotValidException
     */
    @ExceptionHandler(BindException.class)
    public HttpBaseResponse handlerBindException(BindException exception) {
        return handlerNotValidException(exception);
    }

    /**
     * 对方法参数校验异常处理方法(前端提交的方式为json格式出现异常时会被该异常类处理)
     * json格式提交时，spring会采用json数据的数据转换器进行处理（进行参数校验时错误是抛出MethodArgumentNotValidException异常）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public HttpBaseResponse handlerArgumentNotValidException(MethodArgumentNotValidException
                                                                     exception) {
        return handlerNotValidException(exception);
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.OK)
    public HttpBaseResponse exception(Throwable throwable) {
        log.error("系统异常", throwable);
        return HttpBaseResponse.error(ErrorMessage.SYSTEM_ERROR, new Date());
    }

    @ExceptionHandler(QRotException.class)
    @ResponseStatus(HttpStatus.OK)
    public HttpBaseResponse serviceException(Exception e) {
        QRotException exception = (QRotException) e;
        log.error("执行异常。err：={}", exception.toString());
        return HttpBaseResponse.error(exception.getCode(), exception.getMsg());
    }

    public HttpBaseResponse handlerNotValidException(Exception e) {
        log.debug("begin resolve argument exception");
        BindingResult result;
        if (e instanceof BindException) {
            BindException exception = (BindException) e;
            result = exception.getBindingResult();
        } else {
            MethodArgumentNotValidException exception = (MethodArgumentNotValidException) e;
            result = exception.getBindingResult();
        }

        Map<String, Object> maps;
        if (result.hasErrors()) {
            List<FieldError> fieldErrors = result.getFieldErrors();
            maps = new HashMap<>(fieldErrors.size());
            fieldErrors.forEach(error -> {
                maps.put(error.getField(), error.getDefaultMessage());
            });
        } else {
            maps = Collections.EMPTY_MAP;
        }
        //此处的错误码应该定义各枚举类，错误码都在枚举文件中，方便后续统一处理，此处只是演示，就忽略了
        return HttpBaseResponse.error(ErrorMessage.PARAM_BIND_FAILED.getCode(), ErrorMessage.PARAM_BIND_FAILED.getMessage(), maps);

    }
}