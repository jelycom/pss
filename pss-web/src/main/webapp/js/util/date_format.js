function DateFormat(value,currentDate) {
			var date = new Date(parseInt(value.replace("/Date(", "").replace(")/", ""), 10));
            var month = date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1;
			var currentDate = currentDate < 10 ? "0" + currentDate : currentDate;
            return date.getFullYear() + "-" + month + "-" + currentDate;
        }