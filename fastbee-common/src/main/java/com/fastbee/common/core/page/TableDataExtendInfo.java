package com.fastbee.common.core.page;

import java.io.Serializable;
import java.util.List;

/**
 * 表格分页数据对象
 * 可添加扩展参数
 *
 * @author ruoyi
 */
public class TableDataExtendInfo implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 总记录数 */
    private long total;

    /** 列表数据 */
    private List<?> rows;

    /** 消息状态码 */
    private int code;

    /** 消息内容 */
    private String msg;

    /**
     * 全部启用
     */
    private Integer allEnable;

    public Integer getAllEnable() {
        return allEnable;
    }

    public void setAllEnable(Integer allEnable) {
        this.allEnable = allEnable;
    }

    /**
     * 表格数据对象
     */
    public TableDataExtendInfo()
    {
    }

    /**
     * 分页
     *
     * @param list 列表数据
     * @param total 总记录数
     */
    public TableDataExtendInfo(List<?> list, int total)
    {
        this.rows = list;
        this.total = total;
    }

    public long getTotal()
    {
        return total;
    }

    public void setTotal(long total)
    {
        this.total = total;
    }

    public List<?> getRows()
    {
        return rows;
    }

    public void setRows(List<?> rows)
    {
        this.rows = rows;
    }

    public int getCode()
    {
        return code;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }
}
