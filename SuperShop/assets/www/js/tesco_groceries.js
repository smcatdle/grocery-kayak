
			
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
			var dragProduct = false;

			var scrollBarLinks = new Array(
		        '#home',
		        '#link2',
		        '#link3',
		        '#link4'
		    );
		    var scrollBarText = new Array(
		        'Tesco',
		        'Supervalu',
		        'Superquinn',
		        'Lidl'
		    );
		    var scrollBarIcons = new Array(
		        'home',
		        'star',
		        'star',
		        'star'
		    );
    		    
		    var numLinksShown = 2;
		    var scrollCursor = 0;
		    
		    
	    	function initialise() {
	    		
	    		//alert('Init');


    			/*$('a[name=submit_button]').click(function(){
    				var price = $('input[name=price]').val();
    				$("#basket").data("price", price);
    			});*/


	    		/*$("#scrollBar").addEventListener('vmousemove', function(event) {
	    			
	    			alert('vmousemove');
	    		
	    		}, false);*/
	    		    
	    		addItem('Colmans Cranberry Sauce', '1.99');
	    		addToTotalPrice(count, '1.99');
	    		
	    		addItem('Colmans Cranberry Sauce', '1.99');
	    		addToTotalPrice(count, '1.99');
	    		
	    		//$( "#li1" ).draggable({ opacity: 0.7, helper: "clone" });
  			  
	    	    for (var i = 0; i < numLinksShown; i++) {
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
	    	    
	    	    
	    	    
	    		$("#basket").bind('vmousemove',  function(event) {
	    			
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
	    		
	    		$("#basket").bind('vmouseup',  function(event) {
	    			
	    			if (dragProduct = true) {
	    		        var y = event.pageY - window.pageYOffset;
	    		        var x = event.pageX;
	    		        dragProduct = false;
	    		        
	    		        dropProduct(x,y);
	    		        $("#productIcon").remove();
	    			}
	    		});

	    	}

			    
    		/*$('#basket').live('pageshow', function(){
    			var price = $(this).data('price') ? $(this).data('price') : "";
    			alert(price);
    		});*/
			
			function addItem(description, price) {
			var item =                     '<li id=\"li' + count + '\">' +
			'<div id=\"product' + count + '\" ><TABLE><TR align=\"right\"><TD><A style=\"font-size: 15px;text-decoration:none;\" id=\"h-251975475\"' +
				'href=\"/mt/www.tesco.ie/groceries/Product/Details/?id=251975475\">' + description +'</A>' +
				'</TD><TD><a href=\"#dialogPage\" data-role=\"button\" data-transition=\"pop\"><img align=\"left\" src=\"images/edit.png\" alt=\"image\" /></TD><TD><a href=\"#popupMenu\" data-rel=\"popup\" data-role=\"button\" data-inline=\"true\" data-transition=\"slideup\" data-icon=\"gear\" data-theme=\"e\"><img align=\"left\" src=\"images/actions.png\" alt=\"image\" /></a>' +
				'</TD><TD></a><img draggable=\"true\" align=\"right\" src=\"images/bin.png\" alt=\"image\" /></TD></TR><TD><SPAN ' +
				'style=\"font-weight: bold;font-size: 14px; \"><img align=\"left\" src=\"images/bought.png\" alt=\"image\" /><a href=\"#priceComparePopup\" data-rel=\"popup\" data-role=\"button\" data-inline=\"true\" data-transition=\"slideup\" data-icon=\"gear\" data-theme=\"e\"><img align=\"left\" src=\"images/stop.png\" alt=\"image\" /></a>' +'&euro;' + price +
				'  </SPAN></TD><TD></TD><TD></TD><TD></TD></TR><TR><TD align=\"right\">' +		
			'<LABEL style=\"font-size: 14px;\"> ' +
			'Quantity</LABEL></TD><TD align=\"right\" onclick=\"addToTotalPrice(' + count +',' + price + ');\" >' +
			'<img src=\"images/plus.png\" alt=\"image\" /></TD><TD>' +
	'<INPUT align=\"right\" style=\"font-size: 10px;\"' +
		'id=\"quantity' + count +'\" name=\"qty-251975475\"' +
		'size=\"1\" type=\"\"></INPUT></TD><TD align=\"left\" onclick=\"subtractFromTotalPrice(' + count +',' + price + ');\" >' +
		'<img align=\"left\" src=\"images/minus.png\" alt=\"image\" /></TD></TR></TABLE></div>' +
    '</li><li style=\"font-size: 10px;\" data-role="list-divider" role="heading">Groceries</li>';
			
			/*var item = '<DIV>' +
				'<DIV id=\"endFacets-1\" class=\"productLists\">' +
					'<DIV class=\"un_wrapperTB5 un_wrapperLR\" id=\"un_anchor_0\">' +
						'<DIV class=\"un_plp_list_item\">' +
							'<DIV class=\"un_left un_plp_imageSize un_center_text\">' +
								'<A' +
									'href=\"/mt/www.tesco.ie/groceries/Product/Details/?id=251975475\">' +'<IMG' +
									'alt=\"\"' +
									'src=\"/img?url=http%3A%2F%2Fimg.tesco.ie%2FGroceries%2Fpi%2F779%5C5000225005779%5CIDShot_90x90.jpg&amp;ttl=31d\">' +
								'</A>' +
							'</DIV>' +
							'<DIV class=\"un_right un_plp_descSize\">' +
								'<DIV>' +
									'<A style=\"font-size: 22px; \" id=\"h-251975475\"' +
										'href=\"/mt/www.tesco.ie/groceries/Product/Details/?id=251975475\">' + description +'</A>' +
								'</DIV>' +
								'<DIV class=\"quantity\">' +
									'<DIV>' +
										'<DIV class=\"\">' +
											'<SPAN style=\"font-size: 22px;\">' +'<SPAN' +
												'style=\"font-weight: bold; \">' +'&euro;' + price +  '</SPAN>' +'<SPAN' +
												'class=\"un_plp_unit\">' + '(&euro;3.05/100g)</SPAN>' +
											'</SPAN>' +
											'</BR>' +
											'</BR>' +
											'<FORM method=\"post\" id=\"fBrowse-251975475-1\"' +
												'action=\"/mt/www.tesco.ie/groceries/product/browse/default.aspx?N=4294953506&amp;Ne=4294954028\">' +
												'<DIV>' +
													'<DIV>' +
														'<INPUT name=\"formName\" value=\"fBrowse\" type=\"hidden\">' +'<INPUT' +
															'name=\"pageName\"' +
															'value=\"Tesco.Com.Applications.Retail.Superstore.Online.Pages.Product.Browse.Default\"' +
															'type=\"hidden\">' +'<INPUT name=\"__fd\"' +
															'value=\"cGJvcHRmbj1mQmFza2V0JTQwJTNiZlNlYXJjaCUzYmZOb3RlcGFkJTQwJTNiZkJyb3dzZSU0MCUzYiU0MCUzYiU0MCUzYiU0MCUzYg==\"' +
															'type=\"hidden\">' +
													'</DIV>' +
													'<INPUT class=\"hide\" id=\"id_\" disabled=\"disabled\" size=\"1\"'+ 
														'type=\"hidden\">' +
													'<DIV>' +
														'<DIV class=\"un_left un_wrapperR\">' +
														'<LABEL' +
															'class=\"un_quantity_label un_block un_wrapperTB5                \"' +
															'for=\"qty-251975475-3\">' +'Quantity</LABEL>' +
														'</DIV>' +
														'</BR>' +
														'</BR>' +
														'<DIV class=\"un_left un_wrapperR\">' +
															'<INPUT class=\"un_MP_btn\" disabled=\"disabled\"' +
																'name=\"un_jtt_plus_pd\"' +
																'src=\"/mt/a/tesco.com/31d/images/tescoie/minus_qty.png\"' +
																'alt=\"minus\" type=\"image\">' +
														'</DIV>' +
														'<INPUT class=\"un_left un_plp_qtyInput\"' +
															'id=\"qty-251975475-3\" name=\"qty-251975475\" value=\"1\"' +
															'maxlength=\"4\" type=\"\">' +
														'<DIV class=\"un_left un_wrapperLR\">' +
															'<INPUT class=\"un_MP_btn\" name=\"un_jtt_plus_pi\"' +
																'src=\"/mt/a/tesco.com/31d/images/tescoie/plus_qty.png\"' +
																'alt=\"plus\" type=\"image\">' +
														'</DIV>' +
													'</DIV>' +
												'</DIV>' +
											'</FORM>' +
										'</DIV>' +
									'</DIV>' +
								'</DIV>' +
							'</DIV>' +
							'<DIV class=\"un_clear\">' +'</DIV>' +
						'</DIV>' +
					'</DIV>';*/
					
					
				count = count + 1;
				items = items + item;
	
				$("#basket-list").html(items);
				$('#basket-list').listview('refresh');

			}
			
			function onScan() {
				window.echo = function(str, callback) {
				    cordova.exec(callback, function(err) {
				        callback('Nothing to echo.');
				    }, "BarcodeScanner", "echo", [str]);
				};
				
				window.echo("echome1", function(echoValue) {
				    var itemDescription  = '';
				    itemDescription = echoValue;
				    alert(echoValue);
				    var productValues = parseProduct(echoValue);
				    
				    var description = productValues[0];
				    var price = productValues[1];
				    alert('description : ' + description + ' price : ' + price);
				    addItem(description, price);
				    addToTotalPrice(count, price);

				});
			
			}
			
			// TODO: replace with JSON parse
			function parseProduct(str) {
				
				var split = str.split(':');
				return split;
			}
			
			function addToTotalPrice(item, amount) {

				totalPrice = parseFloat(totalPrice) + parseFloat(amount);
				//alert(totalPrice);
				totalPrice.toFixed(2);
				//alert('test1');
				addToTotalAmount(1);
				$("#quantity" + item).html(totalPrice);
				$("#totalPrice").html('&euro;' + totalPrice.toFixed(2));
			}
			
			function subtractFromTotalPrice(item, amount) {
				var subtractAmount = parseFloat(amount);
				if ((totalPrice - subtractAmount) >= 0) {
					totalPrice = totalPrice - subtractAmount;
					totalPrice = totalPrice.toFixed(2);
					subtractFromTotalAmount(1);
					$("#quantity" + item).html(totalPrice);
					$("#totalPrice").html('&euro;' + totalPrice);
				}
			}	
			
			function addToTotalAmount(amount) {
				totalAmount = totalAmount + amount;
				$("#totalAmount").html(totalAmount + ' items ');
			}
			
			function subtractFromTotalAmount(amount) {
				
				if (( totalAmount - amount) >= 0) {
					totalAmount = totalAmount - amount;
					$("#totalAmount").html(totalAmount + ' items ');
				}
				
			}

			
			// check if the product was dropped inside a shopping basket
			function dropProduct(x, y) {

				// first basket
				var x1 = $("#scrollBar > a:first").offset().left;
				var y1 = $("#scrollBar > a:first").offset().top;
				
				// second basket
				var x2 = $("#scrollBar > a:last").offset().left;
				var y2 = $("#scrollBar > a:last").offset().top;
				
				// check the first basket
				if ( (x > x1) && (x < x2) && (y > y1) && (y < (y1 + 40))) {
					
					alert('basket1');
				}
				
				// check the second basket
				if ( (x > x2) && (x < (x2 + 80)) && (y > y2) && (y < (y2 + 40))) {
					
					alert('basket2');
				}
			}

			function createProductIcon(x, y) {
				
				img = document.createElement('img');
				img.src = "images/bin.png";
				img.id = "productIcon";
				img.style.position = "absolute";
				img.style.left = x + "px";
				img.style.top = y + "100px";
				img.style.zIndex = 100;
				document.body.appendChild(img);
			}
			