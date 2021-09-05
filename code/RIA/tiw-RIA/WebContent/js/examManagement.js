(function () {

  var coursesList, examsList, studentsList, pageOrchestrator = new PageOrchestrator();

  window.addEventListener("load", () => {
    if (localStorage.getItem("name") == null) {
      window.location.href = "home.html";
    } else {
      pageOrchestrator.init();
    }
  })

  function PageOrchestrator() {
    coursesList = new CoursesList(document.getElementById("courses_section"), document.getElementById("courses_list"));
    examsList = new ExamsList(document.getElementById("course_section"), document.getElementById("exams_list"));
    studentsList = new StudentsList(document.getElementById("prof_exam_section"), document.getElementById("students_list"))

    this.init = function () {

      examsList.reset();
      studentsList.reset();
      coursesList.reset();
      coursesList.show();
    }

    this.courseView = function (courseid) {

      examsList.reset();
      studentsList.reset();
      coursesList.reset();
      examsList.show(courseid);
    }

    this.registeredStudentsView = function (courseid, examid) {

      examsList.reset();
      studentsList.reset();
      coursesList.reset();
      studentsList.show(courseid, examid);
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
                self.update(listOfCourses);
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

    this.update = function (listOfCourses) {
      var row, datecell, idcell, linkcell, anchor;
      this.listbody.innerHTML = "";
      document.getElementById("home_username").textContent = localStorage.getItem("name");
      var self = this;
      listOfCourses.forEach(function (course) {
        row = document.createElement("tr");
        namecell = document.createElement("td");
        namecell.className = "column1";
        namecell.textContent = course.name;
        row.appendChild(namecell);
        idcell = document.createElement("td");
        idcell.className = "column2";
        idcell.textContent = course.id;
        row.appendChild(idcell);
        linkcell = document.createElement("td");
        linkcell.className = "column3";
        anchor = document.createElement("a");
        linkcell.appendChild(anchor);
        linkText = document.createTextNode("Details");
        anchor.className = "btn btn-outline-dark"
        anchor.appendChild(linkText);
        anchor.setAttribute('courseid', course.id)
        anchor.addEventListener("click", (e) => {
          pageOrchestrator.courseView(e.target.getAttribute("courseid"));
        });
        anchor.href = "#";
        row.appendChild(linkcell);
        self.listbody.appendChild(row);
      });
      this.listcontainer.style.display = "";
    }
  }

  function ExamsList(_listcontainer, _listbody) {
    this.listcontainer = _listcontainer;
    this.listbody = _listbody;

    this.reset = function () {
      this.listcontainer.style.display = "none";
    }

    this.show = function (courseid) {
      var self = this;
      if (localStorage.getItem("role") == "professor") {
        makeCall("GET", 'GetHoldCourse?courseId=' + courseid, null,
          function (x) {
            if (x.readyState == XMLHttpRequest.DONE) {
              switch (x.status) {
                case 200:
                  var listOfExams = JSON.parse(x.responseText);
                  self.update(listOfExams);
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
      } else if (localStorage.getItem("role") == "student") {
        makeCall("GET", 'GetCourse?courseId=' + courseid, null,
          function (x) {
            if (x.readyState == XMLHttpRequest.DONE) {
              switch (x.status) {
                case 200:
                  var listOfExams = JSON.parse(x.responseText);
                  self.update(listOfExams);
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
    }

    this.update = function (listOfExams) {
      var row, datecell, idcell, linkcell, anchor;
      this.listbody.innerHTML = "";
      document.getElementById("course_name").textContent = listOfExams["left"].name;
      var self = this;
      listOfExams["right"].forEach(function (exam) {
        row = document.createElement("tr");
        datecell = document.createElement("td");
        datecell.className = "column1";
        datecell.textContent = exam.date;
        row.appendChild(datecell);
        idcell = document.createElement("td");
        idcell.className = "column2";
        idcell.textContent = exam.id;
        row.appendChild(idcell);
        linkcell = document.createElement("td");
        linkcell.className = "column3";
        anchor = document.createElement("a");
        linkcell.appendChild(anchor);
        linkText = document.createTextNode("Details");
        anchor.className = "btn btn-outline-dark"
        anchor.appendChild(linkText);
        anchor.setAttribute('courseid', listOfExams["left"].id)
        anchor.setAttribute('examid', exam.id)
        anchor.addEventListener("click", (e) => {
          pageOrchestrator.registeredStudentsView(e.target.getAttribute("courseid"), e.target.getAttribute("examid"));
        });
        anchor.href = "#";
        row.appendChild(linkcell);
        self.listbody.appendChild(row);
      });
      this.listcontainer.style.display = "";
    }
  }

  function StudentsList(_listcontainer, _listbody) {
    this.listcontainer = _listcontainer;
    this.listbody = _listbody;

    this.reset = function () {
      this.listcontainer.style.display = "none";
    }

    this.show = function (courseId, examId) {
      var self = this;
      makeCall("GET", 'GetRegisteredStudents?courseId=' + courseId + '&examId=' + examId + '&requestType=load', null,
        function (x) {
          if (x.readyState == XMLHttpRequest.DONE) {
            switch (x.status) {
              case 200:
                var listOfStudents = JSON.parse(x.responseText);
                self.update(listOfStudents);
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

    this.update = function (listOfStudents) {
      var row, datecell, idcell, linkcell, anchor;
      this.listbody.innerHTML = "";
      document.getElementById("home_username").textContent = localStorage.getItem("name");
      var self = this;
      listOfStudents.forEach(function (student) {
        row = document.createElement("tr");
        namecell = document.createElement("td");
        namecell.className = "column1";
        namecell.textContent = student.name;
        row.appendChild(namecell);
        idcell = document.createElement("td");
        idcell.className = "column2";
        idcell.textContent = student.id;
        row.appendChild(idcell);
        linkcell = document.createElement("td");
        linkcell.className = "column3";
        anchor = document.createElement("a");
        linkcell.appendChild(anchor);
        linkText = document.createTextNode("Details");
        anchor.className = "btn btn-outline-dark"
        anchor.appendChild(linkText);
        anchor.setAttribute('courseid', student.id)
        anchor.addEventListener("click", (e) => {
          pageOrchestrator.registeredStudentsView();
        });
        anchor.href = "#";
        row.appendChild(linkcell);
        self.listbody.appendChild(row);
      });
      this.listcontainer.style.display = "";
    }
  }
})()