/*
 * Regex for url matching
/[-a-zA-Z0-9@:%._\+~#=]{1,256}\.[a-zA-Z0-9()]{1,6}\b([-a-zA-Z0-9()@:%_\+.~#?&//=]*)?/
*/
$.validator.addMethod("nameRegex", function(value, element) {
	return /^[A-Za-z]{3,}$/.test(value);
}, "Name must contain only letters");
$.validator.addMethod("phoneRegex", function(value, element) {
	return /^[2-9]\d{2}-\d{3}-\d{4}$/.test(value);
}, "Enter a valid Phone number");
$("form").validate({
	rules: {
		firstName: {
			required: true,
			minlength: 3,
			nameRegex: true
		},
		lastName: {
			required: true,
			minlength: 3,
			nameRegex: true
		},
		about: {
			required: true,
			minlength: 15
		},
		address: {
			required: true,
			minlength: 15
		},
		email: {
			required: true,
			email: true
		},
		phone: {
			required: true,
			phoneRegex: true
		}
	},
	messages: {
		firstName: {
			required: "You must enter your First Name"
		}
	},
	messages: {
		lastName: {
			required: "You must enter your Last Name"
		}
	},
	messages: {
		email: {
			required: "You must enter your email",
			email: "Please enter a valid email"
		}
	},
	messages: {
		about: {
			required: "Please enter something about Yourself",
			minlength: "Please enter atleast 15 characters"
		}
	},
	messages: {
		address: {
			required: "Please enter something about Yourself",
			minlength: "Please enter atleast 15 characters"
		}
	},
	messages: {
		phone: {
			required: "Please enter your Phone Number!"
		}
	},
	highlight: function(element, errorClass) {
		$(element).closest(".form-group").addClass("has-error");
	},
	unhighlight: function(element, errorClass) {
		$(element).closest(".form-group").removeClass("has-error");
	},
	errorPlacement: function (error, element) {
		error.appendTo(element.parent().next());
	},
	errorPlacement: function (error, element) {
		if(element.attr("type") == "checkbox") {
			element.closest(".form-group").children(0).prepend(error);
		}
		else
			error.insertAfter(element);
	}
});