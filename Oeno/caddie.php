<?php include("head.php"); ?>
<?php include("data-store.php"); ?>
	<body class="no-sidebar">

		<!-- Header Wrapper -->
			<div id="header-wrapper">
						
				<!-- Header -->
					<div id="header" class="container">
						
						<!-- Logo -->
							<h1 id="logo"><a href="#">Caddie</a></h1>
							<p>Vos commandes</p>
						
						<!-- Nav -->
						<?php include("nav.php"); ?>

					</div>

			</div>
			
		<!-- Main Wrapper -->
			<div id="main-wrapper">

				<!-- Main -->
					<div id="main" class="container">
						<div class="row">
						
							<!-- Content -->
								<div id="content" class="12u skel-cell-important">

									<!-- Post -->
										<article class="is-post">
										
                                          	<?php
	                                          	$login = $_GET['login'];
	                                          	$bdd = dataBaseConnect();
	                                          	$max = sizeCave();
	                                          	$label = collectLabels($bdd,$max);
	                                          	$domain = collectDomains($bdd,$max);
	                                          	$prices = collectPrices($bdd,$max);
	                                          	$stock = collectQtes($bdd,$max);
	                                          	
	                                          	if(isset($_POST['submit']))
			                                    {
			                                          // Ce que le client a commandé
			                                          $form = getCommand($bdd,$max);

			                                          // Ce qu'il avait initialement commandé
			                                          $bag = collectFormulaire($bdd,$login,$max);

			                                          $newValues = sumArray($form,$bag);
			                                          
			                                          // On vérifie la disponibilité des stocks et la validité de la commande
			                                          if (checkDataEntrees($bag,$form,$stock,$max)==1)
			                                          {
			                                              $prices = collectPrices($bdd,$max);
			                                              $floatprice = getTotalPrice($form,$prices,$max);
			                                              if ($floatprice)  
			                                            	{
				                                                // On modifie les stocks suivant la commande qui est en train d'être effectuée
				                                                updateStocks($bdd,$form,$max);

				                                                $oldPrice = getBill($bdd,$login);
      															$newPrice = $oldPrice + $floatprice;
				                                                // On rentre les valeurs dans la BDD Formulaire
				                                                updateFormulaire($bdd,$form,$newValues,$login,$newPrice,$max);
															}
			                                              else
			                                              {
			                                                  echo "<center>Vous n'avez rien commandé!</center>";
			                                                  echo "<center><a href=\"store.php\" class=\"button button-icon fa fa-file\">Retour à la Cave</a></center>";
			                                              }
			                                          }
			                                    }

	                                          	if (estVideCaddie($bdd,$login,$max))
	                                          	{
													echo '<center>Votre Caddie est vide, Qu\'attendez vous?</center>';
													echo '<center><a href="store.php" class="button button-icon fa fa-hand-o-down">Descente à la Cave</a></center>';	
												}
												else
												{
													$bag = collectFormulaire($bdd,$login,$max);
													$bill = getBill($bdd,$login);

		                                          	echo '<ul>';
		                                          	for ($i=0 ; $i<count($bag) ; $i++)
		                                          	{
		                                          		if ($bag[$i])
		                                          		{
		                                          			printCaddie($bag[$i],$domain[$i],$label[$i],$prices[$i]);
		                                          		}	
		                                          	}
	                                          		echo '</ul>';
		                                          	echo '<font size="6">Total : '.$bill.'€</font>     Payé? : ';
		                                          	if (didPaid($bdd,$login)==1)
		                                          		echo '<i class="fa fa-check-square-o fa-4x"></i>';
		                                          	else
		                                          		echo '<i class="fa fa-times fa-4x"></i>';

		                                          	echo '<center>Le Club vous remercie pour votre commande. Vous pouvez à tout moment rajouter des articles
													en retournant à la Cave</center>';

		                                          	echo '<center><a href="store.php" class="button button-icon fa fa-hand-o-down">Retour à la Cave</a></center>';

		                                          	echo '<center><font size="2">Pour modifier/supprimer une commande, merci de vous adresser directement à l\'un des membres du Club.</font></center>';
												}
											?>
											
											
										</article>
								
								</div>

						</div>
					</div>

			</div>

		<!-- Footer Wrapper -->
		<?php include("foot.php"); ?>

	</body>
</html>