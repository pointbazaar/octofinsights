<div style="width:600px; height: 400px;" class="m-3">
    <!-- the chart is responsive to the size of the parent div -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.7.3/Chart.bundle.js">
    </script>

    <h2 class="text-center">Cash Flow History</h2>
    <canvas id="myChart"></canvas>
    <script>
        var monthly_totals = [
            {
                month:"February 2019",
                total:-10
            },
            {
                month:"March 2019",
                total:10
            },
            {
                month:"April 2019",
                total:300
            },
            {
                month:"June 2019",
                total:30
            },
            {
                month:"July 2019",
                total:-10
            },
            {
                month:"August 2019",
                total:100
            },
            {
                month:"October 2019",
                total:80
            },
            {
                month:"November 2019",
                total:-20
            }
        ];

        var ctx=document.getElementById("myChart").getContext("2d");
        var myChart = new Chart(ctx,
            {
                type:'bar',
                data:{
                    labels:monthly_totals.map(x=>x.month),
                    datasets:[
                        {
                            label:"cashflow",
                            data:monthly_totals.map(x=>x.total),
                            backgroundColor:monthly_totals.map(x=>(x.total>=0)?"blue":"red")
                        }
                    ]
                },
                options:{
                    scales:{
                        yAxes:[{
                            ticks: {
                                beginAtZero:true
                            }
                        }]
                    }
                }
            }
        );
        myChart.aspectRatio=2;
        myChart.maintainAspectRatio=true;

    </script>
</div>
