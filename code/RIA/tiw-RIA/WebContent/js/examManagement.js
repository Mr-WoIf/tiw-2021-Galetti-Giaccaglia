(function () {

  let coursesList, examDetails, studentsList, navbar, pageOrchestrator = new PageOrchestrator();

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
    examDetails = new ExamDetails(document.getElementById("exam_details_section"));
    navbar = new Navbar(document.getElementById("navbar_list"));

    this.reset = function() {
      coursesList.reset();
      examsList.reset();
      studentsList.reset();
      examDetails.reset();
    }

    this.init = function () {
      localStorage.removeItem('courseid');
      this.reset();
      navbar.init();
      coursesList.show();
    }

    this.courseView = function (courseid) {
      localStorage.setItem('courseid', courseid);
      this.reset();
      examsList.show(courseid);
    }

    this.registeredStudentsView = function (courseid, examid) {
      this.reset();
      navbar.showCourseButton();
      studentsList.show(courseid, examid);
    }

    this.examDetailsView = function(courseid, examid) {
      this.reset();
      examDetails.show(courseid, examid);

    }

  }

  function CoursesList(_listcontainer, _listbody) {
    this.listcontainer = _listcontainer;
    this.listbody = _listbody;

    this.reset = function () {
      this.listcontainer.style.display = "none";
    }

    this.show = function () {
      let self = this;
      makeCall("GET", 'GetCoursesList', null,
        function (x) {
          if (x.readyState == XMLHttpRequest.DONE) {
            switch (x.status) {
              case 200:
                let listOfCourses = JSON.parse(x.responseText);
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
      let row, idcell, linkcell, anchor;
      this.listbody.innerHTML = "";
      document.getElementById("home_username").textContent = localStorage.getItem("name");
      let self = this;
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
      let self = this;
      if (localStorage.getItem("role") == "professor") {
        makeCall("GET", 'GetHoldCourse?courseId=' + courseid, null,
          function (x) {
            if (x.readyState == XMLHttpRequest.DONE) {
              switch (x.status) {
                case 200:
                  let listOfExams = JSON.parse(x.responseText);
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
                  let listOfExams = JSON.parse(x.responseText);
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
      let row, datecell, idcell, linkcell, anchor;
      this.listbody.innerHTML = "";
      document.getElementById("course_name").textContent = listOfExams["left"].name;
      let self = this;
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
          if (localStorage.getItem("role") == "professor") {
            pageOrchestrator.registeredStudentsView(e.target.getAttribute("courseid"), e.target.getAttribute("examid"));
          } else if (localStorage.getItem("role") == "student") {
            pageOrchestrator.examDetailsView(e.target.getAttribute("courseid"), e.target.getAttribute("examid"));
          }
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
    this.publishgradebutton = document.getElementById("publish_button");
    this.recordgradebutton = document.getElementById("record_button");

    this.reset = function () {
      this.listcontainer.style.display = "none";
    }

    this.show = function (courseId, examId) {
      let self = this;
      makeCall("GET", 'GetRegisteredStudents?courseId=' + courseId + '&examId=' + examId + '&requestType=load', null,
        function (x) {
          if (x.readyState == XMLHttpRequest.DONE) {
            switch (x.status) {
              case 200:
                let listOfStudents = JSON.parse(x.responseText);
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
      let row, idcell, surnamecell, namecell, emailcell, degreecell, gradecell, gradestatecell, linkcell, anchor;
      this.listbody.innerHTML = "";
      document.getElementById("home_username").textContent = localStorage.getItem("name");
      let self = this;
      let result = Object.keys(listOfStudents.registerMap).map((key) => [listOfStudents.registerMap[key]]);
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
        if (student[0][1].left > 2 && student[0][1].left < 31) {
          gradecell.textContent = student[0][1].left;
        } else if (student[0][1].left == 31) {
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

    this.gradebuttons = function (courseid, examid) {

      publishgradebutton.addEventListener("click", (e) => {
        let data = new FormData();
        data.append("requestType", "publish");
        data.append("examId", examid);
        data.append("courseId", courseid);
        let self = this;
        makeCall("POST", 'GetRegisteredStudents', data,
          function (x) {
            if (x.readyState == XMLHttpRequest.DONE) {
              switch (x.status) {
                case 200:
                  let listOfStudents = JSON.parse(x.responseText);
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
      })

      recordgradebutton.addEventListener("click", (e) => {
        let data = new FormData();
        data.append("requestType", "record");
        data.append("examId", examid);
        data.append("courseId", courseid);
        let self = this;
        makeCall("POST", 'GetRegisteredStudents', data,
          function (x) {
            if (x.readyState == XMLHttpRequest.DONE) {
              switch (x.status) {
                case 200:
                  let listOfStudents = JSON.parse(x.responseText);
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
      })
    }
  }

  function ExamDetails(_listcontainer) {
    this.listcontainer = _listcontainer;

    this.namecard = document.getElementById("student_card_name");
    this.studentidcard = document.getElementById("student_card_studentid");
    this.emailcard = document.getElementById("student_card_email");
    this.degreecard = document.getElementById("student_card_degree");
    this.gradecard = document.getElementById("student_card_grade");
    this.gradestatecard = document.getElementById("student_card_gradestate");

    this.refusebutton = document.getElementById("refuse_button");

    this.reset = function () {
      this.listcontainer.style.display = "none";
    }

    this.show = function (courseId, examId) {
      let self = this;
      makeCall("GET", 'GetExamDetails?courseId=' + courseId + '&examId=' + examId, null,
        function (x) {
          if (x.readyState == XMLHttpRequest.DONE) {
            switch (x.status) {
              case 200:
                let examDetails = JSON.parse(x.responseText);
                self.update(examDetails, courseId, examId);
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

    this.update = function (examDetails, courseId, examId) {

      this.namecard.textContent = examDetails.studentInfo.left.name;
      this.studentidcard.textContent = examDetails.studentInfo.left.id;
      this.emailcard.textContent = examDetails.studentInfo.left.email;
      this.degreecard.textContent = examDetails.studentInfo.left.degree;
      switch (examDetails.studentInfo.right.left) {
        case -1:
          this.gradecard.textContent = "Not inserted";
          break;
        case 0:
          this.gradecard.textContent = "Absent";
          break;
        case 1:
          this.gradecard.textContent = "Posponed";
          break;
        case 2:
          this.gradecard.textContent = "To Sit Again";
          break;
        case 31:
          this.gradecard.textContent = "30 cum laude";
          break;
        default:
          if (examDetails.studentInfo.right.left > 2 && examDetails.studentInfo.right.left < 31)
            this.gradecard.textContent = examDetails.studentInfo.right.left;
          break;
      }
      this.gradestatecard.textContent = examDetails.studentInfo.right.right;

      if (examDetails.areAllPublished && examDetails.studentInfo.right.left > 2 && examDetails.studentInfo.right.left < 32 && (examDetails.studentInfo.right.right).localeCompare("refused") && (examDetails.studentInfo.right.right).localeCompare("recorded"))
        this.refusebutton.style.display = "none";

      this.refusebutton.addEventListener("click", (e) => {
        let data = new FormData();
        data.append("examId", examId);
        data.append("courseId", courseId);
        let self = this;
        makeCall("POST", 'GetExamDetails', data,
          function (x) {
            if (x.readyState == XMLHttpRequest.DONE) {
              switch (x.status) {
                case 200:
                  let examDetails = JSON.parse(x.responseText);
                  self.update(examDetails);
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
      })
      this.listcontainer.style.display = "";
    };

  }
  function Navbar(_navbarList) {
    this.navbarList = _navbarList;

    this.init = function () {

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

    this.hideCourseButton = function () {
      this.navbarList.children[2].style.display = "none";
    }

    this.showCourseButton = function () {
      this.navbarList.children[2].style.display = "";
    }



  }
})();


