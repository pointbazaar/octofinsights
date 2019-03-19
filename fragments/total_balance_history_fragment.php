<div style="width:600px; height: 400px;" class="m-3">
    <!-- the chart is responsive to the size of the parent div -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.7.3/Chart.bundle.js">
    </script>

    <h2 class="text-center">Total Balance History</h2>
    <canvas id="chart_balance_history"></canvas>
    <script>
        var balance_changes = [
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

        var balance_history = [];

        for(var i=1;i<=balance_changes.length;i++){
            var element=balance_changes.map(x=>x.total).slice(0,i).reduce((a,b)=>a+b);


            balance_history.push(element);
        }

        console.log(balance_history);

        var ctx=document.getElementById("chart_balance_history").getContext("2d");
        var myChart = new Chart(ctx,
            {
                type:'line',
                data:{
                    labels:balance_changes.map(x=>x.month).map(str=>"End "+str),
                    datasets:[
                        {
                            label:"balance",
                            data:balance_history,
                            backgroundColor:"black",
                            borderColor:"black",
                            fill:false
                        }
                    ]
                },
                options:{
                    scales:{
                        xAxes:[{
                            ticks:{
                                autoSkip:false
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
