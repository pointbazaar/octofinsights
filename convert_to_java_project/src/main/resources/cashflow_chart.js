var ctx = document.getElementById('myChart').getContext('2d');
var ctx2 = document.getElementById('myChartBusinessValue').getContext('2d');


fetch("/api/cashflow").then(data=>data.json()).then(data=>{draw_sales(data);});



var myChart;

function draw_sales(values){

    console.log(values);

    myChart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: values.map(x=>x.label),
            datasets: [{
                label: 'Sales and Expenses',
                data: values.map(x=>x.value)
            }]
        },
        options: {
            scales: {
                yAxes: [{
                    ticks: {
                        beginAtZero: true
                    }
                }]
            }
        }
    });

    var partial_sums=[];

    for(var i=1;i<=values.length;i++){
        partial_sums.push(
            values
                .slice(0,i)
                .map(x=>x.value)
                .reduce((a,b)=>a+b)
        );
    }

    console.log("partial_sums:");
    console.log(partial_sums);

    var myChart2 = new Chart(ctx2, {
        type: 'line',
        data: {
            labels: values.map(x=>x.label),
            datasets: [{
                label: 'Total Business Value over Time',
                data: partial_sums
            }]
        },
        options: {
            scales: {
                yAxes: [{
                    ticks: {
                        beginAtZero: true
                    }
                }]
            }
        }
    });

}
