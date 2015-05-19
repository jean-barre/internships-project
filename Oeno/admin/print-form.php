<?php
 
require_once 'CAS/CAS.php';
 
// Initialise phpCAS
phpCAS::client(CAS_VERSION_2_0,'cas.enseirb-matmeca.fr',443,'cas');
// Désactive la vérification SSL
phpCAS::setNoCasServerValidation();
// Force l'authentification CAS
phpCAS::forceAuthentication();
 
// getUser permet de récupérer le login de
// l'utilisateur connecté
$login = phpCAS::getUser();

$users = array();
	array_push($users,"jeabarre");
	array_push($users,"csauvage");
	array_push($users,"jduquenoy");
	array_push($users,"fljacquet");
	array_push($users,"fgarreau");
	array_push($users,"luraymon");
	array_push($users,"rboulch");
	array_push($users,"dagoncal");
	array_push($users,"elanternier");
	array_push($users,"mrobin001");
		
	if(!in_array($login, $users))
		header('Location: http://oeno.eirb.fr');

?>
<?php include("head.php"); ?>
	<body class="no-sidebar">

		<!-- Header Wrapper -->
			<div id="header-wrapper">
						
				<!-- Header -->
					<div id="header" class="container">
						
						<!-- Logo -->
							<h1 id="logo"><a href="admin2.php">Admin</a></h1>
							<p>Page des Commandes</p>
						
						<!-- Nav -->
						<?php include("navadmin.php"); ?>

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
											

											<!-- On recense l'ensemble des commandes -->
											<?php
                                          	include("data-store.php");

											$bdd = dataBaseConnect();
											$max = sizeCave();
                                          	$domain = collectDomains($bdd,$max);

											

											// Un membre a modifié le caddie d'un client
											if(isset($_POST['update']))
											{
												$login = $_GET['login'];
												$form = getCommand($bdd,$max);
												$bag = collectFormulaire($bdd,$login,$max);
												$stock = collectQtes($bdd,$max);
												$bill = getBill($bdd,$login);
												$prices = collectPrices($bdd,$max);

												$arrayZero = array_zero($max);
												// On vérifie la disponibilité des stocks et la validité de la commande
		                                        if (checkDataEntrees($arrayZero,$form,$stock,$max)==1)
		                                        {
		                                        	$newValues = sumArray($form,$bag);
		                                        	$newStock = diffArray($form,$bag);
		                                            $prices = collectPrices($bdd,$max);
		                                            $floatprice = getTotalPrice($form,$prices,$max);
		                                            if ($floatprice)
	                                            	{
		                                                // On modifie les stocks suivant la commande qui est en train d'être effectuée
		                                                updateStocks($bdd,$newStock,$max);

		                                                // On rentre les valeurs dans la BDD Formulaire
		                                                updateFormulaire($bdd,$form,$form,$login,$floatprice,$max);
													}
		                                          }
		                                    }
	                                        else if (isset($_GET['delete']))
	                                        {
	                                        	$login = $_GET['login'];
												$form = getCommand($bdd,$max);
												$bag = collectFormulaire($bdd,$login,$max);
												$stock = collectQtes($bdd,$max);
												$bill = getBill($bdd,$login);
												$prices = collectPrices($bdd,$max);
												echo "boucle delete";

												$arrayZero = array_zero($max);
												// On vérifie la disponibilité des stocks et la validité de la commande
		                                        if (checkDataEntrees($arrayZero,$form,$stock,$max)==1)
		                                        {
		                                        	$newValues = sumArray($form,$bag);
		                                        	$newStock = diffArray($form,$bag);
		                                            $prices = collectPrices($bdd,$max);
		                                            $floatprice = getTotalPrice($form,$prices,$max);
		                                            echo "boucle check";
		                                            if ($floatprice)
	                                            	{
	                                            		echo "boucle floatprice";
		                                                // On modifie les stocks suivant la commande qui est en train d'être effectuée
		                                                updateStocks($bdd,$newStock,$max);

		                                                // On rentre les valeurs dans la BDD Formulaire
		                                                $con = mysql_connect("localhost", "oeno", "Hahie=c1");
										                $db = mysql_select_db("oeno", $con);

										                $req = "DELETE FROM Formulaire WHERE login='".$login."'";
		                                      			$result = mysql_query($req,$con);
									                	if (!$result) {
															die('Invalid query1: ' . mysql_error());
		                                          		}
		                                          	}
		                                          }
											}

											if (!empty($_POST['paiement']))
											{
												$login = $_GET['login'];
												$con = mysql_connect("localhost", "oeno", "Hahie=c1");
								                $db = mysql_select_db("oeno", $con);

								                $req = "UPDATE Formulaire SET paid='".$_POST['paiement']."' WHERE login='".$login."'";
                                      			$result = mysql_query($req,$con);
							                	if (!$result) {
													die('Invalid query1: ' . mysql_error());
												}
											}

											// Un membre a supprimé un client
											/*if(isset($_GET['delete']))
											{
												$login = $_GET['login'];
												
											}*/

											$bdd = dataBaseConnect();
											$max = sizeCave();
											$req = $bdd->query('SELECT * FROM Formulaire') or die(print_r($bdd->errorInfo()));
											echo '<ul>';
												while ($donnees = $req->fetch())
												{
													echo '<li>';
													$login = $donnees['login'];
												    $bag = collectFormulaire($bdd,$login,$max);
													$bill = getBill($bdd,$login);
													$paid = paid($bdd,$login);
													echo '<font style="display: inline-block; width: 500px;" align="left" size="4">
																<a href="update.php?login='.$login.'">'.$login.'</a></font>';
		                                          	for ($i=0 ; $i<count($bag) ; $i++)
		                                          	{
		                                          		if ($bag[$i])
		                                          		{
		                                          			printAdmin($bag[$i],$domain[$i]);
		                                          		}
		                                          	}
		                                          	echo ' Facturé : '.$bill.'€ Payé? : ';
                                          			if (didPaid($bdd,$login) == 1)
                                          				echo 'Oui';
                                          			else
                                          				echo 'Non ('.$paid.'€)';
		                                          	echo '</li>';
												}
											echo '</ul>';

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