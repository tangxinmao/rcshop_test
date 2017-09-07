package com.socool.soft.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.socool.soft.bo.RcProdCat;
import com.socool.soft.bo.RcProdSkuProp;
import com.socool.soft.bo.RcProdSkuPropEnum;
import com.socool.soft.bo.RcProdCatSkuProp;
import com.socool.soft.dao.RcProdSkuPropEnumMapper;
import com.socool.soft.dao.RcProdSkuPropMapper;
import com.socool.soft.dao.RcProdCatSkuPropMapper;
import com.socool.soft.service.IProdCatService;
import com.socool.soft.service.IProdSkuPropService;
import com.socool.soft.vo.Page;
import com.socool.soft.vo.PageContext;

@Service
public class ProdSkuPropServiceImpl implements IProdSkuPropService {
	@Autowired
	private RcProdSkuPropMapper prodSkuPropMapper;
	@Autowired
	private RcProdSkuPropEnumMapper prodSkuPropEnumMapper;
	@Autowired
	private RcProdCatSkuPropMapper prodCatSkuPropMapper;
	@Autowired
	private IProdCatService prodCatService;

	@Override
	public List<RcProdSkuProp> findPagedProdSkuProps(RcProdSkuProp param, Page page) {
		PageContext.setPage(page);
		return prodSkuPropMapper.select(param);
	}
	
	@Override
	public List<RcProdSkuProp> findProdSkuProps(RcProdSkuProp param) {
		return prodSkuPropMapper.select(param);
	}

	@Override
	public RcProdSkuProp findProdSkuPropById(int prodSkuPropId) {
		return prodSkuPropMapper.selectByPrimaryKey(prodSkuPropId);
	}

	@Override
	public int addProdSkuProp(RcProdSkuProp prodSkuProp) {
		if(prodSkuPropMapper.insert(prodSkuProp) > 0) {
			return prodSkuProp.getProdPropId();
		}
		return 0;
	}

	@Override
	public int modifyProdSkuProp(RcProdSkuProp prodSkuProp) {
		if(prodSkuPropMapper.updateByPrimaryKey(prodSkuProp) > 0) {
			return prodSkuProp.getProdPropId();
		}
		return 0;
	}

	@Override
	public int removeProdSkuPropWithEnums(int prodSkuPropId) {
		int result = prodSkuPropMapper.deleteByPrimaryKey(prodSkuPropId);
		if(result > 0){
			removeProdSkuPropEnumsByProdSkuPropId(prodSkuPropId);
			removeProdCatSkuPropsByProdSkuPropId(prodSkuPropId);
		}
		return result;
	}

	@Override
	public List<RcProdSkuPropEnum> findProdSkuPropEnumsByProdSkuPropId(int prodSkuPropId) {
		RcProdSkuPropEnum param  = new RcProdSkuPropEnum();
		param.setProdPropId(prodSkuPropId);
		param.setOrderBy("SEQ ASC");
		return prodSkuPropEnumMapper.select(param);
	}

	@Override
	public int addProdSkuPropEnum(RcProdSkuPropEnum prodSkuPropEnum) {
		if(prodSkuPropEnum.getSeq() == null) {
			prodSkuPropEnum.setSeq(99);
		}
		if(prodSkuPropEnumMapper.insert(prodSkuPropEnum) > 0) {
			return prodSkuPropEnum.getProdPropEnumId();
		}
		return 0;
	}

	@Override
	public int removeProdSkuPropEnum(int prodSkuPropEnumId) {
		return prodSkuPropEnumMapper.deleteByPrimaryKey(prodSkuPropEnumId);
	}

	@Override
	public RcProdSkuPropEnum findProdSkuPropEnumById(int prodSkuPropEnumId) {
		return prodSkuPropEnumMapper.selectByPrimaryKey(prodSkuPropEnumId);
	}

