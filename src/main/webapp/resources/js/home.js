/**
 * 
 */
$(document).ready(function(){
	$("#logoutA").click(function(e) {
	    var formURL = $("#formLogout").attr("action");
		$.ajax({
			type : "POST",
			url : formURL,
			data : $("#formLogout").serialize(),
			success : function(ret) {
				window.location.replace("/spring/");
			},
			complete : function() {
			},
			error : function(jqXHR, errorText, errorThrown) {
			}
		})
		e.preventDefault();
	})
})
