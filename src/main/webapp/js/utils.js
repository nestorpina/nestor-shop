function showError(id) {			
	if ($("#messages #"+id).size() == 0) {
		$("#messages").append($("#"+id).clone());
		$("#messages #"+id).fadeIn();
	}
}

function hideError(id) {			
	$("#messages #"+id).fadeOut().remove();
}

