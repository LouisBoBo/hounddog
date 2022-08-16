package com.slxk.hounddog.mvp.model.putbean;

import java.util.List;

/**
 * 移除设备
 */
public class RemoveDevicePutBean {

    /**
     * module : device
     * func : DelToParent
     * params : {"del_type":"e_del_no_move|e_del_to_parent|e_del_to_myself|e_del_to_root","is_del_gid":false,"is_recycle":false,"sfamilyid":"f5f5bdd3-0d2d-4920-a415-9ef065760af8","sgid":"85251890-ceb3-428e-86a9-df337467b3d2","simei":["11b0594e-664c-4d0a-be7d-307e1b61d039","299d11ab-c7cf-440b-8657-e906a36c4442"]}
     */

    private String module;
    private String func;
    private ParamsBean params;

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getFunc() {
        return func;
    }

    public void setFunc(String func) {
        this.func = func;
    }

    public ParamsBean getParams() {
        return params;
    }

    public void setParams(ParamsBean params) {
        this.params = params;
    }

    public static class ParamsBean {
        /**
         * del_type : e_del_no_move|e_del_to_parent|e_del_to_myself|e_del_to_root
         * is_del_gid : false
         * is_recycle : false
         * sfamilyid : f5f5bdd3-0d2d-4920-a415-9ef065760af8
         * sgid : 85251890-ceb3-428e-86a9-df337467b3d2
         * simei : ["11b0594e-664c-4d0a-be7d-307e1b61d039","299d11ab-c7cf-440b-8657-e906a36c4442"]
         */

        // 删除类型,e_del_no_move 清除设备，但是不移动,e_del_to_parent 移动到设备的上一级组织中,将移入新建的分组中,
        // e_del_to_myself 清除设备，并回到当前操作账号的组织下,将移入新建的分组中,e_del_to_root 清除设备，并回到入库状态
        private String del_type;
        private Boolean is_del_gid; // 如果用sgid删除设备，如果这里填写true，则最后会把gid也删除，默认为false，不删除gid
        private Boolean is_recycle; // 如果是回收站的设备用sfamilyid或者sgid删除，这里需要设备为true，默认为false
        private String sfamilyid; // 需要删除指定车组织下面全部设备，simei,sfamilyid,sgid三选一
        private String sgid; // 需要删除指定分组下面全部设备，simei,sfamilyid,sgid三选一
        private List<String> simei; // 设备号,限制数量1000，simei,sfamilyid,sgid三选一

        public String getDel_type() {
            return del_type;
        }

        public void setDel_type(String del_type) {
            this.del_type = del_type;
        }

        public Boolean getIs_del_gid() {
            return is_del_gid;
        }

        public void setIs_del_gid(Boolean is_del_gid) {
            this.is_del_gid = is_del_gid;
        }

        public Boolean getIs_recycle() {
            return is_recycle;
        }

        public void setIs_recycle(Boolean is_recycle) {
            this.is_recycle = is_recycle;
        }

        public String getSfamilyid() {
            return sfamilyid;
        }

        public void setSfamilyid(String sfamilyid) {
            this.sfamilyid = sfamilyid;
        }

        public String getSgid() {
            return sgid;
        }

        public void setSgid(String sgid) {
            this.sgid = sgid;
        }

        public List<String> getSimei() {
            return simei;
        }

        public void setSimei(List<String> simei) {
            this.simei = simei;
        }
    }
}
