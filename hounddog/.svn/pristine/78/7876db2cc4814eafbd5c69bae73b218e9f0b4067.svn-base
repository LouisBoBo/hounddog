package com.slxk.hounddog.wxapi.helper;

import com.google.gson.annotations.SerializedName;
import com.slxk.hounddog.mvp.model.entity.BaseResponse;

public class WeChatPayBean extends BaseResponse {

	/**
	 * data : {"appid":"wx004f27352f2fb41a","noncestr":"1540793346322","package":"Sign=WXPay","partnerid":"1419248602","prepayid":"wx2914125100097727962adef92611813720","sign":"69F91DB1D53832B7B2E376F81DA77413","timestamp":"1540793346"}
	 */

	private DataBean data;

	@Override
    public DataBean getData() {
		return data;
	}

	public void setData(DataBean data) {
		this.data = data;
	}

	public static class DataBean {
		/**
		 * appid : wx004f27352f2fb41a
		 * noncestr : 1540793346322
		 * package : Sign=WXPay
		 * partnerid : 1419248602
		 * prepayid : wx2914125100097727962adef92611813720
		 * sign : 69F91DB1D53832B7B2E376F81DA77413
		 * timestamp : 1540793346
		 */

		private String appid;
		private String noncestr;
		@SerializedName("package")
		private String packageX;
		private String partnerid;
		private String prepayid;
		private String sign;
		private String timestamp;

		public String getAppid() {
			return appid;
		}

		public void setAppid(String appid) {
			this.appid = appid;
		}

		public String getNoncestr() {
			return noncestr;
		}

		public void setNoncestr(String noncestr) {
			this.noncestr = noncestr;
		}

		public String getPackageX() {
			return packageX;
		}

		public void setPackageX(String packageX) {
			this.packageX = packageX;
		}

		public String getPartnerid() {
			return partnerid;
		}

		public void setPartnerid(String partnerid) {
			this.partnerid = partnerid;
		}

		public String getPrepayid() {
			return prepayid;
		}

		public void setPrepayid(String prepayid) {
			this.prepayid = prepayid;
		}

		public String getSign() {
			return sign;
		}

		public void setSign(String sign) {
			this.sign = sign;
		}

		public String getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(String timestamp) {
			this.timestamp = timestamp;
		}
	}
}
