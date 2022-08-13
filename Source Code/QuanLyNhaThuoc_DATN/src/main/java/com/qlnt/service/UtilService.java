package com.qlnt.service;

import com.qlnt.model.Util;

public interface UtilService extends BaseService<Util, String>{
	Boolean existInGoods(String id);
}
