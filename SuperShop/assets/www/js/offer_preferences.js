
			
	    	/*$(document).ready(function () {
	    		
	    		// JQuery performance improvement
	    		$.mobile.defaultPageTransition   = 'none';
    			$.mobile.defaultDialogTransition = 'none';
    			$.mobile.buttonMarkup.hoverDelay = 0;
    			$.mobile.ajaxEnabled = false; 
    			$.mobile.pushStateEnabled = false;
	    			
	    	    //alert('onLoad()');
	    	    //onLoadCatagories();
	    	  //initialise();
	    	});*/
	    	
			var categoriesHtml = '';
			var shelvesHtml = '';
			var categories;
			var shelves;
			var categoryIndex = 0;
			var categoryCount = 0;
			var shelfCount = 0;

			
			function addCategory(name) {
				
				var categoryHtml =  '<div id="c-' + categoryCount + '" data-role="collapsible" data-theme="b" data-mini="true">' +
					'<h3>' + name + '</h3>' +
				    '</div>' +
				    '<p></p>';
					
					
				categoryCount = categoryCount + 1;
				categoriesHtml = categoriesHtml + categoryHtml;

			}
			
			function addPartialCategory(name) {
				var categoryHtml = '<div id="c-' + categoryCount + '" data-role="collapsible" data-theme="b" data-mini="true">' +
					'<h3>' + name + '</h3>';
											
					categoryCount = categoryCount + 1;
					categoriesHtml = categoriesHtml + categoryHtml;

			}
			
			function addShelf(name) {
				var shelfHtml = '<div id="s-' + shelfCount + '" data-role="collapsible" data-theme="b" data-mini="true">' +
						'<h3>' + name + '</h3>' + 
					    '</div>' +
					    '<p></p>';
										
					shelfCount = shelfCount + 1;
					shelvesHtml = shelvesHtml + shelfHtml;

			}
			
			function onLoadAllCategories() {
				
				window.echo = function(str, callback) {
				    cordova.exec(callback, function(err) {
				        callback('Nothing to echo.');
				    }, "BarcodeScanner", "getCategories", [str]);
				};
				
				window.echo("echome1", function(echoValue) {
				    
					parseCategories(echoValue);
					$("#c-0").trigger('expand');
				});
			}
			
			function onLoadAllCategories1() {
				var catHtml = '';
				var cat = ['<div id="c-' + categoryCount++ + '" data-role="collapsible" data-theme="b" data-mini="true"><h3>Test</h3></div><p></p>', '<div id="c-' + categoryCount++ + '" data-role="collapsible" data-theme="b" data-mini="true"><h3>Test</h3></div><p></p>', '<div id="c-' + categoryCount++ + '" data-role="collapsible" data-theme="b" data-mini="true"><h3>Test</h3></div><p></p>', '<div id="c-' + categoryCount++ + '" data-role="collapsible" data-theme="b" data-mini="true"><h3>Test</h3></div><p></p>', '<div id="c-' + categoryCount++ + '" data-role="collapsible" data-theme="b" data-mini="true"><h3>Test</h3></div><p></p>', '<div id="c-' + categoryCount++ + '" data-role="collapsible" data-theme="b" data-mini="true"><h3>Test</h3></div><p></p>', '<div id="c-' + categoryCount++ + '" data-role="collapsible" data-theme="b" data-mini="true"><h3>Test</h3></div><p></p>'];
				
				for (i = 0; i < cat.length; i++) {
					catHtml = catHtml + cat[i];
				}
				$("#categories-list").html(catHtml);
				$("#categories-list").trigger('create');
				
			}
			
			function onLoadShelves(category) {
				
				window.echo1 = function(str, callback) {
				    cordova.exec(callback, function(err) {
				        callback('Nothing to echo.');
				    }, "BarcodeScanner", "getShelves", [str]);
				};
				
				window.echo1(category, function(echoValue) {
				    
					redisplayCategories(echoValue);
					$("#c-0").trigger('expand');
				});
			}
			
			// add the categories
			function parseCategories(str) {
				
				categories = JSON.parse(str);
				
				for (var i = 0; i < categories.length; i++) {
					addCategory(categories[i].category);
				}			
				
				$("#categories-list").html(categoriesHtml);
				$("#categories-list").trigger('create');
				
				$("div:jqmData(role='collapsible')").each(function(){
					//alert($(this).attr('id'));
				    bindEventTouch($(this)); 
				});
			}
			
			function redisplayCategories(str) {
				//alert('redisplayCategories');
				categoriesHtml = "";
				categoryCount = 0;
				shelfCount = 0;
				
				shelves = JSON.parse(str);
				
				for (var i = 0; i < categories.length; i++) {
					//alert(categoryIndex + ' : ' + i)
					
					if (i == categoryIndex) {
						//alert('i = categoryIndex');
						addPartialCategory(categories[i].category);
						shelvesHtml = '';
						
						for (var j = 0; j < shelves.length; j++) {
							addShelf(shelves[j].shelfName);
							
						}
						//alert(shelvesHtml);
						categoriesHtml = categoriesHtml + shelvesHtml + '</div><p></p>';
						
					} else {
						addCategory(categories[i].category);
					}
				}
				//alert(categoriesHtml);
				$("#categories-list").html("");
				$("#categories-list").trigger('create');
				$("#categories-list").html(categoriesHtml);
				$("#categories-list").trigger('create');
				
				$("div:jqmData(role='collapsible')").each(function(){
				//alert($(this).attr('id'));
			    bindEventTouch($(this)); 
				});
			}
			
			
			function bindEventTouch(element) {
			    element.bind('mousedown', function(event, ui) {
			    var categoryId = element.attr('id');
			    var categoryIndexString = categoryId.substring(2, categoryId.length+1);
			    categoryIndex = parseInt(categoryIndexString);
			  //alert(element.attr('id'));
			    onLoadShelves(categories[categoryIndex].category);
			    
			       if(element.hasClass('ui-collapsible-collapsed')) {
			    	   //alert(element.attr('id')+' is closed');
						$("#c-" + categoryIndex).trigger('expand');
			        } else {
			            //alert(element.attr('id')+' is open');
						$("#c-" + categoryIndex).trigger('expand');
			        }
			    });
			}
			
			
			