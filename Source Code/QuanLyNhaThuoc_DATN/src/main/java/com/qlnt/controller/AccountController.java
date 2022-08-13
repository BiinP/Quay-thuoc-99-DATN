package com.qlnt.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.qlnt.model.Account;
import com.qlnt.model.Goods;
import com.qlnt.model.Order;
import com.qlnt.model.OrderDetail;
import com.qlnt.model.Product;
import com.qlnt.service.AccountService;
import com.qlnt.service.FavoriteService;
import com.qlnt.service.GoodsService;
import com.qlnt.service.OrderService;
import com.qlnt.service.UploadService;

@Controller
@RequestMapping("/account")
public class AccountController {
	@Autowired
	private AccountService accountService;
	@Autowired
	private UploadService uploadService;
	@Autowired
	private FavoriteService favService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private GoodsService goodsService;

	@GetMapping("")
	public String detail(Model model, Principal principal) {
		String email = principal.getName();
		Account profile = accountService.findById(email);
		model.addAttribute("profile", profile);
		return "account/profile";
	}

	@PostMapping("/update")
	public String update(@ModelAttribute Account profile, @RequestParam("photo") MultipartFile photo,
			Principal principal, Model model) {
		String email = principal.getName();
		Account account = accountService.findById(email);
		try {
			account.setHoTen(profile.getHoTen());
			account.setNgaySinh(profile.getNgaySinh());
			account.setGioiTinh(profile.getGioiTinh());
			account.setSdt(profile.getSdt());
			account.setDiaChi(profile.getDiaChi());
			if (!photo.isEmpty()) {
				String fileName = uploadService.save(photo, "account").getName();
				account.setAvatar(fileName);
			}
			accountService.save(account);
			model.addAttribute("success", "Cập nhật tài khoản thành công");
			model.addAttribute("profile", accountService.findById(email));
		} catch (Exception e) {
			// TODO: handle exception
			model.addAttribute("error", "Cập nhật tài khoản thất bại");
		}
		return "account/profile";
	}

	@GetMapping("/change-password")
	public String changePw() {
		return "account/change-pw";
	}

	@PostMapping("/change-password")
	public String processChangePw(@RequestParam String oldPassword, @RequestParam String newPassword,
			Principal principal, Model model) {
		String email = principal.getName();
		Account account = accountService.findById(email);
		if (oldPassword.equals(account.getMatKhau())) {
			account.setMatKhau(newPassword);
			accountService.save(account);
			model.addAttribute("success", "Đổi mật khẩu thành công");
		} else {
			model.addAttribute("error", "Mật khẩu cũ không chính xác");
		}
		return "account/change-pw";
	}

	@GetMapping("/order")
	public String myOrder(Model model, Principal principal) {
		String email = principal.getName();
		model.addAttribute("orders", orderService.findByAccount(email));
		return "order/list";
	}

	@GetMapping("/wishlist")
	public String myWishlist(Model model, Principal principal) {
		model.addAttribute("favorites", favService.findByAccount(principal));
		return "account/wishlist";
	}

	@GetMapping("/order/detail/{id}")
	public String orderDetail(Model model, Principal principal, @PathVariable("id") Integer id) {
		Order order = orderService.findById(id);
		String email = principal.getName();
		if (!order.getAccount().getEmail().equals(email)) {
			return "redirect:/login/denied";
		} else {
			model.addAttribute("order", order);
			List<Map<String, Object>> db = new ArrayList<Map<String, Object>>();
			List<OrderDetail> orderDetails = order.getOrderDetails();
			for (OrderDetail od : orderDetails) {
				Map<String, Object> map = new HashMap<String, Object>();
				Integer goodsId = od.getGoodsId();
				Goods goods = goodsService.findById(goodsId);
				Product product = goods.getProduct();
				map.put("name", product.getName());
				map.put("photo", product.getPhoto());
				map.put("price", od.getDonGia());
				map.put("priceSale", od.getGiamGia());
				map.put("soLuong", od.getSoLuong());
				db.add(map);
			}
			model.addAttribute("orderDetails", db);
			// Double total = (double) 0;
//			for(OrderDetail od : order.getorder) {
//				total += od.getPrice() * od.getQuantity();
//			}
//			model.addAttribute("total", total);
			return "order/detail";

		}
	}
}
