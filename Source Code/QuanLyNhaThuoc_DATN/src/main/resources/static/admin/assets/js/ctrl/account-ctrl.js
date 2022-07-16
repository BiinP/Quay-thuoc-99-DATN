let urlAccount = '/api/accounts';
app.controller('account-ctrl', function($scope, $http){
	$scope.accounts = [];
	$scope.account = {};
	$scope.roles = [];
	$scope.chon = false;
	$scope.start = 0;
	$scope.totalPage = 0;
	$scope.keyword = "";
//    $scope.pageSize = 10;
	// GET DANH SACH ACCOUNTS
    $scope.getAll = function(keyword, page){
    	var url = `${urlAccount}?kw=${keyword}&currentPage=${page}`;
    	$http.get(url).then(resp => {
        	$scope.accounts = resp.data.content;
        	$scope.totalPage = resp.data.totalPages;
    	});
    }
	// GET DANH SACH VAI TRO
	$http.get('api/roles').then(resp => {
		$scope.roles = resp.data;
	});
	// EDIT 1 ACCOUNT
	$scope.edit = function(id){
		var url = `${urlAccount}/${id}`;
		$http.get(url).then(resp => {
			$scope.account = resp.data;
		});	
		$scope.chon = true;
	}
	// UPLOAD AVATAR
	$scope.imageChanged = function(files){
        var urlUpload = "/api/upload/account";
        var form = new FormData();
        form.append("file",files[0]);
        $http.post(urlUpload, form, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        }).then(resp => {
            $scope.account.avatar = resp.data.fileName;
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
	//THEM ACCOUNT
	$scope.create = function(){
		var data = angular.copy($scope.account);
		console.log(data);
		$http.post(urlAccount, data).then(resp => {
			$scope.accounts.push(resp.data);
			$scope.reset();
			swal({
		        position: 'top-end',
		        type: 'success',
		        title: 'Thêm tài khoản thành công',
		        showConfirmButton: false,
		        timer: 1500
		    });
		}).catch(error => {
			swal({
		        position: 'top-end',
		        type: 'error',
		        title: 'Thêm tài khoản thất bại',
		        showConfirmButton: false,
		        timer: 1500
		    });
		});
	}
	// XOA ACCOUNT
	$scope.delete = function(id){
		swal({
	        title: `Bạn có muốn xóa tài khoản ${id}?`,
	        text: "Nếu tài khoản đã có đơn hàng sẽ chuyển sang trạng thái ngừng hoạt động",
	        type: 'warning',
	        showCancelButton: true,
	        confirmButtonColor: '#3085d6',
	        cancelButtonColor: '#d33',
	        confirmButtonText: 'Yes, delete it!',
	        confirmButtonClass: 'btn btn-success',
	        cancelButtonClass: 'btn btn-danger',
	    }).then((result) => {
	        if (result.value) {
	        	var url = `${urlAccount}/${id}`;
	        	$http.delete(url).then(resp => {
	        		if(resp.data.isExist){
	        			swal({
		        			position: 'top-end',
		        			type: 'success',
		        			title: 'Tài khoản đã ngừng hoạt động',
		        			showConfirmButton: false,
		        			timer: 1500
		        		});
	        			$scope.reset();
	        		}else{
	        			var index = $scope.accounts.findIndex(acc => acc.email == id);
		        		$scope.accounts.splice(index,1);
		        		swal({
		        			position: 'top-end',
		        			type: 'success',
		        			title: 'Xóa tài khoản thành công',
		        			showConfirmButton: false,
		        			timer: 1500
		        		});
		        		$scope.reset();
	        		}
	        		if(!resp.data.isFound){
	        			swal({
		        			position: 'top-end',
		        			type: 'error',
		        			title: 'Không tìm thấy tài khoản',
		        			showConfirmButton: false,
		        			timer: 1500
		        		});
	        			$scope.reset();
	        		}
	        	}).catch(error => {
	        		swal({
	        			position: 'top-end',
	        			type: 'error',
	        			title: 'Xóa tài khoản thất bại',
	        			showConfirmButton: false,
	        			timer: 1500
	        		});
	        		$scope.reset();
	        	});
	        }
	    })
	}
	$scope.update = function(id){
		var url = `${urlAccount}/${id}`;
		var data = angular.copy($scope.account);
		
		$http.put(url,data).then(resp => {
			var index = $scope.accounts.findIndex(acc => acc.email == id);
			$scope.accounts[index] = resp.data;
			swal({
    			position: 'top-end',
    			type: 'success',
    			title: 'Cập nhật tài khoản thành công',
    			showConfirmButton: false,
    			timer: 1500
    		});
		}).catch(error => {
			swal({
    			position: 'top-end',
    			type: 'error',
    			title: 'Cập nhật tài khoản thất bại',
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
	// TAO MOI ACCOUNT
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
		
		$scope.account = {
			email: "",
			hoTen: "",
			gioiTinh: true,
			ngaySinh: currentDate,
			sdt: "",
			ngayTao: currentDate,
			diem: 0.0,
			matKhau: "",
			avatar: "avatar.png",
			ghiChu: null,
			active: true,
			role: {
				id: "customer"
			}
		};
		$scope.chon = false;
	};
	$scope.getAll("",$scope.start);
	$scope.reset();
});
