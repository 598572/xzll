package com.xzll.common.util.log;

import lombok.Data;

@Data
public class RpcTemplateconfiginfodetailAo {

    private String imgUrl;

    private String appJumpType;

    private String appDirect;

    private String wxJumpType;

    private String wxProgram;

    private String broadcastTimes;

    private String fontColor;

    private String copywriting;

    /**
     * 坑位配置的排序值(查询时默认正序排序，null值在最后)
     */
    private String sortNum;


}
