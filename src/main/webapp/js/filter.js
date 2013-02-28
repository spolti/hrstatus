function useFilter(searchInput, resultTable) {
	$('#' + searchInput).focus(function() {
		clearTableSearch(searchInput, resultTable);
	});

	$('#' + searchInput).keyup(function(event) {
		if ($(this).val() == '') {
			clearTableSearch(searchInput, resultTable);
			return;
		}
		filterTableResult($(this), resultTable);
	});
}

function clearTableSearch(searchInput, resultTable) {
	$('#' + searchInput).val('');
	$('#' + resultTable + ' tr').show();
}

function filterTableResult(elem, resultTable) {
	// index = $(elem).parent().index();
	var query = $(elem).val(), //
		lines = $('#' + resultTable + ' tbody tr'), //
		elements, textLine;
	$(lines).each(
			function(key, line) {
				elements = $(line).children('td');
				if (elements && elements.length) {
					if ($(elements).text().toLowerCase().indexOf(query.toLowerCase()) != -1) {
						$(line).show();
					} else {
						$(line).hide();
					}
				}
			});
}