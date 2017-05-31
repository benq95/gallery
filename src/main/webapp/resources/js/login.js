/**
 * 
 */
$(document).ready(function(){
	$("#loginForm").submit(function(e) {
		var postData = $(this).serializeArray();
	    var formURL = $(this).attr("action");
	    console.info(formURL);
		$.ajax({
			type : "POST",
			url : formURL,
			dataType : "json",
			data : $("#loginForm").serialize(),
			success : function(ret) {
				var response = ret;
				if (response.success == true) {
					console.info("Authentication Success!");
					window.location.replace("/spring/home");
				} else {
					console.error("Unable to login");
					$(".form-group").addClass("has-error");
					$(".help-block").html("Invalid username or password");
				}
			},
			complete : function() {
			},
			error : function(jqXHR, errorText, errorThrown) {
				$("div.form-group").addClass("has-error");
			}
		})
		e.preventDefault();
	})
})

