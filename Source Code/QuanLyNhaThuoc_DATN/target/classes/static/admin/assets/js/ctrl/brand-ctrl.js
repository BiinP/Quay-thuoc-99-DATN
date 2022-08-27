let urlBrand = '/api/brands';
app.controller('brand-ctrl', function($http, $scope){
	$scope.brands = [];
	$scope.brand = {};
	$scope.chon = false;
	$scope.start = 0;
	$scope.totalPage = 0;
	$scope.keyword = "";
	// GET DANH SACH BRANDS
    $scope.getAll = function(keyword, page){
    	var url = `${urlBrand}?kw=${keyword}&currentPage=${page}`;
    	$http.get(url).then(resp => {
        	$scope.brands = resp.data.content;
        	$scope.totalPage = resp.data.totalPages;
    	});
    }
 // TAO MOI ACCOUNT
	$scope.reset = function(){
		$scope.brand = {
			id: null,
			name: null,
			xuatXu: null,
			active: true,
			ghiChu: null,
			photo: "photo.png",
		};
		$scope.chon = false;
	};
	// EDIT 1 ACCOUNT
	$scope.edit = function(id){
		var url = `${urlBrand}/${id}`;
		$http.get(url).then(resp => {
			$scope.brand = resp.data;
		});	
		$scope.chon = true;
	}
	// UPLOAD PHOTO
	$scope.imageChanged = function(files){
        var urlUpload = "/api/upload/brand";
        var form = new FormData();
        form.append("file",files[0]);
        $http.post(urlUpload, form, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        }).then(resp => {
            $scope.brand.photo = resp.data.fileName;
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
    }
	//THEM BRAND
	$scope.create = function(){
		var data = angular.copy($scope.brand);
		$http.post(urlBrand, data).then(resp => {
			$scope.brands.push(resp.data);
			$scope.reset();
			swal({
		        position: 'top-end',
		        type: 'success',
		        title: 'Thêm thương hiệu thành công',
		        showConfirmButton: false,
		        timer: 1500
		    });
		}).catch(error => {
			swal({
		        position: 'top-end',
		        type: 'error',
		        title: 'Thêm thương hiệu thất bại',
		        showConfirmButton: false,
		        timer: 1500
		    });
		});
	}
	// XOA ACCOUNT
	$scope.delete = function(id){
		swal({
	        title: `Bạn có muốn xóa thương hiệu ${id}?`,
	        text: "Nếu thương hiệu có sản phẩm sẽ chuyển sang trạng thái ngừng hoạt động",
	        type: 'warning',
	        showCancelButton: true,
	        confirmButtonColor: '#3085d6',
	        cancelButtonColor: '#d33',
	        confirmButtonText: 'Yes, delete it!',
	        confirmButtonClass: 'btn btn-success',
	        cancelButtonClass: 'btn btn-danger',
	    }).then((result) => {
	        if (result.value) {
	        	var url = `${urlBrand}/${id}`;
	        	$http.delete(url).then(resp => {
	        		if(resp.data.isExist){
	        			swal({
		        			position: 'top-end',
		        			type: 'success',
		        			title: 'Thương hiệu đã ngừng hoạt động',
		        			showConfirmButton: false,
		        			timer: 1500
		        		});
	        			$scope.reset();
	        		}else{
	        			var index = $scope.brands.findIndex(b => b.id == id);
		        		$scope.brands.splice(index,1);
		        		swal({
		        			position: 'top-end',
		        			type: 'success',
		        			title: 'Xóa thương hiệu thành công',
		        			showConfirmButton: false,
		        			timer: 1500
		        		});
		        		$scope.reset();
	        		}
	        		if(!resp.data.isFound){
	        			swal({
		        			position: 'top-end',
		        			type: 'error',
		        			title: 'Không tìm thấy thương hiệu',
		        			showConfirmButton: false,
		        			timer: 1500
		        		});
	        			$scope.reset();
	        		}
	        	}).catch(error => {
	        		console.log(error);
	        		swal({
	        			position: 'top-end',
	        			type: 'error',
	        			title: 'Xóa thương hiệu thất bại',
	        			showConfirmButton: false,
	        			timer: 1500
	        		});
	        		$scope.reset();
	        	});
	        }
	    })
	}
	//SƯA THUONG HIEU
	$scope.update = function(id){
		var url = `${urlBrand}/${id}`;
		var data = angular.copy($scope.brand);
		
		$http.put(url,data).then(resp => {
			var index = $scope.brands.findIndex(b => b.id == id);
			$scope.brands[index] = resp.data;
			swal({
    			position: 'top-end',
    			type: 'success',
    			title: 'Cập nhật thương hiệu thành công',
    			showConfirmButton: false,
    			timer: 1500
    		});
		}).catch(error => {
			swal({
    			position: 'top-end',
    			type: 'error',
    			title: 'Cập nhật thương hiệu thất bại',
    			showConfirmButton: false,
    			timer: 1500
    		});
		});
	}
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
	$scope.search = function(keyword){
		$scope.start = 0; 
		$scope.getAll(keyword,$scope.start);
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