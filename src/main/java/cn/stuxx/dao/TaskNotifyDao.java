package cn.stuxx.dao;

import cn.stuxx.model.entity.TaskNotify;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 消息提醒表(TaskNotify)表数据库访问层
 *
 * @author 秦笑笑
 * @since 2022-12-09 00:31:04
 */
@Mapper
public interface TaskNotifyDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TaskNotify queryById(Long id);
    TaskNotify selectTimingTaskById(String taskId);
    /**
     * 查询指定行数据
     *
     * @param taskNotify 查询条件
     * @return 对象列表
     */
    List<TaskNotify> queryAll(TaskNotify taskNotify);

    /**
     * 统计总行数
     *
     * @param taskNotify 查询条件
     * @return 总行数
     */
    long count(TaskNotify taskNotify);

    /**
     * 新增数据
     *
     * @param taskNotify 实例对象
     * @return 影响行数
     */
    int insert(TaskNotify taskNotify);

    /**
     * 新增数据缺失字段赋默认值
     *
     * @param taskNotify 实例对象
     * @return 影响行数
     */
    int insertOnDefault(TaskNotify taskNotify);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<TaskNotify> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<TaskNotify> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<TaskNotify> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<TaskNotify> entities);

    /**
     * 修改数据
     *
     * @param taskNotify 实例对象
     * @return 影响行数
     */
    int update(TaskNotify taskNotify);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

}
