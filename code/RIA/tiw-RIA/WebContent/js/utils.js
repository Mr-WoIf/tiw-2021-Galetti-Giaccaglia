/**
 * AJAX call management
 */

 function makeCall(method, url, form, cback) {
	var req = new XMLHttpRequest(); // visible by closure
	req.onreadystatechange = function() {
		cback(req)
	}; // closure
	req.open(method, url, true);
	//Send request
	if (form == null) {
		req.send(); //Send empty if no form provided
	} else if (form instanceof FormData) {
		req.send(form); //Send already serialized form
	} else {
		req.send(new FormData(form)); //Send serialized form
	}
	//Eventually reset form (if provided)
	if (form !== null && !(form instanceof FormData)) {
		form.reset(); //Do not touch hidden fields, and restore default values if any
	}
}
