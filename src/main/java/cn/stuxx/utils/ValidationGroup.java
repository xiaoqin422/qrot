package cn.stuxx.utils;

import javax.validation.groups.Default;

public interface ValidationGroup {
    interface QQ_OPTIONS extends Default{}
    interface UID_OPTIONS extends Default{}
    interface TASK_HEALTH_INSERT extends Default {}
    interface TASK_HEALTH_ID extends Default {}
    interface TASK_HEALTH_UPDATE extends Default {}
    interface TASK_HEALTH_DO extends Default {}
    interface USER_INSERT extends Default{}
    interface USER_UPDATE extends Default{}
}
