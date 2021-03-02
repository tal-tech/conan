package com.tal.wangxiao.conan.common.repository.db;

import com.tal.wangxiao.conan.common.entity.db.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * 部门DAO
 */
public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    /**
     * 根据名称查找
     * @param name
     * @return
     */
    Optional<Department> findByDeptName(String name);

    /**
     * 根据部门ID获取该部门下的所有子部门列表
     * @param deptId
     * @return
     */
    @Query(value = "SELECT * from sys_dept where  find_in_set( :deptId, ancestors ) ",nativeQuery = true)
    List<Department> containsDepartmentByDeptId(Integer deptId);

    List<Department> findByParentId(Integer parentId);

    Optional<Department> findFirstByParentId(Integer parentId);

}
