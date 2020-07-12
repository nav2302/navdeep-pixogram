$(function () {
    $('onclicklike').on('click', function () {
    	var request = $.ajax({
    	    url: '/files/getData',
    	    type: 'GET',
    	    data: { id: "hello", likes : "hello2"} ,
    	    contentType: 'application/json; charset=utf-8'
    	});

    	request.done(function(data) {
    	      // your success code here
    	});

    	request.fail(function(jqXHR, textStatus) {
    	      // your failure code here
    	});
    });

});