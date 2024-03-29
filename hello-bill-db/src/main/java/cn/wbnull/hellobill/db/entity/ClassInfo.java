package cn.wbnull.hellobill.db.entity;

import cn.wbnull.hellobill.common.constant.TypeEnum;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author dukunbiao(null)
 * @since 2020-12-31
 */
public class ClassInfo extends Model<ClassInfo> {

    private static final long serialVersionUID = 1L;

    @TableId
    private String uuid;

    @TableField("topClass")
    private String topClass;

    @TableField("secondClass")
    private String secondClass;

    private String type;

    @TableField("serialNo")
    private Integer serialNo;

    private String status;

    public void analyseInfo() {
        this.type = TypeEnum.getTypeEnum(this.type).getTypeName();
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTopClass() {
        return topClass;
    }

    public void setTopClass(String topClass) {
        this.topClass = topClass;
    }

    public String getSecondClass() {
        return secondClass;
    }

    public void setSecondClass(String secondClass) {
        this.secondClass = secondClass;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(Integer serialNo) {
        this.serialNo = serialNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
