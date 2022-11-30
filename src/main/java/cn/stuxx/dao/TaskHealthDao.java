package cn.stuxx.dao;

import cn.stuxx.model.entity.TaskHealth;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 体温打卡表(TaskHealth)表数据库访问层
 *
 * @author 秦笑笑
 * @since 2022-11-29 00:48:32
 */
@Mapper
public interface TaskHealthDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TaskHealth queryById(Long id);
    List<TaskHealth> selectTimingTask();
    TaskHealth selectTaskByUserId(Long userId);
    /**
     * 根据QQ号查询打卡任务
     * @param accountCode QQ号码
     * @return 实例对象
     */
    TaskHealth selectTaskByQCode(String accountCode);
    /**
     * 查询指定行数据
     *
     * @param taskHealth 查询条件
     * @return 对象列表
     */
    List<TaskHealth> queryAll(TaskHealth taskHealth);

    /**
     * 统计总行数
     *
     * @param taskHealth 查询条件
     * @return 总行数
     */
    long count(TaskHealth taskHealth);

    /**
     * 新增数据
     *
     * @param taskHealth 实例对象
     * @return 影响行数
     */
    int insert(TaskHealth taskHealth);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<TaskHealth> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<TaskHealth> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<TaskHealth> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<TaskHealth> entities);

    /**
     * 修改数据
     *
     * @param taskHealth 实例对象
     * @return 影响行数
     */
    int update(TaskHealth taskHealth);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

}
