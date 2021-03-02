package com.tal.wangxiao.conan.common.converter;

import com.google.common.collect.Lists;
import com.tal.wangxiao.conan.common.model.vo.PageVO;
import org.springframework.data.domain.Page;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * DO转VO抽象类，完成分页对象的转换
 * @author huyaoguo
 * @date 2021/1/25
 */
public class ConvertUtil {
    /**
     * page转换为pageVO
     *
     * @param page
     * @param voClass
     * @param converter
     * @param <V>
     * @param <D>
     * @return
     * @throws Exception
     */
    public static <V, D> PageVO<V> convert2PageVO(Page<D> page, Class voClass, AbstractObjectConverter converter) {
        PageVO<V> pageVO = new PageVO<>();
        //JPA分页是从0计数的，为了直接转换为从1计数
        pageVO.setPageNum(page.getNumber() + 1);
        pageVO.setPageSize(page.getSize());
        pageVO.setTotal(page.getTotalElements());
        pageVO.setPages(page.getTotalPages());
        pageVO.setData(convert2List(page.getContent(), voClass, converter));
        return pageVO;
    }

    /**
     * doList转换为voList
     *
     * @param dList
     * @param voClass
     * @param converter
     * @param <V>
     * @param <D>
     * @return
     * @throws Exception
     */
    public static <V, D> List<V> convert2List(List<D> dList, Class voClass, AbstractObjectConverter converter){
        List<V> vList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(dList)) {
            return vList;
        }

        for (D d : dList) {
            try {
                V v = (V) Class.forName(voClass.getCanonicalName()).newInstance();
                vList.add(v);
                converter.convert(d, v);
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return vList;
    }
}
