(function () {

  var coursesList, pageOrchestrator = new PageOrchestrator();

  window.addEventListener("load", () => {
    if (sessionStorage.getItem("username") == null) {
      window.location.href = "home.html";
    } else {
      pageOrchestrator.init();
    }
  })

  function PageOrchestrator() {

    this.init = function() {
      coursesList = new CoursesList(document.getElementById("courses_section"), document.getElementById("courses_list_table"));

      coursesList.reset();
      coursesList.show();
    }

    
  }

  function CoursesList(_listcontainer, _listbody) {
    this.listcontainer = _listcontainer;
    this.listbody = _listbody;

    this.reset = function () {
      this.listcontainer.style.display = "none";
    }

    this.show = function () {
      var self = this;
      makeCall("GET", 'GetCoursesList', null,
        function (x) {
          if (x.readyState == XMLHttpRequest.DONE) {
            switch (x.status) {
              case 200:
                var listOfCourses = JSON.parse(x.responseText);
                self.update(list);
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
        })
    }

    this.update = function(listOfCourses) {
      var row, namecell, idcell, linkcell, anchor;
      this.listcontainerbody.innerHTML = "";
      var self = this;
      listOfCourses.forEach(function(course) {
        row = document.createElement("tr");
        namecell = document.createElement("td");
        namecell.textContent = course.name;
        row.appendChild(namecell);
        idcell = document.createElement("td");
        idcell.textContent = course.id;
        row.appendChild(idcell);
        linkcell = document.createElement("td");
        anchor = document.createElement("a");
        linkcell.appendChild(anchor);
        linkText = document.createTextNode("Details");
        anchor.appendChild(linkText);
        anchor.setAttribute('courseid', course.id)
        anchor.addEventListener("click", (e) => {
          examsList.show(e.target.getAttribute("courseid"));
        });
        row.appendChild(linkcell);
        self.listbody.appendChild("row");
      });
      this.listcontainer.style.display = "";
    }
  }
})()