package com.common.consts.cache;

/**
 * description: 缓存key常量 <br>
 * date: 2019/2/13 17:15 <br>
 * version: 1.0 <br>
 *
 * @author: zhangzhe <br>
 */
public final class CacheKey {
    /**
     * 四局一院下的所有用户
     * 本key在用户增删改时，需要清空
     */
    public static final String MAP_UNIT_USERS  = "map_unit_users";

    /**
     * 四局一院下的所有用户
     * 本key在用户增删改时，需要清空
     */
    public static final String MAP_UNIT_USERS2  = "map_unit_users2";

    /**
     * 所有单位下的所有用户
     * 本key在用户增删改时，需要清空
     */
    public static final String MAP_ALL_UNIT_USERS  = "map_all_unit_users";

    /**
     * 单位包含的人员统计（包含参与项目的人员统计）
     */
    public static final String STATIS_UNIT_USER  = "statis_unit_user";

    /**
     * 按测区统计各单位上传天数
     */
    public static final String STATIS_UNIT_GPS_DAY  = "statis_unit_gps_day";

    /**
     * 按测区统计各单位上传GPS个数
     */
    public static final String STATIS_UNIT_GPS  = "statis_unit_gps";

    /**
     * 在线离线人数统计
     */
    public static final String STATIS_ON_OFF_USER  = "statis_on_off_user";

    /**
     * 综合表格
     */
    public static final String STATIS_COMP_TABLE  = "statis_comp_table";

    /**
     * 按测区统计上传GPS点的人数
     */
    public static final String STATIS_DISTRICT_USER  = "statis_district_user";

    /**
     * 按测区统计各单位上传天数
     */
    public static final String STATIS_DISTRICT_GPS_DAY  = "statis_district_gps_day";

    /**
     * 按测区统计各单位上传GPS个数
     */
    public static final String STATIS_DISTRICT_GPS  = "statis_district_gps";

    /**
     * 所有人员的各自最后N个GPS点
     */
    public static final String STATIS_MAP_GPS  = "statis_map_gps";

    /**
     * 所有人员的各自最后N个GPS点，按人分组
     */
    public static final String STATIS_MAP_GPS_BYUSER  = "statis_map_gps_byuser";

    /**
     * 统计预警的次数(合计)
     */
    public static final String STATIS_WARN_TIME  = "statis_warn_time";

    /**
     * 统计预警的人数(合计)
     */
    public static final String STATIS_WARN_USER  = "statis_warn_user";

    /**
     * 统计预警的次(人)数(按单位统计)
     */
    public static final String STATIS_WARN_BYUNIT  = "statis_warn_byunit";
}