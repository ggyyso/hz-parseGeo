package com.model.enums;

/**
 * description: 任务状态类型 <br>
 * date: 2021/2/27 14:33 <br>
 * version: 1.0 <br>
 *
 * @author: zhangzhe <br>
 */
public enum JobStatus {
    SUBMIT(0, "已提交"),
    DUCCESS(1, "已完成"),
    FAILED(2, "已失败");

    private int code;
    private String info;

    private JobStatus(int code, String info) {
        this.code = code;
        this.info = info;
    }

    public int code() {
        return code;
    }

    public String info() {
        return info;
    }

    public static JobStatus typeOf(int type) {
        for (JobStatus t : values()) {
            if(t.code() == type) {
                return t;
            }
        }
        return null;
    }
}
