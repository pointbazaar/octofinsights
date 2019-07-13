const pdf=new jsPDF();

let button = document.getElementById('generateButton');

button.addEventListener('click',printPDF);

let mybutton = document.getElementById('enterButton');

mybutton.addEventListener('click',entervalue);

var list_items=[];

function printPDF(){
    //alert("print pdf");

    //left offset, top offset, text
    pdf.text(10,10,"Yay PDF");

    pdf.save();
}

function add_list_item(name, price){

}

function update(){
    //clear the list
    var list = document.getElementById("invoice-list");
    list.innerHTML="";

    //add all the items
    list_items.forEach(function(item){

        update_one(item.product_or_service,item.price);
    });
}

function update_one(product_or_service,price){
    var list = document.getElementById("invoice-list");

    var item_price=document.createElement("p");
    item_price.innerHTML=price+"";
    item_price.className="col-md-3";

    var item_name=document.createElement("p");
    item_name.innerHTML=product_or_service;
    item_name.className="col-md-8";

    var item_delete_button = document.createElement("button");
    item_delete_button.className="btn btn-outline-danger col-md-1";
    item_delete_button.innerHTML="X"

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
        price:price
    };
}

function entervalue(){
    //alert("enter value");

    var product_or_service = document.getElementById("product_or_service").value;
    var price = document.getElementById("price").value;

    if(product_or_service=="" || price == ""){
        //do not operate on empty values
        return;
    }

    list_items.push(make_item(product_or_service,price));

    update();
}