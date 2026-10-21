// 後台首頁：近 30 天甜點營收長條圖（單一數列，不需圖例；移到長條上顯示金額與訂單數）
$(function () {
    const canvas = document.getElementById('revenue-chart');
    if (!canvas || typeof Chart === 'undefined') {
        return;
    }
    const trend = Array.isArray(DASHBOARD_TREND) ? DASHBOARD_TREND : [];
    const labels = trend.map(function (day) { return day.date.substr(5).replace('-', '/'); });
    const revenue = trend.map(function (day) { return day.revenue; });
    const orders = trend.map(function (day) { return day.orders; });

    new Chart(canvas, {
        type: 'bar',
        data: {
            labels: labels,
            datasets: [{
                label: '營收',
                data: revenue,
                backgroundColor: '#e91e63',
                hoverBackgroundColor: '#ad1457',
                borderWidth: 0
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            legend: { display: false },
            tooltips: {
                callbacks: {
                    title: function (items) { return trend[items[0].index].date; },
                    label: function (item) {
                        return '營收 $' + Number(item.yLabel).toLocaleString() + '｜' + orders[item.index] + ' 筆訂單';
                    }
                }
            },
            scales: {
                xAxes: [{
                    gridLines: { display: false },
                    barPercentage: 0.7,
                    categoryPercentage: 0.9,
                    ticks: { autoSkip: true, maxTicksLimit: 10, maxRotation: 0, fontColor: '#888' }
                }],
                yAxes: [{
                    gridLines: { color: '#f0f0f0', zeroLineColor: '#ddd' },
                    ticks: {
                        beginAtZero: true,
                        maxTicksLimit: 5,
                        fontColor: '#888',
                        callback: function (value) { return '$' + Number(value).toLocaleString(); }
                    }
                }]
            }
        }
    });
});
