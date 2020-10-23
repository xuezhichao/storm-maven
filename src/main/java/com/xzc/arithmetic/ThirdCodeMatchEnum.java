package com.xzc.arithmetic;


/**
 * Description:业务三方码匹配三方标准码枚举
 * Date: 2019/5/17 17:45<br/>
 *
 * @author yanjinfei
 * @since JDK 1.7
 */
public enum ThirdCodeMatchEnum {

    CXZX_FACE("123", "456", "789", "", "全国公民身份信息中心-人脸比对"),
    ;

    /**
     * 业务三方码
     */
    private String taskCode;
    /**
     * 三方标准码
     */
    private String standardCode;
    /**
     * 三方码
     */
    private String sourceCode;
    /**
     * 数据源类型
     */
    private String type;
    /**
     * 枚举描述
     */
    private String msg;

    public String getTaskCode() {
        return taskCode;
    }

    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
    }

    public String getStandardCode() {
        return standardCode;
    }

    public void setStandardCode(String standardCode) {
        this.standardCode = standardCode;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    ThirdCodeMatchEnum(String taskCode, String standardCode, String sourceCode, String type, String msg) {
        this.taskCode = taskCode;
        this.standardCode = standardCode;
        this.sourceCode = sourceCode;
        this.type = type;
        this.msg = msg;
    }

    public static ThirdCodeMatchEnum judgeCode(String taskCode) {
        ThirdCodeMatchEnum[] ts = ThirdCodeMatchEnum.values();
        for (ThirdCodeMatchEnum status : ts) {
            if (status.getTaskCode().equals(taskCode)) {
                return status;
            }
        }
        return null;
    }

    public static ThirdCodeMatchEnum getEnum(String taskCode, String type) {
        ThirdCodeMatchEnum matchEnum = judgeCode(taskCode);
        if (null != matchEnum) {
            if (matchEnum.getType().equals(type)) {
                return matchEnum;
            }
        }
        return null;
    }

    public static ThirdCodeMatchEnum getEnumByName(String name) {
        ThirdCodeMatchEnum[] ts = ThirdCodeMatchEnum.values();
        for (ThirdCodeMatchEnum status : ts) {
            if (name.equals(status.name())) {
                return status;
            }
        }
        return null;
    }
}