	@Override
	public int modifyProdSkuPropEnum(RcProdSkuPropEnum prodSkuPropEnum) {
		if(prodSkuPropEnumMapper.updateByPrimaryKey(prodSkuPropEnum) > 0) {
			return prodSkuPropEnum.getProdPropEnumId();
		}
		return 0;
	}

	@Override
	public int removeProdSkuPropEnumsByProdSkuPropId(int prodSkuPropId) {
		RcProdSkuPropEnum param = new RcProdSkuPropEnum();
		param.setProdPropId(prodSkuPropId);
		return prodSkuPropEnumMapper.delete(param);
	}

	@Override
	public List<List<RcProdSkuProp>> findProdSkuPropsForBinding(int prodCatId) {
		List<RcProdSkuProp> prodSkuProps = findProdSkuProps(null);
		List<RcProdCatSkuProp> prodCatSkuProps = findProdCatSkuPropsByProdCatId(prodCatId);
		List<RcProdSkuProp> unboundProdSkuProps = new ArrayList<RcProdSkuProp>();
		List<RcProdSkuProp> boundProdSkuProps = new ArrayList<RcProdSkuProp>();
		for(RcProdSkuProp prodSkuProp : prodSkuProps) {
			boolean isFound = false;
			for(RcProdCatSkuProp prodCatSkuProp : prodCatSkuProps) {
				if(prodSkuProp.getProdPropId() == prodCatSkuProp.getProdPropId()) {
					boundProdSkuProps.add(prodSkuProp);
					isFound = true;
					break;
				}
			}
			if(!isFound) {
				unboundProdSkuProps.add(prodSkuProp);
			}
		}
		List<List<RcProdSkuProp>> result = new ArrayList<List<RcProdSkuProp>>();
		result.add(unboundProdSkuProps);
		result.add(boundProdSkuProps);
		return result;
	}

	@Override
	public int removeProdCatSkuPropsByProdCatId(int prodCatId) {
		RcProdCatSkuProp param = new RcProdCatSkuProp();
		param.setProdCatId(prodCatId);
		return prodCatSkuPropMapper.delete(param);
	}

	@Override
	public int addProdCatSkuProp(RcProdCatSkuProp prodCatSkuProp) {
		RcProdCat prodCat = prodCatService.findProdCatById(prodCatSkuProp.getProdCatId());
		if(prodCat.getParentId() > 0) {
			RcProdCatSkuProp param = new RcProdCatSkuProp();
			param.setProdCatId(prodCat.getParentId());
			param.setProdPropId(prodCatSkuProp.getProdPropId());
			RcProdCatSkuProp parentProdCatSkuProp = findProdCatSkuProp(param);
			if(parentProdCatSkuProp != null) {
				return 0;
			}
		}
		if(prodCatSkuPropMapper.insert(prodCatSkuProp) > 0) {
			return prodCatSkuProp.getProdCatSkuPropId();
		}
		return 0;
	}

	@Override
	public List<RcProdCatSkuProp> findProdCatSkuPropsByProdCatId(int prodCatId) {
		RcProdCatSkuProp param = new RcProdCatSkuProp();
		param.setProdCatId(prodCatId);
		List<RcProdCatSkuProp> prodCatSkuProps = prodCatSkuPropMapper.select(param);
		RcProdCat prodCat = prodCatService.findProdCatById(prodCatId);
		while(prodCat.getParentId() > 0) {
			prodCatSkuProps.addAll(findProdCatSkuPropsByProdCatId(prodCat.getParentId()));
			prodCat = prodCatService.findProdCatById(prodCat.getParentId());
		}
		return prodCatSkuProps;
	}

	@Override
	public RcProdCatSkuProp findProdCatSkuProp(RcProdCatSkuProp param) {
		return prodCatSkuPropMapper.selectOne(param);
	}

	@Override
	public int removeProdCatSkuPropsByProdSkuPropId(int prodSkuPropId) {
		RcProdCatSkuProp param = new RcProdCatSkuProp();
		param.setProdPropId(prodSkuPropId);
		return prodCatSkuPropMapper.delete(param);
	}
}
