package com.socool.soft.vo;

import java.util.List;

public class SentSmsResponse {
	private List<SendingData> sending_respon;

	public List<SendingData> getSending_respon() {
		return sending_respon;
	}

	public void setSending_respon(List<SendingData> sending_respon) {
		this.sending_respon = sending_respon;
	}
}
