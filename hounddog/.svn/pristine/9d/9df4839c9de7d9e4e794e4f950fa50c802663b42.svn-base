package com.slxk.hounddog.mvp.model.putbean;

/**
 * 设备分组或账号组织列表
 */
public class DeviceGroupPutBean {

    /**
     * module : family
     * func : GetFamilyList
     * params : {"last_gid":"8c7b69d0-49be-4a12-9153-3d920c408c62","last_fid":"60c1b52f-ceeb-44aa-9fd7-9362084039c7","g_limit_size":2036617846,"familyid":"eb787b5d-890f-4903-a31e-fc271b15ce58","f_limit_size":2036617846}
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
         * last_gid : 8c7b69d0-49be-4a12-9153-3d920c408c62
         * last_fid : 60c1b52f-ceeb-44aa-9fd7-9362084039c7
         * g_limit_size : 2036617846
         * familyid : eb787b5d-890f-4903-a31e-fc271b15ce58
         * f_limit_size : 2036617846
         */

        // 获取分组
        private String last_gid; // 最后获取到的gid，没有可以为空
        private Integer g_limit_size; // 限制分组获取数量
        // 获取组织
        private String last_fid; // 最后获取到的uid，没有可以为空
        private String familyid; // 用户组织id,为空表示最上一级
        private Integer f_limit_size; // 限制组织获取数量
        private Boolean get_all; // 返回当前组织的全部设备总数，默认false，不返回

        public Boolean getGet_all() {
            return get_all;
        }

        public void setGet_all(Boolean get_all) {
            this.get_all = get_all;
        }

        public String getLast_gid() {
            return last_gid;
        }

        public void setLast_gid(String last_gid) {
            this.last_gid = last_gid;
        }

        public String getLast_fid() {
            return last_fid;
        }

        public void setLast_fid(String last_fid) {
            this.last_fid = last_fid;
        }

        public Integer getG_limit_size() {
            return g_limit_size;
        }

        public void setG_limit_size(Integer g_limit_size) {
            this.g_limit_size = g_limit_size;
        }

        public String getFamilyid() {
            return familyid;
        }

        public void setFamilyid(String familyid) {
            this.familyid = familyid;
        }

        public Integer getF_limit_size() {
            return f_limit_size;
        }

        public void setF_limit_size(Integer f_limit_size) {
            this.f_limit_size = f_limit_size;
        }
    }
}
