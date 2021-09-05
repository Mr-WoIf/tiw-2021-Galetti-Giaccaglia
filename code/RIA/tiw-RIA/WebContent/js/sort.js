function resetArrows(rowHeaders){
    for (let j = 0; j < rowHeaders.length; j++ ){
      var toReset =   rowHeaders[j].querySelectorAll("span");
      for (let i = 0; i < toReset.length; i++) {
        toReset[i].className = "normalarrow";
      }
    }
  }

(function() {
  // Returns the text content of a cell.
  var asc = true;

  window.addEventListener("load", () => {
      /*var dest = document.getElementById("id_dest"),
          date =  document.getElementById("id_date");
      dest.addEventListener("click", function(){sortTable(dest.id)});
      date.addEventListener("click", function(){sortTable(date.id)});
      */
      var elements = document.getElementsByClassName("sortable")
      
      for (let i = elements.length - 1; i >= 0; i--) {
        elements[i].addEventListener("click", function(){sortTable(elements[i].id)});
      }
    }, false);


  function getCellValue(tr, idx) {
    return tr.children[idx].textContent; // idx indexes the columns of the tr row
  }

  

  function changeArrow(th){
    var toChange = asc ? th.querySelector("span:first-child") : th.querySelector("span:last-child");
    toChange.className="boldarrow";
  }
  /*
  * Creates a function that compares two rows based on the cell in the idx
  * position.
  */
  function createComparer(idx, asc) {
    return function(rowa, rowb) {
      // get values to compare at column idx
      // if order is ascending, compare 1st row to 2nd , otherwise 2nd to 1st
      var v1 = getCellValue(asc ? rowa : rowb, idx),
      v2 = getCellValue(asc ? rowb : rowa, idx);
      
      // If non numeric value
      if (v1 === '' || v2 === '' || isNaN(v1) || isNaN(v2)) {
        return v1.toString().localeCompare(v2); // lexical comparison
      }

      // If numeric value
      return v1 - v2; // v1 greater than v2 --> true
    };
  }

  // For all table headers f class sortable
  function sortTable(clicked_id) {
    
    var th = document.getElementById(clicked_id);
    var table = th.closest('table'); // get the closest table tag
    var rowHeaders = table.querySelectorAll('th');
    var columnIdx =  Array.from(rowHeaders).indexOf(th);
    // For every row in the table body
    // Use Array.from to build an array from table.querySelectorAll result
    // which is an Array Like Object (see DOM specifications)
    var rowsArray = Array.from(table.querySelectorAll('tbody > tr'));
    // sort rows with the comparator function passing
    // index of column to compare, sort criterion asc or desc)
    rowsArray.sort(createComparer(columnIdx, asc));
    //  Toggle the criterion
    asc =  !asc;
    // Change arrow colors
    resetArrows(rowHeaders);
    changeArrow(th);
    // Append the sorted rows in the table body
    for (var i = 0; i < rowsArray.length; i++) {
      table.querySelector('tbody').appendChild(rowsArray[i]);
    }
    //rowsArray.forEach(function(row){table.querySelector('tbody').appendChild(row);});
  }
})();
