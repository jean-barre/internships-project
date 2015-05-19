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
							<h1 id="logo"><a href="#">Admin</a></h1>
							<p>Pour organiser les commandes</p>
						
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
											
											<div>
												<ul><center>
													<li><h3>Les Stock</h3>
														<ul>
															<li>Afficher les Stocks</li>
															<li>Ajouter une référence</li>
															<li>Supprimer une référence</li>
															<li>Réapprovisionner une référence</li>
														</ul>
													</li>

													<li><h3>Les Commandes</h3>
														<ul>
															<li>Afficher les Commandes</li>
															<li>Modifier une saisie</li>
															<li>Informer le paiement d'une commande</li>
															<li>Supprimer une saisie</li>
														</ul>
													</li></center>
												</ul>
												<!--
													<li>Afficher l'ensemble des <a href="print-form.php">commandes</a>.</li>
													
													<li>Modifier/afficher la commande d'un login :</br>
														<form method="post" action="print-indiv.php" enctype="multipart/form-data">

														<div class="form">
															Login: <input type="text" name="login">
														<input type="submit" class="btn btn-success" value="Valider" name="submit">
														</form>
													
												</ul>-->
											</div>
										</article>
								
								</div>
								
						</div>
					</div>

			</div>

		<!-- Footer Wrapper -->
		<?php include("foot.php"); ?>
	</body>
</html>