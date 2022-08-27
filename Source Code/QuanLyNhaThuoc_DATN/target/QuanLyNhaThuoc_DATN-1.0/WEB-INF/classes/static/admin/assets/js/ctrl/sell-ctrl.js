let urlSell = '/api/sell';
app.controller('sell-ctrl', function ($scope, $http) {
    $scope.reset = function () {
        $scope.results = [];
        $scope.sell.loadFromLocalStorage();
    }
    $scope.search = function (kw) {
        $http.get(`${urlSell}/search?kw=${kw}`).then(resp => {
            $scope.results = resp.data;
        })
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
});
