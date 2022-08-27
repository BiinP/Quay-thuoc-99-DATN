let urlSell = '/api/sell';
app.controller('sell-ctrl', function ($scope, $http) {
    $scope.currentAccount = currentAccount;
    $scope.reset = function () {
        $scope.results = [];
        $scope.customer = {};
        $scope.sell.loadFromLocalStorage();
    }
    $scope.search = function (kw) {
        $http.get(`${urlSell}/search?kw=${kw}`).then(resp => {
            $scope.results = resp.data;
        })
    }
    $scope.searchCustomer = function (kw) {
        if (kw != null && kw != "") {
            $http.get(`${urlSell}/account/search?kw=${kw}`).then(resp => {
                $scope.customers = resp.data;
            })
        }
    }
    $scope.addCustomer = function (email) {
        var index = $scope.customers.findIndex(item => item.email == email);
        $scope.customer = $scope.customers[index];
    }
    $scope.sell = {
        items: [],
        add(id, discount) {
            var item = this.items.find(item => item.id == id);
            if (item) {
                item.qty++;
                this.saveToLocalStorage();
            } else {
                $http.get(`${urlSell}/goods/${id}`).then(resp => {
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
            localStorage.setItem("sell", json);
        },
        clear() {
            this.items = [];
            this.saveToLocalStorage();
        },
        loadFromLocalStorage() {
            var json = localStorage.getItem("sell");
            this.items = json ? JSON.parse(json) : [];
        }
    }
    $scope.reset();
    function getOrder() {
        $scope.order = {
            id: 0,
            ngayTao: new Date(),
            sdt: angular.copy($scope.customer.sdt),
            diaChi: angular.copy($scope.customer.diaChi),
            phiGiaoHang: $scope.giaoHang ? angular.copy($scope.order.phiGiaoHang) : 0,
            ghiChu: angular.copy($scope.customer.ghiChu),
            taiCuaHang: $scope.giaoHang ? false : true,
            maNV: currentAccount.email,
            tongTien: $scope.sell.items.reduce((total, item) => {
                return total + (item.giaBan * item.qty)
            }, 0),
            tongGiamGia: $scope.sell.items.reduce((total, item) => {
                return total + (item.giaBan * item.qty)
            }, 0) - $scope.sell.amount,
            thanhTien: $scope.sell.amount + ($scope.giaoHang ? angular.copy($scope.order.phiGiaoHang) : 0),
            account: { email: angular.copy($scope.customer.email) },
            get orderDetails() {
                return $scope.sell.items.map(item => {
                    return {
                        soLuong: item.qty,
                        donGia: item.giaBan,
                        giamGia: item.giaBan * item.discount / 100,
                        goodsId: item.id
                    }
                });
            },


        }
    }
    $scope.banHang = function () {
        getOrder();
        var order = angular.copy($scope.order);
        console.log(order)

        $http.post(`${urlSell}`, order).then(resp => {
            alert("Đặt hàng thành công");
            $scope.sell.clear();
            $scope.reset();
            // location.href = "/account/order/detail/" + resp.data.id;
        }).catch(error => {
            alert("Đặt hàng thất bại");
            console.log(error);
        });
    }
    //Giao hang
    $scope.diaChi = {
        tinh: null,
        quan: null,
        phuong: null
    }
    async function getToken(){
        const {data} = await $http.get(`${urlSell}/get-token-ghn`);
        return data;
    }
    async function getTinh (){
        $scope.lstPhuong = [];
        $scope.diaChi.phuong = null;
        let authorization = await getToken();
        let token = authorization.Token;
        let shopId = authorization.ShopId;
        let req = {
            method: 'GET',
            url: 'https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/province',
            headers: {
                Token: token,
                ShopId: shopId
            }
        }
        const {data} = await $http(req);
        $scope.lstTinh = data.data;
    }
    getTinh();
    $scope.getQuan = async function(value){
        let tinh = value.replace('number:','');
        let authorization = await getToken();
        let token = authorization.Token;
        let shopId = authorization.ShopId;
        let req = {
            method: 'GET',
            url: `https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/district?province_id=${tinh}`,
            headers: {
                Token: token,
                ShopId: shopId
            }
        }
        $http(req).then(function(resp){
            $scope.lstQuan = resp.data.data;
        })
    }
    $scope.getPhuong = async function(value){
        let phuong = value.replace('number:','');
        let authorization = await getToken();
        let token = authorization.Token;
        let shopId = authorization.ShopId;
        let req = {
            method: 'GET',
            url: `https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/ward?district_id=${phuong}`,
            headers: {
                Token: token,
                ShopId: shopId
            }
        }
        $http(req).then(function(resp){
            $scope.lstPhuong = resp.data.data;
        })
    }

});
