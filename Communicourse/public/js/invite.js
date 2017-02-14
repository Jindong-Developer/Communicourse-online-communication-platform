function inviteMember(groupid){
         var name,age;
         name=prompt("Tips: if more than one members are invited, please separate the addresses with semicolon.\rPlease input the address of the new member:");
		 
		 var xmlhttp;
		if (window.XMLHttpRequest)
		  {// code for IE7+, Firefox, Chrome, Opera, Safari
		  xmlhttp=new XMLHttpRequest();
		  }
		else
		  {// code for IE6, IE5
		  xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
		  }
		xmlhttp.onreadystatechange=function()
		  {
		  if (xmlhttp.readyState==4 && xmlhttp.status==200)
		    {
		        var c=xmlhttp.responseText;
		        alert("opration successful!");
		    }
		  }
        var url="/updatechatgroup_members_processdata/"+groupid+":"+name
		  
		xmlhttp.open("GET",url,true);
		xmlhttp.send();
		 
}