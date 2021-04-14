
			
	    	/*$(document).ready(function () {
	    	  
	    		// JQuery performance improvement
	    		$.mobile.defaultPageTransition   = 'none';
    			$.mobile.defaultDialogTransition = 'none';
    			$.mobile.buttonMarkup.hoverDelay = 0;
    			$.mobile.ajaxEnabled = false; 
    			$.mobile.pushStateEnabled = false;
    			
	    	  initialise();
	    	});*/

	    	
	    	
			var items = '';
			var totalAmount = 0;
			var totalPrice = 0.0;
			var count = 0;
		    
		    
	    	function initialise() {
	    		
	    		//alert('Init');

    			/*$('a[name=submit_button]').click(function(){
    				var price = $('input[name=price]').val();
    				$("#basket").data("price", price);
    			});*/


	    		/*$("#scrollBar").addEventListener('vmousemove', function(event) {
	    			
	    			alert('vmousemove');
	    		
	    		}, false);*/
	    		
	    		//$( "#li1" ).draggable({ opacity: 0.7, helper: "clone" });
  			  
	    	    /*for (var i = 0; i < numLinksShown; i++) {
	    	        $("<a>", {
	    	            'href': scrollBarLinks[i],
	    	            'id': 'scrollBarBtn-' + i,
	    	            'data-role': 'button',
	    	            'data-inline': true,
	    	            'text': scrollBarText[i]
	    	        }).appendTo("#scrollBar").button();
	    	    }
	    	    
	    	    $("#scrollBar").swiperight(function() {
	    	        if (scrollCursor == 0) {
	    	            return false;
	    	        }
	    	        $("#scrollBar > a:last").remove();
	    	        scrollCursor--;
	    	        $("<a>", {
	    	            'href': scrollBarLinks[scrollCursor],
	    	            'id': 'scrollBarBtn-' + (scrollCursor),
	    	            'data-role': 'button',
	    	            'data-inline': true,
	    	            'data-icon': scrollBarIcons[scrollCursor],
	    	            'data-iconpos': 'right',
	    	            'text': scrollBarText[scrollCursor]
	    	        }).prependTo("#scrollBar").button();
	    	    });
	    	    
	    	    $("#scrollBar").swipeleft(function() {
	    	        if (scrollCursor + numLinksShown >= scrollBarLinks.length) {
	    	            return false;
	    	        }
	    	        $("#scrollBar > a:first").remove();
	    	        var nextBtn = scrollCursor + numLinksShown;
	    	        scrollCursor++;
	    	        $("<a>", {
	    	            'href': scrollBarLinks[nextBtn],
	    	            'id': 'scrollBarBtn-' + (nextBtn),
	    	            'data-role': 'button',
	    	            'data-inline': true,
	    	            'data-icon': scrollBarIcons[nextBtn],
	    	            'data-iconpos': 'right',
	    	            'text': scrollBarText[nextBtn]
	    	        }).appendTo("#scrollBar").button();
	    	    });
	    	    
	    	    
	    	    
	    		$("#offers-list").bind('vmousemove',  function(event) {
	    			
	    			if (dragProduct) {
	    		        var y = event.pageY - window.pageYOffset;
	    		        var x = event.pageX;
	    		        
	    		        // move the product icon to the coordinates
	    	            img.style.left = x + "px";
	    	            img.style.top  = y + "px";
	    			}
	    			
	    		});
	    		
	    		
	    		$("#product1").bind('vmousedown',  function(event) {
    		        var y = event.pageY - window.pageYOffset;
    		        var x = event.pageX;
    		        dragProduct = true;
    		        
    		        createProductIcon();
	    		});
	    		
	    		$("#offers-list").bind('vmouseup',  function(event) {
	    			
	    			if (dragProduct = true) {
	    		        var y = event.pageY - window.pageYOffset;
	    		        var x = event.pageX;
	    		        dragProduct = false;
	    		        
	    		        dropProduct(x,y);
	    		        $("#productIcon").remove();
	    			}
	    		});*/
	    		
	    	}

			    
    		/*$('#basket').live('pageshow', function(){
    			var price = $(this).data('price') ? $(this).data('price') : "";
    			alert(price);
    		});*/
			
			function addItem(description, price, shelfName, promotionText, imageURL) {
			var item =                     '<li style=\"font-size: 10px;\" data-role="list-divider" role="heading">' + shelfName + '</li><li id=\"li' + count + '\">' +
			'<div id=\"product' + count + '\" >' +
			'<TABLE>' + 
			  '<TR>' +
			   '<TD>' + 
			    '<TABLE>' +
			      '<TR>' +
			       '<TD colspan=\"3\"><A style=\"font-size: 12px;text-decoration:none;\">' + description +'</A>' +
			       '</TD>' +
			      '</TR>' +
			      '<TR>' +
				   '<TD align=\"left\"><a href=\"#dialogPage\" data-role=\"button\" data-transition=\"pop\"><img align=\"left\" src=\"' + imageURL + '\" alt=\"image\" />' +
				   '</TD>' +
				   '<TD  align=\"left\" colspan=\"2\">' + 
				    '<TABLE>' +
				     '<TR>' + 
				       '<TD style=\"font-size: 20px;\">' +
				         '<SPAN style=\"font-weight: bold; \"><a href=\"#priceComparePopup\" data-rel=\"popup\" data-role=\"button\" data-inline=\"true\" data-transition=\"slideup\" data-icon=\"gear\" data-theme=\"e\"></a>' +'&euro;' + price +
					     '</SPAN>' +
					   '</TD>' + 
					  '</TR>' +
					  '<TR>' +
					    '<TD style=\"font-size: 10px;\"><LABEL> ' + promotionText + '</LABEL>' +
					    '</TD>' + 
					  '</TR>' +
					 '</TABLE>' + 
				   '</TD>' +
			      '</TR>' +
			    '</TABLE>' +
			   '</TD>' +
			  '</TR>' +
			 '</TABLE>' +
		    '</div>' +
			'</li>';
					
					
				count = count + 1;
				items = items + item;
	
				$("#offers-list").html(items);
				$('#offers-list').listview('refresh');

			}
			
			
			function onLoadOffers() {
				
				window.echo = function(str, callback) {
				    cordova.exec(callback, function(err) {
				        callback('Nothing to echo.');
				    }, "BarcodeScanner", "echo", [str]);
				};
				
				window.echo("echome1", function(echoValue) {
				    
					parsePromotions(echoValue);

				});
			}
			
			// add the offers
			function parsePromotions(str) {
				
				var promotions = JSON.parse(str);
				
				for (var i = promotions.length-1; i >= 0; i--) {
					addItem(promotions[i].name, promotions[i].price, promotions[i].shelfName, promotions[i].promotionText, promotions[i].imageURL);
				}
			}

			