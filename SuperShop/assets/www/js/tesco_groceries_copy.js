
			
	    	$(document).ready(function () {
	    		//addItem('test');
	    	});
			
			
			var items = '';
			
			function addItem(barcode) {
				
			var i = '<DIV>' +
				<DIV id="endFacets-1" class="productLists">
					<DIV class="un_wrapperTB5 un_wrapperLR" id="un_anchor_0">
						<DIV class="un_plp_list_item">
							<DIV class="un_left un_plp_imageSize un_center_text">
								<A
									href="/mt/www.tesco.ie/groceries/Product/Details/?id=251975475"><IMG
									alt=""
									src="/img?url=http%3A%2F%2Fimg.tesco.ie%2FGroceries%2Fpi%2F779%5C5000225005779%5CIDShot_90x90.jpg&amp;ttl=31d">
								</A>
							</DIV>
							<DIV class="un_right un_plp_descSize">
								<DIV class="un_border10_bottom un_plp_title">
									<A class="si_pl_251975475-title" id="h-251975475"
										href="/mt/www.tesco.ie/groceries/Product/Details/?id=251975475">Schwartz
										Authentic Beef Casserole Mix 43g</A>
								</DIV>
								<DIV class="quantity">
									<DIV class="content addToBasket">
										<DIV class="">
											<SPAN class="un_wrapperB "><SPAN
												class="un_plp_price">&euro;1.31</SPAN><SPAN
												class="un_plp_unit"> (&euro;3.05/100g)</SPAN>
											</SPAN>
											<FORM method="post" id="fBrowse-251975475-1"
												action="/mt/www.tesco.ie/groceries/product/browse/default.aspx?N=4294953506&amp;Ne=4294954028">
												<DIV>
													<DIV>
														<INPUT name="formName" value="fBrowse" type="hidden"><INPUT
															name="pageName"
															value="Tesco.Com.Applications.Retail.Superstore.Online.Pages.Product.Browse.Default"
															type="hidden"><INPUT name="__fd"
															value="cGJvcHRmbj1mQmFza2V0JTQwJTNiZlNlYXJjaCUzYmZOb3RlcGFkJTQwJTNiZkJyb3dzZSU0MCUzYiU0MCUzYiU0MCUzYiU0MCUzYg=="
															type="hidden">
													</DIV>
													<INPUT class="hide" id="id_" disabled="disabled" size="1"
														type="hidden">
													<DIV>
														<LABEL
															class="un_quantity_label un_block un_wrapperTB5                "
															for="qty-251975475-3">Quantity</LABEL>
														<DIV class="un_left un_wrapperR">
															<INPUT class="un_MP_btn" disabled="disabled"
																name="un_jtt_plus_pd"
																src="/mt/a/tesco.com/31d/images/tescoie/minus_qty.png"
																alt="minus" type="image">
														</DIV>
														<INPUT class="un_left un_plp_qtyInput"
															id="qty-251975475-3" name="qty-251975475" value="1"
															maxlength="4" type="">
														<DIV class="un_left un_wrapperLR">
															<INPUT class="un_MP_btn" name="un_jtt_plus_pi"
																src="/mt/a/tesco.com/31d/images/tescoie/plus_qty.png"
																alt="plus" type="image">
														</DIV>
													</DIV>
												</DIV>
											</FORM>
										</DIV>
									</DIV>
								</DIV>
							</DIV>
							<DIV class="un_clear"></DIV>
						</DIV>
					</DIV>
					
					
					
				
				var item = '	<DIV class=\"un_wrapperTB5 un_wrapperLR\" id=\"item1\">' +
					'<DIV class=\"un_plp_list_item\">' +
						'<DIV>' +
							'<DIV  >' +
								'<A style=\"font-size: 24px; line-height: 22px;\" id=\"h-251975573\"' +
									'href=\"/mt/www.tesco.ie/groceries/Product/Details/?id=251975573\">' + barcode + '</A>' +
							'</DIV>' +
							'<DIV class=\"quantity\">' +
								'<DIV class=\"content addToBasket\">' +
									'<DIV class=\"\">' +
										'<SPAN class=\"un_wrapperB \" style=\"font-size: 24px; line-height: 22px;\">' +
											'<SPAN class=\"un_plp_price\">' + '&euro;1.35' + '</SPAN>' +
											'<SPAN class=\"un_plp_unit\">' + '(&euro;3.75/100g)' + '</SPAN>' +
											'<SPAN>' +
													'<LABEL' +
														'class=\"un_quantity_label un_block un_wrapperTB5                \"' +
														'for=\"qty-251975573-3\">' + 'Amount' + '</LABEL>' +
													'<DIV class=\"un_left un_wrapperR\">' +
														'<INPUT class=\"un_MP_btn\" disabled=\"disabled\"' +
															'name=\"un_jtt_plus_pd\"' +
															'src=\"images/plus-minus.png\"' +
															'alt=\"minus\" type=\"image\">' +
													'</DIV>' +
													'<INPUT class=\"un_left un_plp_qtyInput\"' +
														'id=\"qty-251975573-3\" name=\"qty-251975573\" value=\"1\"' +
														'maxlength=\"4\" type=\"\">' +
													'<DIV class=\"un_left un_wrapperLR\">' +
														'<INPUT class=\"un_MP_btn\" name=\"un_jtt_plus_pi\"' +
															'src=\"images/plus-minus.png\"' +
															'alt=\"plus\" type=\"image\">' +
													'</DIV>' +
												'</SPAN>' +
										'</SPAN>' +
									'</DIV>' +
								'</DIV>' +
							'</DIV>' +
						'</DIV>' +
						'<DIV class=\"un_clear\">' + '</DIV>' +
					'</DIV>' +
				'</DIV>';
				
				items = items + item;
	
				$("#items").html(items);

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
				    addItem(itemDescription);
				});
			
			}

			