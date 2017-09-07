package com.socool.soft.service.impl;

import com.socool.soft.bo.RcHelp;
import com.socool.soft.dao.RcHelpMapper;
import com.socool.soft.service.IHelpService;
import com.socool.soft.vo.Page;
import com.socool.soft.vo.PageContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HelpServiceImpl implements IHelpService {
	@Autowired
	private RcHelpMapper helpMapper;

	@Override
	public List<RcHelp> findAllHelps() {
		RcHelp param = new RcHelp();
		param.setOrderBy("SEQ ASC");
		return helpMapper.select(param);
	}

	@Override
	public List<RcHelp> findAllPagedHelps(Page page) {
		PageContext.setPage(page);
		RcHelp param = new RcHelp();
		param.setOrderBy("SEQ ASC");
		return helpMapper.select(param);
	}

	@Override
	public List<String> findAllDescriptions() {
		RcHelp param = new RcHelp();
		param.setOrderBy("SEQ ASC");
		List<RcHelp> helps = helpMapper.select(param);
		List<String> descriptions = new ArrayList<String>();
		for(RcHelp help : helps) {
			if(!descriptions.contains(help.getDescription())) {
				descriptions.add(help.getDescription());
			}
		}
		return descriptions;
	}

	@Override
	public int addHelp(RcHelp help) {
		if(helpMapper.insert(help) > 0) {
			return help.getHelpId();
		}
		return 0;
	}

	@Override
	public RcHelp findHelpById(int helpId) {
		return helpMapper.selectByPrimaryKey(helpId);
	}

	@Override
	public int modifyHelp(RcHelp help) {
		if(helpMapper.updateByPrimaryKey(help) > 0) {
			return help.getHelpId();
		}
		return 0;
	}

	@Override
	public int removeHelp(int helpId) {
		return helpMapper.deleteByPrimaryKey(helpId);
	}
}
