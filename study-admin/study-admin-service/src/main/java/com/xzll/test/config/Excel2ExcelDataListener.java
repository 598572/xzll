package com.xzll.test.config;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.xzll.test.entity.ExcelSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

// 有个很重要的点 TListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
public class Excel2ExcelDataListener extends AnalysisEventListener<ExcelSource> {
    private static final Logger LOGGER = LoggerFactory.getLogger(Excel2ExcelDataListener.class);
    /**
     * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    private List<ExcelSource> list = new ArrayList<>();

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        LOGGER.info("所有数据解析完成！");
    }

    /**
     * 这个每一条数据解析都会来调用
     *
     * @param data    one row value. Is is same as {@link AnalysisContext#readRowHolder()}
     * @param context
     */
    @Override
    public void invoke(ExcelSource data, AnalysisContext context) {
        LOGGER.info("解析到一条数据:{}", JSON.toJSONString(data));
        list.add(data);
    }

    public List<ExcelSource> getList() {
        return list;
    }

}
