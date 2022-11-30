package cn.stuxx.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 模块资源表(Module)实体类
 *
 * @author 秦笑笑
 * @since 2022-11-28 22:33:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Module implements Serializable {
    private static final long serialVersionUID = -41112329163988453L;
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 模块编号
     */
    private String code;
    /**
     * 模块名称
     */
    private String name;
}
