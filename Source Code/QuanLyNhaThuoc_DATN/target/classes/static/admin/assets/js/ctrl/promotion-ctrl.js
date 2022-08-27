let urlPromotion = "/api/promotions";
app.controller("promotion-ctrl", function ($scope, $http, $window) {
	$http.get('/api/products/all').then(resp => {
		$scope.products = resp.data;
	});
    $scope.getAll = function(keyword, page){
    	var url = `${urlPromotion}?kw=${keyword}&currentPage=${page}`;
    		$http.get(url).then(resp => {
        	$scope.promotions = resp.data.content;
        	$scope.totalPage = resp.data.totalPages;
    	});
    };
    
    $scope.validNgayKetThuc = function(date){
// let dateParse = Date.parse(date);
    	let finishDate = new Date(date);
    	let currentDate = new Date();
    	if(finishDate.getTime() < currentDate.getTime()) return false;
    	else return true;
// console.log(dateParse.getTime());
// if(date)
    }
    $scope.reset = function(){
    	var today = new Date();
		var dd = today.getDate();
		if(dd < 10){
			dd = "0"+dd;
		}
		var mm = today.getMonth() + 1;
		if(mm < 10){
			mm = "0"+mm;
		}
		var yyyy = today.getFullYear();
		
		var currentDate = `${yyyy}-${mm}-${dd}`; 
		
    	$scope.promotion = {
    		id: null,
    		ngayTao: currentDate,
    		ngayBatDau: currentDate,
    		ngayKetThuc: currentDate,
    		active: true,
    		moTa: null,
    		photo: 'photo.png',
    	};
    	$scope.chon = false;
    	$scope.promotionDetails = [];
    	$scope.resetPromotionDetail();
    }
    
    $scope.edit = function(id){
    	let url = `${urlPromotion}/${id}`;
    	$http.get(url).then(resp => {
    		$scope.promotion = resp.data;
    		$scope.chon = true;
    	});
    	let detailUrl = `${urlPromotion}/promotion-detail/${id}`;
    	$http.get(detailUrl).then(resp => {
    		$scope.resetPromotionDetail();
    		$scope.promotionDetails = resp.data;
    	})
    };
    $scope.redirectTo = function(page){
    	$window.location.href = `/admin#!/${page}`;
    }
	$scope.imageChanged = function(files){
        var urlUpload = "/api/upload/promotion";
        var form = new FormData();
        form.append("file",files[0]);
        $http.post(urlUpload, form, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        }).then(resp => {
            $scope.promotion.photo = resp.data.fileName;
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
    
    $scope.create = function(){
		var data = angular.copy($scope.promotion);
		$http.post(urlPromotion, data).then(resp => {
			$scope.promotions.push(resp.data);
			swal({
		        position: 'top-end',
		        type: 'success',
		        title: 'Thêm khuyến mãi thành công',
		        showConfirmButton: false,
		        timer: 1500
		    });
			$scope.chon = true;
		}).catch(error => {
			swal({
		        position: 'top-end',
		        type: 'error',
		        title: 'Thêm khuyến mãi thất bại',
		        showConfirmButton: false,
		        timer: 1500
		    });
		});
	};
    
	
	$scope.delete = function(id){
		swal({
	        title: `Bạn có muốn xóa khuyến mãi ${id}?`,
	        type: 'warning',
	        showCancelButton: true,
	        confirmButtonColor: '#3085d6',
	        cancelButtonColor: '#d33',
	        confirmButtonText: 'Yes, delete it!',
	        confirmButtonClass: 'btn btn-success',
	        cancelButtonClass: 'btn btn-danger',
	    }).then((result) => {
	        if (result.value) {
	        	var url = `${urlPromotion}/${id}`;
	        	$http.delete(url).then(resp => {
	        			var index = $scope.promotions.findIndex(p => p.id == id);
		        		$scope.promotions.splice(index,1);
		        		swal({
		        			position: 'top-end',
		        			type: 'success',
		        			title: 'Xóa khuyến mãi thành công',
		        			showConfirmButton: false,
		        			timer: 1500
		        		});
		        		$scope.reset();
	        	}).catch(error => {
	        		swal({
	        			position: 'top-end',
	        			type: 'error',
	        			title: 'Xóa khuyến mãi thất bại',
	        			showConfirmButton: false,
	        			timer: 1500
	        		});
	        		$scope.reset();
	        	});
	        }
	    })
	};
	$scope.update = function(id){
		var url = `${urlPromotion}/${id}`;
		var data = angular.copy($scope.promotion);
		
		$http.put(url,data).then(resp => {
			var index = $scope.promotions.findIndex(p => p.id == id);
			$scope.promotions[index] = resp.data;
			swal({
    			position: 'top-end',
    			type: 'success',
    			title: 'Cập nhật khuyến mãi thành công',
    			showConfirmButton: false,
    			timer: 1500
    		});
		}).catch(error => {
			swal({
    			position: 'top-end',
    			type: 'error',
    			title: 'Cập nhật khuyến mãi thất bại',
    			showConfirmButton: false,
    			timer: 1500
    		});
		});
	};
	
	$scope.search = function(keyword){
		$scope.start = 0; 
		$scope.getAll(keyword,$scope.start);
	};
	
	$scope.updatePagination = function(start){
		if(start <= 0){
			$('#first').addClass('disabled');
			$('#prev').addClass('disabled');
			$('#last').removeClass('disabled');
			$('#next').removeClass('disabled');
		}
		if(start >= $scope.totalPage-1){
			$('#last').addClass('disabled');
			$('#next').addClass('disabled');
			$('#first').removeClass('disabled');
			$('#prev').removeClass('disabled');
		}
		if(start > 0 && start < $scope.totalPage-1){
			$('#first').removeClass('disabled');
			$('#prev').removeClass('disabled');
			$('#last').removeClass('disabled');
			$('#next').removeClass('disabled');
		}
	}
	
	$scope.next = function(){
		var keyword = angular.copy($scope.keyword);
		if($scope.start < $scope.totalPage-1){
            $scope.start ++;
            $scope.getAll(keyword, $scope.start);
            $scope.updatePagination($scope.start);
        } 
	}
	$scope.prev = function(){
		var keyword = angular.copy($scope.keyword);
		if($scope.start > 0){
            $scope.start --;
            $scope.getAll(keyword, $scope.start);
            $scope.updatePagination($scope.start);
        }
	}
	$scope.first = function(){
		var keyword = angular.copy($scope.keyword);
		$scope.start = 0;
		$scope.getAll(keyword, $scope.start);
		$scope.updatePagination($scope.start);
	}
	$scope.last = function(){
		var keyword = angular.copy($scope.keyword);
		$scope.start = $scope.totalPage -1;
		$scope.getAll(keyword, $scope.start);
		$scope.updatePagination($scope.start);
	}
	
	// PHAN CHI TIET KHUYEN MAI
	let urlPromotionDetail = `${urlPromotion}/promotion-detail`;
	$scope.editPromoDetail = function(id){
		let url = `${urlPromotionDetail}/detail/${id}`;
		$http.get(url).then(resp => {
    		$scope.promotionDetail = resp.data;
    	});
		$scope.chonPromotionDetail = true;
	}
	$scope.resetPromotionDetail = function(){
		$scope.promotionDetail = {
			id : 'Mã tự động',
			discount: 0,
			promotion : {
				id: null
			},
			product: {
				id: 0
			}
		}
		$scope.chonPromotionDetail = false;
		$scope.existProduct = false;
		$scope.validExistProduct($scope.promotionDetail.product.id)
	}
	$scope.validExistProduct = function(value){
		 let index = $scope.promotionDetails.findIndex(p => p.product.id == value);
		 if(index >= 0) $scope.existProduct = true;
		 else $scope.existProduct = false;
	}
	$scope.createPromoDetail = function(){
		$scope.promotionDetail.id = 0;
		$scope.promotionDetail.promotion.id = $scope.promotion.id;
		let data = angular.copy($scope.promotionDetail);		
		$http.post(urlPromotionDetail, data).then(resp => {
			$scope.promotionDetails.push(resp.data);
			swal({
		        position: 'top-end',
		        type: 'success',
		        title: 'Thêm sản phẩm khuyến mãi thành công',
		        showConfirmButton: false,
		        timer: 1500,
		    });
			$scope.resetPromotionDetail();
		}).catch(error => {
			swal({
		        position: 'top-end',
		        type: 'error',
		        title: 'Thêm sản phẩm khuyến mãi thất bại',
		        showConfirmButton: false,
		        timer: 1500
		    });
		});
	}
	$scope.updatePromoDetail = function(id){
		let url = `${urlPromotionDetail}/${id}`;
		$scope.promotionDetail.promotion.id = $scope.promotion.id;
		let data = angular.copy($scope.promotionDetail);
		$http.put(url,data).then(resp => {
			var index = $scope.promotionDetails.findIndex(p => p.id == id);
			$scope.promotionDetails[index] = resp.data;
			swal({
    			position: 'top-end',
    			type: 'success',
    			title: 'Cập nhật sản phẩm khuyến mãi thành công',
    			showConfirmButton: false,
    			timer: 1500
    		});
		}).catch(error => {
			swal({
    			position: 'top-end',
    			type: 'error',
    			title: 'Cập nhật sản phẩm khuyến mãi thất bại',
    			showConfirmButton: false,
    			timer: 1500
    		});
		});
	}
	$scope.deletePromoDetail = function(id){
		swal({
	        title: `Bạn có muốn xóa sản phẩm khuyến mãi ${id}?`,
	        type: 'warning',
	        showCancelButton: true,
	        confirmButtonColor: '#3085d6',
	        cancelButtonColor: '#d33',
	        confirmButtonText: 'Yes, delete it!',
	        confirmButtonClass: 'btn btn-success',
	        cancelButtonClass: 'btn btn-danger',
	    }).then((result) => {
	        if (result.value) {
	        	var url = `${urlPromotionDetail}/${id}`;
	        	$http.delete(url).then(resp => {
	        			var index = $scope.promotionDetails.findIndex(p => p.id == id);
		        		$scope.promotionDetails.splice(index,1);
		        		swal({
		        			position: 'top-end',
		        			type: 'success',
		        			title: 'Xóa sản phẩm khuyến mãi thành công',
		        			showConfirmButton: false,
		        			timer: 1500
		        		});
		        		$scope.resetPromotionDetail();
	        	}).catch(error => {
	        		swal({
	        			position: 'top-end',
	        			type: 'error',
	        			title: 'Xóa sản phẩm khuyến mãi thất bại',
	        			showConfirmButton: false,
	        			timer: 1500
	        		});
	        		$scope.resetPromotionDetail();
	        	});
	        }
	    })
	};
	
	
	$scope.getAll("",0);
    $scope.reset();
});