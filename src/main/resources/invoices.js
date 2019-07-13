const pdf=new jsPDF();

let button = document.getElementById('generateButton');

button.addEventListener('click',printPDF);

function printPDF(){
    //alert("print pdf");

    //left offset, top offset, text
    pdf.text(10,10,"Yay PDF");

    pdf.save();
}

let mybutton = document.getElementById('enterButton');

mybutton.addEventListener('click',entervalue);

function entervalue(){
    //alert("enter value");

    var product_or_service = document.getElementById("product_or_service").value;
    var price = document.getElementById("price").value;

    if(product_or_service=="" || price == ""){
        //do not operate on empty values
        return;
    }

    var list = document.getElementById("invoice-list");

    var item_price=document.createElement("p");
    item_price.innerHTML=price+"";
    item_price.className="col-md-4";

    var item_name=document.createElement("p");
    item_name.innerHTML=product_or_service;
    item_name.className="col-md-8";

    var item = document.createElement("li");
    item.className="list-group-item";

    var div = document.createElement("div");
    div.className="row";

    list.appendChild(item);

    div.appendChild(item_name);
    div.appendChild(item_price);

    item.appendChild(div);


}