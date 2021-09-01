/**
 * Login management
 */

(function () { // avoid variables ending up in the global scope

    document.getElementById("login_button").addEventListener('click', (e) => {
        var form = e.target.closest("form");
        if (form.checkValidity()) {
            makeCall("POST", 'CheckLogin', e.target.closest("form"),
                function (x) {
                    if (x.readyState == XMLHttpRequest.DONE) {
                        switch (x.status) {
                            case 200:
                                var user = JSON.parse(x.responseText)
                                localStorage.setItem('name', user.name);
                                localStorage.setItem('id', user.id);
                                localStorage.setItem('role', user.role)
                                window.location.href = "home.html";
                                break;
                            case 400: // bad request
                                document.getElementById("errormessage").textContent = x.responseText;
                                break;
                            case 401: // unauthorized
                                document.getElementById("errormessage").textContent = x.responseText;
                                break;
                            case 500: // server error
                                document.getElementById("errormessage").textContent = x.responseText;
                                break;
                        }
                    }
                }
            );
        } else {
            form.reportValidity();
        }
    });

})();