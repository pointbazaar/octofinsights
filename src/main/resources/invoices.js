const pdf=new jsPDF();

let button = document.getElementById('generateButton');

button.addEventListener('click',printPDF);

function printPDF(){
    //alert("print pdf");

    //left offset, top offset, text
    pdf.text(10,10,"Yay PDF");

    pdf.save();
}