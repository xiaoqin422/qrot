package cn.stuxx.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 权限表(Permission)实体类
 *
 * @author 秦笑笑
 * @since 2022-11-28 22:33:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Permission implements Serializable {
    private static final long serialVersionUID = -31699398662862018L;
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 权限CODE
     */
    private String code;
    /**
     * 权限名称
     */
    private String name;
    /**
     * 模块ID
     */
    private Long moduleId;
}
