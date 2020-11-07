package life.majiang.community.enums;

public enum CommentTypeEnum {
    QUESTION(1),
    COMMENT(2);

    private Integer type;

    public static boolean exist(Integer type) {
        for (CommentTypeEnum typeEnum : CommentTypeEnum.values())
            if (typeEnum.getType() == type) return true;
        return false;
    }

    public Integer getType() {
        return type;
    }

    CommentTypeEnum(int type) {
        this.type = type;
    }
}
