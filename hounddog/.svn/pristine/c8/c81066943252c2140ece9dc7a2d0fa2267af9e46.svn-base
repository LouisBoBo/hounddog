package com.slxk.hounddog.mvp.model.bean;

public class BluetoothUpgradeVersionBean {


    /**
     * errcode : 0
     * data : {"flag":2,"task_id":330,"src_version":"all","dst_version":"M31_V0.3","upgrade_type":0,"url":"http://118.190.58.226/full_file/M31/FULL_M31_V0.3.bin","checkcode":"85aaabf44ee8d4f746c0c73c179ec300"}
     * msg : OK
     * success : true
     */

    private int errcode;
    private DataBean data;
    private String msg;
    private boolean success;

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public static class DataBean {
        /**
         * flag : 2
         * task_id : 330
         * src_version : all
         * dst_version : M31_V0.3
         * upgrade_type : 0
         * url : http://118.190.58.226/full_file/M31/FULL_M31_V0.3.bin
         * checkcode : 85aaabf44ee8d4f746c0c73c179ec300
         */

        private int flag; //flag  0 无升级 1 有升级，但需排队 2 有升级
        private int task_id;
        private String src_version;
        private String dst_version;
        private int upgrade_type;
        private String url;
        private String checkcode;

        public int getFlag() {
            return flag;
        }

        public void setFlag(int flag) {
            this.flag = flag;
        }

        public int getTask_id() {
            return task_id;
        }

        public void setTask_id(int task_id) {
            this.task_id = task_id;
        }

        public String getSrc_version() {
            return src_version;
        }

        public void setSrc_version(String src_version) {
            this.src_version = src_version;
        }

        public String getDst_version() {
            return dst_version;
        }

        public void setDst_version(String dst_version) {
            this.dst_version = dst_version;
        }

        public int getUpgrade_type() {
            return upgrade_type;
        }

        public void setUpgrade_type(int upgrade_type) {
            this.upgrade_type = upgrade_type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getCheckcode() {
            return checkcode;
        }

        public void setCheckcode(String checkcode) {
            this.checkcode = checkcode;
        }
    }
}
