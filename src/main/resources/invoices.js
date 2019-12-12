var list_items=[];

var pdf=new jsPDF();
//http://raw.githack.com/MrRio/jsPDF/master/docs/jsPDF.html#addPage




let button = document.getElementById('generateButton');

button.addEventListener('click',printPDF);

let mybutton = document.getElementById('enterButton');

mybutton.addEventListener('click',entervalue);


function printPDF(){
    //alert("print pdf");

    //left offset, top offset, text
    pdf.text(20,10,"Invoice");

    var euro="\u20AC";



    var offset_x_name=20;
    var offset_x_price=160;

    var mid_width=170;

    var y0=20;

    pdf.line(offset_x_name,y0,offset_x_name+mid_width,y0);

    var offset_y=y0+10;

    var i=0;
    list_items.forEach(function(item){
        pdf.text(offset_x_name,i*10+offset_y,item.product_or_service);
        pdf.text(
            offset_x_price,
            i*10+offset_y,
            (item.price+euro).padStart(10," ")
        );
        i++;
    });

    var y1=i*10+offset_y+8;

    pdf.line(offset_x_name,y1,offset_x_name+mid_width,y1);

    var y2=y1+10;

    var sum_string = list_items.map(myitem => myitem.price).reduce((a,b)=>a+b)+euro;

    pdf.text(offset_x_name,y2,"Sum: ");
    pdf.text(
        offset_x_price,
        //i*10+offset_y+30,
        y2,
        sum_string.padStart(10," ")
    );

    var y3 = 280;

    pdf.text(offset_x_name,y3,"(TODO) Company Name");
    pdf.text(offset_x_name,y3+10,"(TODO) Company Address");

    pdf.text(offset_x_name+(mid_width/2),y3,"(TODO) Company Website Link");
    pdf.text(offset_x_name+(mid_width/2),y3+7,"(TODO) Company Email");
    pdf.text(offset_x_name+(mid_width/2),y3+14,"(TODO) Company Telephone");

    pdf.save();

    pdf=new jsPDF();
}

function update(){
    //clear the list
    var list = document.getElementById("invoice-list");
    list.innerHTML="";

    //add all the items
    list_items.forEach(function(item){

        update_one(item.product_or_service,item.price,item.id);
    });
}

function update_one(product_or_service,price,id){
    var list = document.getElementById("invoice-list");

    var item_price=document.createElement("p");
    item_price.innerHTML=price+" \u20AC";
    item_price.className="col-md-3";

    var item_name=document.createElement("p");
    item_name.innerHTML=product_or_service;
    item_name.className="col-md-8";

    var item_delete_button = document.createElement("button");
    item_delete_button.className="btn btn-outline-danger col-md-1";
    item_delete_button.onclick=delete_item;
    item_delete_button.innerHTML="X";
    item_delete_button.id=id+"";

    var item = document.createElement("li");
    item.className="list-group-item";

    var div = document.createElement("div");
    div.className="row";

    list.appendChild(item);

    div.appendChild(item_name);
    div.appendChild(item_price);
    div.appendChild(item_delete_button);

    item.appendChild(div);
}

function make_item(product_or_service,price){
    return {
        product_or_service: product_or_service,
        price:price,
        id:Math.floor((Math.random()*1000000))+""
    };
}

function delete_item(e){
    console.log("try to delete an item");
    var delete_id = e.target.id;
    list_items=list_items.filter(function(item){
        return (item.id != delete_id)
    });

    update();
}

function entervalue(e){
    //alert("enter value");
    console.log(e);

    var product_or_service = document.getElementById("product_or_service").value;
    var price = document.getElementById("price").value;

    document.getElementById("product_or_service").value="";
    document.getElementById("price").value="";

    if(product_or_service=="" || price == ""){
        //do not operate on empty values
        return;
    }

    list_items.push(make_item(product_or_service,price));

    update();
}
