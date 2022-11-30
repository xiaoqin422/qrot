package cn.stuxx.service.impl;

import cn.hutool.core.util.ReUtil;
import cn.stuxx.dao.PermissionDao;
import cn.stuxx.dao.UserDao;
import cn.stuxx.exception.ErrorMessage;
import cn.stuxx.exception.QRotException;
import cn.stuxx.model.entity.User;
import cn.stuxx.service.UserService;
import cn.stuxx.utils.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;


import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@Validated
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private PermissionDao permissionDao;

    @Override
    public User insertUser(User user) {
        User searchParam = new User();
        String username = user.getUsername();
        String qCode = user.getQCode();
        searchParam.setUsername(username);
        List<User> list = userDao.queryAll(searchParam);
        if (list.size() != 0) {
            User item = list.get(0);
            //如果用户被删除
            if (item.getIsDelete() == 1) {
                BeanUtils.copyProperties(user, item);
                item.setIsDelete(0);
                userDao.update(item);
                return item;
            }
            throw new QRotException(ErrorMessage.USERNAME_IS_EXIST.getMessage(), ErrorMessage.USERNAME_IS_EXIST.getCode());
        }
        //QQ号不为空的话需要单独校验
        if (Strings.isNotBlank(qCode) && !ReUtil.isMatch(Constant.Regex.ACCOUNT_CODE, qCode)) {
            throw new QRotException(ErrorMessage.ACCOUNT_CODE_ERROR.getMessage(), ErrorMessage.ACCOUNT_CODE_ERROR.getCode());
        }
        searchParam = new User();
        searchParam.setQCode(qCode);
        searchParam.setIsDisable(0);
        list = userDao.queryAll(searchParam);
        if (list.size() != 0) {
            User item = list.get(0);
            //如果用户被删除
            if (item.getIsDelete() == 1) {
                BeanUtils.copyProperties(user, item);
                item.setIsDelete(0);
                userDao.update(item);
                return item;
            }
            throw new QRotException(ErrorMessage.ACCOUNT_CODE_IS_EXIST.getMessage(), ErrorMessage.ACCOUNT_CODE_IS_EXIST.getCode());
        }
        try {
            log.info("插入用户{}", user);
            userDao.insert(user);
        } catch (DuplicateKeyException e) {
            log.error("用户{}插入失败。ErrorMsg{}", user, e.getMessage());
            throw new QRotException(ErrorMessage.USER_IS_EXIST.getMessage(), ErrorMessage.USERNAME_IS_EXIST.getCode());
        }
        return user;
    }

    @Override
    public boolean updateUser(User user) {
        return userDao.update(user) == 1;
    }

    /**
     * 逻辑删除
     *
     * @param userID 用户ID
     * @return 修改结果
     */
    @Override
    public boolean deleteUser(Long userID) {
        return userDao.deleteById(userID) == 1;
    }

    /**
     * 通过QQ修改禁用用户
     *
     * @param accountCode QQ号码
     * @return 修改操作结果
     */
    @Override
    public boolean disableUserByQQ(String accountCode) {
        User search = new User();
        search.setQCode(accountCode);
        search.setIsDisable(0);
        search.setIsDisable(0);
        List<User> resList = userDao.queryAll(search);
        if (resList.size() == 0) {
            throw new QRotException(ErrorMessage.ACCOUNT_CODE_NOT_EXIST.getMessage(), ErrorMessage.ACCOUNT_CODE_NOT_EXIST.getCode());
        }
        User user = resList.get(0);
        user.setIsDisable(1);
        return userDao.update(user) == 1;
    }

    /**
     * 通过QQ删除用户
     *
     * @param accountCode QQ号码
     * @return 修改操作结果
     */
    @Override
    public boolean deleteUserByQQ(String accountCode) {
        User search = new User();
        search.setQCode(accountCode);
        search.setIsDisable(0);
        search.setIsDisable(0);
        List<User> resList = userDao.queryAll(search);
        if (resList.size() == 0) {
            throw new QRotException(ErrorMessage.ACCOUNT_CODE_NOT_EXIST.getMessage(), ErrorMessage.ACCOUNT_CODE_NOT_EXIST.getCode());
        }
        User user = resList.get(0);
        user.setIsDelete(0);
        return userDao.update(user) == 1;
    }

    @Override
    public boolean userHasPermission(String permissionCode, Long userId) {
        Long permissionId = permissionDao.selectIDByCode(permissionCode);
        return userDao.selectUserByIDANDPermissionId(userId,permissionId) != null;
    }

    @Override
    public List<User> selectHasPermissionsUser(String permissionCode) {
        Long permissionId = permissionDao.selectIDByCode(permissionCode);
        return userDao.selectUserByPermissionId(permissionId);
    }
}
