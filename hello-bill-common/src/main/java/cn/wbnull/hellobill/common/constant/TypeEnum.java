package cn.wbnull.hellobill.common.constant;

/**
 * Type枚举
 *
 * @author dukunbiao(null)  2022-01-04
 */
public enum TypeEnum {

    EXPEND("0", "支出"),
    INCOME("1", "收入"),
    OTHER("", "其他");

    private final String typeCode;
    private final String typeName;

    TypeEnum(String typeCode, String typeName) {
        this.typeCode = typeCode;
        this.typeName = typeName;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public String getTypeName() {
        return typeName;
    }

    public static TypeEnum getTypeEnum(String typeCode) {
        for (TypeEnum typeEnum : TypeEnum.values()) {
            if (typeEnum.typeCode.equals(typeCode)) {
                return typeEnum;
            }
        }

        return OTHER;
    }
}
