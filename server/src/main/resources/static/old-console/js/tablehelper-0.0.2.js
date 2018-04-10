// requires jQuery, druidTable, dataTables
var oTable = [];

// flattens JSON from Druid and builds a table row per segment
function buildTable(data, el, dontDisplay, table, row) {
  table = typeof table !== 'undefined' ? table : new DruidTable();
  row = typeof row !== 'undefined' ? row : 0;
  dontDisplay = typeof dontDisplay !== 'undefined' ? dontDisplay : [];

  if (!Array.isArray(data) || data.length == 0) {
    return;
  }

  if (oTable[el.attr('id')] != null) {
    oTable[el.attr('id')].fnDestroy();
    el.empty();
  }

  // parse JSON
  for (var item in data) {
    setTable(data[item], el, dontDisplay, table, row, "");
    row++;
  }

  table.toHTMLTable(el);
  initDataTable(el);
}

function setTable(data, el, dontDisplay, table, row, fieldNamespace) {
  for (var field in data) {
    if (_.contains(dontDisplay, field)) {
      // do nothing
    } else if (Array.isArray(data[field])) {
      table.setCell(row, fieldNamespace + field, JSON.stringify(data[field]));
    } else if (!(data[field] instanceof Object)) {
      table.setCell(row, fieldNamespace + field, data[field]);
    } else {
      setTable(data[field], el, dontDisplay, table, row, fieldNamespace + field + " ");
    }
  }
}

function initDataTable(el) {
  // dataTable stuff (http://www.datatables.net/)
  var asInitVals = [];

  oTable[el.attr('id')] = el.dataTable({
    "oLanguage": {
      "sSearch": "Search all columns:"
    },
    "oSearch": {
      "sSearch": "",
      "bRegex": true
    },
    "sPaginationType": "full_numbers",
    "bProcessing": true,
    "aaSorting": []
  });

  $("thead input").keyup(function() {
      var tbl = oTable[$(this).parents('table').attr('id')];
      tbl.fnFilter(this.value, tbl.children("thead").find("input").index(this), true);
  });

  $("thead input").each(function(i) {
    asInitVals[i] = this.value;
  });

  $("thead input").focus(function() {
    if (this.className === "search_init" ) {
      this.className = "";
      this.value = "";
    }
  });

  $("thead input").blur(function(i) {
    if (this.value === "" ) {
      this.className = "search_init";
      this.value = asInitVals[$("thead input").index(this)];
    }
  });
  }