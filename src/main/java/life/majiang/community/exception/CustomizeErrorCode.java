package life.majiang.community.exception;

public enum CustomizeErrorCode {
    QUESTION_NOT_FOUND(2002,"你找的问题不见了，要不换个试试?"),
    TARGET_PARAM_NOT_FOUND(2001, "未选择任何问题或评论"),
    NO_LOGIN(2000,"宁还没有登录的，请先登录"),
    SYSTEM_ERR(2003, "服务冒烟了，要不然你稍后再试试!!!"),
    TYPE_PARAM_WRONG(2004, "评论或回复类型错误"),
    COMMENT_NOT_FOUND(2005, "你找的评论不存在"),
    COMMENT_IS_EMPTY(2006, "输入内容不能为空"),
    COMMENT_TOO_LONG(2007, "输入内容过长");

    private Integer errCode;
    private String errMessage;

    public Integer getErrCode() {
        return errCode;
    }

    public String getErrMessage() {
        return errMessage;
    }

    CustomizeErrorCode(Integer errCode, String errMessage) {
        this.errCode = errCode;
        this.errMessage = errMessage;
    }
}
