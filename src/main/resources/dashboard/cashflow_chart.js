var ctx = document.getElementById('myChart').getContext('2d');
var ctx2 = document.getElementById('myChartBusinessValue').getContext('2d');


fetch("/api/cashflow").then(data=>data.json()).then(data=>{draw_sales(data);});
fetch("/api/businessvaluehistory").then(data=>data.json()).then(data=>{draw_business_value_history(data);});


//to be able to manipulate in browser
var myChart;
var myChart2;

function draw_sales(values){

    var debug=true;
        if(debug){
            console.log("draw_sales");
            console.log(values);
        }

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

function draw_business_value_history(values){

    var debug=true;
    if(debug){
        console.log("draw_business_value_history");
        console.log(values);
    }

    var partial_sums=[];

    for(var i=1;i<=values.length;i++){
        partial_sums.push(
            values
                .slice(0,i)
                .map(x=>x.value)
                .reduce((a,b)=>a+b)
        );
    }


    myChart2 = new Chart(ctx2, {
        type: 'line',
        data: {
            labels: values.map(x=>x.label),
            datasets: [{
                label: 'Total Business Value over Time',
                //data: values.map(x=>x.value),
                data: partial_sums,
                fill:false,
                backgroundColor:"black",
                borderColor:"black"
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
            },
            elements:{
                line:{
                    tension:0
                }
            }
        }
    });
}
