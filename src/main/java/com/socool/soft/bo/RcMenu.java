package com.socool.soft.bo;

import java.io.Serializable;
import java.util.List;

public class RcMenu extends QueryParam implements Serializable {

	private static final long serialVersionUID = -5979695894880484490L;
	
	// db
	private Integer menuId;
	private String name;
	private String location;
	private Integer seq;
	private Integer parentId;
	
	// rel
	private RcMenu parentMenu;
	
	// vo
	private Byte checked;
	
	// se
	private List<Integer> menuIds;

	public Integer getMenuId() {
		return menuId;
	}

	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public RcMenu getParentMenu() {
		return parentMenu;
	}

	public void setParentMenu(RcMenu parentMenu) {
		this.parentMenu = parentMenu;
	}

	public Byte getChecked() {
		return checked;
	}

	public void setChecked(Byte checked) {
		this.checked = checked;
	}

	public List<Integer> getMenuIds() {
		return menuIds;
	}

	public void setMenuIds(List<Integer> menuIds) {
		this.menuIds = menuIds;
	}
}