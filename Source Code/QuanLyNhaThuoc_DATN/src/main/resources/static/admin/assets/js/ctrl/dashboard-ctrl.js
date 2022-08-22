app.controller("dashboard-ctrl", function ($scope, $http) {
  $scope.dataBestSeller = [];
  $scope.getInformation = function () {
    $http.get('/api/report/information').then(resp => {
      $scope.information = resp.data;
    })
  }
  async function getCostInMonth () {
    $http.get('/api/report/cost-today').then(resp => {
      $scope.costToday = resp.data;
      $scope.inMonth = (new Date()).getMonth();
    })
    const { data } = await $http.get('/api/report/cost-today');
    return data.costInMonth;
  }
  $scope.loadBieuDoCostInMonth = async function() {
    let inMonth = (new Date()).getMonth()+1;
    let lastMonth = (new Date()).getMonth();
    const data = await getCostInMonth();
    var morrisdemo3 = jQuery("#morrisdemo3");
    if (morrisdemo3.length > 0) {
      Morris.Bar({
        element: morrisdemo3,
        data: data,
        xkey: 'd',
        ykeys: ['lastMonth', 'inMonth'],
        labels: [`Tháng ${lastMonth}`, `Tháng ${inMonth}`],
        barColors: ['#4776E6', '#8E54E9'],
        resize: true,
        fillOpacity: 0.4,
        padding: 15,
        grid: true,
        gridTextFamily: 'Roboto',
        gridTextSize: 10
      });
    }
  }
  // const days = getDaysInMonth(7,2022)
  async function getDataForBestSeller() {
    const { data } = await $http.get("/api/report/best-seller");
    return data;
  }
  $scope.loadBieuDoBestSeller = async function () {
    const data = await getDataForBestSeller();
    var apexdemo8 = jQuery('#apexdemo8')
    if (apexdemo8.length > 0) {
      var optionDonut = {
        chart: {

          type: 'pie',
          height: '350'
        },
        dataLabels: {
          enabled: false,
        },
        plotOptions: {
          pie: {
            donut: {
              size: '75%',
            },
            offsetY: 0,
          },
          stroke: {
            colors: undefined
          },

        },
        colors: ['#8E54E9', '#2bcbba', '#f7b731', '#45aaf2', '#e3324c'],

        series: data.lstQuantity,
        labels: data.lstProductName,
      }

      var donut = new ApexCharts(
        document.querySelector("#apexdemo8"),
        optionDonut
      )
      donut.render();

    }
  }
  async function getDataForCustomerVip() {
    const { data } = await $http.get("/api/report/customer-vip");
    return data;
  }
  $scope.loadBieuDoCustomerVip = async function () {
    const data = await getDataForCustomerVip();

    var apexdemo5 = jQuery('#apexdemo5')
    if (apexdemo5.length > 0) {
      var options = {
        chart: {
          height: 420,
          type: 'bar',
        },
        plotOptions: {
          bar: {
            horizontal: true,
          }
        },
        colors: ['#8E54E9'],
        dataLabels: {
          enabled: false
        },
        series: [{
          data: data.lstDiem
        }],
        xaxis: {
          categories: data.lstAccountName,
        }
      }

      var chart = new ApexCharts(
        document.querySelector("#apexdemo5"),
        options
      );

      chart.render();

    }
  }
  async function getDataForCustomerVipInMonth() {
    const { data } = await $http.get("/api/report/customer-vip-inmonth");
    return data;
  }
  $scope.loadBieuDoCustomerVipInMonth = async function () {
    const data = await getDataForCustomerVipInMonth();

    var apexdemo5 = jQuery('#apexdemo51')
    if (apexdemo5.length > 0) {
      var options = {
        chart: {
          height: 420,
          type: 'bar',
        },
        plotOptions: {
          bar: {
            horizontal: true,
          }
        },
        colors: ['#8E54E9'],
        dataLabels: {
          enabled: false
        },
        series: [{
          data: data.lstDiem
        }],
        xaxis: {
          categories: data.lstAccountName,
        }
      }

      var chart = new ApexCharts(
        document.querySelector("#apexdemo51"),
        options
      );

      chart.render();

    }
  }

  $scope.getInformation();
  $scope.loadBieuDoBestSeller();
  $scope.loadBieuDoCustomerVip();
  $scope.loadBieuDoCustomerVipInMonth();
  $scope.loadBieuDoCostInMonth();


});

