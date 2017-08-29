function check(checkbox) {
	if(checkbox.checked == true){
      document.getElementById("increment").disabled = true;
	  var x = document.getElementsByClassName("timeleft");
	  Array.prototype.forEach.call(x, disableTimeleft);
	} else {
	  document.getElementById("increment").disabled = false;
	  var y = document.getElementsByClassName("timeleft");
	  Array.prototype.forEach.call(y, reviveTimeleft);
	}
}

function disableTimeleft(item){
	item.disabled = true;
}

function reviveTimeleft(item){
	item.disabled = false;
}

function setMin() {
	var today = new Date();
var dd = today.getDate();
var mm = today.getMonth()+1; //January is 0!
var yyyy = today.getFullYear();
var hh = today.getHours();
var mins = today.getMinutes();
var sec = today.getSeconds();
 if(dd<10){
        dd='0'+dd
    } 
    if(mm<10){
        mm='0'+mm
    } 
	if(mins<10){
		mins='0'+mins
	}
	if(hh<10) {
		hh='0'+hh
	}
	if(sec<10) {
		sec='0'+sec
	}

today = yyyy+'-'+mm+'-'+dd+'T'+hh+':'+mins+':'+sec;
	document.getElementById("datefield").min = today;
}

window.onload = setMin;



