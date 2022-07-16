package com.qlnt.service;

import java.util.List;

import com.qlnt.model.Account;

public interface AccountService extends BaseService<Account, String>{
	//KIỂM TRA TÀI CÓ CÓ TỒN TẠI ORDER HAY INPUT HAY KHÔNG
	Boolean existInOrderOrInput(String id);
}
