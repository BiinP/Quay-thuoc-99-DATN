let urlSubCategory = "/api/sub-categories";
app.controller("subCategory-ctrl",function($scope, $http){
	$scope.subCategories = [];
	$scope.subCategory = {};
	$scope.chon = false;
	$scope.start = 0;
	$scope.totalPage = 0;
	$scope.keyword = "";
	// GET DANH SACH SUBCATEGORIES
    $scope.getAll = function(keyword, page){
    	var url = `${urlSubCategory}?kw=${keyword}&currentPage=${page}`;
    	$http.get(url).then(resp => {
        	$scope.subCategories = resp.data.content;
        	$scope.totalPage = resp.data.totalPages;
    	});
    }
    //GET DANH SACH CATEGORY
    $http.get('/api/categories').then(resp => {
    	$scope.categories = resp.data.content;
    });
 // TAO MOI SUBCATEGORY
	$scope.reset = function(){
		$scope.subCategory = {
			id: null,
			name: null,
			photo:"photo.png",
			category: {
				id: "thuc-pham-chuc-nang"
			},
			active: true,
		};
		$scope.chon = false;
	};
	// UPLOAD AVATAR
	$scope.imageChanged = function(files){
        var urlUpload = "/api/upload/sub-category";
        var form = new FormData();
        form.append("file",files[0]);
        $http.post(urlUpload, form, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        }).then(resp => {
            $scope.subCategory.photo = resp.data.fileName;
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
	// EDIT 1 SUBCATEGORY
	$scope.edit = function(id){
		var url = `${urlSubCategory}/${id}`;
		$http.get(url).then(resp => {
			$scope.subCategory = resp.data;
		});	
		$scope.chon = true;
	}
	//THEM SUBCATEGORY
	$scope.create = function(){
		var data = angular.copy($scope.subCategory);
		$http.post(urlSubCategory, data).then(resp => {
			$scope.subCategories.push(resp.data);
			$scope.reset();
			swal({
		        position: 'top-end',
		        type: 'success',
		        title: 'Thêm danh mục thành công',
		        showConfirmButton: false,
		        timer: 1500
		    });
		}).catch(error => {
			swal({
		        position: 'top-end',
		        type: 'error',
		        title: 'Thêm danh mục thất bại',
		        showConfirmButton: false,
		        timer: 1500
		    });
		});
	}
	// XOA SUBCATEGORY
	$scope.delete = function(id){
		swal({
	        title: `Bạn có muốn xóa danh mục ${id}?`,
	        text: "Nếu danh mục có sản phẩm sẽ chuyển sang trạng thái ngừng hoạt động",
	        type: 'warning',
	        showCancelButton: true,
	        confirmButtonColor: '#3085d6',
	        cancelButtonColor: '#d33',
	        confirmButtonText: 'Yes, delete it!',
	        confirmButtonClass: 'btn btn-success',
	        cancelButtonClass: 'btn btn-danger',
	    }).then((result) => {
	        if (result.value) {
	        	var url = `${urlSubCategory}/${id}`;
	        	$http.delete(url).then(resp => {
	        		if(resp.data.isExist){
	        			swal({
		        			position: 'top-end',
		        			type: 'success',
		        			title: 'Danh mục đã ngừng hoạt động',
		        			showConfirmButton: false,
		        			timer: 1500
		        		});
	        			$scope.reset();
	        		}else{
	        			var index = $scope.subCategories.findIndex(b => b.id == id);
		        		$scope.subCategories.splice(index,1);
		        		swal({
		        			position: 'top-end',
		        			type: 'success',
		        			title: 'Xóa danh mục thành công',
		        			showConfirmButton: false,
		        			timer: 1500
		        		});
		        		$scope.reset();
	        		}
	        		if(!resp.data.isFound){
	        			swal({
		        			position: 'top-end',
		        			type: 'error',
		        			title: 'Không tìm thấy danh mục',
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
	        			title: 'Xóa danh mục thất bại',
	        			showConfirmButton: false,
	        			timer: 1500
	        		});
	        		$scope.reset();
	        	});
	        }
	    })
	}
	//SƯA SUBCATEGORY
	$scope.update = function(id){
		var url = `${urlSubCategory}/${id}`;
		var data = angular.copy($scope.subCategory);
		
		$http.put(url,data).then(resp => {
			var index = $scope.subCategories.findIndex(b => b.id == id);
			$scope.subCategories[index] = resp.data;
			swal({
    			position: 'top-end',
    			type: 'success',
    			title: 'Cập nhật danh mục thành công',
    			showConfirmButton: false,
    			timer: 1500
    		});
		}).catch(error => {
			swal({
    			position: 'top-end',
    			type: 'error',
    			title: 'Cập nhật danh mục thất bại',
    			showConfirmButton: false,
    			timer: 1500
    		});
		});
	}
	$scope.updatePagination = function(start){
		console.log(start);
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