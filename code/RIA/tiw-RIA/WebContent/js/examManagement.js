(function () {

    var coursesList, examsList, pageOrchestrator = new PageOrchestrator();
  
    window.addEventListener("load", () => {
      if (localStorage.getItem("name") == null) {
        window.location.href = "home.html";
      } else {
        pageOrchestrator.init();
      }
    })
  
    function PageOrchestrator() {
     
      this.init = function() {
          coursesList = new CoursesList(document.getElementById("courses_section"), document.getElementById("courses_list_table"));
          examsList = new ExamsList(document.getElementById("prof_course_section"), document.getElementById("prof_exams_list_table"));
          
        examsList.reset();
        coursesList.reset();
        coursesList.show();
      }
  
      this.courseView = function (courseid) {
      coursesList = new CoursesList(document.getElementById("courses_section"), document.getElementById("courses_list_table"));
          examsList = new ExamsList(document.getElementById("prof_course_section"), document.getElementById("prof_exams_list_table"));
          
        examsList.reset();
        coursesList.reset();
        examsList.show(courseid);
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
          anchor.setAttribute('examid', exam.id)
          anchor.addEventListener("click", (e) => {
            examsList.show(e.target.getAttribute("examid"));
          });
          anchor.href = "#";
          row.appendChild(linkcell);
          self.listbody.appendChild(row);
        });
        this.listcontainer.style.display = "";
      }
    }
  })()