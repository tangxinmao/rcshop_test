package com.socool.soft.controller;

import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.socool.soft.bo.RcMember;
import com.socool.soft.bo.RcMenu;
import com.socool.soft.bo.RcMenuRole;
import com.socool.soft.bo.RcRole;
import com.socool.soft.bo.RcUser;
import com.socool.soft.bo.RcUserRole;
import com.socool.soft.bo.constant.YesOrNoEnum;
import com.socool.soft.service.IMemberService;
import com.socool.soft.service.IMenuRoleService;
import com.socool.soft.service.IMenuService;
import com.socool.soft.service.IRoleService;
import com.socool.soft.service.IUserRoleService;
import com.socool.soft.service.IUserService;
import com.socool.soft.vo.Page;

@RequestMapping(value = "role")
@Controller
public class RoleController extends BaseController {
	@Autowired
	private IRoleService roleService;
	@Autowired
	private IMemberService memberService;
	@Autowired
	private IUserService userService;
	@Autowired 
	private IMenuService menuService;
	@Autowired
	private IUserRoleService userRoleService;
	@Autowired
	private IMenuRoleService menuRoleService;

	/**
	 * 后台角色页面
	 * 
	 * @param request
	 * @param model
	 * @param currentPage
	 * @param rcVendorVo
	 * @param isSuccess
	 * @param upDown
	 * @return
	 */
	@RequestMapping(value = "roleMemuRelation")
	public String roleMemuRelation(Model model, Integer currentPage) {
		boolean flag = false;
		if (currentPage == null) {
			currentPage = 1;
			flag = true;
		}
		// 分页
		Page page = new Page();
		// 初始化时第一页
		page.setPagination(true);
		page.setPageSize(10);
		page.setCurrentPage(currentPage);
		model.addAttribute("page", page);
		List<RcRole> roles = roleService.findAllPagedRoles(page);
		model.addAttribute("page", page);
		model.addAttribute("list", roles);
		if (flag) {
			return "roleMemuRelation";
		} else {
			return "roleMemuRelation_inner";
		}
	}

	/**
	 * 后台用户页面
	 * 
	 * @param request
	 * @param model
	 * @param currentPage
	 * @param rcRole
	 * @param rcMemberInfop
	 * @param isSuccess
	 * @param upDown
	 * @return
	 */
	@RequestMapping(value = "roleMemberRelation")
	public String roleMemberRelation(Model model, Integer currentPage, Integer memberId, String account, Integer roleId) {
		List<RcRole> roles = roleService.findAllRoles();
		boolean flag = false;
		if (currentPage == null) {
			currentPage = 1;
			flag = true;
		}
		Page page = new Page();
		page.setPagination(true);
		page.setCurrentPage(currentPage);
		page.setPageSize(10);
		List<RcUser> users = userService.findPagedUsersWithRoles(memberId, account, roleId, page);
		model.addAttribute("page", page);
		model.addAttribute("rcRoles", roles);
		model.addAttribute("list", users);
		if (flag) {
			return "roleMemberRelation";
		} else {
			return "roleMemberRelation_inner";
		}
	}

	/**
	 * 后台菜单页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "menuIndex")
	public String menuIndex() {
		return "menuIndex";
	}

	/**
	 * 修改角色
	 * 
	 * @return
	 */
	@RequestMapping(value = "updateRole")
	@ResponseBody
	public String updateRole(Integer roleId, String name, String description) {
		RcRole role = new RcRole();
		role.setRoleId(roleId);
		role.setName(name);
		role.setDescription(description);
		int result = 0;
		if (roleId == null) {
			result = roleService.addRole(role);
		} else {
			result = roleService.modifyRole(role);
		}
		if(result > 0) {
			return "{\"code\":\"1\"}";
		}
		return "{\"code\":\"0\"}";
	}

	/**
	 * 获取所有菜单
	 * @return
	 */
	@RequestMapping(value = "menu")
	@ResponseBody
	public List<RcMenu> menu() {
		List<RcMenu> menus = menuService.findAllMenus();
		return menus;
	}

	/**
	 * 查询所有菜单通过角色
	 * 
	 * @param roleId
	 * @return
	 */
	@RequestMapping(value = "menuByRole")
	@ResponseBody
	public List<RcMenu> menuByRole(int roleId) {
		List<RcMenu> menus = menuService.findSortedMenusByRoleId(roleId);
		return menus;
	}

	/**
	 * 查询可用菜单通过角色
	 * 
	 * @param roleId
	 * @return
	 */
	@RequestMapping(value = "menuCheckedByRole")
	@ResponseBody
	public List<RcMenu> menuCheckedByRole(int roleId) {
		List<RcMenu> allMenus = menuService.findAllMenus();
		List<RcMenu> roleMenus = menuService.findMenusByRoleId(roleId);
		if(!CollectionUtils.isEmpty(roleMenus)) {
			for(RcMenu menu : allMenus) {
				for(RcMenu roleMenu : roleMenus) {
					if(menu.getMenuId().equals(roleMenu.getMenuId())) {
						menu.setChecked(YesOrNoEnum.YES.getValue());
						break;
					}
				}
			}
		}
		return allMenus;
	}

