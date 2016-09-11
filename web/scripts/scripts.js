function signalerAbus(idAnnonce) {
    option=3;
    obj_req=null;
    obj_req=new createXMLHttpRequestObject();
    sendReq(obj_req,'./signaler-abus-'+idAnnonce+'.html');
}
function changeCategorie(idCategorie) {
    option=5;
    obj_req=null;
    obj_req=new createXMLHttpRequestObject();
    sendReq(obj_req,'./change-categorie-'+idCategorie+'.html');
}
function changeRegion(idRegion) {
    option=0;
    obj_req=null;
    obj_req=new createXMLHttpRequestObject();
    sendReq(obj_req,'./change-region-'+idRegion+'.html');
}
function changeDepartement(idDepartement) {
    option=1;
    obj_req=null;
    obj_req=new createXMLHttpRequestObject();
    sendReq(obj_req,'./change-departement-'+idDepartement+'.html');

}
function createXMLHttpRequestObject() {

var objReq=null;
	try {
		objReq=new ActiveXObject("Microsoft.XMLHTTP");
	}
	catch(Error) {
		try {
			objReq=new ActiveXObject("MSXML2.XMLHTTP");
		}
		catch(Error) {
			try {
				objReq=new XMLHttpRequest();
			}
			catch(Error) {
			}
		}
	}
	return objReq;
}
function sendReq(objReq,file) {
	objReq.open('GET',file,true);
	objReq.onreadystatechange=treat_response;
	objReq.send(null);
}
function treat_response() {
	if(obj_req.readyState==4) {
		if(obj_req.responseText!=-1)
                    {
                        switch(option)
                        {
                            case 0:
                                document.getElementById('innerDepartements').innerHTML=obj_req.responseText;
                                break;
                            case 1:
                                document.getElementById('innerCommunes').innerHTML=obj_req.responseText;
                                break;
                            case 3:
                                alert(obj_req.responseText);
                                break;
                             case 4:
                                resultat=obj_req.responseText;
                                break;
                             case 5:
                                 document.getElementById("innerSousCategories").innerHTML=obj_req.responseText;
                                 break;
                        }
                    }

	}
}
