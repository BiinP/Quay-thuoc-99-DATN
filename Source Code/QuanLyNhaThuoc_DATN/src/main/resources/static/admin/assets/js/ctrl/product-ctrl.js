let urlProduct = "/api/products";
app.controller("product-ctrl", function ($scope, $http, $window) {
	$scope.start = 0;
	$scope.totalPage = 0;
	$scope.keyword = "";

	$scope.getAll = async function (keyword, page) {
		var url = `${urlProduct}?kw=${keyword}&currentPage=${page}`;
		await $http.get(url).then(resp => {
			$scope.products = resp.data.content;
			$scope.totalPage = resp.data.totalPages;
		});
	};

	$http.get('/api/sub-categories/all').then(resp => {
		$scope.subCates = resp.data;
	});

	$http.get('/api/brands/all').then(resp => {
		$scope.brands = resp.data;
	});

	$http.get('/api/utils').then(resp => {
		$scope.utils = resp.data.content;
	})

	$scope.reset = async function () {
		await $http.get(`${urlProduct}/last-id`).then(resp => {
			$scope.lastProductId = resp.data;
		})
		$scope.product = {
			id: angular.copy($scope.lastProductId) + 1,
			name: null,
			soDangKy: null,
			donViGoc: null,
			rx: false,
			active: true,
			moTa: null,
			photo: 'photo.png',
			subCategory: {
				id: null
			},
			brand: {
				id: null
			}
		};
		$scope.chon = false;
		$scope.lstGoods = [];
		var resetSummernote = null;
		$('#summernote').summernote('code', resetSummernote);
		$scope.resetGoods();
	}

	$scope.edit = function (id) {
		let url = `${urlProduct}/${id}`;
		$http.get(url).then(resp => {
			$scope.product = resp.data.product;
			$scope.lstGoods = resp.data.lstGoods;
			$scope.chon = true;
			$scope.resetGoods();
		});

	};
	$scope.loadMoTa = function(){
		let moTa = angular.copy($scope.product.moTa);
		$('#summernote').summernote('code', moTa);
	}
	$scope.redirectTo = function (page) {
		$window.location.href = `/admin#!/${page}`;
	}
	$scope.imageChanged = function (files) {
		var urlUpload = "/api/upload/product";
		var form = new FormData();
		form.append("file", files[0]);
		$http.post(urlUpload, form, {
			transformRequest: angular.identity,
			headers: { 'Content-Type': undefined }
		}).then(resp => {
			$scope.product.photo = resp.data.fileName;
			swal({
				position: 'top-end',
				type: 'success',
				title: 'Upload hình thành công',
				showConfirmButton: false,
				timer: 1500
			});
		}).catch(error => {
			swal({
				position: 'top-end',
				type: 'error',
				title: 'Upload hình thất bại',
				text: 'Hình phải bé hơn 5MB!',
				showConfirmButton: false,
				timer: 1500
			})
		});
	};
	$scope.saveMoTa = function(){
		try {
			var moTa = $('#summernote').summernote('code');
			if(moTa.length > 20000){
				swal({
					position: 'top-end',
					type: 'error',
					title: 'Mô tả phải ngắn hơn 20.000 từ',
					showConfirmButton: false,
					timer: 1500
				});
			}else{
				$scope.product.moTa = moTa;
				$('#largeModal').modal({
					focus: true
				})
				$('#largeModal1').modal({
					focus: false
				})
				swal({
					position: 'top-end',
					type: 'success',
					title: 'Thêm mô tả thành công',
					showConfirmButton: false,
					timer: 1500
				});
			}
		} catch (error) {
			swal({
				position: 'top-end',
				type: 'error',
				title: 'Thêm mô tả thất bại',
				showConfirmButton: false,
				timer: 1500
			});
		}
	}

	$scope.create = function () {
		var data = angular.copy($scope.product);
		$http.post(urlProduct, data).then(resp => {
			$scope.products.push(resp.data);
			swal({
				position: 'top-end',
				type: 'success',
				title: 'Thêm sản phẩm thành công',
				showConfirmButton: false,
				timer: 1500
			});
			$scope.chon = true;
		}).catch(error => {
			swal({
				position: 'top-end',
				type: 'error',
				title: 'Thêm sản phẩm thất bại',
				showConfirmButton: false,
				timer: 1500
			});
		});

		// console.log(data.moTa)
	};

	$scope.delete = function (id) {
		swal({
			title: `Bạn có muốn xóa sản phẩm ${id}?`,
			text: "Nếu sản phẩm đã có phiếu nhập sẽ chuyển sang trạng thái ngừng hoạt động",
			type: 'warning',
			showCancelButton: true,
			confirmButtonColor: '#3085d6',
			cancelButtonColor: '#d33',
			confirmButtonText: 'Yes, delete it!',
			confirmButtonClass: 'btn btn-success',
			cancelButtonClass: 'btn btn-danger',
		}).then((result) => {
			if (result.value) {
				var url = `${urlProduct}/${id}`;
				$http.delete(url).then(resp => {
					if (resp.data.isExist) {
						swal({
							position: 'top-end',
							type: 'success',
							title: 'Sản phẩm đã ngừng hoạt động',
							showConfirmButton: false,
							timer: 1500
						});
						$scope.reset();
					} else {
						var index = $scope.products.findIndex(p => p.id == id);
						$scope.products.splice(index, 1);
						swal({
							position: 'top-end',
							type: 'success',
							title: 'Xóa sản phẩm thành công',
							showConfirmButton: false,
							timer: 1500
						});
						$scope.reset();
					}
					if (!resp.data.isFound) {
						swal({
							position: 'top-end',
							type: 'error',
							title: 'Không tìm thấy sản phẩm',
							showConfirmButton: false,
							timer: 1500
						});
						$scope.reset();
					}
				}).catch(error => {
					swal({
						position: 'top-end',
						type: 'error',
						title: 'Xóa sản phẩm thất bại',
						showConfirmButton: false,
						timer: 1500
					});
					$scope.reset();
				});
			}
		})
	};

	$scope.update = function (id) {
		var url = `${urlProduct}/${id}`;
		var data = angular.copy($scope.product);

		$http.put(url, data).then(resp => {
			var index = $scope.products.findIndex(p => p.id == id);
			$scope.products[index] = resp.data;
			swal({
				position: 'top-end',
				type: 'success',
				title: 'Cập nhật sản phẩm thành công',
				showConfirmButton: false,
				timer: 1500
			});
		}).catch(error => {
			swal({
				position: 'top-end',
				type: 'error',
				title: 'Cập nhật sản phẩm thất bại',
				showConfirmButton: false,
				timer: 1500
			});
		});
	};

	$scope.search = function (keyword) {
		$scope.start = 0;
		$scope.getAll(keyword, $scope.start);
	};

	$scope.updatePagination = function (start) {
		if (start <= 0) {
			$('#first').addClass('disabled');
			$('#prev').addClass('disabled');
			$('#last').removeClass('disabled');
			$('#next').removeClass('disabled');
		}
		if (start >= $scope.totalPage - 1) {
			$('#last').addClass('disabled');
			$('#next').addClass('disabled');
			$('#first').removeClass('disabled');
			$('#prev').removeClass('disabled');
		}
		if (start > 0 && start < $scope.totalPage - 1) {
			$('#first').removeClass('disabled');
			$('#prev').removeClass('disabled');
			$('#last').removeClass('disabled');
			$('#next').removeClass('disabled');
		}
	}

	$scope.next = function () {
		var keyword = angular.copy($scope.keyword);
		if ($scope.start < $scope.totalPage - 1) {
			$scope.start++;
			$scope.getAll(keyword, $scope.start);
			$scope.updatePagination($scope.start);
		}
	}
	$scope.prev = function () {
		var keyword = angular.copy($scope.keyword);
		if ($scope.start > 0) {
			$scope.start--;
			$scope.getAll(keyword, $scope.start);
			$scope.updatePagination($scope.start);
		}
	}
	$scope.first = function () {
		var keyword = angular.copy($scope.keyword);
		$scope.start = 0;
		$scope.getAll(keyword, $scope.start);
		$scope.updatePagination($scope.start);
	}
	$scope.last = function () {
		var keyword = angular.copy($scope.keyword);
		$scope.start = $scope.totalPage - 1;
		$scope.getAll(keyword, $scope.start);
		$scope.updatePagination($scope.start);
	}

	//PHAN QUAN LY HANG HOA
	let urlGoods = `/api/goods`;
	$scope.editGoods = function (id) {
		let url = `${urlGoods}/${id}`;
		$http.get(url).then(resp => {
			$scope.goods = resp.data;
		});
		$scope.chonGoods = true;
	}
	$scope.resetGoods = function () {
		$scope.goods = {
			id: 'Mã tự động',
			quiDoi: 1.0,
			giaBan: 0.0,
			active: true,
			ghiChu: null,
			banOnline: false,
			util: {
				id: 'hop'
			},
			product: {
				id: 1
			}
		}
		$scope.chonGoods = false;
		$scope.existUtil = false;
		$scope.validExistUtil($scope.goods.util.id)
	}
	$scope.validExistUtil = function (value) {
		let index = $scope.lstGoods.findIndex(g => g.util.id == value);
		if (index >= 0) $scope.existUtil = true;
		else $scope.existUtil = false;
	}
	$scope.createGoods = function () {
		$scope.goods.id = 0;
		$scope.goods.product.id = $scope.product.id;
		let data = angular.copy($scope.goods)
		$http.post(urlGoods, data).then(resp => {
			$scope.lstGoods.push(resp.data);
			swal({
				position: 'top-end',
				type: 'success',
				title: 'Thêm hàng hóa thành công',
				showConfirmButton: false,
				timer: 1500,
			});
			$scope.resetGoods();
		}).catch(error => {
			swal({
				position: 'top-end',
				type: 'error',
				title: 'Thêm hàng hóa thất bại',
				showConfirmButton: false,
				timer: 1500
			});
		});
	}
	$scope.deleteGoods = function(id){
		swal({
	        title: `Bạn có muốn xóa hàng hóa ${id}?`,
	        type: 'warning',
	        showCancelButton: true,
	        confirmButtonColor: '#3085d6',
	        cancelButtonColor: '#d33',
	        confirmButtonText: 'Yes, delete it!',
	        confirmButtonClass: 'btn btn-success',
	        cancelButtonClass: 'btn btn-danger',
	    }).then((result) => {
	        if (result.value) {
	        	var url = `${urlGoods}/${id}`;
	        	$http.delete(url).then(resp => {
	        			var index = $scope.lstGoods.findIndex(p => p.id == id);
		        		$scope.lstGoods.splice(index,1);
		        		swal({
		        			position: 'top-end',
		        			type: 'success',
		        			title: 'Xóa hàng hóa thành công',
		        			showConfirmButton: false,
		        			timer: 1500
		        		});
		        		$scope.resetGoods();
	        	}).catch(error => {
	        		swal({
	        			position: 'top-end',
	        			type: 'error',
	        			title: 'Xóa hàng hóa thất bại',
	        			showConfirmButton: false,
	        			timer: 1500
	        		});
	        		$scope.resetGoods();
	        	});
	        }
	    })
	};
	$scope.updateGoods = function (id) {
		let url = `${urlGoods}/${id}`;
		let data = angular.copy($scope.goods);
		$http.put(url, data).then(resp => {
			var index = $scope.lstGoods.findIndex(g => g.id == id);
			$scope.lstGoods[index] = resp.data;
			swal({
				position: 'top-end',
				type: 'success',
				title: 'Cập nhật hàng hóa thành công',
				showConfirmButton: false,
				timer: 1500
			});
		}).catch(error => {
			swal({
				position: 'top-end',
				type: 'error',
				title: 'Cập nhật hàng hóa thất bại',
				showConfirmButton: false,
				timer: 1500
			});
		});
	}


	$scope.getAll("", 0);
	$scope.reset();
});