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
											
											<?php 
											include("data-store.php");
											$bdd = dataBaseConnect();
											?>

											<form method="post" action="stock.php" enctype="multipart/form-data">
												<div class="form">
													<ul>
														<li>
															Attribuer un label, court et minuscule
															<input type="text" name="label">
															<i>La photo associé à ce nouveau produit sera appelée label.jpeg</i>
														</li>
														<li>
															Renseigner le domaine ou le nom du pack
															<input type="text" name="domain">
														</li>
														<li>
															Renseigner l'appelation (éventuellement multiples)
															<input type="text" name="appel">
														</li>
														<li>
															Renseigner les autres informations sous la forme "Couleur Année" (Ex : <i>Blanc Sec 2010</i>)
															(éventuellemnt multiples)
															<input type="text" name="spec">
														</li>
														<li>
															Prix
															<input type="text" name="prix">
														</li>
														<li>
															Quantité initiale
															<input type="text" name="qte">
														</li>
													</ul>
													<center>
													<input type="submit" class="button button-icon fa fa-file" value="Ajouter" name="submit">
													</center>
												</div>
											</form>

										</article>
								</div>
						</div>
					</div>
			</div>

			<?php include("foot.php"); ?>
	</body>
</html>