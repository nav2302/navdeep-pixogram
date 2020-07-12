var body = document.getElementById("body");
body.addEventListener("keyup", function(event) {
  if (event.keyCode === 39) {
	  event.preventDefault();
	  document.getElementById("showRightImage").click();
  }
  else if (event.keyCode === 37){
	  event.preventDefault();
	  document.getElementById("showLeftImage").click();
  }
});
