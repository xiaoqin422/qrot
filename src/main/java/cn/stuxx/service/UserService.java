package cn.stuxx.service;

import cn.stuxx.model.entity.User;
import cn.stuxx.utils.ValidationGroup;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

public interface UserService {
    @Validated({ValidationGroup.USER_INSERT.class})
    User insertUser(@Valid @NotNull(message = "用户信息缺失") User user);

    /**
     * 用户信息修改
     *
     * @param user 需要更改的用户信息
     * @return 修改操作结果
     */
    @Validated({ValidationGroup.USER_UPDATE.class})
    boolean updateUser(@Valid @NotNull(message = "用户信息缺失") User user);

    /**
     * 逻辑删除
     *
     * @param userID 用户ID
     * @return 修改结果
     */
    boolean deleteUser(@Valid @NotNull(message = "用户ID为空") Long userID);

    /**
     * 通过QQ修改禁用用户
     *
     * @param accountCode QQ号码
     * @return 修改操作结果
     */
    boolean disableUserByQQ(@Valid @NotNull(message = "QQ号码不能为空") String accountCode);

    /**
     * 通过QQ删除用户
     * @param accountCode QQ号码
     * @return 修改操作结果
     */
    boolean deleteUserByQQ(@Valid @NotNull(message = "QQ号码不能为空") String accountCode);

    boolean userHasPermission(@Valid @NotBlank(message = "权限CODE不能为空") String permissionCode, @NotNull(message = "用户ID不能为空") Long userId);
    List<User> selectHasPermissionsUser(@Valid @NotBlank(message = "权限CODE不能为空") String permissionCode);
}