	@RequestMapping(value = "saveMenu")
	@ResponseBody
	public String saveMenu(Integer menuId, Integer parentId, String name, String location, int seq) {
		RcMenu menu = new RcMenu();
		menu.setMenuId(menuId);
		menu.setParentId(parentId);
		menu.setName(name);
		menu.setLocation(location);
		menu.setSeq(seq);
		int result = 0;
		if (menuId == null) {
			result = menuService.addMenu(menu);
		} else {
			result = menuService.modifyMenu(menu);
		}
		if(result > 0) {
			return "{\"code\":\"1\"}";
		}
		return "{\"code\":\"0\"}";
	}

	@RequestMapping(value = "deleteMenu")
	@ResponseBody
	public String deleteMenu(int menuId) {
		int result = menuService.removeMenu(menuId);
		if(result > 0) {
			return "{\"code\":\"1\"}";
		}
		return "{\"code\":\"0\"}";
	}

	/**
	 * 保存角色菜单
	 * 
	 * @param roleIds
	 * @return
	 */
	@RequestMapping(value = "menuRole")
	@ResponseBody
	public String menuRole(int roleId, int[] menuIds) {
		menuRoleService.removeMenuRolesByRoleId(roleId);
		if(menuIds != null) {
			for (int menuId : menuIds) {
				RcMenuRole menuRole = new RcMenuRole();
				menuRole.setRoleId(roleId);
				menuRole.setMenuId(menuId);
				menuRoleService.addMenuRole(menuRole);
			}
		}
		
//		//iRoleService.saveMenuRole(list, roleId);
//		List<Item> items = menuService.findItemsByRoleId(roleId);
//		result.setData(items);
//		
//		// 手动去失效当前角色的所在登录用户session 重新获取角色分配菜单
//		Vector<HttpSession> httpSessions = SessionListener.httpSessions;
//		List<RcUserRole> rcMemberRoles = memberRoleService.findMemberRolesByRoleId(roleId);
//		List<Integer> memberIds = new ArrayList<Integer>();
//		String memberId = httpServletRequest.getSession().getAttribute("memberId").toString();
//		for (RcUserRole rcMemberRole : rcMemberRoles) {
//			if (!memberId.equals(rcMemberRole.getMemberId())) 
//				memberIds.add(rcMemberRole.getMemberId());
//		
//		}
//		 for (HttpSession httpSession : httpSessions) {
//			 try{
//			if(memberIds.contains(httpSession.getAttribute("memberId"))){
//				httpSession.invalidate();
//			}
//			 }catch(Exception e){
//				 
//			 }
//			}

		return "{\"code\":\"1\"}";
	}

	@RequestMapping(value = "saveMemberRole")
	@ResponseBody
	public String saveMemberRole(Integer memberId, String account, String password, 
			String email, String description, int roleId) {
		RcUser user = new RcUser();
		user.setMemberId(memberId);
		user.setAccount(account);
		user.setPassword(password);
		user.setEmail(email);
		user.setDescription(description);
		user.setIsSuper(YesOrNoEnum.NO.getValue());
		if (memberId == null) {
			RcMember oldMember = memberService.findMemberByAccount(account);
			if(oldMember != null) {
				return "{\"code\":\"-1\",\"result\":\"account has been used\"}";
				
			}
			oldMember = memberService.findMemberByEmail(email);
			if(oldMember != null) {
				return "{\"code\":\"-1\",\"result\":\"email has been used\"}";
				
			}
			user.setPassword(DigestUtils.md5Hex(user.getPassword()));
			memberId = userService.addUser(user);
			if(memberId > 0) {
				RcUserRole userRole = new RcUserRole();
				userRole.setMemberId(memberId);
				userRole.setRoleId(roleId);
				userRoleService.addUserRole(userRole);
			}
		} else {	
			RcMember oldMember = memberService.findMemberByAccount(account);
			if(oldMember != null && !memberId.equals(oldMember.getMemberId())){
				return "{\"code\":\"-1\",\"result\":\"account has been used\"}";
				
			}	
			oldMember = memberService.findMemberByEmail(email);
			if(oldMember != null && !memberId.equals(oldMember.getMemberId())){
				return "{\"code\":\"-1\",\"result\":\"email has been used\"}";
				
			}
			if (StringUtils.isNotBlank(user.getPassword())) {
				user.setPassword(DigestUtils.md5Hex(user.getPassword()));
			} else {
				user.setPassword(null);
			}
			int result = userService.modifyUser(user);
			if(result > 0) {
				userRoleService.removeUserRolesByUserId(user.getMemberId());
				RcUserRole userRole = new RcUserRole();
				userRole.setMemberId(memberId);
				userRole.setRoleId(roleId);
				userRoleService.addUserRole(userRole);
			}
		}
		return "{\"code\":\"1\"}";
	}

	@RequestMapping(value = "deleteMemberAndMemberRole")
	@ResponseBody
	public String deleteMemberAndMemberRole(int memberId) {
		int result = userService.removeUser(memberId);
		return "{\"code\":\"" + result + "\"}";
	}
}
