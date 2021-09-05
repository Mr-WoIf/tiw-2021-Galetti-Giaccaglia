(function () {

  var coursesList, examsList, studentsList, navbar, pageOrchestrator = new PageOrchestrator();

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
    navbarList = new NavbarList(document.getElementById("navbar_list"));

    this.init = function () {
      localStorage.removeItem('courseid');
      navbarList.init();
      examsList.reset();
      studentsList.reset();
      coursesList.reset();
      coursesList.show();
    }

    this.courseView = function (courseid) {
      localStorage.setItem('courseid', courseid);
      examsList.reset();
      studentsList.reset();
      coursesList.reset();
      examsList.show(courseid);
    }

    this.registeredStudentsView = function (courseid, examid) {
      navbarList.showCourseButton();
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
      var row, idcell, surnamecell, namecell, emailcell, degreecell, gradecell, gradestatecell, linkcell, anchor;
      this.listbody.innerHTML = "";
      document.getElementById("home_username").textContent = localStorage.getItem("name");
      var self = this;
      var result = Object.keys(listOfStudents.registerMap).map((key) => [listOfStudents.registerMap[key]]);
      result.forEach(function (student) {
        row = document.createElement("tr");
        idcell = document.createElement("td");
        idcell.className = "column1";
        idcell.textContent = "\t" + student[0][0].id + "\t";
        row.appendChild(idcell);
        surnamecell = document.createElement("td");
        surnamecell.className = "column2";
        surnamecell.textContent = student[0][0].surname;
        row.appendChild(surnamecell);
        namecell = document.createElement("td");
        namecell.className = "column3";
        namecell.textContent = student[0][0].name;
        row.appendChild(namecell);
        emailcell = document.createElement("td");
        emailcell.className = "column4";
        emailcell.textContent = student[0][0].email;
        row.appendChild(emailcell);
        degreecell = document.createElement("td");
        degreecell.className = "column5";
        degreecell.textContent = student[0][0].degree;
        row.appendChild(degreecell);
        gradecell = document.createElement("td");
        gradecell.className = "column6";
        if(student[0][1].left > 2 && student[0][1].left < 31) {
        gradecell.textContent = student[0][1].left;
        } else if(student[0][1].left == 31) {
          gradecell.textContent = "30 Cum laude";
        }
        row.appendChild(gradecell);
        gradestatecell = document.createElement("td");
        gradestatecell.className = "column7";
        gradestatecell.textContent = student[0][1].right;
        row.appendChild(gradestatecell);
        linkcell = document.createElement("td");
        linkcell.className = "column8";
        anchor = document.createElement("a");
        linkcell.appendChild(anchor);
        linkText = document.createTextNode("Modify");
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

  function NavbarList(_navbarList) {
    this.navbarList = _navbarList;

    this.init = function() {

      document.getElementById("navbar_name").textContent = localStorage.getItem("name");
      document.getElementById("navbar_role").textContent = localStorage.getItem("role");
      document.getElementById("navbar_id").textContent = localStorage.getItem("id");

      this.navbarList.children[1].addEventListener("click", (e) => {
        pageOrchestrator.init();
      });

      this.navbarList.children[2].addEventListener("click", (e) => {
        pageOrchestrator.courseView(localStorage.getItem('courseid'));
      });

      document.getElementById("logout_button").addEventListener("click", (e) => {
        localStorage.clear();
      });

      this.hideCourseButton();
    }

    this.hideCourseButton = function(){
      this.navbarList.children[2].style.display = "none";
    }

    this.showCourseButton = function() {
      this.navbarList.children[2].style.display = "";
    }



  }
})()