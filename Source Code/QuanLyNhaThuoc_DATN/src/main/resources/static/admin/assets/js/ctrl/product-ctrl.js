let urlProduct = "/api/products";
app.controller("product-ctrl", function ($scope, $http) {
	$scope.products = [];
	$scope.product = {};
    $scope.pageSize = 10;
    $scope.subCates = [];
    $scope.brands = [];
	$scope.chon = false;
	$scope.start = 0;
	$scope.totalPage = 0;
	$scope.keyword = "";
	
    $scope.getAll = function(keyword, page){
    	var url = `${urlProduct}?kw=${keyword}&currentPage=${page}`;
    	$http.get(url).then(resp => {
        	$scope.products = resp.data.content;
        	$scope.totalPage = resp.data.totalPages;
    	});
    };
    
    $http.get('/api/sub-categories').then(resp => {
    	$scope.subCates = resp.data.content;
    });
    
    $http.get('/api/brands').then(resp => {
    	$scope.brands = resp.data.content;
    });
    
    $scope.reset = function(){
    	$scope.chon = false;
    	$scope.product = {
    		id: 'Mã tự động',
    		name: null,
    		soDangKy: null,
    		donViGoc: null,
    		rx: false,
    		active: true,
    		ghiChu: null,
    		photo: 'photo.png',
    		subCategory: {
    			id: ''
    		},
    		brand: {
    			id: 'OBG'
    		}
    	};
    }
    
    $scope.edit = function(id){
    	let url = `${urlProduct}/${id}`;
    	$http.get(url).then(resp => {
    		$scope.product = resp.data;
    		$scope.chon = true;
    	});
    };
    
	$scope.imageChanged = function(files){
        var urlUpload = "/api/upload/product";
        var form = new FormData();
        form.append("file",files[0]);
        $http.post(urlUpload, form, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
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
    
    $scope.create = function(){
    	$scope.product.id = 0;
		var data = angular.copy($scope.product);
		$http.post(urlProduct, data).then(resp => {
			$scope.products.push(resp.data);
			$scope.reset();
			swal({
		        position: 'top-end',
		        type: 'success',
		        title: 'Thêm sản phẩm thành công',
		        showConfirmButton: false,
		        timer: 1500
		    });
		}).catch(error => {
			swal({
		        position: 'top-end',
		        type: 'error',
		        title: 'Thêm sản phẩm thất bại',
		        showConfirmButton: false,
		        timer: 1500
		    });
		});
	};
    
	$scope.delete = function(id){
		swal({
	        title: `Bạn có muốn xóa sản phẩm ${id}?`,
	        text: "Nếu sản phẩm đã có hàng hóa và phiếu nhập sẽ chuyển sang trạng thái ngừng hoạt động",
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
	        		if(resp.data.isExist){
	        			swal({
		        			position: 'top-end',
		        			type: 'success',
		        			title: 'Sản phẩm đã ngừng hoạt động',
		        			showConfirmButton: false,
		        			timer: 1500
		        		});
	        			$scope.reset();
	        		}else{
	        			var index = $scope.products.findIndex(p => p.id == id);
		        		$scope.products.splice(index,1);
		        		swal({
		        			position: 'top-end',
		        			type: 'success',
		        			title: 'Xóa sản phẩm thành công',
		        			showConfirmButton: false,
		        			timer: 1500
		        		});
		        		$scope.reset();
	        		}
	        		if(!resp.data.isFound){
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
	
	$scope.update = function(id){
		var url = `${urlProduct}/${id}`;
		var data = angular.copy($scope.product);
		
		$http.put(url,data).then(resp => {
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
	
    $scope.reset();
    $scope.getAll("",0);
});