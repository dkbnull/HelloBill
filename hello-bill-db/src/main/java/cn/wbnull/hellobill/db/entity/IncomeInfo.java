package cn.wbnull.hellobill.db.entity;

import cn.wbnull.hellobill.common.util.DateUtils;
import cn.wbnull.hellobill.common.util.SnowflakeUtils;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author null
 * @since 2024-11-22
 */
@Getter
@Setter
@TableName("income_info")
public class IncomeInfo {

    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    @TableField("username")
    private String username;

    @TableField("incomeDate")
    private LocalDate incomeDate;

    @TableField("topClass")
    private String topClass;

    @TableField("secondClass")
    private String secondClass;

    @TableField("detail")
    private String detail;

    @TableField("amount")
    private BigDecimal amount;

    @TableField("remark")
    private String remark;

    @TableField("createTime")
    private LocalDateTime createTime;

    @TableField("updateTime")
    private LocalDateTime updateTime;

    public void build(String username, String topClass) {
        LocalDateTime localDateTime = this.incomeDate.atStartOfDay();
        long epochMilli = DateUtils.toEpochMilli(localDateTime);

        this.id = SnowflakeUtils.getInstance().nextId(epochMilli);
        this.username = username;
        this.topClass = topClass;
    }
}
