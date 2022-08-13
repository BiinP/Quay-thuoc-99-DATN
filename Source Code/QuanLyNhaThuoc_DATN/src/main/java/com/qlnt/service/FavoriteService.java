package com.qlnt.service;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import com.qlnt.model.Favorite;

public interface FavoriteService extends BaseService<Favorite, Integer>{
	Map<String, Boolean> like(Integer id, Principal principal);
	List<Map<String, Object>> findByAccount (Principal principal);
}
