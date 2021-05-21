package com.common.enums;

/**
 * description: 通用用户类型 <br>
 * date: 2019/2/22 9:40 <br>
 * version: 1.0 <br>
 *
 * @author: zhangzhe <br>
 */
public enum ResultCode {
    /**
     * 操作成功
     */
    SUCCESS(1, "操作成功"),

    /**
     * 操作失败
     */
    FAILURE(0, "操作失败"),

    /**
     * 未知错误
     */
    UNKNOWN_ERROR(999, "程序错误"),

    /**
     * 验证参数错误
     */
    PARAM_ERROR(901, "验证参数错误"),

    /**
     * 数据删除错误
     */
    DELETE_ERROR(902, "数据删除错误"),

    /**
     * 数据转换错误
     */
    CONVERT_ERROR(903, "数据转换错误"),

    /**
     * 数据操作错误
     */
    DATA_ERROR(904, "数据操作错误"),

    /**
     * 数据不存在错误
     */
    ENTITY_ERROR(905, "数据不存在错误"),

    /**
     * 文件操作错误
     */
    FILE_ERROR(906, "文件操作错误"),

    /**
     * 状态转换错误
     */
    STATE_ERROR(907, "状态转换错误"),

    /**
     * 发送用户激活邮件错误
     */
    SENDEMAIL_ERROR(908, "发送用户激活邮件错误，请联系管理员"),

    /**
     * 用户错误
     */
    ACTIVEUSER_ERROR(909, "注册用户错误，请联系管理员");

    private int code;
    private String info;

    private ResultCode(int code, String info) {
        this.code = code;
        this.info = info;
    }

    public String info() {
        return this.info;
    }

    public int code() {
        return this.code;
    }

    public static ResultCode codeOf(int code) {
        ResultCode[] var1 = values();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            ResultCode c = var1[var3];
            if (c.code() == code) {
                return c;
            }
        }

        return null;
    }
}
