<div style="width:40vmin;" class="m-3">
    <!-- the chart is responsive to the size of the parent div -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.7.3/Chart.bundle.js">
    </script>

    <h2 class="text-center">Leads by Source</h2>
    <canvas id="lead_sources_canvas"></canvas>
    <script>

        var colors=[
            "rgb(255,100,0)",
            "rgb(255,150,50)",
            "rgb(255,200,100)",
        ];

        var ctx=document.getElementById("lead_sources_canvas").getContext("2d");
        var myChart = new Chart(ctx,
            {
                type:'doughnut',
                data:{
                    labels:["local","freelancer site 1","agent xyz"],
                    datasets:[
                        {
                            label:"leads by source",
                            data:[1,3,4],
                            backgroundColor:colors
                        }
                    ]
                },
                options:{
                    responsive:true,
                    rotation:-Math.PI,
                    circumference:Math.PI
                }
            }
        );
        myChart.aspectRatio=2;
        myChart.maintainAspectRatio=true;

    </script>
</div>
