package com.socool.soft.vo.newvo.android;

public class OrderNumVO {
	private int unpaid;
	private int unreceived;
	private int unevaluated;
	private int rejected;
	private int undelivered;
	
	private int send;
	private int unreply;
	

	public int getUnpaid() {
		return unpaid;
	}

	public void setUnpaid(int unpaid) {
		this.unpaid = unpaid;
	}

	public int getUnreceived() {
		return unreceived;
	}

	public void setUnreceived(int unreceived) {
		this.unreceived = unreceived;
	}

	public int getUnevaluated() {
		return unevaluated;
	}

	public void setUnevaluated(int unevaluated) {
		this.unevaluated = unevaluated;
	}

	public int getRejected() {
		return rejected;
	}

	public void setRejected(int rejected) {
		this.rejected = rejected;
	}

	public int getUnreply() {
		return unreply;
	}

	public void setUnreply(int unreply) {
		this.unreply = unreply;
	}

	public int getSend() {
		return send;
	}

	public void setSend(int send) {
		this.send = send;
	}

	public int getUndelivered() {
		return undelivered;
	}

	public void setUndelivered(int undelivered) {
		this.undelivered = undelivered;
	}
}
