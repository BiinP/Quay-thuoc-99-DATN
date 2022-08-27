let urlInput = "/api/inputs";
app.controller("input-ctrl", function ($scope, $http, $window) {
	$scope.start = 0;
	$scope.totalPage = 0;
	$scope.keyword = "";

	$scope.getAll = async function (keyword, page) {
		var url = `${urlInput}?kw=${keyword}&currentPage=${page}`;
		await $http.get(url).then(resp => {
			$scope.inputs = resp.data.content;
			$scope.totalPage = resp.data.totalPages;
		});
	};
	$scope.getAllProduct = function () {
		$http.get('/api/products/all').then(resp => {
			$scope.products = resp.data;
		});
	}

	$scope.reset = function () {
		var today = new Date();
		var dd = today.getDate();
		if (dd < 10) {
			dd = "0" + dd;
		}
		var mm = today.getMonth() + 1;
		if (mm < 10) {
			mm = "0" + mm;
		}
		var yyyy = today.getFullYear();

		var currentDate = `${yyyy}-${mm}-${dd}`;
		$scope.input = {
			id: 'Mã tự tăng',
			ngayTao: currentDate,
			tongTien: 0.0,
			tongGiamGia: 0.0,
			tongVAT: 0.0,
			thanhTien: 0.0,
			ghiChu: null,
			account: {
				email: 'phucnguyenhoang769@gmail.com',
				hoTen: 'Nguyễn Hoàng Phúc'
			}
		};
		$scope.chon = false;
		$scope.inputDetails = [];
		$scope.resetInputDetail();
	}

	$scope.edit = function (id) {
		let url = `${urlInput}/${id}`;
		$http.get(url).then(resp => {
			$scope.input = resp.data.input;
			$scope.inputDetails = resp.data.inputDetails;
			$scope.chon = true;
			$scope.resetInputDetail();
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

	//PHAN QUAN LY CHI TIET PHIEU NHAP
	let urlInputDetail = `${urlInput}/detail`;
	$scope.resetInputDetail = function () {
		$scope.inputDetail = {
			id: 0,
			soLuong: 1,
			donGia: 0,
			giamGia: 0,
			vat: 10,
			input: {
				id: 0
			},
			product: {
				id: 0
			}
		}
		$scope.chonInputDetail = false;
		$scope.existProduct = false;
	}
	$scope.validExistProduct = function (value) {
		let indexInProducts = $scope.products.findIndex(p => p.id == value);
		if (indexInProducts >= 0) {
			$scope.inputDetail.product.name = $scope.products[indexInProducts].name;
			$scope.inputDetail.product.donViGoc = $scope.products[indexInProducts].donViGoc;
			$scope.existInProduct = true;
		} else {
			$scope.existInProduct = false;
		}
		let index = $scope.inputDetails.findIndex(i => i.product.id == value);
		if (index >= 0) $scope.existProduct = true;
		else $scope.existProduct = false;
	}
	$scope.updateInput = function () {
		if($scope.chonInputDetail == false){
			$http.get(`${urlInput}/${$scope.input.id}`).then(resp => {
				$scope.input.tongTien = resp.data.input.tongTien + $scope.inputDetail.donGia * $scope.inputDetail.soLuong;
				$scope.input.tongGiamGia = resp.data.input.tongGiamGia + $scope.inputDetail.giamGia;
				$scope.input.tongVAT = resp.data.input.tongVAT + (($scope.inputDetail.donGia * $scope.inputDetail.soLuong) / $scope.inputDetail.vat);
				$scope.input.thanhTien = ($scope.input.tongTien + $scope.input.tongVAT - $scope.input.tongGiamGia);
			}).catch(error => {
				$scope.input.tongTien = $scope.inputDetail.donGia * $scope.inputDetail.soLuong;
				$scope.input.tongGiamGia = $scope.inputDetail.giamGia;
				$scope.input.tongVAT = (($scope.inputDetail.donGia * $scope.inputDetail.soLuong) / $scope.inputDetail.vat);
				$scope.input.thanhTien = ($scope.input.tongTien + $scope.input.tongVAT - $scope.input.tongGiamGia);
			})
		}else{
			let index = $scope.inputDetails.findIndex(i => i.id == $scope.inputDetail.id);
			let inputDetail = $scope.inputDetails[index];
			let indexInput = $scope.inputs.findIndex(i => i.id == $scope.input.id);
			let input = $scope.inputs[indexInput]
			$scope.input.tongTien = input.tongTien + ($scope.inputDetail.donGia - inputDetail.donGia)*$scope.inputDetail.soLuong;
		}
	}
	$scope.createInputDetail = function () {
		if($scope.chon == false){
			$scope.input.id = 0;
			let dataInput = angular.copy($scope.input)
			$http.post(urlInput, dataInput).then(resp => {
				$scope.inputs.push(resp.data);
				$scope.input = resp.data;
				$scope.inputDetail.input.id = resp.data.id;
				let dataInputDetail = angular.copy($scope.inputDetail)
				$http.post(urlInputDetail, dataInputDetail).then(resp => {
					$scope.inputDetails.push(resp.data);
					swal({
						position: 'top-end',
						type: 'success',
						title: 'Thêm hàng hóa thành công',
						showConfirmButton: false,
						timer: 1500,
					});
					$scope.resetInputDetail();
					$scope.chon = true;
				}).catch(error => {
					swal({
						position: 'top-end',
						type: 'error',
						title: 'Thêm hàng hóa thất bại',
						showConfirmButton: false,
						timer: 1500
					});
				});
			}).catch(error => {
				console.log(error)
				swal({
					position: 'top-end',
					type: 'error',
					title: 'Thêm hàng hóa thất bại',
					showConfirmButton: false,
					timer: 1500
				});
			});
		}
		if($scope.chon == true){
			let dataInput = angular.copy($scope.input)
			$http.put(`${urlInput}/${dataInput.id}`, dataInput).then(resp => {
				let index = $scope.inputs.findIndex(p => p.id == dataInput.id);
				$scope.inputs[index] = resp.data;
				$scope.input = resp.data;
				$scope.inputDetail.input.id = resp.data.id;
				let dataInputDetail = angular.copy($scope.inputDetail)
				$http.post(urlInputDetail, dataInputDetail).then(resp => {
					$scope.inputDetails.push(resp.data);
					swal({
						position: 'top-end',
						type: 'success',
						title: 'Thêm hàng hóa thành công',
						showConfirmButton: false,
						timer: 1500,
					});
					$scope.resetInputDetail();
					$scope.chon = true;
				}).catch(error => {
					console.log(error)
					swal({
						position: 'top-end',
						type: 'error',
						title: 'Thêm hàng hóa thất bại',
						showConfirmButton: false,
						timer: 1500
					});
				});
			}).catch(error => {
				console.log(error)
				swal({
					position: 'top-end',
					type: 'error',
					title: 'Thêm hàng hóa thất bại',
					showConfirmButton: false,
					timer: 1500
				});
			});
		}
	}

	$scope.getAll("", 0);
	$scope.getAllProduct();
	$scope.reset();
});