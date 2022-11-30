package cn.stuxx.model.entity;

import java.util.Date;

import cn.hutool.core.util.IdUtil;
import cn.stuxx.utils.Constant;
import cn.stuxx.utils.ValidationGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 用户表(User)实体类
 *
 * @author 秦笑笑
 * @since 2022-11-29 23:58:49
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    private static final long serialVersionUID = -91456145742793783L;
    /**
     * 主键ID
     */
    @NotNull(message = "用户ID不能为空",
            groups = {ValidationGroup.USER_UPDATE.class})
    private Long id;
    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空", groups = {ValidationGroup.USER_INSERT.class})
    @Length(message = "用户名最长为20个字符", max = 20,
            groups = {ValidationGroup.USER_INSERT.class,ValidationGroup.USER_UPDATE.class})
    private String username;
    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空",
            groups = {ValidationGroup.USER_INSERT.class,ValidationGroup.USER_UPDATE.class})
    @Length(message = "密码最少为8个字符，最长为30个字符", min = 8, max = 20,
            groups = {ValidationGroup.USER_INSERT.class,ValidationGroup.USER_UPDATE.class})
    private String password;


    /**
     * 性别 0未知 1女 2男
     */
    private Integer sex;
    /**
     * 姓名
     */
    private String name;
    /**
     * QQ号码
     */
    private String qCode;
    /**
     * 邮箱
     */
    @Email(message = "邮箱格式错误")
    private String email;

    private String phone;
    /**
     * 角色集合
     */
    private String roles;
    /**
     * 是否被禁用 1禁用
     */
    private Integer isDisable;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 是否删除 1删除
     */
    private Integer isDelete;

    public static User generateUser(String accountCode) {
        User user = new User();
        user.setUsername(IdUtil.getSnowflakeNextIdStr());
        user.setQCode(accountCode);
        user.setPassword(accountCode);
        user.setEmail(accountCode + "@qq.com");
        user.setRoles("[1]");
        return user;
    }
}
