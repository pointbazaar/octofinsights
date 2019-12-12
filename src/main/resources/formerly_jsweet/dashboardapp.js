    function main(args) {
        console.log("Dashboard Frontend App Running");

        //test.textContent="hi frontend apps in java !";
        main2();
        main3();
        main4();
        main5();
        main_active_projects();
        main_active_tasks();
        main_open_leads();
    }



    function main2(){
        var balancediv = $("#balancediv");
        //balancediv.hide();
        lowOpacity(balancediv);

        fetch("/api/current_balance").then(data=>data.json()).then(parse=>{
            var balance = $("#balance").get()[0];
            balance.textContent=parse.value +" \u20AC";
            if(parse.value>=0) {
                balance.classList.add("text-success");
            }else {
                balance.classList.add("text-danger");
            }

            fullOpacity(balancediv);
        });
    }

    function main3(){
        var sales = $("#salesthismonth").get()[0];
        var sales_div = $("#salesdiv");
        lowOpacity(sales_div);
        //sales_div.hide();
        fetch("/api/salesthismonth").then(data=>data.json()).then(parse=>{
            //console.log(parse.value);
            sales.textContent=parse.value+" \u20AC";
            if(parse.value>0){
                sales.classList.add("text-success");
            }else if(parse.value==0){
                sales_div.remove();
            }
            fullOpacity(sales_div);
        });
    }


    function main4(){
        var profitdiv = $("#profitdiv");
        lowOpacity(profitdiv);
        fetch("/api/profit").then(data=>data.json()).then(parse=>{
            $("#profit").get()[0].textContent=parse.value+" \u20AC";
            if(parse.value==0){
                profitdiv.remove();
            }
            fullOpacity(profitdiv);
        });
    }

    function main5(){
        var expensesdiv = $("#expensesdiv");
        lowOpacity(expensesdiv);
        fetch("/api/salesthismonth").then(data=>data.json()).then(parse=>{
            $("#expensesthismonth").get()[0].textContent=((-1)*parse.value)+" \u20AC";
            if(parse.value==0){
                expensesdiv.remove();
            }
            fullOpacity(expensesdiv);
        });
    }

    function main_active_projects(){
        var mydiv = $("#activeprojectsdiv");
        lowOpacity(mydiv);
        fetch("/api/activeprojects").then(data=>data.json()).then(active_project_count=>{
            $("#activeprojects").get()[0].textContent=(active_project_count.value)+"";

            if(active_project_count.value>6){
                //too many projects
                mydiv.get()[0].classList.add("text-danger");
            }else if(active_project_count.value>4){
                //still too many projects at once
                mydiv.get()[0].classList.add("text-warning");
            }else if(active_project_count.value==0){
                mydiv.remove();
            }

            fullOpacity(mydiv);
        });
    }

    function main_active_tasks() {

        var mydiv = $("#activetasksdiv");
        lowOpacity(mydiv);
        fetch("/api/activetasks").then(data=>data.json()).then(active_task_count=>{

            $("#activetasks").get()[0].textContent=(active_task_count.value)+"";

            if(active_task_count.value>20){
                //too many projects
                mydiv.get()[0].classList.add("text-danger");
            }else if(active_task_count.value>10){
                //still too many projects at once
                mydiv.get()[0].classList.add("text-warning");
            }else if(active_task_count.value==0){
                mydiv.remove();
            }

            fullOpacity(mydiv);
        });
    }

    function main_open_leads(){
        var mydiv = $("#openleadsdiv");
        lowOpacity(mydiv);
        fetch("/api/openleads").then(data=>data.json()).then(openleads=>{
            $("#openleads").get()[0].textContent=openleads.value+"";

            fullOpacity(mydiv);
        });
    }

    function lowOpacity(element){
        element.get()[0].style.opacity="0.1";
    }

    function fullOpacity(element){
        element.get()[0].style.opacity="1.0";
    }

    main();