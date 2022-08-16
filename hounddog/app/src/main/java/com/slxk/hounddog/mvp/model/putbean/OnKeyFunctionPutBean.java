package com.slxk.hounddog.mvp.model.putbean;

/**
 * 一键功能
 */
public class OnKeyFunctionPutBean {

    /**
     * module : device
     * func : Cmd
     * params : {"type":"e_cmd_sleep|e_cmd_wakeup|e_cmd_restart|e_cmd_reset|e_cmd_finddev|e_cmd_query_location","simei":"d26b0e34-3b75-4d9b-9fa7-8b6d6b5cdd4a"}
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
         * type : e_cmd_sleep|e_cmd_wakeup|e_cmd_restart|e_cmd_reset|e_cmd_finddev|e_cmd_query_location
         * simei : d26b0e34-3b75-4d9b-9fa7-8b6d6b5cdd4a
         */

        private String type; // 下发指令类型
        private String simei;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getSimei() {
            return simei;
        }

        public void setSimei(String simei) {
            this.simei = simei;
        }
    }
}
