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
							<p>Fiche Individuelle</p>
						
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
									
									<?php include("data-store.php");

									if(isset($_GET['login']))
									{
										$bdd = dataBaseConnect();
										$max = sizeCave();
										$domain = collectDomains($bdd,$max);
										$label = collectLabels($bdd,$max);
										$login = $_GET['login'];

										$bag = collectFormulaire($bdd,$login,$max);
									    $bill = getBill($bdd,$login);
										$paid = didPaid($bdd,$login);
											
										echo '<center><h2>'.$login.'</h2></center>';
										echo '<form method="post" action="print-form.php?login='.$login.'" enctype="multipart/form-data">';

										for ($i=0 ; $i<count($bag) ; $i++)
                                      	{
                                      		echo '<li style="height:60px;" type="disc">';
                                      		echo '<font style="display: inline-block; width: 500px;" align="left" size="4">';
                                  			printAdmin($bag[$i],$domain[$i]);
                                  			echo '</font>';
                                  			echo '<label style="float:right;" for="appro_id">Nouvelle quantité :<div style="display: inline-block;">
														<select style="width: 20%;" name="nb'.$label[$i].'" id="appro_id">';
											printSelect($bag[$i],5);
											echo 		'</select></div></label></li>';
                                      	}
                                      	echo $login.' a payé : <input type="text" name="paiement">';

                                      	echo 	'<center><input type="submit" class="button button-icon" value="Enregistrer" name="update"></center>
                              				</form>';
                              			/*
                              			echo '<center><font size="6">OU</font></br>
                              				<a href="print-form.php?delete=1&login='.$login.'" class="button button-icon fa fa-trash-o">Tout Supprimer</a></center>';
                              				*/
									}
									else
										echo 'Mauvaise redirection';

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