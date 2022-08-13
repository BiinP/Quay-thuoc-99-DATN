let urlOrder = '/api/orders';
app.controller('order-ctrl', function($scope, $http){
	$scope.chon = false;
	$scope.start = 0;
	$scope.totalPage = 0;
	$scope.keyword = "";

	$scope.getAll = function(keyword, page){
    	let url = `${urlOrder}?kw=${keyword}&currentPage=${page}`;
    	$http.get(url).then(resp => {
        	$scope.orders = resp.data.content;
        	$scope.totalPage = resp.data.totalPages;
    	});
    }

    $scope.edit = function(id){
		var url = `${urlOrder}/detail/${id}`;
		console.log(url)
		$http.get(url).then(resp => {
			$scope.order = resp.data;
		});	
		$scope.chon = true;
	}

    $scope.create = function(){
		var data = angular.copy($scope.order);
		$http.post(urlOrder, data).then(resp => {
			$scope.orders.push(resp.data);
			$scope.reset();
			swal({
		        position: 'top-end',
		        type: 'success',
		        title: 'Thêm đơn vị thành công',
		        showConfirmButton: false,
		        timer: 1500
		    });
		}).catch(error => {
			swal({
		        position: 'top-end',
		        type: 'error',
		        title: 'Thêm đơn vị thất bại',
		        showConfirmButton: false,
		        timer: 1500
		    });
		});
	};

	$scope.delete = function(id){
		swal({
	        title: `Bạn có muốn xóa đơn vị ${id}?`,
	        text: "Nếu đơn vị đã có hàng hóa sẽ chuyển sang trạng thái ngừng hoạt động",
	        type: 'warning',
	        showCancelButton: true,
	        confirmButtonColor: '#3085d6',
	        cancelButtonColor: '#d33',
	        confirmButtonText: 'Yes, delete it!',
	        confirmButtonClass: 'btn btn-success',
	        cancelButtonClass: 'btn btn-danger',
	    }).then((result) => {
	        if (result.value) {
	        	var url = `${urlOrder}/${id}`;
	        	$http.delete(url).then(resp => {
	        		if(resp.data.isExist){
	        			swal({
		        			position: 'top-end',
		        			type: 'success',
		        			title: 'Đơn vị đã ngừng hoạt động',
		        			showConfirmButton: false,
		        			timer: 1500
		        		});
	        			$scope.reset();
	        		}else{
	        			var index = $scope.orders.findIndex(u => u.id == id);
		        		$scope.orders.splice(index,1);
		        		swal({
		        			position: 'top-end',
		        			type: 'success',
		        			title: 'Xóa đơn vị thành công',
		        			showConfirmButton: false,
		        			timer: 1500
		        		});
		        		$scope.reset();
	        		}
	        		if(!resp.data.isFound){
	        			swal({
		        			position: 'top-end',
		        			type: 'error',
		        			title: 'Không tìm thấy đơn vị',
		        			showConfirmButton: false,
		        			timer: 1500
		        		});
	        			$scope.reset();
	        		}
	        	}).catch(error => {
	        		swal({
	        			position: 'top-end',
	        			type: 'error',
	        			title: 'Xóa đơn vị thất bại',
	        			showConfirmButton: false,
	        			timer: 1500
	        		});
	        		$scope.reset();
	        	});
	        }
	    })
	};
	
	$scope.update = function(id){
		var url = `${urlOrder}/${id}`;
		var data = angular.copy($scope.order);
		
		$http.put(url,data).then(resp => {
			var index = $scope.orders.findIndex(u => u.id == id);
			$scope.orders[index] = resp.data;
			swal({
    			position: 'top-end',
    			type: 'success',
    			title: 'Cập nhật đơn vị thành công',
    			showConfirmButton: false,
    			timer: 1500
    		});
		}).catch(error => {
			swal({
    			position: 'top-end',
    			type: 'error',
    			title: 'Cập nhật đơn vị thất bại',
    			showConfirmButton: false,
    			timer: 1500
    		});
		});
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
	$scope.reset = function(){
		$scope.order = {
		};
		$scope.chon = false;
	};
	$scope.getAll("",$scope.start);
	$scope.reset();
});
