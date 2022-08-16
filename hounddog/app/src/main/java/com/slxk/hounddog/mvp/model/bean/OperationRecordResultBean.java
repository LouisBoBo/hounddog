package com.slxk.hounddog.mvp.model.bean;

import com.slxk.hounddog.mvp.model.entity.BaseBean;

import java.util.List;

/**
 * 获取操作记录数据结果
 */
public class OperationRecordResultBean extends BaseBean {

    /**
     * items : [{"account":"72ff2d16-10d1-4732-be63-b18265c71fc7","imei":619243578,"remark":"baf803e9-889e-4374-ba11-419091ce42cd","time":619243578},{"account":"d6e03aed-ddca-4b9c-a039-be7e88e88b7e","imei":619243578,"remark":"7dcd7032-75ae-41d1-a3ca-8fe8c0633b87","time":619243578},{"account":"ba6ab7dd-41c0-4456-826b-712cc393c116","imei":619243578,"remark":"cca70143-0c4f-4964-bf8b-556f37c69b27","time":619243578},{"account":"fe845400-cd30-4d2d-aae3-fa67d7945a3d","imei":619243578,"remark":"92bc03f8-1b36-4a28-a7ec-405dfd0ceb80","time":619243578}]
     * last_imei : 619243578
     * last_time : 619243578
     * errcode : 0
     */

    private long last_imei; // 再次请求时，请带上这个imei最为最后一次请求参数
    private long last_time; // 再次请求时，请带上这个时间戳最为最后一次请求参数
    private List<ItemsBean> items;

    public long getLast_imei() {
        return last_imei;
    }

    public void setLast_imei(long last_imei) {
        this.last_imei = last_imei;
    }

    public long getLast_time() {
        return last_time;
    }

    public void setLast_time(long last_time) {
        this.last_time = last_time;
    }

    public List<ItemsBean> getItems() {
        return items;
    }

    public void setItems(List<ItemsBean> items) {
        this.items = items;
    }

    public static class ItemsBean {
        /**
         * account : 72ff2d16-10d1-4732-be63-b18265c71fc7
         * imei : 619243578
         * remark : baf803e9-889e-4374-ba11-419091ce42cd
         * time : 619243578
         */

        private String account; // 操作账号
        private long imei; // 车载设备IMEI,仅用于展示
        private String remark; // 操作信息
        private long time; // 操作时间，精确到ms

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public long getImei() {
            return imei;
        }

        public void setImei(long imei) {
            this.imei = imei;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }
    }
}
