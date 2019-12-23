var ctx3 = document.getElementById('myChartProjects').getContext('2d');


fetch("/api/projects_gantt").then(data=>data.json()).then(data=>{draw_projects(data);});

var myChart3;

function make_data_item(record){
    var r1 = Math.floor(Math.random()*256);
    var r2 = Math.floor(Math.random()*256);
    var r3 = Math.floor(Math.random()*256);

    var color = "rgba("+r1+","+r2+","+r3+",1)";

    return {
        label: record.project_name,
        backgroundColor: color,
        borderColor: color,
        fill: false,
        borderWidth : 10,
        pointRadius : 0,
        data: [
            {
             x: record.start_month,
             y: record.index
            }, {
             x: record.end_month,
             y: record.index
            }
        ]
    };
}

function draw_projects(values){

    console.log("values");
    console.log(values);

    var myChart3 = new Chart(ctx3, {
            type: 'line',
            data: {
                labels: [0,1,2,3,4,5,6,7,8,9,10,11,12],
                datasets: values.map(x => make_data_item(x))
            },
            options: {
                responsive: true,
                aspectRatio: 4,
                legend : {
                    display : true
                },
                scales: {
                    /*
                    xAxes: [{
                        type: 'linear',
                        position: 'bottom',
                        ticks : {
                            beginAtzero :true,
                            stepSize : 1
                        }
                    }],
                    */
                    yAxes : [{

                        scaleLabel : {
                            display : false
                        },
                        ticks : {
                            beginAtZero :true,
                            max : values.length+1,
                            display: false
                        }

                    }]

                }
            }
        });
}
