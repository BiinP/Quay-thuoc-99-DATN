var app = angular.module("app", []);
const REGEX_DAY = /([12]\d{3}\/(0[1-9]|1[0-2])\/(0[1-9]|[12]\d|3[01]))/;
app.directive('validateDay', function () {
	return {
		require: 'ngModel',
		link: function (scope, element, attr, mCtrl) {
			function fnValidate(value) {
				if (!value.match(REGEX_DAY)) {
					mCtrl.$setValidity('charE', false);
				} else {
					mCtrl.$setValidity('charE', true);
				}
				return value;
			}
			mCtrl.$parsers.push(fnValidate);
		}
	};
});
const REGEX_SDT = /^(0?)(3[2-9]|5[6|8|9]|7[0|6-9]|8[0-6|8|9]|9[0-4|6-9])[0-9]{7}$/;
app.directive('validateSdt', function () {
	return {
		require: 'ngModel',
		link: function (scope, element, attr, mCtrl) {
			function fnValidate(value) {
				if (!value.match(REGEX_SDT)) {
					mCtrl.$setValidity('charE', false);
				} else {
					mCtrl.$setValidity('charE', true);
				}
				return value;
			}
			mCtrl.$parsers.push(fnValidate);
		}
	};
});
app.controller("app-ctrl", function ($scope, $http, $window) {
	$scope.sortProduct = function (value, brandId, subCateId, range) {
		console.log(value);
		console.log(brandId);
		let brandid = '';
		if (brandId.indexOf('true') >= 0) {
			let startIndex = brandId.indexOf('?');
			let endIndex = brandId.indexOf(':');
			brandid = brandId.slice(startIndex + 1, endIndex);
		}
		let subcateid = '';
		if (subCateId.indexOf('true') >= 0) {
			let startIndex = subCateId.indexOf('?');
			let endIndex = subCateId.indexOf(':');
			subcateid = subCateId.slice(startIndex + 1, endIndex);
		}
		$window.location.href = `/product?page=&brandId=${brandid}&subCateId=${subcateid}&range=0&sort=${value}`;
	}
	$scope.cart = {
		items: [],
		add(id, discount) {
			var item = this.items.find(item => item.id == id);
			if (item) {
				item.qty++;
				this.saveToLocalStorage();
			} else {
				$http.get(`/cart/goods/${id}`).then(resp => {
					resp.data.qty = 1;
					resp.data.discount = discount;
					this.items.push(resp.data);
					this.saveToLocalStorage();
				})
			}
		},
		minus(id) {
			var index = this.items.findIndex(item => item.id == id);
			if (this.items[index].qty == 1) {
				this.remove(id);
			} else {
				this.items[index].qty -= 1;
				this.saveToLocalStorage();
			}
		},
		remove(id) {
			var index = this.items.findIndex(item => item.id == id);
			this.items.splice(index, 1);
			this.saveToLocalStorage();
		},
		get count() {
			return this.items
				.map(item => item.qty)
				.reduce((total, qty) => total += qty, 0);
		},
		priceItem(id) {
			var index = this.items.findIndex(item => item.id == id);
			return this.items[index].qty * (this.items[index].giaBan * (1 - this.items[index].discount / 100));
		},
		get amount() {
			return this.items
				.map(item => item.qty * (item.giaBan * (1 - item.discount / 100)))
				.reduce((total, giaBan) => total += giaBan, 0);
		},
		saveToLocalStorage() {
			var json = JSON.stringify(angular.copy(this.items));
			localStorage.setItem("cart", json);
		},
		clear() {
			this.items = [];
			this.saveToLocalStorage();
		},
		loadFromLocalStorage() {
			var json = localStorage.getItem("cart");
			this.items = json ? JSON.parse(json) : [];
		}
	}
	$scope.cart.loadFromLocalStorage();
	$scope.order = {
		id: 0,
		ngayTao: new Date(),
		sdt: $("#sdtDatHang").val(),
		diaChi: $("#diaChiDatHang").val(),
		phiGiaoHang: $("#phiGiaoHang").text(),
		tongTien: $scope.cart.items.reduce((total, item) => {
			return total + (item.giaBan * item.qty)
		}, 0),
		tongGiamGia: $scope.cart.items.reduce((total, item) => {
			return total + (item.giaBan * item.qty)
		}, 0) - $scope.cart.amount,
		thanhTien: $scope.cart.amount + 15000,
		account: { email: $("#emailDatHang").val() },
		get orderDetails() {
			return $scope.cart.items.map(item => {
				return {
					soLuong: item.qty,
					donGia: item.giaBan,
					giamGia: item.giaBan * item.discount / 100,
					goodsId: item.id
				}
			});
		},

		purchase() {
			var order = angular.copy(this);
			console.log(order)
			if (order.diaChi == null || order.diaChi == '') {
				alert("Địa chỉ không được để trống")
			} else {

				$http.post('/cart/order/purchase', order).then(resp => {
					alert("Đặt hàng thành công");
					$scope.cart.clear();
					location.href = "/account/order/detail/" + resp.data.id;
				}).catch(error => {
					alert("Đặt hàng thất bại");
					console.log(error);
				});
			}
		}
	}
	$scope.wishList = {
		add(id) {
			$http.post(`/wishList/${id}`).then(resp => {
				if (resp.data.exist) {
					alert("Bỏ thích thành công");
				} else {
					alert("Yêu thích thành công");
				}
			}).catch(error => {
				alert("Yêu thích thất bại");
			})
		}
	}
});
