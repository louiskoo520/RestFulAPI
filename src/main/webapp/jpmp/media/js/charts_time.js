var Charts = function () {
	
    return {
        //main function to initiate the module

        init: function () {
            App.addResponsiveHandler(function () {
                 Charts.initPieCharts(); 
            });
            
        },

        initPieCharts: function () {
            var data = [];
            var series = Math.floor(Math.random() * 10) + 1;
            series = series < 5 ? 5 : series;
            
            for (var i = 0; i < series; i++) {
                data[i] = {
                    label: "部门" + (i + 1),
                    data: Math.floor(Math.random() * 100) + 1
                }
            }
			
            // INTERACTIVE
            $.plot($("#interactive"), data, {
                    series: {
                        pie: {
                            show: true
                        }
                    },
                    grid: {
                        hoverable: true,
                        clickable: true
                    }
                });
            $("#interactive").bind("plothover", pieHover);
            $("#interactive").bind("plotclick", pieClick);

            function pieHover(event, pos, obj) {
            if (!obj)
                    return;
                percent = parseFloat(obj.series.percent).toFixed(2);
                $("#hover").html('<span style="font-weight: bold; color: ' + obj.series.color + '">' + obj.series.label + ' (' + percent + '%)</span>');
            }

            function pieClick(event, pos, obj) {
                if (!obj)
                    return;
                percent = parseFloat(obj.series.percent).toFixed(2);
				window.location.href="department_stat.html";
            }

        }
        
    };

}();