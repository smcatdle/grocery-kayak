
			
	    	$(document).ready(function () {
	    		
	    		//alert('ready()');
	    		// JQuery performance improvement
	    		$.mobile.defaultPageTransition   = 'none';
    			$.mobile.defaultDialogTransition = 'none';
    			$.mobile.buttonMarkup.hoverDelay = 0;
    			$.mobile.ajaxEnabled = true; 
    			//$.mobile.pushStateEnabled = false;
    			
			  /*var mySwiper = new Swiper('.swiper-container',{
				    pagination: '.pagination',
				    paginationClickable: true,
				    slidesPerView: 3
				  });*/
	
			  var mySwiper = new Swiper('.swiper-container',{
				    pagination: '.pagination',
				    paginationClickable: true,
				    slidesPerView: 1
				  });
			  
			  // set the search collapsable to open slowly
			  /*$('#ex').bind('onclick', function () {
				    $('#test').children().next().hide();
				    $('#test').children().next().slideDown(2000);
				}).bind('onmouseup', function () {
				    $(this).children().next().slideUp(2000);
				});*/
    			
			  

	    	  initialise();
	    	  
	    	  /*$("#search-mini").on("blur",function () { 
	    		    search();
	    	  });*/

	    	  // Set the event on the 'enter' action for searches
	    	  $("#search-mini").on("keyup",function (event) { 
	    		    if (event.keyCode == 13){ // Detect Enter
	    		        
	    		        search();
	    		    }
	    	  });
	    	  
	    	  render();
	    	});
	    	
	    	
			var totalAmount = 0;
			var totalPrice = 0.0;
			var count = 0;
			var dragProduct = false;

    		    
		    var numLinksShown = 2;
		    var products = new Array();
		    var suggestions = new Array();
		    var productIndex = 0;
		    var suggestionIndex = 0;
		    var basketIndex = 1;
		    
		    
	    	function initialise() {

    			/*$('a[name=submit_button]').click(function(){
    				var price = $('input[name=price]').val();
    				$("#basket").data("price", price);
    			});*/


	    		/*$("#scrollBar").addEventListener('vmousemove', function(event) {
	    			
	    			alert('vmousemove');
	    		
	    		}, false);*/
	    		    
	    		addItem(-1, basketIndex, 'Colmans Cranberry Sauce', '1.99', '234234234', 1, 't');  
	    		addToTotalPrice(count, '1.99');
	    		
	    		addItem(-1, basketIndex, 'Colmans Cranberry Sauce', '1.99', '234234234', 1, 't');    		
	    		addToTotalPrice(count, '1.99');
	    		
	    		//onLoadBasket();
	    		
	    		//$( "#li1" ).draggable({ opacity: 0.7, helper: "clone" });

	    		/*$("#basket").bind('vmousemove',  function(event) {
	    			
	    			if (dragProduct) {
	    		        var y = event.pageY - window.pageYOffset;
	    		        var x = event.pageX;
	    		        
	    		        // move the product icon to the coordinates
	    	            img.style.left = x + "px";
	    	            img.style.top  = y + "px";
	    			}
	    			
	    		});
	    		
	    		$("#basket").bind('vmouseup',  function(event) {
	    			
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
			
			function addItem(id, basketId, description, price, barcode, amount, type) {		
					
				var basketItem = new BasketItem('t');
				basketItem.id = id;
				basketItem.basketId = basketId;
				basketItem.description = description;
				basketItem.price = price;
				basketItem.barcode = barcode;
				basketItem.amount = 1;
				basketItem.type = type;
				
				products[count] = basketItem;
				count = count + 1;
				
				//alert('Indexed barcode : ' + products[productIndex].barcode);
				
			}
			
			// move the basket item
			function moveBasketItem() {
				
				//img = document.createElement('div');
				
				var basketItemWidget = $("#product0");
				
				var x1 = $("#product0").offset().left;
				var y1 = $("#product0").offset().top;
				
				createProductIcon(x1, y1);
				//basketItemWidget.style.top = 200 + "px"
				
			}
			
			function render() {
				
				$("#basket-list").html(renderProductList());
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
				    var product = parsePrice(echoValue);
				    alert(product);
				    alert('calling displaySlideDown() for item : [' + product + ']');
				    displaySlideDown('Scanned Item', product);
				    
				    //alert('basketIndex : ' + basketIndex + ' description : ' + description + ' price : ' + price + ' barcode : ' + barcode);
				    //addItem(-1, basketIndex, description, price, barcode, 1, 't');  
				    //addToTotalPrice(count, price);
				    
				    //render();
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
			
			// parse the json price
			function parsePrice(str) {
				
				var price = JSON.parse(str);
				//alert('JSON.parse(str) ' + price.price);
				var prices = new Array;
				var basketItem;
				
				// TODO: Use below when multiple prices for each are returned
				/*for (var i = price.length-1; i >= 0; i--) {
					basketItem = new BasketItem(price[i].type);
					basketItem = price[i].name;
					basketItem = price[i].barcode;
					basketItem = price[i].amount;
					
					prices[i] = basketItem;
				}*/
				
				return price;
			}
			
			
			function onUpdatePrice() {
				
				var updatedPrices;
				var typeIndex = 0;
				
				window.echo = function(str, callback) {
				    cordova.exec(callback, function(err) {
				        callback('Nothing to echo.');
				    }, "GetLatestPricePlugin", "echo", [str]);
				};
				
				//alert('Updating barcode ' + products[productIndex].barcode);
				window.echo(products[productIndex].barcode, function(price) {
				    
					var prices = parsePrice(price);
					
					// TODO: Use below when multiple prices for each are returned
					// find the price for this type
					/*for (var i = prices.length-1; i >= 0; i--) {
						if (prices[i].type == 't') {
							alert(prices[i].title);
							typeIndex = i;
							break;
						}
					}*/
					
					// Update the products price
					alert('Updating to price : ' + prices.price);
					products[productIndex].price = prices.price;
					
					render();
					
					// TODO: Update the price comparison data model

				});
			}
			
			
			function onSaveBasket() {
				
				var basket = JSON.stringify(products, replacer);
				
				alert(basket);
				
				window.echo = function(str, callback) {
				    cordova.exec(callback, function(err) {
				        callback('Nothing to echo.');
				    }, "SaveBasketPlugin", "saveBasket", [str]);
				};
				
				//alert('Saving basket ');
				window.echo(basket, function(status) {
				    alert(status);
				});
			}
		
			
			function onLoadBasket() {

				//alert('Loading basket ' + basketIndex);
				products = new Array();
				count = 0;
				
				window.echo = function(str, callback) {
				    cordova.exec(callback, function(err) {
				        callback('Nothing to echo.');
				    }, "LoadBasketPlugin", "loadBasket", [str]);
				};
				
				//alert('Loading basket ');
				window.echo(basketIndex, function(basketItemsStr) {
					
					//alert(basketItemsStr);
					var basketItems = JSON.parse(basketItemsStr);
					
					for (var i = 0; i < basketItems.length; i++) {
						//alert('loading item : ' + basketItems[i].description)
						addItem(basketItems[i].id, basketItems[i].basketId, basketItems[i].description, basketItems[i].price, basketItems[i].barcode, basketItems[i].amount, basketItems[i].type);
					}
					
					render();
				});		
					
			}
			
			// Delete a product item from the basket
			function onDeleteItem() {
				
				var basketItem = JSON.stringify(products[productIndex], replacer);
				
				alert(basketItem);
				
				window.echo = function(str, callback) {
				    cordova.exec(callback, function(err) {
				        callback('Nothing to echo.');
				    }, "DeleteItemPlugin", "deleteItem", [str]);
				};
				
				window.echo(basketItem, function(status) {
				    alert(status);
				});
				
				// close the 'options' dialog
		    	$("#popupMenu").popup("close");
		    	
				// Reload the basket
				onLoadBasket();
				render();
				
			}
			
			// Convert speech to text
			function onSpeechToText() {
				
				window.echo = function(str, callback) {
				    cordova.exec(callback, function(err) {
				        callback('Nothing to echo.');
				    }, "SearchProductsByVoicePlugin", "speechToText", [str]);
				};
				
				//alert('Initializing Speech to Text');
				window.echo('test', function(text) {
					
					$("#search-mini").val(text) ;
					
					// Load the spoken product
					loadSuggestedProducts('Search Results', text)
				});
				
			}
			
			
			function renderProductList() {
				
				var renderedProductList = '';
				var renderedProduct = '';
				var price = 0.0;
				
				for (var i = 0; i < products.length; i++) {
					
					renderedProduct = 	//'<td></td>'; /*'<li id=\"li' + i + '\" style=\"width:95%\">' +
										'<div id=\"product' + i + '\" style=\"width:100%\"><TABLE id="gradient-style" style=\"width:100%\"><TR align=\"left\" style=\"width:100%\" ><TD><A style=\"color: blue;font-size: 10px;text-decoration:none; width:85%\"' +
										' href=\"#popupMenu\" onclick=\"setProductIndex(' + i + '); \" data-rel=\"popup\" data-transition=\"slideup\" data-icon=\"gear\">' + products[i].description + '</A>' +
										'</TD><TD align=\"right\" style=\"width:15%\">' + '   &euro;' + products[i].price + '</TD>' +
										'</TR></TABLE></div>';
										//'</li>';	//+ '<li style=\"font-size: 10px;\" data-role="list-divider" role="heading">Groceries</li>'	
					//alert(renderedProduct);
					renderedProductList = renderedProductList + renderedProduct;
					price = parseFloat(price) + parseFloat(products[i].price);
					addToTotalAmount(1);
				}
				
				// update the price total
				$("#totalAmount").html(count + ' items');
				$("#totalPrice").html('&euro;' + price.toFixed(2));
				
				return renderedProductList;
			}
			
			
			function renderSuggestionsList(title, productItems) {
				
				var renderedProductList = '<li data-role=\"divider\" data-theme=\"e\" style=\"font-size: 16px; align: center\">' + title + '</li>';
				var renderedProduct = '';
				var suggestionColor = '';
				
				for (var i = 0; i < productItems.length; i++) {
					
					// set the color by chain type
					suggestionColor = getChainColor(productItems[i].type);
					
					renderedProduct = 	'<li><a onclick=\"setSuggestionIndex(' + i + ')\" class=\"suggestion\" style=\"font-size: 10px; background-color: ' + suggestionColor + '\">' + productItems[i].name + '  &euro;' + productItems[i].price + '</a></li>';	
					//alert(renderedProduct);
					renderedProductList = renderedProductList + renderedProduct;
				}
				
				return renderedProductList;
			}
			
			
			function renderSuggestionsSlider(title, productItems) {
				
				var renderedProductList = '<p></p><div class=\"swiper-container\" style=\"height: 140px \"><div style=\"height: 140px \" class=\"swiper-wrapper\" >';
				var renderedProduct = '';
				var suggestionColor = '';
				
				for (var i = 0; i < productItems.length; i++) {
					
					// set the color by chain type
					suggestionColor = getChainColor(productItems[i].type);
					
					renderedProduct = 	'<div class=\"swiper-slide \" style=\"background-color: #FFFFFF; height: 140px; height: 140px \" ><div class=\"title\" style=\"background-color: #FFFFFF; height: 140px; \" onclick=\"setSuggestionIndex(' + i + ');addSearchProduct();\" class=\"suggestion\" style=\"font-size: 10px; \"><table><tr><td><img src=\"' + productItems[i].imageURL + '\" /></td><td>' + productItems[i].name + '  &euro;' + productItems[i].price + '</td></tr><tr><td style=\"font-color: #ca4040;\" >' + getChainName(productItems[i].type) + '</td></tr></table></div></div>';	
					//alert(renderedProduct);
					renderedProductList = renderedProductList + renderedProduct;
				}
				
				renderedProductList = renderedProductList + '</div></div>';
				return renderedProductList;
				
			}
			
			
			function setProductIndex(index) {
				productIndex = index;
			}
			
			function setBasketIndex(index) {
				basketIndex = index;
			}
			
			function setSuggestionIndex(index) {
				suggestionIndex = index;
			}
			
			
			// called when a user hits return on search input
			function search() {
				
				onSpeechToText();
				
				//var searchString = $("#search-mini").val();
				//loadSuggestedProducts('Search Results', searchString);
				
			}
			
			
			// called when user wishes to view other suggestions for this selected product
			function suggestAlternativeProduct() {
			
				// close the options dialog
		    	$("#popupMenu").popup("close");
		    	
				loadSuggestedProducts('Suggestions', products[productIndex].description);
			}
			
			
			function loadSuggestedProducts(context, productName) {
				
				// close the 'options' dialog
		    	//$("#popupMenu").popup("close");
		    	
				//alert('Loading suggestions ' + productName);   // TODO : create a classification attribute for suggestions?
				var productItems = new Array();
				
				window.echo = function(str, callback) {
				    cordova.exec(callback, function(err) {
				        callback('Nothing to echo.');
				    }, "SearchProductsPlugin", "loadSuggestions", [str]);
				};
				
				window.echo(productName, function(result) {
					
					var suggestionsList = JSON.parse(result);
					//alert(result + ':' + suggestionsList.length);
					
					for (var i = 0; (i < suggestionsList.length) && (i < 20); i++) {
						
						//alert('creating suggestion : ' + suggestionsList[i].name);
						// Create the product 
						var product = new Product();
						product.id = suggestionsList[i].id;
						product.name = suggestionsList[i].name;
						product.price = suggestionsList[i].price;
						product.barcode = suggestionsList[i].barcode;
						product.type = suggestionsList[i].chain;
						product.imageURL = suggestionsList[i].imageURL;

						productItems[i] = product;
						//alert('created suggestion : ' + productItems[i].name);
					}
					
					// Record the current suggestion list for future reference
					suggestions = suggestionsList;
					
					displaySlideDown(context, productItems);
					  
					/*$('#suggestions-list').listview('refresh');
			    	
					// open the suggestions dialog
			    	$("#priceComparePopup").popup("open");
			    	$('#suggestions-list').listview('refresh');
			    	
			    	$(".suggestion").on("click",function (event) { 
			    		addSearchProduct();
			    	});*/
			    	
				});
			    	
			}
			
			function displaySlideDown(context, productItems) {
				
				$("#search-result").html(renderSuggestionsSlider(context, productItems));
			    $('#search-result').children().next().hide();
			    $('#search-result').children().next().slideDown(2000);
			    
				  var mySwiper = new Swiper('.swiper-container',{
					    pagination: '.pagination',
					    paginationClickable: true,
					    slidesPerView: 1
					  });
				
			}
			
			// add a product returned from search (to currently selected basket).
			function addSearchProduct() {
				
				//alert('adding product : ' + suggestions[suggestionIndex].name);
				addItem(-1, basketIndex, suggestions[suggestionIndex].name, suggestions[suggestionIndex].price, suggestions[suggestionIndex].barcode, 1, suggestions[suggestionIndex].type);
				
				// close the suggestions dialog
		    	//$("#priceComparePopup").popup("close");
		    	
				render();	
			}
			
			// swap the currently selected product with the suggested product
			function swapProduct() {
				
				onDeleteItem();
				
				addItem(-1, basketIndex, suggestions[suggestionIndex].name, suggestions[suggestionIndex].price, suggestions[suggestionIndex].barcode, 1, suggestions[suggestionIndex].type);	
				
				// close the suggestions dialog
		    	$("#priceComparePopup").popup("close");
		    	
		    	render();
			}
			
			
			// Define custom types
			function BasketItem (type) {
				this.id = 0;
				this.basketId = 1;
			    this.type = type;
			    this.barcode = '';
			    this.description = '';
			    this.price = '';
			    this.amount = 0;
			}
			
			// Define custom types
			function Product (type) {
				this.id = 0;
				this.barcode = '';
			    this.name = '';
			    this.price = '';
			    this.url = '';
			    this.imageURL = '';
			    this.category = '';
			    this.subCategory = '';
			    this.type = '';
			}
			
			
			function getChainColor(type) {
				
				var color = '';
				//alert(type);
				if (type == 't') {
					color = '#ca4040';
				} else if (type == 'v') {
					color = '#ca4040';
				} else if (type == 's') {
					color = '#ff8604';
				} else if (type == 'l') {
					color = '#49a430';
				} else {
					color = '#973e76';
				}
				
				return color;
			}
			
			function getChainName(type) {
				
				var name = '';

				if (type == 't') {
					name = 'Tesco';
				} else if (type == 'v') {
					name = 'Supervalu';
				} else if (type == 's') {
					name = 'Superquinn';
				} else if (type == 'l') {
					name = 'Lidl';
				} else if (type == 'a'){
					name = 'Aldi';
				} else if (type == 'd'){
					name = 'Dealz';
				} else if (type == 'm'){
					name = 'Mace';
				} else if (type == 'e'){
					name = 'EuroSpar';
				} else {
					name = 'Unknown Chain';
				}
				
				return name;
			}
			
			function replacer(key, value) {
			    if (typeof value === 'number' && !isFinite(value)) {
			        return String(value);
			    }
			    return value;
			}
			