package cn.stuxx.dao;

import cn.stuxx.model.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户表(User)表数据库访问层
 *
 * @author 秦笑笑
 * @since 2022-11-30 12:30:09
 */
@Mapper
public interface UserDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    User queryById(Long id);

    /**
     * 查询指定行数据
     *
     * @param user 查询条件
     * @return 对象列表
     */
    List<User> queryAll(User user);

    /**
     * 根据用户ID和权限ID查询用户（判断用户是否有相关权限）
     * @param userId 用户ID
     * @param permissionIds 权限列表
     * @return 实例对象
     */
    User selectUserByIDANDPermissionId(@Param("userId") Long userId,@Param("permissionId") Long permissionId);

    /**
     * 查询拥有指定权限的用户列表
     * @param permissionIds 权限列表
     * @return 对象列表
     */
    List<User> selectUserByPermissionId(Long permissionId);
    /**
     * 统计总行数
     *
     * @param user 查询条件
     * @return 总行数
     */
    long count(User user);

    /**
     * 新增数据
     *
     * @param user 实例对象
     * @return 影响行数
     */
    int insert(User user);

    /**
     * 新增数据缺失字段赋默认值
     *
     * @param user 实例对象
     * @return 影响行数
     */
    int insertOnDefault(User user);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<User> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<User> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<User> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<User> entities);

    /**
     * 修改数据
     *
     * @param user 实例对象
     * @return 影响行数
     */
    int update(User user);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

}
