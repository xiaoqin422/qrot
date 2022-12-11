package cn.stuxx.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 角色表(Role)实体类
 *
 * @author 秦笑笑
 * @since 2022-11-28 22:33:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role implements Serializable {
    private static final long serialVersionUID = -82049664161134909L;
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 角色名称
     */
    private String name;
    /**
     * 权限集合
     */
    private String permissions;
    /**
     * 描述
     */
    private String desc;
    /**
     * 系统角色内置权限
     */
    private String defaultPermissions;
    /**
     * 是否为系统内置角色
     */
    private Integer systemic;
}
