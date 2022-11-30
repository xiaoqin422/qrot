package cn.stuxx.dao;

import cn.stuxx.model.entity.Module;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 模块资源表(Module)表数据库访问层
 *
 * @author 秦笑笑
 * @since 2022-11-28 22:33:48
 */
@Mapper
public interface ModuleDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Module queryById(Long id);

    /**
     * 查询指定行数据
     *
     * @param module 查询条件
     * @return 对象列表
     */
    List<Module> queryAll(Module module);

    /**
     * 统计总行数
     *
     * @param module 查询条件
     * @return 总行数
     */
    long count(Module module);

    /**
     * 新增数据
     *
     * @param module 实例对象
     * @return 影响行数
     */
    int insert(Module module);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<Module> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<Module> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<Module> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<Module> entities);

    /**
     * 修改数据
     *
     * @param module 实例对象
     * @return 影响行数
     */
    int update(Module module);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

}
