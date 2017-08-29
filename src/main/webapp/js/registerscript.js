function validate() {  
  var passwordlength=document.loginform.password.value.length; 
  var password = document.loginform.password.value;
  var repassword = document.loginform.repassword.value;
 
  var status=false;  
  
   if(passwordlength < 6) {  
    document.getElementById("passwordtip").innerHTML="Password must be >= than 6";  
    document.getElementById("password").style.border = '1px solid red';
    return status;  
   }else { 
    document.getElementById("password").style.border = '1px solid black';
    document.getElementById("passwordtip").innerHTML="";  
    status=true; 
   }  

  status = repass(password, repassword);

  return status;  
}  

function repass(password, repassword) {
	if(password !== repassword) {
	document.getElementById("repasswordtip").innerHTML="Passwords don't match";
	document.getElementById("repassword").style.border = '1px solid red';
	return false;
    } else {
	return true;
    }
}