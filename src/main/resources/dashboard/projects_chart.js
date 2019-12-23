var ctx3 = document.getElementById('myChartProjects').getContext('2d');


fetch("/api/projects_gantt").then(data=>data.json()).then(data=>{draw_projects(data);});

var myChart3;

function make_data_item(record){

    return {
        label: record.project_name,
        backgroundColor: "rgba(246,156,85,1)",
        borderColor: "rgba(246,156,85,1)",
        fill: false,
        borderWidth : 15,
        pointRadius : 0,
        data: [
            {
             x: 0,
             y: record.index
            }, {
             x: 3,
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
                labels: [0,1,2,3],
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
                            max : values.length,
                            display: false
                        }

                    }]

                }
            }
        });
}
