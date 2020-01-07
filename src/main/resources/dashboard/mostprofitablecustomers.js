var ctx4 = document.getElementById('myChartCustomerProfits').getContext('2d');


fetch("/api/mostprofitablecustomers").then(data=>data.json()).then(data=>{draw_most_profitable(data);});

var myChartCustomerProfits;

function draw_most_profitable(values){

    var debug=true;
    if(debug){
        console.log("draw_most_profitable");
        console.log(values);
    }

    myChartCustomerProfits = new Chart(ctx4, {
        type: 'bar',
        data: {
            labels: values.map(x=>x.label),
            datasets: [{
                label: 'Most profitable Customers',
                data: values.map(x=>x.value),
                fill:true,
                backgroundColor:"grey",
                borderColor:"grey"
            }]
        },
        options: {
            responsive:true,
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