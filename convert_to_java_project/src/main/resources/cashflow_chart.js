var ctx = document.getElementById('myChart').getContext('2d');

fetch("/api/cashflow").then(data=>data.json()).then(data=>{draw_sales(data);});


function draw_sales(values){

    console.log(values);

    var myChart = new Chart(ctx, {
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

}