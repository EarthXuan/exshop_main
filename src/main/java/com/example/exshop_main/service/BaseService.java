package com.example.exshop_main.service;

public interface BaseService<T> {
    /**
     * 新增
     * @param t
     * @return
     */
    int save(T t);
    /**
     * 通过id删除
     * @param id
     * @return
     */
    int deleteByPrimaryKey(int id);
    /**
     * 选择更新
     * @param t
     * @return
     */
    int updateByPrimaryKeySelective(T t);
    /**
     * 更新
     * @param t
     * @return
     */
    int updateByPrimaryKey(T t);
    /**
     * 通过id查找
     * @param id
     * @return
     */
    T selectByPrimaryKey(Integer id);

}
