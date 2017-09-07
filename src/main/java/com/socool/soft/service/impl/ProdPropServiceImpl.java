package com.socool.soft.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.socool.soft.bo.RcProdCat;
import com.socool.soft.bo.RcProdCatProp;
import com.socool.soft.bo.RcProdProp;
import com.socool.soft.bo.RcProdPropEnum;
import com.socool.soft.dao.RcProdCatPropMapper;
import com.socool.soft.dao.RcProdPropEnumMapper;
import com.socool.soft.dao.RcProdPropMapper;
import com.socool.soft.service.IProdCatService;
import com.socool.soft.service.IProdPropService;
import com.socool.soft.vo.Page;
import com.socool.soft.vo.PageContext;

@Service
public class ProdPropServiceImpl implements IProdPropService {
	@Autowired
	private RcProdPropMapper prodPropMapper;
	@Autowired
	private RcProdPropEnumMapper prodPropEnumMapper;
	@Autowired
	private RcProdCatPropMapper prodCatPropMapper;
	@Autowired
	private IProdCatService prodCatService;

	@Override
	public List<RcProdProp> findPagedProdProps(RcProdProp param, Page page) {
		PageContext.setPage(page);
		return prodPropMapper.select(param);
	}
	
	@Override
	public List<RcProdProp> findProdProps(RcProdProp param) {
		return prodPropMapper.select(param);
	}

	@Override
	public RcProdProp findProdPropById(int prodPropId) {
		return prodPropMapper.selectByPrimaryKey(prodPropId);
	}

	@Override
	public int addProdProp(RcProdProp prodProp) {
		if(prodPropMapper.insert(prodProp) > 0) {
			return prodProp.getProdPropId();
		}
		return 0;
	}

	@Override
	public int modifyProdProp(RcProdProp prodProp) {
		if(prodPropMapper.updateByPrimaryKey(prodProp) > 0) {
			return prodProp.getProdPropId();
		}
		return 0;
	}

	@Override
	public List<List<RcProdProp>> findProdPropsForBinding(int prodCatId) {
		List<RcProdProp> prodProps = findProdProps(null);
		List<RcProdCatProp> prodCatProps = findProdCatPropsByProdCatId(prodCatId);
		List<RcProdProp> unboundProdProps = new ArrayList<RcProdProp>();
		List<RcProdProp> boundProdProps = new ArrayList<RcProdProp>();
		for(RcProdProp prodProp : prodProps) {
			boolean isFound = false;
			for(RcProdCatProp prodCatProp : prodCatProps) {
				if(prodProp.getProdPropId() == prodCatProp.getProdPropId()) {
					boundProdProps.add(prodProp);
					isFound = true;
					break;
				}
			}
			if(!isFound) {
				unboundProdProps.add(prodProp);
			}
		}
		List<List<RcProdProp>> result = new ArrayList<List<RcProdProp>>();
		result.add(unboundProdProps);
		result.add(boundProdProps);
		return result;
	}

	@Override
	public int removeProdCatPropsByProdCatId(int prodCatId) {
		RcProdCatProp param = new RcProdCatProp();
		param.setProdCatId(prodCatId);
		return prodCatPropMapper.delete(param);
	}

	@Override
	public int addProdCatProp(RcProdCatProp prodCatProp) {
		RcProdCat prodCat = prodCatService.findProdCatById(prodCatProp.getProdCatId());
		if(prodCat.getParentId() > 0) {
			RcProdCatProp param = new RcProdCatProp();
			param.setProdCatId(prodCat.getParentId());
			param.setProdPropId(prodCatProp.getProdPropId());
			RcProdCatProp parentProdCatProp = findProdCatProp(param);
			if(parentProdCatProp != null) {
				return 0;
			}
		}
		if(prodCatPropMapper.insert(prodCatProp) > 0) {
			return prodCatProp.getProdCatPropId();
		}
		return 0;
	}

	@Override
	public List<RcProdCatProp> findProdCatPropsByProdCatId(int prodCatId) {
		RcProdCatProp param = new RcProdCatProp();
		param.setProdCatId(prodCatId);
		List<RcProdCatProp> prodCatProps = prodCatPropMapper.select(param);
		RcProdCat prodCat = prodCatService.findProdCatById(prodCatId);
		while(prodCat.getParentId() > 0) {
			prodCatProps.addAll(findProdCatPropsByProdCatId(prodCat.getParentId()));
			prodCat = prodCatService.findProdCatById(prodCat.getParentId());
		}
		return prodCatProps;
	}

	@Override
	public RcProdCatProp findProdCatProp(RcProdCatProp param) {
		return prodCatPropMapper.selectOne(param);
	}

	@Override
	public int removeProdCatPropsByProdPropId(int prodPropId) {
		RcProdCatProp param = new RcProdCatProp();
		param.setProdPropId(prodPropId);
		return prodCatPropMapper.delete(param);
	}

	@Override
	public int removeProdPropWithEnums(int prodPropId) {
		int result = prodPropMapper.deleteByPrimaryKey(prodPropId);
		if(result > 0){
			removeProdPropEnumsByProdPropId(prodPropId);
			removeProdCatPropsByProdPropId(prodPropId);
		}
		return result;
	}

	@Override
	public List<RcProdPropEnum> findProdPropEnumsByProdPropId(int prodPropId) {
		RcProdPropEnum param  = new RcProdPropEnum();
		param.setProdPropId(prodPropId);
		param.setOrderBy("SEQ ASC");
		return prodPropEnumMapper.select(param);
	}

	@Override
	public int addProdPropEnum(RcProdPropEnum prodPropEnum) {
		if(prodPropEnum.getSeq() == null) {
			prodPropEnum.setSeq(99);
		}
		if(prodPropEnumMapper.insert(prodPropEnum) > 0) {
			return prodPropEnum.getProdPropEnumId();
		}
		return 0;
	}

	@Override
	public int removeProdPropEnum(int prodPropEnumId) {
		return prodPropEnumMapper.deleteByPrimaryKey(prodPropEnumId);
	}

	@Override
	public int modifyProdPropEnum(RcProdPropEnum prodPropEnum) {
		if(prodPropEnumMapper.updateByPrimaryKey(prodPropEnum) > 0) {
			return prodPropEnum.getProdPropEnumId();
		}
		return 0;
	}

	@Override
	public int removeProdPropEnumsByProdPropId(int prodPropId) {
		RcProdPropEnum param = new RcProdPropEnum();
		param.setProdPropId(prodPropId);
		return prodPropEnumMapper.delete(param);
	}
}
