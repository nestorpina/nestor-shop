function showError(id) {			
	if ($("#messages #"+id).size() == 0) {
		$("#messages").append($("#"+id).clone());
		$("#messages #"+id).fadeIn();
	}
}

function hideError(id) {			
	$("#messages #"+id).fadeOut().remove();
}

/**
 * Helper methods that removes an object from an array, 
 * looking for the id of the supplied object
 * 
 * @param list
 * @param object
 */
function removeFromModel(list, object) {
	jQuery.each(list, function(index, item) {
		if(item && item.id == object.id) {
			list.splice(index,1);
			return;
		}
	});
}

/**
 * Helper methods that updates an object in an array, 
 * looking for the id of the supplied object 
 * 
 * @param list
 * @param object
 */
function updateModel(list, object) {
	jQuery.each(list, function(index, item) {
		if(item.id == object.id) {
			list[index] = object;
			return;
		}
	});
}

function preventCache() {
	return "?preventCache="+Math.random();;
}
