package com.slxk.hounddog.mvp.model.bean;

import com.slxk.hounddog.mvp.model.entity.BaseBean;

import java.util.List;

/**
 * 手机国际区号结果
 */
public class PhoneAreaResultBean extends BaseBean {

    /**
     * items : [{"country":"42fcff49-ebff-4640-b60d-e364265edd94","zone":950025856},{"country":"56e65d61-2d83-4472-8ba1-4b0de61e56ef","zone":950025856},{"country":"c00400be-0896-42c3-99da-2b33e1a6d20f","zone":950025856},{"country":"f4bc5c90-995c-4c2b-9bbc-3e4c137f6e71","zone":950025856}]
     * errcode : 0
     */

    private List<ItemsBean> items;

    public List<ItemsBean> getItems() {
        return items;
    }

    public void setItems(List<ItemsBean> items) {
        this.items = items;
    }

    public static class ItemsBean {
        /**
         * country : 42fcff49-ebff-4640-b60d-e364265edd94
         * zone : 950025856
         */

        private String country; // 国家
        private int zone; // 区号

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public int getZone() {
            return zone;
        }

        public void setZone(int zone) {
            this.zone = zone;
        }
    }
}
