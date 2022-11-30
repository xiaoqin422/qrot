package cn.stuxx.model.eum;

public enum PERMISSION {
    ROOT(0,"ROOT"),
    USER_ADD(1,"USER:ADD"),
    USER_DELETE(2,"USER:DELETE"),
    USER_UPDATE(3,"USER:UPDATE"),
    USER_SELECT(4,"USER:SELECT"),
    TASK_HEALTH_ADD(5,"TASK_HEALTH:ADD"),
    TASK_HEALTH_DELETE(6,"TASK_HEALTH:DELETE"),
    TASK_HEALTH_UPDATE(7,"TASK_HEALTH:UPDATE"),
    TASK_HEALTH_SELECT(8,"TASK_HEALTH:SELECT"),
    TASK_HEALTH_LISTEN_ADD(9,"TASK_HEALTH_LISTEN:ADD"),
    TASK_HEALTH_LISTEN_DELETE(10,"TASK_HEALTH_LISTEN:DELETE"),
    TASK_HEALTH_LISTEN_UPDATE(11,"TASK_HEALTH_LISTEN:UPDATE"),
    TASK_HEALTH_LISTEN_SELECT(12,"TASK_HEALTH_LISTEN:SELECT"),
    TASK_HEALTH_TIMING(13,"TASK_HEALTH:TIMING"),
    TASK_HEALTH_LISTEN_TIMING(14,"TASK_HEALTH_LISTEN:TIMING"),
    ;
    private Integer ID;
    private String CODE;

    PERMISSION(Integer ID, String CODE) {
        this.ID = ID;
        this.CODE = CODE;
    }

    PERMISSION() {
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getCODE() {
        return CODE;
    }

    public void setCODE(String CODE) {
        this.CODE = CODE;
    }
}
