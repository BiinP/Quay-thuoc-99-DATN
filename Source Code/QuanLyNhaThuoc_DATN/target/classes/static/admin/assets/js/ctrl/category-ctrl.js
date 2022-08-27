let urlCategory = "/api/categories";
app.controller("category-ctrl",function($scope, $http){
	$scope.categories = [];
	$scope.category = {};
	$scope.chon = false;
	$scope.start = 0;
	$scope.totalPage = 0;
	$scope.keyword = "";
	// GET DANH SACH CATEGORIES
    $scope.getAll = function(keyword, page){
    	var url = `${urlCategory}?kw=${keyword}&currentPage=${page}`;
    	$http.get(url).then(resp => {
        	$scope.categories = resp.data.content;
        	$scope.totalPage = resp.data.totalPages;
    	});
    }
 // TAO MOI CATEGORY
	$scope.reset = function(){
		$scope.category = {
			id: null,
			name: null,
			active: true,
		};
		$scope.chon = false;
	};
	// EDIT 1 CATEGORY
	$scope.edit = function(id){
		var url = `${urlCategory}/${id}`;
		$http.get(url).then(resp => {
			$scope.category = resp.data;
		});	
		$scope.chon = true;
	}
	//THEM CATEGORY
	$scope.create = function(){
		var data = angular.copy($scope.category);
		$http.post(urlCategory, data).then(resp => {
			$scope.categories.push(resp.data);
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
	// XOA CATEGORY
	$scope.delete = function(id){
		swal({
	        title: `Bạn có muốn xóa danh mục ${id}?`,
	        text: "Nếu danh mục có danh mục con sẽ chuyển sang trạng thái ngừng hoạt động",
	        type: 'warning',
	        showCancelButton: true,
	        confirmButtonColor: '#3085d6',
	        cancelButtonColor: '#d33',
	        confirmButtonText: 'Yes, delete it!',
	        confirmButtonClass: 'btn btn-success',
	        cancelButtonClass: 'btn btn-danger',
	    }).then((result) => {
	        if (result.value) {
	        	var url = `${urlCategory}/${id}`;
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
	        			var index = $scope.categories.findIndex(b => b.id == id);
		        		$scope.categories.splice(index,1);
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
	//SƯA CATEGORY
	$scope.update = function(id){
		var url = `${urlCategory}/${id}`;
		var data = angular.copy($scope.category);
		
		$http.put(url,data).then(resp => {
			var index = $scope.categories.findIndex(b => b.id == id);
			$scope.categories[index] = resp.data;
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